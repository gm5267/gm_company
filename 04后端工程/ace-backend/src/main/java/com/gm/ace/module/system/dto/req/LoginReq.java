package com.gm.ace.module.system.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * 登录请求
 *
 * @author guoym
 */
@Data
public class LoginReq implements Serializable {

    @NotBlank(message = "{valid.username.notblank}")
    private String username;

    @NotBlank(message = "{valid.password.notblank}")
    private String password;

    /** 租户编码（可选，演示默认平台租户） */
    private String tenantCode;
}
