package com.github.tianjing.tgtools.security.csrf.filter;


import com.github.tianjing.tgtools.security.csrf.bean.SecurityCsrfConfigProperty;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 田径
 * @date 2019-07-05 9:41
 * @desc
 **/

public class CsrfHostFilter extends CsrfRefererFilter {

    public CsrfHostFilter(SecurityCsrfConfigProperty pSecurityCsrfConfigProperty) {
        super(pSecurityCsrfConfigProperty);
        ignoringAntPathMatcher = antMatchers(pSecurityCsrfConfigProperty.getOriginIgnorePath());
    }


    public CsrfHostFilter(SecurityCsrfConfigProperty pSecurityCsrfConfigProperty, boolean pUseMinRequiresCsrfMatcher) {
        super(pSecurityCsrfConfigProperty, pUseMinRequiresCsrfMatcher);
        ignoringAntPathMatcher = antMatchers(securityCsrfConfigProperty.getOriginIgnorePath());
        if (pUseMinRequiresCsrfMatcher) {
            requireCsrfProtectionMatcher = new MinRequiresCsrfMatcher();
        }
    }

    @Override
    protected String getValidHeader() {
        return "Host";
    }

    @Override
    protected boolean validHeader(HttpServletRequest pRequest) {
        String vHeaderValue = pRequest.getHeader(getValidHeader());

        if (null == vHeaderValue) {
            return false;
        }

        if ("".equals(vHeaderValue)) {
            return false;
        }

        //默认按请求的ip / ServerName 对比
        if ( securityCsrfConfigProperty.getHostWhiteList().length < 1) {
            return (vHeaderValue.indexOf(pRequest.getServerName()) >= 0 ||pRequest.getServerName().indexOf(vHeaderValue) >= 0);
        }

        //白名单对比
        else {
            for (String vItem : securityCsrfConfigProperty.getHostWhiteList()) {
                if (vHeaderValue.indexOf(vItem) >= 0) {
                    return true;
                }
            }
        }
        return false;
    }

}
