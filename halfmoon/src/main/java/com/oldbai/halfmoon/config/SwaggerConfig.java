package com.oldbai.halfmoon.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author 老白
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    boolean isEnable = false;

    //版本
    public static final String VERSION = "1.0.0";

    /**
     * 门户api，接口前缀：portal
     *
     * @return
     */
    @Bean
    public Docket portalApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(isEnable)
                .apiInfo(portalApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.oldbai.halfmoon.controller.portal"))
                // 可以根据url路径设置哪些请求加入文档，忽略哪些请求
                .paths(PathSelectors.any())
                .build()
                .groupName("前端门户");
    }

    private ApiInfo portalApiInfo() {
        return new ApiInfoBuilder()
                //设置文档的标题
                .title("月半湾博客系统门户接口文档")
                // 设置文档的描述
                .description("门户接口文档")
                // 设置文档的版本信息-> 1.0.0 Version information
                .version(VERSION)
                .build();
    }


    /**
     * 管理中心api，接口前缀：admin
     *
     * @return
     */
    @Bean
    public Docket adminApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(isEnable)
                .apiInfo(adminApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.oldbai.halfmoon.controller.admin"))
                // 可以根据url路径设置哪些请求加入文档，忽略哪些请求
                .paths(PathSelectors.any())
                .build()
                .groupName("管理中心");
    }


    private ApiInfo adminApiInfo() {
        return new ApiInfoBuilder()
                //设置文档的标题
                .title("月半湾管理中心接口文档")
                // 设置文档的描述
                .description("管理中心接口")
                // 设置文档的版本信息-> 1.0.0 Version information
                .version(VERSION)
                .build();
    }


    @Bean
    public Docket UserApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(isEnable)
                .apiInfo(userApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.oldbai.halfmoon.controller.user"))
                // 可以根据url路径设置哪些请求加入文档，忽略哪些请求
                .paths(PathSelectors.any())
                .build()
                .groupName("用户中心");
    }

    private ApiInfo userApiInfo() {
        return new ApiInfoBuilder()
                //设置文档的标题
                .title("月半湾博客系统用户接口")
                // 设置文档的描述
                .description("用户接口的接口")
                // 设置文档的版本信息-> 1.0.0 Version information
                .version(VERSION)
                .build();
    }

}
