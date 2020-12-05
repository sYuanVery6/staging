package com.ayuan.staging.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;


/**
 * @author sYuan
 * @ClassName SwaggerConfig
 * @Date 2020-11-16
 * @Description Swagger配置文件
 * @Remark 使用地址：http://localhost:7777/swagger-ui.html
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {


    @Bean
    public Docket createRestApi() {
        List<ApiKey> apiKeyList= new ArrayList();
        apiKeyList.add(new ApiKey("token", "token", "header"));
        return new Docket(DocumentationType.SWAGGER_2)
                .securitySchemes(apiKeyList)
                .pathMapping("/")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ayuan.staging.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(new ApiInfoBuilder()
                        .title("myStaging")
                        .description("swagger-接口测试文档")
                        .version("v_1.0")
                        .contact(new Contact("ayuan", "www.ayuan.com", "1908925010@qq.com"))
                        .license("theStaging")
                        .licenseUrl("http://www.baidu.com")
                        .build());
    }

    /**
     * 给swagger设置全局token
     * @return apiKeyList
     */
    private List<ApiKey> securitySchemes() {
        List<ApiKey> apiKeyList= new ArrayList();
        apiKeyList.add(new ApiKey("token", "token", "header"));
        return apiKeyList;
    }



}
