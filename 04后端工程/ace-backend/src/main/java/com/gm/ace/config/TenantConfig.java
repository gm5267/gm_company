package com.gm.ace.config;

import com.gm.ace.tenant.TenantFactoryImpl;
import com.mybatisflex.core.tenant.TenantManager;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

/**
 * 租户配置：把 {@link TenantFactoryImpl} 注册到 MyBatis-Flex 的静态租户管理器
 *
 * @author guoym
 */
@Configuration
public class TenantConfig {

    private final TenantFactoryImpl tenantFactory;

    public TenantConfig(TenantFactoryImpl tenantFactory) {
        this.tenantFactory = tenantFactory;
    }

    @PostConstruct
    public void register() {
        TenantManager.setTenantFactory(tenantFactory);
    }
}
