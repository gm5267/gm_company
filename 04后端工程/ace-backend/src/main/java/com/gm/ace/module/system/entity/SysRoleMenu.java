package com.gm.ace.module.system.entity;

import com.gm.ace.common.base.TenantEntity;
import com.gm.ace.common.fill.BaseEntityInsertListener;
import com.gm.ace.common.fill.BaseEntityUpdateListener;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 角色-菜单关联表
 *
 * @author guoym
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table(value = "sys_role_menu", onInsert = BaseEntityInsertListener.class, onUpdate = BaseEntityUpdateListener.class)
public class SysRoleMenu extends TenantEntity implements Serializable {

    private Long roleId;
    private Long menuId;
}
