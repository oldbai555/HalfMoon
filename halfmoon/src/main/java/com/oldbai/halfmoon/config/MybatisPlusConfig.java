package com.oldbai.halfmoon.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@MapperScan("com.oldbai.halfmoon.mapper")
public class MybatisPlusConfig {

}
