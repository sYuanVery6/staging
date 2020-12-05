package com.ayuan.staging.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
/**
 * @author sYuan
 * @ClassName Constant
 * @Date 2020-11-16
 * @Description 常量类
 * @Remark 通过配置文件注入值
 */
@Component
@ConfigurationProperties(prefix = "constant")
public class Constant {

    private String projectName;

    private String version;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
