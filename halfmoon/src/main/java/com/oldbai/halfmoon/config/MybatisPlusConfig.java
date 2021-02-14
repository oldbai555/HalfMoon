package com.oldbai.halfmoon.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @EnableTransactionManagement 开启事务管理
 * @author 老白
 */
@Configuration
@EnableTransactionManagement
@MapperScan("com.oldbai.halfmoon.mapper")
public class MybatisPlusConfig {

}
