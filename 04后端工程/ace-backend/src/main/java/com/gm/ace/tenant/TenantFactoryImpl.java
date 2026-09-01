package com.gm.ace.tenant;

import com.mybatisflex.core.tenant.TenantFactory;
import org.springframework.stereotype.Component;

/**
 * 租户工厂：从 {@link TenantContext} 读取当前租户，交给 MyBatis-Flex 拼接租户条件
 *
 * @author guoym
 */
@Component
public class TenantFactoryImpl implements TenantFactory {

    @Override
    public Object[] getTenantIds() {
        Long tenantId = TenantContext.get();
        return tenantId == null ? new Object[0] : new Object[]{tenantId};
    }

    @Override
    public Object[] getTenantIds(String tableName) {
        // 实体未标注 @Column(tenantId = true) 的表（如 sys_tenant）天然不加租户条件
        return getTenantIds();
    }
}
