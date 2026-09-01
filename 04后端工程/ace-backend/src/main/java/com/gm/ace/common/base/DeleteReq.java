package com.gm.ace.common.base;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 删除请求基类
 *
 * @author guoym
 */
@Data
public class DeleteReq implements Serializable {

    /** 主键 id */
    @NotNull(message = "{valid.id.notnull}")
    private Long id;
}
