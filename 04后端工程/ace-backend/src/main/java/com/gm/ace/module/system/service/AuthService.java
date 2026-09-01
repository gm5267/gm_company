package com.gm.ace.module.system.service;

import com.gm.ace.common.exception.BizException;
import com.gm.ace.common.result.ResultCode;
import com.gm.ace.module.system.dto.req.LoginReq;
import com.gm.ace.module.system.dto.resp.LoginResp;
import com.gm.ace.module.system.entity.SysUser;
import com.gm.ace.module.system.repository.SysRoleRepository;
import com.gm.ace.module.system.repository.SysUserRepository;
import com.gm.ace.security.JwtTokenService;
import com.gm.ace.tenant.LoginUserContext;
import com.gm.ace.tenant.TenantContext;
import com.mybatisflex.core.tenant.TenantManager;
import jakarta.annotation.Resource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 登录认证服务
 *
 * @author guoym
 */
@Service
public class AuthService {

    @Resource
    private SysUserRepository userRepo;
    @Resource
    private SysRoleRepository roleRepo;
    @Resource
    private JwtTokenService jwtTokenService;

    public LoginResp login(LoginReq req) {
        // 演示：默认平台租户；正式可按 tenantCode 解析
        TenantContext.set(1L);
        SysUser user;
        try {
            // 登录需跨租户按用户名查找，临时忽略租户条件
            TenantManager.ignoreTenantCondition();
            user = userRepo.selectByUsername(req.getUsername());
        } finally {
            TenantManager.restoreTenantCondition();
        }
        if (user == null) {
            throw new BizException(ResultCode.UNAUTHORIZED, "用户不存在");
        }
        if (user.getStatus() != null && user.getStatus() == 0) {
            throw new BizException(ResultCode.FORBIDDEN, "账号已停用");
        }
        if (!new BCryptPasswordEncoder().matches(req.getPassword(), user.getPassword())) {
            throw new BizException(ResultCode.UNAUTHORIZED, "密码错误");
        }
        // 登录成功后写入上下文，供后续操作与令牌使用
        TenantContext.set(user.getTenantId());
        LoginUserContext.set(user.getId());
        List<String> roles = roleRepo.selectRoleCodesByUserId(user.getId());
        String token = jwtTokenService.issue(user.getId(), user.getUsername(), user.getTenantId(), roles);
        return LoginResp.of(token, user, roles);
    }
}
