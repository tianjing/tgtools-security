package com.github.tianjing.tgtools.security.util.matcher;

import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashSet;

/**
 * 数据请求匹配器
 * 除 "GET", "HEAD", "TRACE", "OPTIONS" 以外的所有请求。
 * @author 田径
 * @date 2020-12-28 9:15
 * @desc
 **/
public class DataRequiresMatcher implements RequestMatcher  {

    private final HashSet<String> allowedMethods = new HashSet<>(
            Arrays.asList("GET", "HEAD", "TRACE", "OPTIONS"));

    @Override
    public boolean matches(HttpServletRequest request) {
        return !this.allowedMethods.contains(request.getMethod());
    }
}
