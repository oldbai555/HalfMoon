package com.oldbai.halfmoon.service.impl;

import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.oldbai.blog.utils.Constants;
import com.oldbai.halfmoon.entity.RefreshToken;
import com.oldbai.halfmoon.entity.Settings;
import com.oldbai.halfmoon.entity.User;
import com.oldbai.halfmoon.mapper.RefreshTokenMapper;
import com.oldbai.halfmoon.mapper.SettingsMapper;
import com.oldbai.halfmoon.mapper.UserMapper;
import com.oldbai.halfmoon.response.ResponseResult;
import com.oldbai.halfmoon.response.ResponseState;
import com.oldbai.halfmoon.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oldbai.halfmoon.util.*;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author oldbai
 * @since 2021-02-14
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SettingsMapper settingsMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private TaskService taskService;
    @Autowired
    private RefreshTokenMapper refreshTokenMapper;
    @Autowired
    private SnowflakeIdWorker idWorker;

    private Gson gson = new Gson();

    HttpServletRequest request = null;
    HttpServletResponse response = null;

    public void getRequestAndResponse() {
        this.request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        this.response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();

    }

    /**
     * 注册功能
     *
     * @param user
     * @param emailCode
     * @param captcha
     * @param captchaKey
     * @return
     */
    @Transactional
    @Override
    public ResponseResult register(User user, String emailCode, String captcha, String captchaKey) {
        getRequestAndResponse();
        //1.检查当前用户名是否已经注册，或者为不为空
        String username = user.getUserName();
        if (StringUtils.isEmpty(username)) {
            return ResponseResult.FAILED("用户名不可以为空！");
        }
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("user_name", username);
        User one = userMapper.selectOne(wrapper);
        if (!StringUtils.isEmpty(one)) {
            return ResponseResult.FAILED("该用户已注册！");
        }
        //2.检查邮箱格式是否正确
        String email = user.getEmail();
        if (!ValidateUtil.validateEamil(email)) {
            return ResponseResult.FAILED("邮箱格式不正确！");
        }
        if (StringUtils.isEmpty(email)) {
            return ResponseResult.FAILED("邮箱地址不可以为空！");
        }
        //3.检查该邮箱是否已经注册
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("email", email);
        User oneByEmail = userMapper.selectOne(userQueryWrapper);
        if (!StringUtils.isEmpty(oneByEmail)) {
            return ResponseResult.FAILED("该邮箱地址已被注册！");
        }
        //4.检查邮箱验证码是否正确
        String code = (String) redisUtil.get(Constants.User.KEY_EMAIL_CODE_CONTENT + email);
        if (StringUtils.isEmpty(code)) {
            return ResponseResult.FAILED("验证码已经过期了......");
        }
        if (!emailCode.equals(code)) {
            return ResponseResult.FAILED("邮箱验证码不正确......");
        } else {
            //正确 ，删除redis里的内容
            redisUtil.del(Constants.User.KEY_EMAIL_CODE_CONTENT + email);
        }
        //5.检查图灵验证码是否正确
        String key = (String) redisUtil.get(Constants.User.KEY_CAPTCHA_CONTENT + captchaKey);
        log.info("captcha" + captcha);
        if (StringUtils.isEmpty(key)) {
            return ResponseResult.FAILED("图灵验证码已经过期了......");
        }
        if (!key.equals(captcha)) {
            return ResponseResult.FAILED("图灵验证码不正确......");
        } else {
            redisUtil.del(Constants.User.KEY_CAPTCHA_CONTENT + captchaKey);
        }
        //达到可以注册条件
        //6.对密码进行加密
        String password = user.getPassword();
        if (StringUtils.isEmpty(password)) {
            return ResponseResult.FAILED("密码不可以为空......");
        }
        user.setPassword(BCrypt.hashpw(password));
        //7.补全数据
        //包括：注册IP，登陆IP，角色（普通角色），头像，创建时间，更新时间
        //角色
        user.setRoles(Constants.User.ROLE_NORMAL);
        //头像
        user.setAvatar(Constants.User.DEFAULT_AVATAR);
        //默认状态
        user.setState(Constants.User.DEFAULT_STATE);
        //注册IP
        user.setRegIp(request.getRemoteAddr());
        //登陆IP
        user.setLoginIp(request.getRemoteAddr());
        //创建时间
        user.setCreateTime(new Date());
        //更新时间
        user.setUpdateTime(new Date());
        //8.保存到数据库中
        userMapper.insert(user);
        //9.返回结果
        return ResponseResult.JOIN_SUCCESS();
    }

    /**
     * 初始化管理员账号
     *
     * @param user
     * @return
     */
    @Transactional
    @Override
    public ResponseResult initManagerAccount(User user) {
        getRequestAndResponse();
        //检查是否有初始化管理员账号
        QueryWrapper<Settings> wrapper = new QueryWrapper<>();
        wrapper.eq("`key`", Constants.Settings.HAS_MANAGER_ACCOUNT_INIT_STATE);
        Settings managerAccountState = settingsMapper.selectOne(wrapper);
        if (!StringUtils.isEmpty(managerAccountState)) {
            return ResponseResult.FAILED("已经初始化过管理员账号了！");
        }
        //检查数据
        if (StringUtils.isEmpty(user.getUserName())) {
            return ResponseResult.FAILED("用户名不能为空");
        }
        if (StringUtils.isEmpty(user.getPassword())) {
            return ResponseResult.FAILED("密码不能为空");
        }
        if (StringUtils.isEmpty(user.getEmail())) {
            return ResponseResult.FAILED("邮箱不能为空");
        }
        //加密密码
        user.setPassword(BCrypt.hashpw(user.getPassword()));
        //补充数据
        //角色
        user.setRoles(Constants.User.ROLE_ADMIN);
        //头像
        user.setAvatar(Constants.User.DEFAULT_AVATAR);
        //默认状态
        user.setState(Constants.User.DEFAULT_STATE);
        //注册IP
        user.setRegIp(request.getRemoteAddr());
        //登陆IP
        user.setLoginIp(request.getRemoteAddr());
        //创建时间
        user.setCreateTime(new Date());
        //更新时间
        user.setUpdateTime(new Date());
        //保存到数据库中
        //更新标记
        int insert = userMapper.insert(user);
        Settings settings = new Settings();
        settings.setKey(Constants.Settings.HAS_MANAGER_ACCOUNT_INIT_STATE);
        settings.setValue("1");
        settingsMapper.insert(settings);
        if (insert == 0) {
            return ResponseResult.FAILED("初始化失败");
        } else {
            return ResponseResult.SUCCESS("初始化成功");
        }
    }

    /**
     * 图灵验证码
     *
     * @param captchaKey
     * @throws Exception
     */
    @Override
    public void createCaptcha(String captchaKey) throws IOException, FontFormatException {
        getRequestAndResponse();
        //进行判断是否传入key
        if (StringUtils.isEmpty(captchaKey) || !(captchaKey.length() < 13)) {
            return;
        }
        long key = 01;
        try {
            key = Long.parseLong(captchaKey);
        } catch (Exception e) {
            return;
        }
        //可以用了

        // 设置请求头为输出图片类型
        response.setContentType("image/gif");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        // 三个参数分别为宽、高、位数
        SpecCaptcha specCaptcha = new SpecCaptcha(200, 60, 5);
        // 设置字体
        // specCaptcha.setFont(new Font("Verdana", Font.PLAIN, 32));  // 有默认字体，可以不用设置
        specCaptcha.setFont(Captcha.FONT_1);
        // 设置类型，纯数字、纯字母、字母数字混合
        specCaptcha.setCharType(Captcha.TYPE_DEFAULT);

        String content = specCaptcha.text().toLowerCase();
        log.info("captcha content == > " + content);
        // 验证码存入redis , 5 分钟内有效
        //删除时机
        //1.自然过期，比如 10 分钟后过期
        //2.验证码用完后删除
        //3.用完的情况：有get的地方
        redisUtil.set(Constants.User.KEY_CAPTCHA_CONTENT + key, content, 60 * 5);
        // 输出图片流
        specCaptcha.out(response.getOutputStream());
    }

    /**
     * 发送邮件验证码
     * 使用场景：注册、找回密码、修改邮箱（输入新的邮箱）
     * 注册(register)：如果已经注册过，就提示该邮箱注册过了
     * 找回密码(forget)：如果没有注册过，就提示该邮箱没有注册
     * 修改邮箱（输入新的邮箱）(update)：如果已经注册过，就提示该邮箱注册过了
     *
     * @param type
     * @param emailAddress
     * @return
     */
    @Override
    public ResponseResult sendEmail(String type, String emailAddress) {
        getRequestAndResponse();
        if (StringUtils.isEmpty(emailAddress)) {
            return ResponseResult.FAILED("邮箱地址不可以为空");
        }
        QueryWrapper<User> wrapper = null;
        //根据类型进行查询邮箱是否存在。
        if ("register".equals(type) || "update".equals(type)) {
            wrapper = new QueryWrapper<>();
            wrapper.eq("email", emailAddress);
            User oneByEmail = userMapper.selectOne(wrapper);
            if (!StringUtils.isEmpty(oneByEmail)) {
                return ResponseResult.FAILED("该邮箱已被注册！...");
            }
        } else if ("forget".equals(type)) {
            wrapper = new QueryWrapper<>();
            wrapper.eq("email", emailAddress);
            User oneByEmail = userMapper.selectOne(wrapper);
            if (StringUtils.isEmpty(oneByEmail)) {
                return ResponseResult.FAILED("该邮箱未被注册！...");
            }
        } else {
            return ResponseResult.FAILED("未标明操作类型");
        }
        //1.防止暴力发送，就是不断发生：同一个邮箱 ，间隔要超过30s ,同一个ip , 1h 最多只能发10次（短信，你最多发3次）。
        //TODO 获取不到IP
        String remoteAddr = request.getRemoteAddr();
//        String remoteAddr = getIpAddr(request);
        if (!StringUtils.isEmpty(remoteAddr)) {
            remoteAddr = remoteAddr.replace(":", "_");
        }
        log.info("sendEmail ==> ip ==> " + remoteAddr);
        //从redis拿出来，如果没有，那就过了 。主要判断 IP 地址 和 发送邮箱地址次数
        Integer ipSendTime = null;

        String o = (String) redisUtil.get(Constants.User.KEY_EMAIL_SEND_IP + remoteAddr);
        if (StringUtils.isEmpty(o)) {
            ipSendTime = null;
        } else {
            ipSendTime = Integer.valueOf(o);
        }

        log.info("ipSendTime : " + (String) redisUtil.get(Constants.User.KEY_EMAIL_SEND_IP + remoteAddr));
        if (!StringUtils.isEmpty(ipSendTime) && ipSendTime > 10) {
            //如果有，判断次数
            return ResponseResult.FAILED("ip...请不要发送太频繁！这验证码还没来得及产生...");
        }
        Object addressSendTime = redisUtil.get(Constants.User.KEY_EMAIL_SEND_ADDRESS + emailAddress);
        log.info((String) redisUtil.get(Constants.User.KEY_EMAIL_SEND_ADDRESS + emailAddress));
        if (!StringUtils.isEmpty(addressSendTime)) {
            return ResponseResult.FAILED("address...请不要发送太频繁！这验证码还没来得及产生...");
        }
        //2.检查邮箱地址是否正确
        boolean isEamil = ValidateUtil.validateEamil(emailAddress);
        if (!isEamil) {
            return ResponseResult.FAILED("邮箱格式不正确");
        }
        //3.发送验证码 , 6位数 ： 100000-999999
        Random random = new Random();
        int code = random.nextInt(999999);
        if (code < 100000) {
            code += 100000;
        }
        try {
            taskService.sendEmailVerifyCode(String.valueOf(code), emailAddress);
        } catch (Exception e) {
            return ResponseResult.FAILED("验证码发送失败,请稍后再试");
        }
        //4.做记录
        //发送记录：code
        if (StringUtils.isEmpty(ipSendTime)) {
            ipSendTime = 0;
        } else {
            ipSendTime = ipSendTime + 1;
        }
        // 五分钟有效期
        redisUtil.set(Constants.User.KEY_EMAIL_SEND_IP + remoteAddr, String.valueOf(ipSendTime), 60 * 5);
        //30秒内不让重新发送
        redisUtil.set(Constants.User.KEY_EMAIL_SEND_ADDRESS + emailAddress, "true", 30);
        //保存code
        redisUtil.set(Constants.User.KEY_EMAIL_CODE_CONTENT + emailAddress, String.valueOf(code), 60 * 5);
        return ResponseResult.SUCCESS("发送成功！");
    }

    @Override
    public ResponseResult checkEmail(String email) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("email", email);
        User oneByEmail = userMapper.selectOne(userQueryWrapper);
        return oneByEmail == null ? ResponseResult.SUCCESS("该邮箱未注册.") : ResponseResult.FAILED("该邮箱已经注册.");
    }

    @Override
    public ResponseResult checkUserName(String userName) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("user_name", userName);
        User oneByEmail = userMapper.selectOne(userQueryWrapper);
        return oneByEmail == null ? ResponseResult.SUCCESS("该用户名未注册.") : ResponseResult.FAILED("该用户名已经注册.");

    }

    @Transactional
    @Override
    public User checkUser() {
        getRequestAndResponse();
        //1.拿到tokenKey
        String tokenKey = CookieUtils.getCookie(request, Constants.User.COOKIE_TOKE_KEY);
        User parseByTokenKey = parseByTokenKey(tokenKey);

        if (StringUtils.isEmpty(parseByTokenKey)) {
            //根据 refreshToken 去判断是否已经登陆过了
            //说明出错了，过期了
            //1.去数据库查询 refreshToken
            QueryWrapper<RefreshToken> tokenQueryWrapper = new QueryWrapper<>();
            tokenQueryWrapper.eq("token_key", tokenKey);
            RefreshToken refreshToken = refreshTokenMapper.selectOne(tokenQueryWrapper);
            //2.如果不存在，就重新登陆
            if (StringUtils.isEmpty(refreshToken)) {
                return null;
            }
            //3.如果存在，就解析 refreshToken
            try {
                Claims claims = JwtUtil.parseJWT(refreshToken.getRefreshToken());
                //5.如果 refreshToken 有效，创建新的token 和新的 refreshToken
                QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
                userQueryWrapper.eq("id", refreshToken.getUserId());
                User user = userMapper.selectOne(userQueryWrapper);
                //千万别这么干，事务还没提交，这样做数据库密码就没了
                // user.setPassword("");
                // 删掉 refreshToken 的记录
                tokenQueryWrapper = new QueryWrapper<>();
                tokenQueryWrapper.eq("id", refreshToken.getId());
                refreshTokenMapper.delete(tokenQueryWrapper);
                String tokenKey1 = createToken(user);
                //返回token
                return parseByTokenKey(tokenKey1);
            } catch (Exception exception) {
                //4.如果 refreshToken 过期了，就当前访问没有登录，提示用户登录
                return null;
            }
        }

        return parseByTokenKey;
    }

    /**
     * 通过tokenKey 解析 token 获取 user对象
     *
     * @param tokenKey
     * @return
     */
    private User parseByTokenKey(String tokenKey) {
        String token = (String) redisUtil.get(Constants.User.KEY_TOKEN + tokenKey);
        if (!StringUtils.isEmpty(token)) {
            try {
                Claims claims = JwtUtil.parseJWT(token);
                return ClaimsUtils.claims2ToUser(claims);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    /**
     * 登陆
     *
     * @param captcha
     * @param captchaKey
     * @param user
     * @return
     */
    @Override
    public ResponseResult login(String captcha, String captchaKey, User user) {
        getRequestAndResponse();
        log.info(Constants.User.KEY_CAPTCHA_CONTENT);
        //1.验证图灵验证码
        String code = (String) redisUtil.get(Constants.User.KEY_CAPTCHA_CONTENT + captchaKey);
        if (!captcha.equals(code)) {
            return ResponseResult.FAILED("登陆验证码错误......");
        }
        //验证成功，删除
        redisUtil.del(Constants.User.KEY_CAPTCHA_CONTENT + captchaKey);
        //2.用户名
        String username = user.getUserName();
        if (StringUtils.isEmpty(username)) {
            return ResponseResult.FAILED("登陆用户名不为空......");
        }
        //3.密码
        String password = user.getPassword();
        if (StringUtils.isEmpty(password)) {
            return ResponseResult.FAILED("登陆密码不为空......");
        }
        //4.查用户，有可能是账号，有可能是邮箱
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("user_name", username);
        User oneByUsername = userMapper.selectOne(wrapper);
        if (StringUtils.isEmpty(oneByUsername)) {
            wrapper = new QueryWrapper<>();
            wrapper.eq("email", user.getEmail());
            oneByUsername = userMapper.selectOne(wrapper);
            if (StringUtils.isEmpty(oneByUsername)) {
                return ResponseResult.FAILED("用户名或密码错误......");
            }
        }
        //用户存在
        //对比密码
        boolean matches = BCrypt.checkpw(password, oneByUsername.getPassword());
        if (!matches) {
            return ResponseResult.FAILED("用户名或密码错误......");
        }
        //密码正确
        //判断用户状态是否正常。
        if (!oneByUsername.getState().equals("1")) {
            return ResponseResult.GET(ResponseState.ACCOUNT_FORBID);
        }

        createToken(oneByUsername);

        return ResponseResult.SUCCESS("登陆成功");
    }

    /**
     * 更新密码
     *
     * @param verifyCode
     * @param user
     * @return
     */
    @Override
    public ResponseResult updateUserPassword(String verifyCode, User user) {
        getRequestAndResponse();
        //检查邮箱是否有填写
        if (StringUtils.isEmpty(user.getEmail())) {
            return ResponseResult.FAILED("邮箱不可以为空");
        }
        //根据邮箱去 rdis 拿验证码
        String code = (String) redisUtil.get(Constants.User.KEY_EMAIL_CODE_CONTENT + user.getEmail());
        //进行比对
        if (StringUtils.isEmpty(verifyCode) || !verifyCode.equals(code)) {
            return ResponseResult.FAILED("验证码错误......");
        }
        //干掉资源,删除redis里的验证码
        redisUtil.del(Constants.User.KEY_EMAIL_CODE_CONTENT + user.getEmail());
        //修改密码
        QueryWrapper<User> wrapper = new QueryWrapper<User>();
        wrapper.eq("id", user.getId()).eq("email", user.getEmail());
        user.setPassword(BCrypt.hashpw(user.getPassword()));
        int i = userMapper.update(user, wrapper);
        return i > 0 ? ResponseResult.SUCCESS("密码修改成功") : ResponseResult.FAILED("密码修改失败");
    }

    /**
     * 获取用户信息
     *
     * @param userId
     * @return
     */
    @Override
    public ResponseResult getUserInfo(String userId) {
        getRequestAndResponse();
        //1.从数据库获取
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("id", userId);

        User one = userMapper.selectOne(userQueryWrapper);
        //判断结果
        if (StringUtils.isEmpty(one)) {
            //如果不存在
            return ResponseResult.FAILED("用户不存在......");
        }
        //如果存在，就复制对象清空密码、email、登陆IP 注册IP
        String userJson = gson.toJson(one);
        User newUser = gson.fromJson(userJson, User.class);
        newUser.setPassword("");
        newUser.setAvatar("");
        newUser.setRegIp("");
        newUser.setLoginIp("");
        //返回结果
        return ResponseResult.SUCCESS("获取成功", newUser);
    }

    /**
     * 生成token,抽取出来的方法
     *
     * @param oneByUsername
     * @return
     */
    @Transactional
    String createToken(User oneByUsername) {
        getRequestAndResponse();
        QueryWrapper<RefreshToken> refreshTokenQueryWrapper = new QueryWrapper<>();
        refreshTokenQueryWrapper.eq("user_id", oneByUsername.getId());
        int i = refreshTokenMapper.delete(refreshTokenQueryWrapper);
        log.info("createNew+" + i);
        //TODO 生成token
        Map<String, Object> cliams = new HashMap<>();
        //默认一个小时
        cliams.put("id", oneByUsername.getId());
        cliams.put("user_name", oneByUsername.getUserName());
        cliams.put("roles", oneByUsername.getRoles());
        cliams.put("avatar", oneByUsername.getAvatar());
        cliams.put("email", oneByUsername.getEmail());
        cliams.put("sign", oneByUsername.getSign());
        String token = JwtUtil.createToken(cliams);
        // 返回一个token 的 md5 值 key ，保存在redis中。
        String tokenKey = DigestUtils.md5DigestAsHex(token.getBytes());
        //保存在redis 中 , 1个小时有效期 , key 是 tokenKey
        redisUtil.set(Constants.User.KEY_TOKEN + tokenKey, token, Constants.TimeValue.HOUR);
        //把token 写入 cookies 里
        Cookie cookie = new Cookie(Constants.User.COOKIE_TOKE_KEY, tokenKey);
        //TODO 动态获取域名，可以从request里获取
        cookie.setDomain("localhost");
        //设置存活时间
        cookie.setPath("/");
        cookie.setMaxAge(Constants.User.COOKIE_TOKE_AGE);
        response.addCookie(cookie);
        // 前端访问时，携带 md5 key ，后端从 redis中取出 token

        //TODO 生成 refreshToken
        String refreshToken = JwtUtil.createRefreshToken(oneByUsername.getId(), Constants.TimeValue.MONTH);
        //TODO 保存到数据库中
        // refreshToken tokenKey 用户ID 创建时间 更新时间
        RefreshToken refreshTokenBean = new RefreshToken();
        refreshTokenBean.setId(String.valueOf(idWorker.nextId()));
        refreshTokenBean.setRefreshToken(refreshToken);
        refreshTokenBean.setTokenKey(tokenKey);
        refreshTokenBean.setUserId(oneByUsername.getId());
        refreshTokenBean.setCreateTime(new Date());
        refreshTokenBean.setUpdateTime(new Date());
        refreshTokenMapper.insert(refreshTokenBean);
        return tokenKey;
    }
}
