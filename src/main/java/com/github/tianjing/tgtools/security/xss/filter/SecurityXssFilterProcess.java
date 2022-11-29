package com.github.tianjing.tgtools.security.xss.filter;


import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * Xss 验证 过滤器
 * 如果包含 非法字符则返回403
 *
 * @author 田径
 * @date 2019-07-05 9:41
 * @desc
 **/
public interface SecurityXssFilterProcess {

    AccessDeniedHandler getAccessDeniedHandler();

    boolean validTextFiledContent(String pContent);
}
