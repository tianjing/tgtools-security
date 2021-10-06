package com.github.tianjing.tgtools.security.replayattack.mvc.bean;

import org.springframework.beans.factory.annotation.Value;

/**
 * @author 田径
 * @date 2019-12-21 20:34
 * @desc
 **/
public class ReplayAttackConfigProperty {
    public static final String CACHE_NAME = "ReplayAttackToken";


    @Value("${app.security.replay-attack.encrypt-id:sm4c}")
    protected String encryptId;

    /**
     * token 密钥
     */
    @Value("${app.security.replay-attack.encrypt-key:@tianjing123*}")
    protected String encryptKey;

    /**
     * 暂时没用
     * spring cache manage  比如 redis ehcache
     */
    @Value("${app.security.replay-attack.cache-manage-name:}")
    protected String cacheManageName;

    /**
     * cache name  使用前 请 添加
     * 默认使用
     */
    @Value("${app.security.replay-attack.cache-name:ReplayAttackToken}")
    protected String cacheName;

    /**
     * 过期时间
     */
    @Value("${app.security.replay-attack.cache-ttl:300}")
    protected int cacheTtl;

    /**
     * 是否启用时间戳验证
     */
    @Value("${app.security.replay-attack.use-valid-time:false}")
    protected boolean useValidTime;

    /**
     * 允许的时间戳 间隔
     */
    @Value("${app.security.replay-attack.valid-time-second:60}")
    protected int validTimeSecond;

    @Value("${app.security.replay-attack.ignore-path:}")
    protected String[] ignorePath;


    public String getEncryptId() {
        return encryptId;
    }

    public void setEncryptId(String pEncryptId) {
        encryptId = pEncryptId;
    }

    public void init() {
        setEncryptId("sm4c");
        setEncryptKey("@tianjing123*");
    }

    public String getEncryptKey() {
        return encryptKey;
    }

    public void setEncryptKey(String encryptKey) {
        this.encryptKey = encryptKey;
    }

    public String getCacheManageName() {
        return cacheManageName;
    }

    public void setCacheManageName(String cacheManageName) {
        this.cacheManageName = cacheManageName;
    }

    public String getCacheName() {
        return cacheName;
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

    public int getCacheTtl() {
        return cacheTtl;
    }

    public void setCacheTtl(int cacheTtl) {
        this.cacheTtl = cacheTtl;
    }

    public boolean getUseValidTime() {
        return useValidTime;
    }

    public void setUseValidTime(boolean pUseValidTime) {
        useValidTime = pUseValidTime;
    }

    public int getValidTimeSecond() {
        return validTimeSecond;
    }

    public void setValidTimeSecond(int pValidTimeSecond) {
        validTimeSecond = pValidTimeSecond;
    }

    public String[] getIgnorePath() {
        return ignorePath;
    }

    public void setIgnorePath(String[] pIgnorePath) {
        ignorePath = pIgnorePath;
    }
}
