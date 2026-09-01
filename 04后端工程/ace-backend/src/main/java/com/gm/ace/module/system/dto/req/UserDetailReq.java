package com.gm.ace.module.system.dto.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户详情请求
 *
 * @author guoym
 */
@Data
public class UserDetailReq implements Serializable {

    @NotNull(message = "{valid.id.notnull}")
    private Long id;
}
