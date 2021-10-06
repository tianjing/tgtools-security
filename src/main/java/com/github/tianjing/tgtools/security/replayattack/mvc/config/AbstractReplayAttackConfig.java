package com.github.tianjing.tgtools.security.replayattack.mvc.config;

import com.github.tianjing.tgtools.security.replayattack.mvc.bean.ReplayAttackConfigProperty;
import com.github.tianjing.tgtools.security.replayattack.mvc.filter.ReplayAttackFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * @author 田径
 * @date 2019-12-21 20:33
 * @desc
 **/
public abstract class AbstractReplayAttackConfig {
    @Autowired
    ApplicationContext applicationContext;

    @Bean
    public ReplayAttackFilter replayAttackTokenFilter(ReplayAttackConfigProperty pReplayAttackConfigProperty, @Qualifier("replayAttackCache") Cache pReplayAttackCache) {
        return new ReplayAttackFilter(pReplayAttackConfigProperty, pReplayAttackCache);
    }

    @Bean
    public ReplayAttackConfigProperty replayAttackTokenConfigProperty() {
        return new ReplayAttackConfigProperty();
    }

}
