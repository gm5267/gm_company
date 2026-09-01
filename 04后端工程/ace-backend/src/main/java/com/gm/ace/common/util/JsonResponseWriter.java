package com.gm.ace.common.util;

import tools.jackson.databind.ObjectMapper;
import com.gm.ace.common.result.R;
import com.gm.ace.common.result.ResultCode;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 统一把 {@link R} 以 JSON 写回响应，并显式带 charset=UTF-8，
 * 供 Security 的 401/403 处理器与全局 ErrorController 复用，避免空 body 或白标错误页。
 *
 * @author guoym
 */
@Component
public class JsonResponseWriter {

    @Resource
    private ObjectMapper objectMapper;

    /** 用返回码默认文案写回 */
    public void write(HttpServletResponse response, int httpStatus, ResultCode code) throws IOException {
        write(response, httpStatus, code, code.getMessage());
    }

    /** 自定义文案写回 */
    public void write(HttpServletResponse response, int httpStatus, ResultCode code, String message) throws IOException {
        response.setStatus(httpStatus);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        R<Void> r = R.fail(code, message);
        response.getWriter().write(objectMapper.writeValueAsString(r));
    }
}
