package com.github.tianjing.tgtools.security.csrf.bean;

import org.springframework.beans.factory.annotation.Value;

/**
 * @author 田径
 * @date 2019-12-21 20:34
 * @desc
 **/
public class SecurityCsrfConfigProperty {

    @Value("${app.security.csrf.ignore-path:}")
    protected  String[] refererIgnorePath;

    @Value("${app.security.csrf.write-list:}")
    protected  String[] refererWriteList;

    @Value("${app.security.csrf.origin.ignore-path:}")
    protected  String[] originIgnorePath;

    @Value("${app.security.csrf.origin.write-list:}")
    protected  String[] originWriteList;

    @Value("${app.security.csrf.host.white-list:}")
    protected  String[] hostWhiteList;

    public String[] getRefererIgnorePath() {
        return refererIgnorePath;
    }

    public void setRefererIgnorePath(String[] pRefererIgnorePath) {
        refererIgnorePath = pRefererIgnorePath;
    }

    public String[] getRefererWriteList() {
        return refererWriteList;
    }

    public void setRefererWriteList(String[] pRefererWriteList) {
        refererWriteList = pRefererWriteList;
    }

    public String[] getOriginIgnorePath() {
        return originIgnorePath;
    }

    public void setOriginIgnorePath(String[] pOriginIgnorePath) {
        originIgnorePath = pOriginIgnorePath;
    }

    public String[] getOriginWriteList() {
        return originWriteList;
    }

    public void setOriginWriteList(String[] pOriginWriteList) {
        originWriteList = pOriginWriteList;
    }

    public String[] getHostWhiteList() {
        return hostWhiteList;
    }

    public void setHostWhiteList(String[] pHostWhiteList) {
        hostWhiteList = pHostWhiteList;
    }
}
