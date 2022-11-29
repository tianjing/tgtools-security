package com.github.tianjing.tgtools.security.sql.filter;

import org.springframework.security.web.access.AccessDeniedHandler;

public interface SqlInjectionProcess {

    AccessDeniedHandler getAccessDeniedHandler();

    boolean validTextFiledContent(String pContent);

}
