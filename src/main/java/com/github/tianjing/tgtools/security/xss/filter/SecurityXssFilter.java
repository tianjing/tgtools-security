package com.github.tianjing.tgtools.security.xss.filter;


import com.github.tianjing.tgtools.security.sql.filter.SqlInjectionFilter;
import com.github.tianjing.tgtools.security.util.matcher.DataRequiresMatcher;
import com.github.tianjing.tgtools.security.util.wrapper.JsonHttpServletRequestWrapper;
import com.github.tianjing.tgtools.security.xss.bean.SecurityXssConfigProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.MimeTypeUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Xss 验证 过滤器
 * 如果包含 非法字符则返回403
 *
 * @author 田径
 * @date 2019-07-05 9:41
 * @desc
 **/
public class SecurityXssFilter extends OncePerRequestFilter {

    private RequestMatcher requireCsrfProtectionMatcher = new DataRequiresMatcher();
    private List<RequestMatcher> ignoringAntPathMatcher;
    private SecurityXssConfigProperty securityXssConfigProperty;

    @Autowired(required = false)
    protected SecurityXssFilterProcess securityXssFilterProcess;

    public SecurityXssFilter(SecurityXssConfigProperty pSecurityXssConfigProperty) {
        securityXssConfigProperty = pSecurityXssConfigProperty;
        ignoringAntPathMatcher = antMatchers(pSecurityXssConfigProperty.getIgnorePath());
    }

    protected String getErrorMessage() {
        return "不安全的请求X 检测不通过，非本站请求无法处理！";
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
        if (Objects.isNull(securityXssFilterProcess)) {
            securityXssFilterProcess = new DefaultSecurityXssFilterProcess();
        }
        //请求方法
        if (!this.requireCsrfProtectionMatcher.matches(pRequest)) {
            pFilterChain.doFilter(pRequest, pResponse);
            return;
        }

        //请求路径 判断
        if (ignoringAntPathMatcher.size() > 0) {
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
        }


        if (Objects.nonNull(pRequest.getContentType()) && pRequest.getContentType().contains(MimeTypeUtils.APPLICATION_JSON_VALUE)) {
            if (!(pRequest instanceof JsonHttpServletRequestWrapper)) {
                pRequest = new JsonHttpServletRequestWrapper(pRequest);
            }
            String vContent = ((JsonHttpServletRequestWrapper) pRequest).getRequestJsonBody();
            if (!validContent(vContent, pRequest, pResponse)) {
                return;
            }
        } else if (Objects.nonNull(pRequest.getContentType()) && pRequest.getContentType().contains("application/x-www-form-urlencoded")) {
            if (!validUrlEncoded(pRequest, pResponse)) {
                return;
            }
        }

        pFilterChain.doFilter(pRequest, pResponse);
        return;

    }

    protected boolean validUrlEncoded(HttpServletRequest pRequest, HttpServletResponse pResponse) throws ServletException, IOException {
        Map<String, String[]> vParameterMap = pRequest.getParameterMap();
        if (Objects.isNull(vParameterMap) || vParameterMap.size() < 1) {
            return true;
        }
        boolean vResult = true;
        for (Map.Entry<String, String[]> vItem : vParameterMap.entrySet()) {
            if (Objects.isNull(vItem.getValue()) || vItem.getValue().length < 1) {
                continue;
            }

            for (String vValue : vItem.getValue()) {
                vResult = validContent(vValue, pRequest, pResponse);
            }
            if (!vResult) {
                return vResult;
            }
        }
        return true;
    }

    protected boolean validContent(String pContent, HttpServletRequest pRequest, HttpServletResponse pResponse) throws ServletException, IOException {
        if (!this.securityXssFilterProcess.validTextFiledContent(pContent)) {
            this.securityXssFilterProcess.getAccessDeniedHandler().handle(pRequest, pResponse,
                    new SqlInjectionFilter.ErrorRefererException(getErrorMessage()));
            return false;
        }
        return true;
    }

}
