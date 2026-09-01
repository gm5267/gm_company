package com.gm.ace.common.constant;

import java.util.TimeZone;

/**
 * 全局常量
 *
 * @author guoym
 */
public final class AceConst {

    private AceConst() {
    }

    /**
     * 系统默认时区
     * <p>
     * Date 与 LocalDateTime 互转统一使用，避免隐式依赖 JVM 默认时区
     */
    public static final TimeZone TIME_ZONE = TimeZone.getTimeZone("Asia/Shanghai");

    /** 逻辑删除：正常 */
    public static final int NOT_DELETED = 0;

    /** 逻辑删除：已删除 */
    public static final int DELETED = 1;

    /** 状态：启用 */
    public static final int STATUS_ENABLE = 1;

    /** 状态：停用 */
    public static final int STATUS_DISABLE = 0;

    /** 数据库模式名 */
    public static final String DB_SCHEMA = "gm_company";
}
