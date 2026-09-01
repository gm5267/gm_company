package com.gm.ace.module.system.dto.req;

import com.gm.ace.common.base.PageReq;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户分页请求
 *
 * @author guoym
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserPageReq extends PageReq {

    /** 用户名关键字（模糊） */
    private String username;

    /** 状态（1 启用 0 停用） */
    private Integer status;
}
