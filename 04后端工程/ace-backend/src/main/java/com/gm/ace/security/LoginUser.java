package com.gm.ace.security;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 登录用户主体（放入 SecurityContext）
 *
 * @author guoym
 */
public class LoginUser {

    private final Long userId;
    private final String username;
    private final Long tenantId;
    private final Collection<? extends GrantedAuthority> authorities;

    public LoginUser(Long userId, String username, Long tenantId,
                     Collection<? extends GrantedAuthority> authorities) {
        this.userId = userId;
        this.username = username;
        this.tenantId = tenantId;
        this.authorities = authorities;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
}
