package com.gm.ace.module.system.controller;

import com.gm.ace.common.result.R;
import com.gm.ace.module.system.dto.req.LoginReq;
import com.gm.ace.module.system.dto.resp.LoginResp;
import com.gm.ace.module.system.service.AuthService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 认证接口
 *
 * @author guoym
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Resource
    private AuthService authService;

    @PostMapping("/login")
    public R<LoginResp> login(@Valid @RequestBody LoginReq req) {
        return R.data(authService.login(req));
    }
}
