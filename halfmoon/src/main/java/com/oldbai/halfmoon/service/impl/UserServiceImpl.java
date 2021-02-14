package com.oldbai.halfmoon.service.impl;

import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.oldbai.blog.utils.Constants;
import com.oldbai.halfmoon.entity.Settings;
import com.oldbai.halfmoon.entity.User;
import com.oldbai.halfmoon.mapper.SettingsMapper;
import com.oldbai.halfmoon.mapper.UserMapper;
import com.oldbai.halfmoon.response.ResponseResult;
import com.oldbai.halfmoon.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oldbai.halfmoon.util.RedisUtil;
import com.oldbai.halfmoon.util.TaskService;
import com.oldbai.halfmoon.util.ValidateUtil;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.Date;
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
    UserMapper userMapper;
    @Autowired
    SettingsMapper settingsMapper;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    private TaskService taskService;

    HttpServletRequest request = null;
    HttpServletResponse response = null;

    public void getRequestAndResponse() {
        this.request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        this.response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();

    }

    /**
     * 初始化管理员账号
     *
     * @param user
     * @return
     */
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
     * 注册功能
     *
     * @param user
     * @param emailCode
     * @param captcha
     * @param captchaKey
     * @return
     */
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
        QueryWrapper<User> wrapper =null;
        //根据类型进行查询邮箱是否存在。
        if ("register".equals(type) || "update".equals(type)) {
            wrapper = new QueryWrapper<>();
            wrapper.eq("email",emailAddress);
            User oneByEmail = userMapper.selectOne(wrapper);
            if (!StringUtils.isEmpty(oneByEmail)) {
                return ResponseResult.FAILED("该邮箱已被注册！...");
            }
        } else if ("forget".equals(type)) {
            wrapper = new QueryWrapper<>();
            wrapper.eq("email",emailAddress);
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
}
