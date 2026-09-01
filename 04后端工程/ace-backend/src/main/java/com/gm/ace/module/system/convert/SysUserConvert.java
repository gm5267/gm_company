package com.gm.ace.module.system.convert;

import com.gm.ace.common.convert.BaseConverter;
import com.gm.ace.module.system.entity.SysUser;
import com.gm.ace.module.system.dto.resp.UserDetailResp;
import com.gm.ace.module.system.dto.resp.UserPageResp;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * 用户实体 ↔ 出参 转换
 *
 * @author guoym
 */
@Mapper(componentModel = "spring", uses = BaseConverter.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SysUserConvert {

    UserPageResp toPageResp(SysUser user);

    UserDetailResp toDetailResp(SysUser user);

    List<UserPageResp> toPageRespList(List<SysUser> users);
}
