package com.gm.ace.common.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 返回码枚举
 * <p>
 * 除 401 / 403 / 404 / 405 走 HTTP 状态码外，其余业务失败统一返回 HTTP 200，
 * 由 {@link R#getCode()} 承载业务状态，便于前端拦截器统一处理。
 *
 * @author guoym
 */
@Getter
@AllArgsConstructor
public enum ResultCode {

    /** 成功 */
    SUCCESS(200, "成功"),

    /** 业务失败 */
    BIZ_ERROR(1000, "业务失败"),

    /** 参数错误 */
    PARAM_ERROR(1001, "参数错误"),

    /** 未认证 */
    UNAUTHORIZED(401, "未认证或令牌已失效"),

    /** 未授权 */
    FORBIDDEN(403, "没有访问权限"),

    /** 资源不存在 */
    NOT_FOUND(404, "资源不存在"),

    /** 系统异常 */
    SYSTEM_ERROR(500, "系统繁忙，请稍后重试");

    private final int code;

    private final String message;
}
