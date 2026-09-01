package com.gm.ace.common.exception;

import com.gm.ace.common.result.ResultCode;
import com.gm.ace.common.util.JsonResponseWriter;
import jakarta.annotation.Resource;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.webmvc.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

/**
 * 容器级错误（含真实 500、404、405 等）统一返回 JSON 返回体 {@link R}，
 * 替代 Spring Boot 默认的白标错误页。实现 ErrorController 后 Boot 不再注册 BasicErrorController。
 *
 * @author guoym
 */
@Controller
public class JsonErrorController implements ErrorController {

    @Resource
    private JsonResponseWriter jsonResponseWriter;

    @RequestMapping("/error")
    public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Object statusAttr = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        int status = statusAttr instanceof Integer i ? i : HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        jsonResponseWriter.write(response, status, toResultCode(status));
    }

    private ResultCode toResultCode(int status) {
        return switch (status) {
            case 400 -> ResultCode.PARAM_ERROR;
            case 401 -> ResultCode.UNAUTHORIZED;
            case 403 -> ResultCode.FORBIDDEN;
            case 404 -> ResultCode.NOT_FOUND;
            case 405 -> ResultCode.BIZ_ERROR;
            default -> ResultCode.SYSTEM_ERROR;
        };
    }
}
