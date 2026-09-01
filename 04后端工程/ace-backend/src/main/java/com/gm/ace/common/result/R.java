package com.gm.ace.common.result;

import com.gm.ace.common.trace.TraceIdFilter;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.MDC;

import java.io.Serializable;

/**
 * 统一返回体
 * <p>
 * 所有静态工厂均为泛型方法，返回 {@code R<T>} 而非具体类型，
 * 以保证 {@code AbstractActionTemplate} 中 {@code R.status(bool)} 可赋值给 {@code R<Response>}。
 *
 * @param <T> 数据类型
 * @author guoym
 */
@Getter
@Setter
public class R<T> implements Serializable {

    /** 业务状态码 */
    private int code;

    /** 提示消息 */
    private String message;

    /** 业务数据 */
    private T data;

    /** 时间戳 */
    private long timestamp;

    /** 链路追踪 ID，创建时从 MDC 自动注入 */
    private String traceId;

    public R() {
        this.timestamp = System.currentTimeMillis();
        this.traceId = MDC.get(TraceIdFilter.TRACE_ID);
    }

    public R(ResultCode resultCode) {
        this();
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }

    public R(ResultCode resultCode, String message) {
        this();
        this.code = resultCode.getCode();
        this.message = message;
    }

    // ==================== 泛型静态工厂 ====================

    /** 成功，携带数据 */
    public static <T> R<T> data(T data) {
        R<T> r = new R<>(ResultCode.SUCCESS);
        r.setData(data);
        return r;
    }

    /** 成功，无数据 */
    public static <T> R<T> ok() {
        return new R<>(ResultCode.SUCCESS);
    }

    /**
     * 按布尔结果返回，用于删除、启用禁用等无数据返回的场景
     * <p>
     * 注意返回类型是 {@code R<T>} 而非 {@code R<Boolean>}，
     * 否则 AbstractActionTemplate 中无法赋值给 {@code R<Response>}
     */
    public static <T> R<T> status(boolean success) {
        return new R<>(success ? ResultCode.SUCCESS : ResultCode.BIZ_ERROR);
    }

    /** 失败，使用返回码默认文案 */
    public static <T> R<T> fail(ResultCode resultCode) {
        return new R<>(resultCode);
    }

    /** 失败，自定义文案 */
    public static <T> R<T> fail(ResultCode resultCode, String message) {
        return new R<>(resultCode, message);
    }
}
