package com.github.tianjing.tgtools.security.sql.filter;

import com.github.tianjing.tgtools.security.sql.SqlEntityValidHelper;
import com.github.tianjing.tgtools.security.util.handler.NotAcceptableAccessDeniedHandlerImpl;
import org.springframework.security.web.access.AccessDeniedHandler;

public class DefaultSqlInjectionProcess implements SqlInjectionProcess {

    protected NotAcceptableAccessDeniedHandlerImpl notAcceptableAccess = new NotAcceptableAccessDeniedHandlerImpl();

    @Override
    public AccessDeniedHandler getAccessDeniedHandler() {
        return notAcceptableAccess;
    }

    @Override
    public boolean validTextFiledContent(String pContent) {
        try {
            SqlEntityValidHelper.validString(pContent);
            return true;
        }catch (Throwable e){
            return false;
        }
    }
}
