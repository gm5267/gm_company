package com.gm.ace.module.system.entity;

import com.gm.ace.common.base.BaseEntity;
import com.gm.ace.common.fill.BaseEntityInsertListener;
import com.gm.ace.common.fill.BaseEntityUpdateListener;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 租户表（全局表，不带 tenant_id）
 *
 * @author guoym
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table(value = "sys_tenant", onInsert = BaseEntityInsertListener.class, onUpdate = BaseEntityUpdateListener.class)
public class SysTenant extends BaseEntity implements Serializable {

    private String tenantName;
    private String tenantCode;
    private String contact;
    private String phone;
    private Integer status;
    private LocalDateTime expireTime;
}
