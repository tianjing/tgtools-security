package com.github.tianjing.tgtools.security.replayattack.mvc.config;

import net.sf.ehcache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.util.ResourceUtils;
import tgtools.cache.CacheFactory;
import tgtools.log.LoggerFactory;

import java.net.URL;

/**
 * @author 田径
 * @date 2021-02-01 14:54
 * @desc
 **/
public class EhcacheConnectionConfig {
    @Bean
    public CacheManager ehcacheManager() {
        try {
            URL url = ResourceUtils.getURL("classpath:config/ehcache.xml");
            CacheFactory.init(url);
            return CacheFactory.getCacheManager();
        } catch (Exception var2) {
            LoggerFactory.getDefault().error("缓存初始化失败；原因：" + var2.getMessage(), var2);
            return null;
        }
    }
}
