package com.github.tianjing.tgtools.security.replayattack.mvc.config;

import com.github.tianjing.tgtools.security.replayattack.mvc.bean.ReplayAttackConfigProperty;
import org.springframework.cache.Cache;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author 田径
 * @date 2019-12-21 20:33
 * @desc
 **/
public class ReplayAttackRedisCacheConfig extends AbstractReplayAttackConfig {


    @Bean
    public Cache replayAttackCache(RedisConnectionFactory factory, ReplayAttackConfigProperty pReplayAttackTokenConfigProperty) {
        RedisCacheConfiguration vDefaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig();
        //设置默认超过期时间是30秒
        vDefaultCacheConfig.entryTtl(Duration.ofSeconds(30));
        //初始化RedisCacheManager

        Map<String, RedisCacheConfiguration> vConfigMap = new HashMap<>();
        vConfigMap.put(pReplayAttackTokenConfigProperty.getCacheName(), vDefaultCacheConfig.entryTtl(Duration.ofSeconds(pReplayAttackTokenConfigProperty.getCacheTtl())));
        Set<String> vCacheNames = new HashSet<>();
        vCacheNames.add(pReplayAttackTokenConfigProperty.getCacheName());

        RedisCacheManager fd;

        RedisCacheManager vRedisCacheManager = RedisCacheManager.builder(factory).initialCacheNames(vCacheNames).withInitialCacheConfigurations(vConfigMap).build();
        return vRedisCacheManager.getCache(pReplayAttackTokenConfigProperty.getCacheName());
    }

}
