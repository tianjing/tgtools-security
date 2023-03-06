package com.github.tianjing.tgtools.security.sql.filter;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.tianjing.tgtools.security.sql.bean.SecuritySqlInjectionConfigProperty;
import com.github.tianjing.tgtools.security.util.wrapper.JsonHttpServletRequestWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfException;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.MimeTypeUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import tgtools.util.StringUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * @author 田径
 * @date 2019-07-05 9:41
 * @desc
 **/

public class SqlInjectionFilter extends OncePerRequestFilter {

    protected RequestMatcher requireCsrfProtectionMatcher = new DefaultRequiresCsrfMatcher();
    protected List<RequestMatcher> ignoringAntPathMatcher = null;
    protected SecuritySqlInjectionConfigProperty configProperty;

    @Autowired(required = false)
    protected SqlInjectionProcess sqlInjectionProcess;

    public SqlInjectionFilter(SecuritySqlInjectionConfigProperty pSecuritySqlInjectionConfigProperty) {
        configProperty = pSecuritySqlInjectionConfigProperty;
        ignoringAntPathMatcher = antMatchers(configProperty.getIgnorePath());
    }


    public SqlInjectionFilter(SecuritySqlInjectionConfigProperty pSecurityCsrfConfigProperty, boolean pUseMinRequiresMatcher) {
        configProperty = pSecurityCsrfConfigProperty;
        ignoringAntPathMatcher = antMatchers(configProperty.getIgnorePath());
        if (pUseMinRequiresMatcher) {
            requireCsrfProtectionMatcher = new MinRequiresMatcher();
        }
    }


    protected String getErrorMessage() {
        return "不安全的请求S 检测不通过，非本站请求无法处理！";
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
        if (Objects.isNull(sqlInjectionProcess)) {
            sqlInjectionProcess = new DefaultSqlInjectionProcess();
        }
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

        if (Objects.nonNull(pRequest.getContentType()) && StringUtil.contains(pRequest.getContentType(), MimeTypeUtils.APPLICATION_JSON.toString())) {
            if (!(pRequest instanceof JsonHttpServletRequestWrapper)) {
                pRequest = new JsonHttpServletRequestWrapper(pRequest);
            }
            validJsonParam((JsonHttpServletRequestWrapper) pRequest, pResponse);
        } else if (Objects.nonNull(pRequest.getContentType()) && StringUtil.contains(pRequest.getContentType(), "application/x-www-form-urlencoded")) {
            validUrlEncoded(pRequest, pResponse);
        }

        pFilterChain.doFilter(pRequest, pResponse);
    }

    protected void validJsonParam(JsonHttpServletRequestWrapper pRequest, HttpServletResponse pResponse) throws ServletException, IOException {
        JsonNode vJson = pRequest.getRequestJsonNode();
        if (Objects.isNull(vJson)) {
            return;
        }

        if (vJson.isObject()) {
            validJsonObject((ObjectNode) vJson, pRequest, pResponse);
        }
        if (vJson.isArray()) {
            validJsonArray((ArrayNode) vJson, pRequest, pResponse);
        }
    }

    protected void validJsonObject(ObjectNode pJson, HttpServletRequest pRequest, HttpServletResponse pResponse) throws ServletException, IOException {
        if (Objects.isNull(pJson)) {
            return;
        }
        Iterator<String> vNames = pJson.fieldNames();
        while (vNames.hasNext()) {
            String vName = vNames.next();
            JsonNode vValue = pJson.get(vName);
            if (vValue.isObject()) {
                validJsonObject((ObjectNode) vValue, pRequest, pResponse);
            } else if (vValue.isArray()) {
                validJsonArray((ArrayNode) vValue, pRequest, pResponse);
            } else if (vValue.isTextual()) {
                validContent(vValue.toString(), pRequest, pResponse);
            } else {
                continue;
            }
        }


    }

    protected void validJsonArray(ArrayNode pJson, HttpServletRequest pRequest, HttpServletResponse pResponse) throws ServletException, IOException {
        if (Objects.isNull(pJson)) {
            return;
        }
        for (JsonNode vJson : pJson) {
            if (vJson.isObject()) {
                validJsonObject((ObjectNode) vJson, pRequest, pResponse);
            } else if (vJson.isArray()) {
                validJsonArray((ArrayNode) vJson, pRequest, pResponse);
            } else if (vJson.isTextual()) {
                validContent(vJson.toString(), pRequest, pResponse);
            } else {
                continue;
            }
        }

    }

    protected void validContent(String pContent, HttpServletRequest pRequest, HttpServletResponse pResponse) throws ServletException, IOException {
        if (!this.sqlInjectionProcess.validTextFiledContent(pContent)) {
            this.sqlInjectionProcess.getAccessDeniedHandler().handle(pRequest, pResponse,
                    new ErrorRefererException(getErrorMessage()));
        }
    }

    protected void validUrlEncoded(HttpServletRequest pRequest, HttpServletResponse pResponse) throws ServletException, IOException {
        Map<String, String[]> vParameterMap = pRequest.getParameterMap();
        if (Objects.isNull(vParameterMap) || vParameterMap.size() < 1) {
            return;
        }

        for (Map.Entry<String, String[]> vItem : vParameterMap.entrySet()) {
            if (Objects.isNull(vItem.getValue()) || vItem.getValue().length < 1) {
                continue;
            }

            for (String vValue : vItem.getValue()) {
                validContent(vValue, pRequest, pResponse);
            }
        }

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

    static final class MinRequiresMatcher implements RequestMatcher {
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
