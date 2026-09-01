package com.gm.ace.security.handler;

import com.gm.ace.common.result.ResultCode;
import com.gm.ace.common.util.JsonResponseWriter;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 未授权（403）统一返回 JSON 返回体 {@link R}，替代默认 BearerTokenAccessDeniedHandler 的空 body。
 *
 * @author guoym
 */
@Component
public class JsonAccessDeniedHandler implements AccessDeniedHandler {

    @Resource
    private JsonResponseWriter jsonResponseWriter;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        jsonResponseWriter.write(response, HttpServletResponse.SC_FORBIDDEN, ResultCode.FORBIDDEN);
    }
}
