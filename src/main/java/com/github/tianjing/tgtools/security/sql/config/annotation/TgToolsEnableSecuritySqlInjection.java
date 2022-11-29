package com.github.tianjing.tgtools.security.sql.config.annotation;

import com.github.tianjing.tgtools.security.sql.config.SecuritySqlInjectionConfig;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author tian_
 */
@Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(value = {java.lang.annotation.ElementType.TYPE})
@Documented
@ImportAutoConfiguration({
        SecuritySqlInjectionConfig.class})
public @interface TgToolsEnableSecuritySqlInjection {
}
