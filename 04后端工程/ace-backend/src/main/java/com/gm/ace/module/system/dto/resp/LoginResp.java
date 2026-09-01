package com.gm.ace.module.system.dto.resp;

import com.gm.ace.module.system.entity.SysUser;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 登录出参
 *
 * @author guoym
 */
@Data
public class LoginResp implements Serializable {

    private String token;
    private Long userId;
    private String username;
    private Long tenantId;
    private List<String> roles;

    public static LoginResp of(String token, SysUser user, List<String> roles) {
        LoginResp resp = new LoginResp();
        resp.setToken(token);
        resp.setUserId(user.getId());
        resp.setUsername(user.getUsername());
        resp.setTenantId(user.getTenantId());
        resp.setRoles(roles);
        return resp;
    }
}
