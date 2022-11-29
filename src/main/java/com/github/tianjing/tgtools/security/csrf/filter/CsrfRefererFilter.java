package com.github.tianjing.tgtools.security.csrf.filter;


import com.github.tianjing.tgtools.security.csrf.bean.SecurityCsrfConfigProperty;
import com.github.tianjing.tgtools.security.util.handler.NotAcceptableAccessDeniedHandlerImpl;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.csrf.CsrfException;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import tgtools.util.StringUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * @author 田径
 * @date 2019-07-05 9:41
 * @desc
 **/

public class CsrfRefererFilter extends OncePerRequestFilter {
    protected AccessDeniedHandler accessDeniedHandler = new NotAcceptableAccessDeniedHandlerImpl();
    protected RequestMatcher requireCsrfProtectionMatcher = new DefaultRequiresCsrfMatcher();
    protected List<RequestMatcher> ignoringAntPathMatcher = null;
    protected SecurityCsrfConfigProperty securityCsrfConfigProperty;

    public CsrfRefererFilter(SecurityCsrfConfigProperty pSecurityCsrfConfigProperty) {
        securityCsrfConfigProperty = pSecurityCsrfConfigProperty;
        ignoringAntPathMatcher = antMatchers(pSecurityCsrfConfigProperty.getRefererIgnorePath());
    }


    public CsrfRefererFilter(SecurityCsrfConfigProperty pSecurityCsrfConfigProperty, boolean pUseMinRequiresCsrfMatcher) {
        securityCsrfConfigProperty = pSecurityCsrfConfigProperty;
        ignoringAntPathMatcher = antMatchers(securityCsrfConfigProperty.getRefererIgnorePath());
        if (pUseMinRequiresCsrfMatcher) {
            requireCsrfProtectionMatcher = new MinRequiresCsrfMatcher();
        }
    }

    protected String getValidHeader() {
        return "Referer";
    }
    protected String getErrorMessage() {
        return "不安全的请求C 检测不通过，非本站请求无法处理！";
    }


    protected List<RequestMatcher> antMatchers(String... antPatterns) {
        List<RequestMatcher> matchers = new ArrayList<>();

        if (StringUtils.isEmpty(antPatterns)) {
            return matchers;
        }

        for (String pattern : antPatterns) {
            matchers.add(new AntPathRequestMatcher(pattern, null));
        }
        return matchers;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest pRequest, HttpServletResponse pResponse, FilterChain pFilterChain) throws ServletException, IOException {
        //请求方法
        if (!this.requireCsrfProtectionMatcher.matches(pRequest)) {
            pFilterChain.doFilter(pRequest, pResponse);
            return;
        }

        //请求路径
        boolean vValidPath = false;
        for (RequestMatcher vMatcher : ignoringAntPathMatcher) {
            if (vMatcher.matches(pRequest)) {
                vValidPath = true;
                break;
            }
        }
        if (vValidPath) {
            pFilterChain.doFilter(pRequest, pResponse);
            return;
        }

        if (validHeader(pRequest)) {
            pFilterChain.doFilter(pRequest, pResponse);
            return;
        }
        this.accessDeniedHandler.handle(pRequest, pResponse,
                new ErrorRefererException(getErrorMessage()));

    }

    protected boolean validHeader(HttpServletRequest pRequest) {
        String vReferer = pRequest.getHeader(getValidHeader());

        if (null == vReferer) {
            return true;
        }

        if ("".equals(vReferer)) {
            return false;
        }
        //默认按请求的ip / ServerName 对比
        if (null == securityCsrfConfigProperty.getOriginIgnorePath() || securityCsrfConfigProperty.getOriginWriteList().length < 1) {
            return (vReferer != null) && (vReferer.trim().startsWith("https://" + pRequest.getServerName()) || vReferer.trim().startsWith("http://" + pRequest.getServerName()));
        }
        //白名单对比
        else {
            for (String vItem : securityCsrfConfigProperty.getOriginWriteList()) {
                if (StringUtil.equals(vItem, pRequest.getServerName())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static class ErrorRefererException extends CsrfException {

        public ErrorRefererException(String pMessage) {
            super(pMessage);
        }
    }


    static final class DefaultRequiresCsrfMatcher implements RequestMatcher {
        private final HashSet<String> allowedMethods = new HashSet<>(
                Arrays.asList("HEAD", "TRACE", "OPTIONS"));//"GET",

        @Override
        public boolean matches(HttpServletRequest request) {
            return !this.allowedMethods.contains(request.getMethod());
        }
    }

    static final class MinRequiresCsrfMatcher implements RequestMatcher {
        private final HashSet<String> allowedMethods = new HashSet<>(
                Arrays.asList("POST", "PUT", "DELETE", "HEAD", "TRACE", "OPTIONS"));//"GET",

        @Override
        public boolean matches(HttpServletRequest request) {
            return !this.allowedMethods.contains(request.getMethod());
        }
    }

    private static final class AllRequiresCsrfMatcher implements RequestMatcher {
        private final HashSet<String> allowedMethods = new HashSet<>(
                Arrays.asList("POST", "PUT", "DELETE", "HEAD", "TRACE", "OPTIONS"));//"GET",

        @Override
        public boolean matches(HttpServletRequest request) {
            return !this.allowedMethods.contains(request.getMethod());
        }
    }
}
