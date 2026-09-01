package com.gm.ace.module.system.action;

import cn.hutool.core.util.StrUtil;
import com.gm.ace.common.action.AbstractActionTemplate;
import com.gm.ace.common.result.PageResult;
import com.gm.ace.common.util.QueryKit;
import com.gm.ace.module.system.convert.SysUserConvert;
import com.gm.ace.module.system.dto.req.UserPageReq;
import com.gm.ace.module.system.dto.resp.UserPageResp;
import com.gm.ace.module.system.entity.SysUser;
import com.gm.ace.module.system.repository.SysUserRepository;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 用户分页
 *
 * @author guoym
 */
@Component
public class UserPageAction extends AbstractActionTemplate<UserPageReq, PageResult<UserPageResp>> {

    private static final Set<String> ORDER_WHITE = Set.of("id", "username", "create_time", "status");

    @Resource
    private SysUserRepository userRepo;
    @Resource
    private SysUserConvert convert;

    @Override
    protected PageResult<UserPageResp> executeAction(UserPageReq req) {
        Page<SysUser> page = req.toFlexPage();
        QueryWrapper qw = QueryWrapper.create();
        QueryKit.applyOrders(qw, req.getOrders(), ORDER_WHITE);
        if (StrUtil.isNotBlank(req.getUsername())) {
            qw.like("username", req.getUsername());
        }
        if (req.getStatus() != null) {
            qw.eq("status", req.getStatus());
        }
        Page<SysUser> result = userRepo.paginate(page, qw);
        return PageResult.of(result, convert::toPageResp);
    }
}
