package com.oldbai.halfmoon.interceptor;

import com.google.gson.Gson;
import com.oldbai.halfmoon.response.ResponseResult;
import com.oldbai.halfmoon.util.Constants;
import com.oldbai.halfmoon.util.CookieUtils;
import com.oldbai.halfmoon.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 做一个拦截器
 *
 * @author 老白
 */
@Slf4j
@Component
public class ApiInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    RedisUtil redisUtils;

    @Autowired
    Gson gson;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //instanceof 严格来说是Java中的一个双目运算符，用来测试一个对象是否为一个类的实例
        if (handler instanceof HandlerMethod) {
            //某一些提交的请求需要拦截
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            CheckTooFrequentCommit methodAnnotation = handlerMethod.getMethodAnnotation(CheckTooFrequentCommit.class);
            if (methodAnnotation != null) {
                String methodName = handlerMethod.getMethod().getName();
                //所有提交内容的方法，必须用户登录的，所以使用token作为key来记录请求频率
                String tokenKey = CookieUtils.getCookie(request, Constants.User.COOKIE_TOKE_KEY);
                log.info("tokenKey -||- > " + tokenKey);
                if (!StringUtils.isEmpty(tokenKey)) {
                    String hasCommit = (String) redisUtils.get(Constants.User.KEY_COMMIT_TOKEN_RECORD + tokenKey + methodName);
                    if (!StringUtils.isEmpty(hasCommit)) {
                        //从redis里获取，判断是否存在，如果存在，则返回提交太频繁
                        response.setCharacterEncoding("UTF-8");
                        response.setContentType("application/json");
                        ResponseResult failed = ResponseResult.FAILED("提交过于频繁,请稍后重试." + methodName);
                        PrintWriter writer = response.getWriter();
                        writer.write(gson.toJson(failed));
                        writer.flush();
                        return false;
                    } else {
                        //如果不存在，说明可以提交，并且记录此次提交，有效期为30秒
                        redisUtils.set(Constants.User.KEY_COMMIT_TOKEN_RECORD + tokenKey + methodName,
                                "true", Constants.TimeValue.HALFT_MIN);
                    }
                }
                //去判断是否真提交太频繁了
                log.info("check commit too frequent...");
            }
        }
        //true表示放行
        //false表示拦截
        return true;
    }
}
