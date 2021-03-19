package com.oldbai.halfmoon.controller.user;


import com.oldbai.halfmoon.entity.User;
import com.oldbai.halfmoon.exception.NotLoginException;
import com.oldbai.halfmoon.response.ResponseResult;
import com.oldbai.halfmoon.service.UserService;
import com.oldbai.halfmoon.service.impl.UserServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author oldbai
 * @since 2021-02-14
 */
@Api(description = "用户中心")
@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/halfmoon/user")
public class UserController {

    @Autowired
    UserService userService;

    /**
     * 初始化管理员账号
     * 需要：
     * username 账号
     * password 密码
     * email 邮箱
     *
     * @return
     */
    @ApiOperation("初始化管理员账号")
    @PostMapping("/admin_account")
    public ResponseResult initManagerAccount(@RequestBody User user) {
        log.info(user.getUserName());
        log.info(user.getPassword());
        log.info(user.getEmail());
        return userService.initManagerAccount(user);
    }

    /**
     * 注册
     * 需要用户输入：
     * 邮箱地址（邮箱验证码）
     * 邮箱验证码
     * 昵称(用户名)
     * 密码
     * 人类验证码（图灵验证码）
     *
     * @param user
     * @return
     */
    @ApiOperation("注册")
    @PostMapping("/register")
    public ResponseResult register(@RequestBody User user,
                                   @RequestParam("verify_code") String emailCode,
                                   @RequestParam("captcha") String captcha,
                                   @RequestParam("captcha_key") String captchaKey) {

        return userService.register(user, emailCode, captcha, captchaKey);
    }

    /**
     * 获取图灵验证码
     * 使用：
     * 1.用户前端请求一个验证码
     * 2.后台生成验证码，并且保存在 session 中
     * 3.用户提交注册信息（携带了用户所输入的图灵验证码内容）
     * 4.从 session 中拿出存储的验证码内容跟用户输入的验证码进行比较
     * 5.返回结果
     * 6.在前后端分离下，可以把验证码存入 redis 中。设置有效期为10分钟。
     * 7.存 redis 中时，key 可以由前端生成随机数，然后以参数的形式添加到请求图灵验证码的URL下
     * 8.后台生成图灵验证码，返回并且保存到 redis 中， 以 key - value 形式
     * 9.用户提交注册信息（携带了用户所输入的图灵验证码内容 + key）
     * 10.从 redis 中 根据 key 拿出图灵验证码跟用户输入的验证码进行比较
     * 11.返回结果。正确进行注册并删除redis里的记录，失败返回结果给前端。
     *
     * @return 访问路径 http://localhost:8058/user/captcha
     */
    @ApiOperation("获取图灵验证码")
    @GetMapping("/captcha")
    public void captcha(@RequestParam("captcha_key") String captchaKey) {

        try {
            userService.createCaptcha(captchaKey);
        } catch (Exception e) {
            log.error(e.toString());
        }


    }


    /**
     * 发送邮件，获取邮箱验证码
     * 通过参数的方式获取。不用result风格
     * <p>
     * 使用场景：注册、找回密码、修改邮箱（输入新的邮箱）
     * 注册：如果已经注册过，就提示该邮箱注册过了
     * 找回密码：如果没有注册过，就提示该邮箱没有注册
     * 修改邮箱（输入新的邮箱）：如果已经注册过，就提示该邮箱注册过了
     *
     * @param emailAddress 防止同一IP轰炸
     * @return
     */
    @ApiOperation("发送邮件，获取邮箱验证码")
    @GetMapping("/send/verify_code")
    public ResponseResult sendVerifyCode(@RequestParam("email") String emailAddress,
                                         @RequestParam("type") String type) {
        log.info(emailAddress);
        return userService.sendEmail(type, emailAddress);
    }

    /**
     * 检查该Email是否已经注册
     *
     * @param email 邮箱地址
     * @return FAILED -- > 已经注册了，SUCCESS ===> 没有注册
     */
    @ApiOperation("检查该Email是否已经注册")
    @ApiResponses({
            @ApiResponse(code = 20000, message = "表示当前邮箱已经注册了"),
            @ApiResponse(code = 40000, message = "表示当前邮箱未注册")
    })
    @GetMapping("/email")
    public ResponseResult checkEmail(@RequestParam("email") String email) {
        return userService.checkEmail(email);
    }

    /**
     * 检查该用户是否已经注册
     *
     * @param userName 用户名
     * @return FAILED -- > 已经注册了，SUCCESS ===> 没有注册
     */
    @ApiOperation("检查该用户是否已经注册")
    @ApiResponses({
            @ApiResponse(code = 20000, message = "表示用户名已经注册了"),
            @ApiResponse(code = 40000, message = "表示用户名未注册")
    })
    @GetMapping("/check/username")
    public ResponseResult checkUserName(@RequestParam("userName") String userName) {
        return userService.checkUserName(userName);
    }

    /**
     * 登陆 sign-up
     * 1.用户提交数据：用户名(邮箱地址)，密码，图灵验证码,图灵验证码的key
     * 2.检查验证码是否正确。
     * 3.通过用户名查找用户，用户是否存在
     * 4.不存在通过邮箱查找，用户是否存在
     * 5.存在，则判断密码是否正确
     * 6.生成token，存入redis,返回登陆结果
     * <p>
     * 客户端想要解析
     * 1.算出token的 md5 值，返回这个 md5 值给客户端，就是 key ,将这个 token 保存到 redis 中。
     * 2.解析过程，用户访问的时候，携带 md5key ，我们从 redis 中拿到 token ， 解析token 就知道用户是否有效，角色是什么......
     * 3.设置有效期
     *
     * @param user       用户对象
     * @param captcha    验证码
     * @param captchaKey 验证码key
     * @return
     */

    @ApiOperation("登陆")
    @PostMapping("/login/{captcha}/{captcha_key}")
    public ResponseResult login(@RequestBody User user,
                                @PathVariable("captcha") String captcha,
                                @PathVariable("captcha_key") String captchaKey) {

        return userService.login(captcha, captchaKey, user);
    }

    /**
     * 修改密码password
     * 普通做法：通过旧密码对比来更新密码
     * <p>
     * 找回密码：既可以找回密码又可以修改密码
     * 发送验证码到邮箱/手机-->判断验证码是否正确来判断
     * 对应的邮箱/手机号码所注册的账号是否属于你
     * <p>
     * 步骤
     * 1.用户填写邮箱
     * 2.获取验证码 type = forget
     * 3.用户填写新的密码
     * 4.填写验证码
     * 5.提交数据
     * <p>
     * 需要提交的参数
     * 1.邮箱
     * 2.新密码
     * 3.验证码
     * <p>
     * 如果验证码正确，所有邮箱注册的账号就是你的，可以修改密码
     *
     * @param user
     * @return
     */
    @ApiOperation("修改密码")
    @PostMapping("/update/password/{verifyCode}")
    public ResponseResult updatePassword(@PathVariable("verifyCode") String verifyCode,
                                         @RequestBody User user) {
        return userService.updateUserPassword(verifyCode, user);
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    @ApiOperation("获取用户信息")
    @GetMapping("/get/user_info/{userId}")
    public ResponseResult getUserInfo(@PathVariable("userId") String userId) {
        //1.用户ID
        return userService.getUserInfo(userId);
    }

    /**
     * 修改用户信息
     * <p>
     * 允许用户修改的内容：
     * 1.头像 ()
     * 2.用户名 (唯一的)
     * 3.密码 (单独更新)
     * 4.签名 ()
     * 5.用户邮箱 (单独修改，唯一的)
     *
     * @param user
     * @return
     */
    @ApiOperation("修改用户信息")
    @PostMapping("/update/user_info/{userId}")
    public ResponseResult updateUserInfo(@RequestBody User user,
                                         @PathVariable("userId") String userId) {
        return userService.updateUserInfo(userId, user);
    }

    /**
     * 获取用户集合
     * 分页查询
     * 需要管理员权限
     *
     * @param page
     * @param size
     * @return ------
     * 权限注解 @PreAuthorize("@permission.adminPermission()")
     */
    @PreAuthorize("@permission.adminPermission()")
    @ApiOperation("获取用户集合")
    @GetMapping("/user/list")
    public ResponseResult listUser(@RequestParam("page") int page,
                                   @RequestParam("size") int size,
                                   @RequestParam(value = "userName", required = false) String userName,
                                   @RequestParam(value = "email", required = false) String email
    ) {
        return userService.listUsers(page, size, userName, email);
    }

    /**
     * 删除用户
     * 需要管理员权限
     *
     * @param userId
     * @return
     */
    @PreAuthorize("@permission.adminPermission()")
    @ApiOperation("删除用户")
    @GetMapping("/delete/user/{userId}")
    public ResponseResult deleteUser(@PathVariable("userId") String userId) {
        //判断当前操作的用户是谁
        //根据用户角色判断是否可以删除
        //TODO :通过注解的方式来控制权限

        return userService.deleteUserById(userId);
    }

    /**
     * 1、必须已经登录了
     * 2、新的邮箱没有注册过
     * <p>
     * 用户的步骤：
     * 1、已经登录
     * 2、输入新的邮箱地址
     * 3、获取验证码 type=update
     * 4、输入验证码
     * 5、提交数据
     * <p>
     * 需要提交的数据
     * 1、新的邮箱地址
     * 2、验证码
     * 3、其他信息我们可以token里获取
     *
     * @return
     */
    @ApiOperation("更新邮箱")
    @PostMapping("/update/email")
    public ResponseResult updateEmail(@RequestParam("email") String email,
                                      @RequestParam("verify_code") String verifyCode) {
        return userService.updateEmail(email, verifyCode);
    }

    /**
     * 退出登录
     * <p>
     * 拿到token_key
     * -> 删除redis里对应的token
     * -> 删除mysql里对应的refreshToken
     * -> 删除cookie里的token_key
     *
     * @return
     */
    @ApiOperation("退出登录")
    @GetMapping("/logout")
    public ResponseResult logout() throws NotLoginException {
        return userService.doLogout();
    }

    @ApiOperation("检查token")
    @GetMapping("/checkToken")
    public ResponseResult parseTOken() {
        return userService.parseTOken();
    }

    @ApiOperation("重置密码")
    @PreAuthorize("@permission.adminPermission()")
    @PostMapping("/resetPassword/{userId}")
    public ResponseResult resetPassword(@PathVariable("userId") String userId,
                                        @RequestParam("password")String password) {
        return userService.resetPassword(userId,password);
    }
}

