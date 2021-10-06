package com.github.tianjing.tgtools.security.xss.bean;

import org.springframework.beans.factory.annotation.Value;

/**
 * @author 田径
 * @date 2019-12-21 20:34
 * @desc
 **/
public class SecurityXssConfigProperty {

    @Value("${app.security.xss.ignore-path:}")
    protected  String[] ignorePath;

    public String[] getIgnorePath() {
        return ignorePath;
    }

    public void setIgnorePath(String[] pIgnorePath) {
        ignorePath = pIgnorePath;
    }
}
