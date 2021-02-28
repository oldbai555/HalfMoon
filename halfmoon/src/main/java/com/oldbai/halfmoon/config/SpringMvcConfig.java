package com.oldbai.halfmoon.config;

import com.oldbai.halfmoon.interceptor.ApiInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * springMVC的配置类
 *
 * @author 老白
 */
@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {
    @Autowired
    private ApiInterceptor apiInterceptor;

    /**
     * 添加拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(apiInterceptor);
    }
}
