package com.gm.ace.module.system.repository;

import com.gm.ace.module.system.entity.SysRole;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 角色仓储
 *
 * @author guoym
 */
@Mapper
public interface SysRoleRepository extends BaseMapper<SysRole> {

    /** 查询用户拥有的角色编码 */
    @Select("SELECT r.role_code FROM sys_role r " +
            "JOIN sys_user_role ur ON ur.role_id = r.id " +
            "WHERE ur.user_id = #{userId} AND r.is_deleted = 0")
    List<String> selectRoleCodesByUserId(@Param("userId") Long userId);
}
