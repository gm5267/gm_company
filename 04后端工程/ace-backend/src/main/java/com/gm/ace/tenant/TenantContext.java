package com.gm.ace.tenant;

/**
 * 租户上下文（线程级）
 * <p>
 * 由登录/请求鉴权阶段写入，MyBatis-Flex 租户处理器与填充监听器读取。
 * 异步线程需手动透传（详见架构设计说明书 6.4）。
 *
 * @author guoym
 */
public final class TenantContext {

    private static final ThreadLocal<Long> TENANT = new ThreadLocal<>();

    private TenantContext() {
    }

    public static void set(Long tenantId) {
        TENANT.set(tenantId);
    }

    public static Long get() {
        return TENANT.get();
    }

    public static void clear() {
        TENANT.remove();
    }
}
