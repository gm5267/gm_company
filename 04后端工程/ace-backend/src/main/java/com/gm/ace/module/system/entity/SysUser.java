package com.gm.ace.module.system.entity;

import com.gm.ace.common.base.TenantEntity;
import com.gm.ace.common.fill.BaseEntityInsertListener;
import com.gm.ace.common.fill.BaseEntityUpdateListener;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 用户表
 *
 * @author guoym
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table(value = "sys_user", onInsert = BaseEntityInsertListener.class, onUpdate = BaseEntityUpdateListener.class)
public class SysUser extends TenantEntity implements Serializable {

    private String username;
    private String password;
    private String nickname;
    private String realName;
    private String email;
    private String phone;
    private String avatar;
    private Integer gender;
    private Integer status;
    private Long deptId;
    private String remark;
}
