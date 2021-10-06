package com.github.tianjing.tgtools.security.util.handler;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.util.MimeTypeUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author 田径
 * @date 2019-12-22 14:31
 * @desc
 **/
public class NotAcceptableAccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException,
            ServletException {

        response.reset();
        response.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MimeTypeUtils.TEXT_PLAIN_VALUE);

        PrintWriter vPrintWriter = response.getWriter();
        vPrintWriter.write(HttpStatus.NOT_ACCEPTABLE.getReasonPhrase() + " " + accessDeniedException.getMessage());
        vPrintWriter.close();
    }

}
