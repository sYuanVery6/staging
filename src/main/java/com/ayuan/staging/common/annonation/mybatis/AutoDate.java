package com.ayuan.staging.common.annonation.mybatis;

import java.lang.annotation.*;

/**
 * @Description 自动注入创建时间和修改时间
 * @author sYuan
 */
@Target(value = ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AutoDate {
}
