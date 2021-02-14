package com.oldbai.halfmoon.service;

import com.oldbai.halfmoon.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.oldbai.halfmoon.response.ResponseResult;

import java.awt.*;
import java.io.IOException;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author oldbai
 * @since 2021-02-14
 */
public interface UserService extends IService<User> {

    ResponseResult initManagerAccount(User user);

    ResponseResult register(User user, String emailCode, String captcha, String captchaKey);

    void createCaptcha(String captchaKey) throws IOException, FontFormatException;

    ResponseResult sendEmail(String type, String emailAddress);

    ResponseResult checkEmail(String email);

    ResponseResult checkUserName(String userName);
}