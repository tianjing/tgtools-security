package com.github.tianjing.tgtools.security.util.wrapper;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.util.MimeTypeUtils;
import tgtools.util.StringUtil;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.nio.charset.Charset;

/**
 * @author 田径
 * @date 2019-10-17 16:07
 * @desc
 **/
public class JsonHttpServletRequestWrapper extends HttpServletRequestWrapper {
    protected ServletInputStream inputStream;
    protected String requestJsonBody;

    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request The request to wrap
     * @throws IllegalArgumentException if the request is null
     */
    public JsonHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    public JsonNode getRequestJsonNode() throws IOException {
        if (StringUtil.isNullOrEmpty(requestJsonBody)) {
            this.getInputStream();
        }
        return tgtools.util.JsonParseHelper.getMapper(false).readTree(requestJsonBody);
    }

    public String getRequestJsonBody() throws IOException {
        if (StringUtil.isNullOrEmpty(requestJsonBody)) {
            this.getInputStream();
        }
        return requestJsonBody;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (null == inputStream) {
            if (this.getContentType().contains(MimeTypeUtils.APPLICATION_JSON_VALUE)) {
                String vContent = getRequestBody(super.getInputStream());
                requestJsonBody = vContent;
                inputStream = new ByteArrayServletInputStream(new ByteArrayInputStream(vContent.getBytes("UTF-8")));
            } else {
                inputStream = super.getInputStream();
            }
        }
        return inputStream;
    }

    private String getRequestBody(InputStream pStream) {
        String line = "";
        StringBuilder body = new StringBuilder();
        int counter = 0;
        // 读取POST提交的数据内容
        BufferedReader reader = new BufferedReader(new InputStreamReader(pStream, Charset.forName("UTF-8")));
        try {
            while ((line = reader.readLine()) != null) {
                body.append(line);
                counter++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return body.toString();
    }


    public static class ByteArrayServletInputStream extends ServletInputStream {
        ByteArrayInputStream byteArrayInputStream;

        private ByteArrayServletInputStream(ByteArrayInputStream pByteArrayInputStream) {
            byteArrayInputStream = pByteArrayInputStream;
        }

        @Override
        public boolean isFinished() {
            return false;
        }

        @Override
        public boolean isReady() {
            return false;
        }

        @Override
        public void setReadListener(ReadListener listener) {

        }

        @Override
        public int read() throws IOException {
            return byteArrayInputStream.read();
        }
    }
}
