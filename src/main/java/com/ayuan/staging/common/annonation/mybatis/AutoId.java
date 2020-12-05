package com.ayuan.staging.common.annonation.mybatis;

import java.lang.annotation.*;

/**
 * 作用于实体类上的主键字段生成注解
 * @author sYuan
 */
@Target(value = ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AutoId {
    String name();
}
