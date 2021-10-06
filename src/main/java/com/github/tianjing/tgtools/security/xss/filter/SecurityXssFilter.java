package com.github.tianjing.tgtools.security.xss.filter;


import com.github.tianjing.tgtools.security.util.handler.NotAcceptableAccessDeniedHandlerImpl;
import com.github.tianjing.tgtools.security.util.matcher.DataRequiresMatcher;
import com.github.tianjing.tgtools.security.util.wrapper.JsonHttpServletRequestWrapper;
import com.github.tianjing.tgtools.security.xss.bean.SecurityXssConfigProperty;
import com.github.tianjing.tgtools.security.xss.util.XssHelper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Xss 验证 过滤器
 * 如果包含 非法字符则返回403
 *
 * @author 田径
 * @date 2019-07-05 9:41
 * @desc
 **/
public class SecurityXssFilter extends OncePerRequestFilter {
    private AccessDeniedHandler accessDeniedHandler = new NotAcceptableAccessDeniedHandlerImpl();
    private RequestMatcher requireCsrfProtectionMatcher = new DataRequiresMatcher();
    private List<RequestMatcher> ignoringAntPathMatcher;
    private SecurityXssConfigProperty securityXssConfigProperty;

    public SecurityXssFilter(SecurityXssConfigProperty pSecurityXssConfigProperty) {
        securityXssConfigProperty = pSecurityXssConfigProperty;
        ignoringAntPathMatcher = antMatchers(pSecurityXssConfigProperty.getIgnorePath());

    }

    protected List<RequestMatcher> antMatchers(String... antPatterns) {
        List<RequestMatcher> matchers = new ArrayList<>();

        if (null != securityXssConfigProperty.getIgnorePath() && securityXssConfigProperty.getIgnorePath().length > 0) {
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


        try {
            if (pRequest.getContentType().contains(MimeTypeUtils.APPLICATION_JSON_VALUE)) {
                pRequest = new JsonHttpServletRequestWrapper(pRequest);
                String vContent = ((JsonHttpServletRequestWrapper) pRequest).getRequestJsonBody();
                if (XssHelper.matchXssContent(vContent)) {
                    this.accessDeniedHandler.handle(pRequest, pResponse,
                            new AccessDeniedException("不安全的请求X 检测不通过，非本站请求无法处理！"));
                    return;
                }
            }
        } catch (Exception e) {

        }

        pFilterChain.doFilter(pRequest, pResponse);
        return;

    }
}
