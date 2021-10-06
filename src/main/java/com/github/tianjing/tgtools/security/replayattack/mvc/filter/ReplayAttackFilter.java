package com.github.tianjing.tgtools.security.replayattack.mvc.filter;

import com.github.tianjing.tgtools.encrypt.EncrypterFactory;
import com.github.tianjing.tgtools.encrypt.spring.security.DelegatingEncrypter;
import com.github.tianjing.tgtools.security.replayattack.mvc.bean.ReplayAttackConfigProperty;
import com.github.tianjing.tgtools.security.util.handler.NotAcceptableAccessDeniedHandlerImpl;
import com.github.tianjing.tgtools.security.util.matcher.DataRequiresMatcher;
import org.springframework.cache.Cache;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import tgtools.exceptions.APPErrorException;
import tgtools.json.JSONObject;
import tgtools.util.StringUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 田径
 * @date 2019-07-05 9:41
 * @desc
 **/

public class ReplayAttackFilter extends OncePerRequestFilter {
    protected static final String TOKEN_NAME_UPPER = "SECURITY_TOKEN";
    protected static final String TOKEN_NAME_LOWER = TOKEN_NAME_UPPER.toLowerCase();
    protected static final String TOKEN_NAME1_UPPER = "SECURITY-TOKEN";
    protected static final String TOKEN_NAME1_LOWER = TOKEN_NAME1_UPPER.toLowerCase();
    protected static final String CACHE_VALUE = "1";
    protected static final String CACHE_NAME = "replayAttackCache";

    protected AccessDeniedHandler accessDeniedHandler = new NotAcceptableAccessDeniedHandlerImpl();
    protected RequestMatcher requireMatcher = new DataRequiresMatcher();
    protected List<RequestMatcher> ignoringAntPathMatcher = null;
    protected ReplayAttackConfigProperty replayAttackTokenConfigProperty;
    protected String encryptKey;
    protected String encryptType;
    protected DelegatingEncrypter delegatingEncrypter;
    protected Cache cache;


    public ReplayAttackFilter(ReplayAttackConfigProperty pReplayAttackTokenConfigProperty, Cache pCache) {
        replayAttackTokenConfigProperty = pReplayAttackTokenConfigProperty;
        encryptKey = replayAttackTokenConfigProperty.getEncryptKey();
        encryptType = "{" + replayAttackTokenConfigProperty.getEncryptId() + "}";
        cache = pCache;
        try {
            delegatingEncrypter = tgtools.web.platform.Platform.getBeanFactory().getBean(DelegatingEncrypter.class);
        } catch (Throwable e) {
            delegatingEncrypter = EncrypterFactory.createDelegatingEncrypter(replayAttackTokenConfigProperty.getEncryptId(), replayAttackTokenConfigProperty.getEncryptKey());
        }


        ignoringAntPathMatcher = antMatchers(replayAttackTokenConfigProperty.getIgnorePath());
    }

    protected List<RequestMatcher> antMatchers(String... antPatterns) {
        List<RequestMatcher> matchers = new ArrayList<>();
        for (String pattern : antPatterns) {
            matchers.add(new AntPathRequestMatcher(pattern, null));
        }
        return matchers;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest pRequest, HttpServletResponse pResponse, FilterChain pFilterChain) throws ServletException, IOException {
        //请求方法
        if (!this.requireMatcher.matches(pRequest)) {
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

        String vToken = getToken(pRequest);

        if (StringUtil.isEmpty(vToken)) {
            this.accessDeniedHandler.handle(pRequest, pResponse,
                    new AccessDeniedException("不安全的请求 检测不通过，非本站请求无法处理！"));
            return;
        }


        if (null == cache) {
            throw new IOException("无效的 cacheManager；不安全的请求 无法工作");
        }

        if (null == cache.get(vToken)) {
            try {
                String vJson = delegatingEncrypter.decode(encryptType + vToken);
                validDate(replayAttackTokenConfigProperty, vJson);
                cache.put(vToken, CACHE_VALUE);
                pFilterChain.doFilter(pRequest, pResponse);
                return;
            } catch (APPErrorException e) {
                this.accessDeniedHandler.handle(pRequest, pResponse,
                        new AccessDeniedException("不安全的请求 检测不通过，非本站请求无法处理！", e));
                return;
            }

        }


        this.accessDeniedHandler.handle(pRequest, pResponse,
                new AccessDeniedException("不安全的请求 检测不通过，非本站请求无法处理！"));
        return;

    }

    protected String getToken(HttpServletRequest pRequest) {
        String vToken = pRequest.getHeader(TOKEN_NAME_UPPER);
        if (!StringUtil.isNullOrEmpty(vToken)) {
            return vToken;
        }

        vToken = pRequest.getHeader(TOKEN_NAME_LOWER);
        if (!StringUtil.isNullOrEmpty(vToken)) {
            return vToken;
        }

        vToken = pRequest.getHeader(TOKEN_NAME1_UPPER);
        if (!StringUtil.isNullOrEmpty(vToken)) {
            return vToken;
        }


        vToken = pRequest.getHeader(TOKEN_NAME1_LOWER);
        if (!StringUtil.isNullOrEmpty(vToken)) {
            return vToken;
        }

        return vToken;
    }

    protected void validDate(ReplayAttackConfigProperty pReplayAttackTokenConfigProperty, String pJson) throws APPErrorException {
        if (!pReplayAttackTokenConfigProperty.getUseValidTime()) {
            return;
        }


        JSONObject vJson = new JSONObject(pJson);
        if (!vJson.has(StringUtil.EMPTY_STRING)) {
            throw new APPErrorException("缺少时间戳！");
        }

        Date vDate = tgtools.util.DateUtil.parseFullLongmtime(vJson.getString(StringUtil.EMPTY_STRING));
        long vTime = vDate.getTime();
        long vMaxTime = System.currentTimeMillis() + (pReplayAttackTokenConfigProperty.getValidTimeSecond() * 1000);
        long vMinTime = System.currentTimeMillis() - (pReplayAttackTokenConfigProperty.getValidTimeSecond() * 1000);

        //验证时间
        if (vMinTime > vTime || vTime > vMaxTime) {
            throw new APPErrorException("时间戳不符合要求");
        }
    }

}
