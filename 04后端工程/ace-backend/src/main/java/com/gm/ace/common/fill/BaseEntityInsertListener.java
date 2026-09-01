package com.gm.ace.common.fill;

import cn.hutool.core.util.IdUtil;
import com.gm.ace.common.base.BaseEntity;
import com.gm.ace.common.base.TenantEntity;
import com.gm.ace.tenant.LoginUserContext;
import com.gm.ace.tenant.TenantContext;
import com.mybatisflex.annotation.InsertListener;

import java.time.LocalDateTime;

/**
 * 插入自动填充：雪花主键、审计字段；若是租户实体且上下文有租户则填充 tenantId
 * <p>
 * 通过实体类 {@code @Table(onInsert = BaseEntityInsertListener.class)} 引用。
 * 仅在字段为空时填充，避免覆盖显式赋值（如种子数据自带主键）。
 *
 * @author guoym
 */
public class BaseEntityInsertListener implements InsertListener {

    @Override
    public void onInsert(Object entity) {
        if (!(entity instanceof BaseEntity be)) {
            return;
        }
        if (be.getId() == null) {
            be.setId(IdUtil.getSnowflakeNextId());
        }
        Long uid = LoginUserContext.get();
        if (uid != null && be.getCreateBy() == null) {
            be.setCreateBy(uid);
        }
        if (be.getCreateTime() == null) {
            be.setCreateTime(LocalDateTime.now());
        }
        if (entity instanceof TenantEntity te) {
            Long tenantId = TenantContext.get();
            if (tenantId != null && te.getTenantId() == null) {
                te.setTenantId(tenantId);
            }
        }
    }
}
