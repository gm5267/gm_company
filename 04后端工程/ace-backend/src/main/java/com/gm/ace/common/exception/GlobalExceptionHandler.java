package com.gm.ace.common.exception;

import com.gm.ace.common.result.R;
import com.gm.ace.common.result.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import jakarta.annotation.Resource;
import java.util.stream.Collectors;

/**
 * 全局异常处理器（全工程唯一）
 * <p>
 * 统一把异常包装为 {@link R}；校验错误信息经 MessageSource 国际化。
 *
 * @author guoym
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @Resource
    private MessageSource messageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<Void> handleValid(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + resolve(err.getDefaultMessage()))
                .collect(Collectors.joining("; "));
        return R.fail(ResultCode.PARAM_ERROR, msg);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public R<Void> handleMethodValid(HandlerMethodValidationException e) {
        return R.fail(ResultCode.PARAM_ERROR, "参数校验失败");
    }

    @ExceptionHandler(BizException.class)
    public R<Void> handleBiz(BizException e) {
        return R.fail(e.getResultCode(), resolve(e.getMessage()));
    }

    @ExceptionHandler(SystemException.class)
    public R<Void> handleSystem(SystemException e) {
        log.error("系统异常", e);
        return R.fail(ResultCode.SYSTEM_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public R<Void> handleException(Exception e) {
        log.error("未捕获异常", e);
        return R.fail(ResultCode.SYSTEM_ERROR);
    }

    /** 把 {key} 形式占位符翻译为当前语言文案；非占位符原样返回 */
    private String resolve(String message) {
        if (message == null || !message.startsWith("{") || !message.endsWith("}")) {
            return message;
        }
        return messageSource.getMessage(message.substring(1, message.length() - 1), null, LocaleContextHolder.getLocale());
    }
}
