package com.github.tianjing.tgtools.security.xss.filter;


import com.github.tianjing.tgtools.security.util.handler.NotAcceptableAccessDeniedHandlerImpl;
import com.github.tianjing.tgtools.security.xss.util.XssHelper;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * Xss 验证 过滤器
 * 如果包含 非法字符则返回403
 *
 * @author 田径
 * @date 2019-07-05 9:41
 * @desc
 **/
public class DefaultSecurityXssFilterProcess implements SecurityXssFilterProcess {
    protected NotAcceptableAccessDeniedHandlerImpl notAcceptableAccess = new NotAcceptableAccessDeniedHandlerImpl();

    @Override
    public AccessDeniedHandler getAccessDeniedHandler() {
        return notAcceptableAccess;
    }

    @Override
    public boolean validTextFiledContent(String pContent) {
        return !XssHelper.matchXssContent(pContent);
    }
}
