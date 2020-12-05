package com.ayuan.staging;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author sYuan
 */
@MapperScan(basePackages = "com.ayuan.staging.dao")
@SpringBootApplication
public class StagingApplication {

    public static void main(String[] args) {
        SpringApplication.run(StagingApplication.class, args);
    }

}
