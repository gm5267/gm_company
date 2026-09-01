package com.gm.ace.tenant;

/**
 * 登录用户上下文（线程级），供自动填充监听器写入 create_by / update_by
 *
 * @author guoym
 */
public final class LoginUserContext {

    private static final ThreadLocal<Long> UID = new ThreadLocal<>();

    private LoginUserContext() {
    }

    public static void set(Long userId) {
        UID.set(userId);
    }

    public static Long get() {
        return UID.get();
    }

    public static void clear() {
        UID.remove();
    }
}
