package com.github.tianjing.tgtools.security.sql.bean;

import org.springframework.beans.factory.annotation.Value;

/**
 * @author 田径
 * @date 2019-12-21 20:34
 * @desc
 **/
public class SecuritySqlInjectionConfigProperty {

    @Value("${app.security.injection.sql.ignore-path:}")
    protected  String[] ignorePath;

    public String[] getIgnorePath() {
        return ignorePath;
    }

    public void setIgnorePath(String[] pIgnorePath) {
        ignorePath = pIgnorePath;
    }

}
