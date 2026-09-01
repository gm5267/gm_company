package com.gm.ace.security;

import com.gm.ace.tenant.LoginUserContext;
import com.gm.ace.tenant.TenantContext;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 把 Spring Security 的 {@link Jwt} 转换为认证信息，并写入租户/登录用户上下文
 *
 * @author guoym
 */
@Component
public class JwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        String username = jwt.getSubject();
        Object uidObj = jwt.getClaim("uid");
        Long userId = uidObj == null ? null : ((Number) uidObj).longValue();
        Object tidObj = jwt.getClaim("tid");
        Long tenantId = tidObj == null ? null : ((Number) tidObj).longValue();
        List<String> roles = jwt.getClaimAsStringList("roles");
        List<GrantedAuthority> authorities = (roles == null ? List.<String>of() : roles).stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                .collect(Collectors.toList());

        LoginUser loginUser = new LoginUser(userId, username, tenantId, authorities);
        TenantContext.set(tenantId);
        LoginUserContext.set(userId);
        return new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                loginUser, jwt.getTokenValue(), authorities);
    }
}
