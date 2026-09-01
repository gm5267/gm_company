package com.gm.ace.common.base;

import com.mybatisflex.annotation.Column;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 租户实体基类：在 {@link BaseEntity} 基础上增加租户ID
 * <p>
 * 全局表（如 sys_tenant）直接继承 {@link BaseEntity} 即可，不带 tenant_id 列。
 *
 * @author guoym
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class TenantEntity extends BaseEntity {

    @Column(tenantId = true)
    private Long tenantId;
}
