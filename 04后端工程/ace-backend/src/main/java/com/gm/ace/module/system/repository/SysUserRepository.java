package com.gm.ace.module.system.repository;

import com.gm.ace.module.system.entity.SysUser;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 用户仓储
 *
 * @author guoym
 */
@Mapper
public interface SysUserRepository extends BaseMapper<SysUser> {

    /** 按用户名查询（忽略租户，供登录使用；调用方需自行控制租户可见性） */
    @Select("SELECT * FROM sys_user WHERE username = #{username} AND is_deleted = 0")
    SysUser selectByUsername(@Param("username") String username);
}
