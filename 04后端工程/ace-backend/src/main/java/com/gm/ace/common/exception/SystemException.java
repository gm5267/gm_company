package com.gm.ace.common.exception;

/**
 * 系统异常（预期外错误，需记录全量堆栈，响应仅返回泛化提示）
 *
 * @author guoym
 */
public class SystemException extends RuntimeException {

    public SystemException(String message) {
        super(message);
    }

    public SystemException(String message, Throwable cause) {
        super(message, cause);
    }
}
