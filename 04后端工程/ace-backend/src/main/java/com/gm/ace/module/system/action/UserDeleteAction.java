package com.gm.ace.module.system.action;

import com.gm.ace.common.action.AbstractActionTemplate;
import com.gm.ace.common.base.DeleteReq;
import com.gm.ace.module.system.repository.SysUserRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * 用户删除（逻辑删除）
 *
 * @author guoym
 */
@Component
public class UserDeleteAction extends AbstractActionTemplate<DeleteReq, Boolean> {

    @Resource
    private SysUserRepository userRepo;

    @Override
    protected Boolean executeAction(DeleteReq req) {
        return userRepo.deleteById(req.getId()) > 0;
    }
}
