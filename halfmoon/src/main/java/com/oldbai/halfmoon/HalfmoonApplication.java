package com.oldbai.halfmoon;

import com.oldbai.halfmoon.util.SnowflakeIdWorker;
import org.apache.solr.client.solrj.SolrClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HalfmoonApplication {

    public static void main(String[] args) {
        SpringApplication.run(HalfmoonApplication.class, args);
    }

    /**
     * 雪花算法注入
     */
    @Bean
    public SnowflakeIdWorker createIdWorker() {
        return new SnowflakeIdWorker(0, 1);
    }


}
