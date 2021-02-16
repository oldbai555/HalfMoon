package com.oldbai.halfmoon.util;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 专做异步操作的服务类
 * @author 老白
 */
@Service
public class TaskService {
    @Async
    public void sendEmailVerifyCode(String verifyCode, String emailAddress) throws Exception {
        EmailSender.sendRegisterVerifyCode(verifyCode, emailAddress);
    }
}
