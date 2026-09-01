package com.gm.ace.module.system.repository;

import com.gm.ace.module.system.entity.SysMenu;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 菜单仓储
 *
 * @author guoym
 */
@Mapper
public interface SysMenuRepository extends BaseMapper<SysMenu> {
}
