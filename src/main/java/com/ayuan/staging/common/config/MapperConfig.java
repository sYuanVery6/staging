package com.ayuan.staging.common.config;

import com.ayuan.staging.common.interceptor.AutoIdInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
@MapperScan(basePackages = "com.ayuan.staging.dao")
public class MapperConfig {

    @Bean
    public AutoIdInterceptor autoIdInterceptor(){

        AutoIdInterceptor autoIdInterceptor = new AutoIdInterceptor();
        //设置参数，比如阈值等，可以在配置文件中配置，这里直接写死便于测试
        Properties properties = new Properties();
        //这里设置慢查询阈值为1毫秒，便于测试
        properties.setProperty("idStrategy","100");
        autoIdInterceptor.setProperties(properties);

        return autoIdInterceptor;
    }
}
