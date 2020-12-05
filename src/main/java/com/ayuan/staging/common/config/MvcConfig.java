package com.ayuan.staging.common.config;

import com.ayuan.staging.common.interceptor.TokenInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 配置 spring mvc
 * @author sYuan
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {


    /**
     * 注入token拦截器
     * @return tokenInterceptor
     */
    @Bean
    public TokenInterceptor tokenInterceptor(){
        return new TokenInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 对所有请求进行拦截
        registry.addInterceptor(tokenInterceptor()).addPathPatterns("/**").excludePathPatterns("/swagger-resources/configuration/ui").excludePathPatterns("/swagger-resources");
    }

    /**
     * 全局跨域请求配置
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowCredentials(true)
                .allowedMethods("*")
                .maxAge(3600);
    }
}
