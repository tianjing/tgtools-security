package com.github.tianjing.tgtools.security.replayattack.mvc.config;

import com.github.tianjing.tgtools.security.replayattack.mvc.bean.ReplayAttackConfigProperty;
import net.sf.ehcache.CacheManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.annotation.Bean;

/**
 * @author 田径
 * @date 2019-12-21 20:33
 * @desc
 **/
public class ReplayAttackEhCacheConfig extends AbstractReplayAttackConfig {


    @Bean
    public Cache replayAttackCache(@Qualifier("ehcacheManager") CacheManager pCacheManager, ReplayAttackConfigProperty pReplayAttackTokenConfigProperty) {
        EhCacheCacheManager vCacheManage = new EhCacheCacheManager();
        vCacheManage.setCacheManager(pCacheManager);
        net.sf.ehcache.Cache vCache = new net.sf.ehcache.Cache(pReplayAttackTokenConfigProperty.getCacheName(),
                99999999, false, false, (long) pReplayAttackTokenConfigProperty.getCacheTtl(), (long) 900);
        pCacheManager.addCache(vCache);
        return vCacheManage.getCache(pReplayAttackTokenConfigProperty.getCacheName());
    }
}
