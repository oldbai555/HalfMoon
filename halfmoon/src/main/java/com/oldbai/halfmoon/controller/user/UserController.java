package com.oldbai.halfmoon.controller.user;


import com.oldbai.halfmoon.entity.User;
import com.oldbai.halfmoon.response.ResponseResult;
import com.oldbai.halfmoon.service.UserService;
import com.oldbai.halfmoon.service.impl.UserServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/plusblog/user")
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

}

