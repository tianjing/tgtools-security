package com.github.tianjing.tgtools.security.replayattack.mvc.config.annotation;

import com.github.tianjing.tgtools.security.replayattack.mvc.config.ReplayAttackEhCacheConfig;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author
 */
@Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(value = {java.lang.annotation.ElementType.TYPE})
@Documented
@ImportAutoConfiguration({
        ReplayAttackEhCacheConfig.class})
public @interface TgToolsEnableEhCacheReplayAttack {
}
