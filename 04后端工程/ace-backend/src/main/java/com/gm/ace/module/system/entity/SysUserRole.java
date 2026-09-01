package com.gm.ace.module.system.entity;

import com.gm.ace.common.base.TenantEntity;
import com.gm.ace.common.fill.BaseEntityInsertListener;
import com.gm.ace.common.fill.BaseEntityUpdateListener;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 用户-角色关联表
 *
 * @author guoym
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table(value = "sys_user_role", onInsert = BaseEntityInsertListener.class, onUpdate = BaseEntityUpdateListener.class)
public class SysUserRole extends TenantEntity implements Serializable {

    private Long userId;
    private Long roleId;
}
