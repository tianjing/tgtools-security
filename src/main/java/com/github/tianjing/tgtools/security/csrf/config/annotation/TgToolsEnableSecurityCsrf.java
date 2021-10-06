package com.github.tianjing.tgtools.security.csrf.config.annotation;

import com.github.tianjing.tgtools.security.csrf.config.SecurityCsrfConfig;
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
        SecurityCsrfConfig.class})
public @interface TgToolsEnableSecurityCsrf {
}
