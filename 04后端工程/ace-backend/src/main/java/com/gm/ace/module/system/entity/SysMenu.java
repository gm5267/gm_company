package com.gm.ace.module.system.entity;

import com.gm.ace.common.base.TenantEntity;
import com.gm.ace.common.fill.BaseEntityInsertListener;
import com.gm.ace.common.fill.BaseEntityUpdateListener;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 菜单/权限表
 *
 * @author guoym
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table(value = "sys_menu", onInsert = BaseEntityInsertListener.class, onUpdate = BaseEntityUpdateListener.class)
public class SysMenu extends TenantEntity implements Serializable {

    private Long parentId;
    private String menuName;
    private Integer menuType;
    private String permission;
    private String path;
    private String component;
    private String icon;
    private Integer sort;
    private Integer status;
}
