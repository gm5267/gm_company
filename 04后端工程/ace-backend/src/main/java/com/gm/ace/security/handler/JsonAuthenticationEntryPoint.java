package com.gm.ace.security.handler;

import com.gm.ace.common.result.ResultCode;
import com.gm.ace.common.util.JsonResponseWriter;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 未认证（401）统一返回 JSON 返回体 {@link R}，替代默认 BearerTokenAuthenticationEntryPoint 的空 body。
 *
 * @author guoym
 */
@Component
public class JsonAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Resource
    private JsonResponseWriter jsonResponseWriter;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        jsonResponseWriter.write(response, HttpServletResponse.SC_UNAUTHORIZED, ResultCode.UNAUTHORIZED);
    }
}
