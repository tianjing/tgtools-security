package com.github.tianjing.tgtools.security.csrf.filter;


import com.github.tianjing.tgtools.security.csrf.bean.SecurityCsrfConfigProperty;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 田径
 * @date 2019-07-05 9:41
 * @desc
 **/

public class CsrfOriginFilter extends CsrfRefererFilter {

    public CsrfOriginFilter(SecurityCsrfConfigProperty pSecurityCsrfConfigProperty) {
        super(pSecurityCsrfConfigProperty);
        ignoringAntPathMatcher = antMatchers(pSecurityCsrfConfigProperty.getOriginIgnorePath());
    }


    public CsrfOriginFilter(SecurityCsrfConfigProperty pSecurityCsrfConfigProperty, boolean pUseMinRequiresCsrfMatcher) {
        super(pSecurityCsrfConfigProperty, pUseMinRequiresCsrfMatcher);
        ignoringAntPathMatcher = antMatchers(securityCsrfConfigProperty.getOriginIgnorePath());
        if (pUseMinRequiresCsrfMatcher) {
            requireCsrfProtectionMatcher = new MinRequiresCsrfMatcher();
        }
    }
    @Override
    protected String getErrorMessage() {
        return "不安全的请求O 检测不通过，非本站请求无法处理！";
    }

    @Override
    protected String getValidHeader() {
        return "Origin";
    }

    @Override
    protected boolean validHeader(HttpServletRequest pRequest) {
        String vHeaderValue = pRequest.getHeader(getValidHeader());

        if (null == vHeaderValue) {
            return true;
        }

        if ("".equals(vHeaderValue)) {
            return false;
        }

        //默认按请求的ip / ServerName 对比
        if (null == securityCsrfConfigProperty.getOriginIgnorePath() || securityCsrfConfigProperty.getOriginWriteList().length < 1) {
            return (vHeaderValue.indexOf("https://" + pRequest.getServerName()) >= 0 || vHeaderValue.indexOf("http://" + pRequest.getServerName()) >= 0);
        }

        //白名单对比
        else {
            for (String vItem : securityCsrfConfigProperty.getRefererWriteList()) {
                if (vHeaderValue.indexOf(vItem) >= 0) {
                    return true;
                }
            }
        }
        return false;
    }

}
