package com.gm.ace.module.system.controller;

import com.gm.ace.common.base.DeleteReq;
import com.gm.ace.common.result.PageResult;
import com.gm.ace.common.result.R;
import com.gm.ace.module.system.action.UserDeleteAction;
import com.gm.ace.module.system.action.UserDetailAction;
import com.gm.ace.module.system.action.UserPageAction;
import com.gm.ace.module.system.dto.req.UserDetailReq;
import com.gm.ace.module.system.dto.req.UserPageReq;
import com.gm.ace.module.system.dto.resp.UserDetailResp;
import com.gm.ace.module.system.dto.resp.UserPageResp;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户管理（极薄 Controller：仅做协议转换与权限声明）
 *
 * @author guoym
 */
@RestController
@RequestMapping("/system/user")
public class SysUserController {

    @Resource
    private UserPageAction userPageAction;
    @Resource
    private UserDetailAction userDetailAction;
    @Resource
    private UserDeleteAction userDeleteAction;

    @GetMapping("/page")
    public R<PageResult<UserPageResp>> page(@Valid UserPageReq req) {
        return userPageAction.execute(req);
    }

    @GetMapping("/{id}")
    public R<UserDetailResp> detail(@PathVariable Long id) {
        UserDetailReq req = new UserDetailReq();
        req.setId(id);
        return userDetailAction.execute(req);
    }

    @PostMapping("/delete")
    public R<Boolean> delete(@Valid @RequestBody DeleteReq req) {
        return userDeleteAction.execute(req);
    }
}
