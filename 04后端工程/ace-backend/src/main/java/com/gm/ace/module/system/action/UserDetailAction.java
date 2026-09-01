package com.gm.ace.module.system.action;

import com.gm.ace.common.action.AbstractActionTemplate;
import com.gm.ace.common.exception.BizException;
import com.gm.ace.common.result.ResultCode;
import com.gm.ace.module.system.convert.SysUserConvert;
import com.gm.ace.module.system.dto.req.UserDetailReq;
import com.gm.ace.module.system.dto.resp.UserDetailResp;
import com.gm.ace.module.system.entity.SysUser;
import com.gm.ace.module.system.repository.SysRoleRepository;
import com.gm.ace.module.system.repository.SysUserRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 用户详情
 *
 * @author guoym
 */
@Component
public class UserDetailAction extends AbstractActionTemplate<UserDetailReq, UserDetailResp> {

    @Resource
    private SysUserRepository userRepo;
    @Resource
    private SysRoleRepository roleRepo;
    @Resource
    private SysUserConvert convert;

    @Override
    protected UserDetailResp executeAction(UserDetailReq req) {
        SysUser user = userRepo.selectOneById(req.getId());
        if (user == null) {
            throw new BizException(ResultCode.NOT_FOUND, "用户不存在");
        }
        UserDetailResp resp = convert.toDetailResp(user);
        List<String> roles = roleRepo.selectRoleCodesByUserId(user.getId());
        resp.setRoles(roles);
        return resp;
    }
}
