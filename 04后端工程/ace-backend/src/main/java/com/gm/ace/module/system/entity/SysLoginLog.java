package com.gm.ace.module.system.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 登录日志（仅追加写，无逻辑删除）
 *
 * @author guoym
 */
@Data
@Table("sys_login_log")
public class SysLoginLog implements Serializable {

    @Id(keyType = KeyType.None)
    private Long id;

    @Column("tenant_id")
    private Long tenantId;

    @Column("user_id")
    private Long userId;

    private String username;
    private String ip;

    @Column("user_agent")
    private String userAgent;

    @Column("login_time")
    private LocalDateTime loginTime;

    @Column("login_status")
    private Integer loginStatus;

    private String msg;
}
