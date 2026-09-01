-- ============================================================================
--  种子数据：默认租户 + 超级管理员 + 角色 + 菜单
--  管理员账号：admin / 密码：123456（bcrypt 加密）
--  执行：psql -h localhost -p 5432 -U postgres -d postgres -f db/seed.sql
-- ============================================================================

SET search_path TO gm_company, public;

-- 租户
INSERT INTO sys_tenant (id, tenant_name, tenant_code, contact, phone, status)
VALUES (1, '平台租户', 'platform', 'GM', '13800000000', 1)
ON CONFLICT (id) DO NOTHING;

-- 管理员用户（密码 123456 的 bcrypt 哈希）
INSERT INTO sys_user (id, tenant_id, username, password, nickname, real_name, status)
VALUES (1, 1, 'admin', '$2b$12$3.g.n.Ym9TKbbyirihOYTuWEUJIvch3XF2mMPn6cQDn3BssFIse3a', '超级管理员', 'GM', 1)
ON CONFLICT (id) DO NOTHING;

-- 超级管理员角色
INSERT INTO sys_role (id, tenant_id, role_code, role_name, status)
VALUES (1, 1, 'SUPER_ADMIN', '超级管理员', 1)
ON CONFLICT (id) DO NOTHING;

-- 用户-角色
INSERT INTO sys_user_role (id, tenant_id, user_id, role_id)
VALUES (1, 1, 1, 1)
ON CONFLICT (id) DO NOTHING;

-- 菜单/权限
INSERT INTO sys_menu (id, tenant_id, parent_id, menu_name, menu_type, permission, path, component, sort, status) VALUES
    (1, 1, 0, '仪表盘', 2, 'dashboard:view', '/dashboard', 'dashboard', 1, 1),
    (2, 1, 0, '系统管理', 1, 'system:manage', '/system', '', 2, 1),
    (3, 1, 2, '用户管理', 2, 'system:user:list', '/system/user', 'system/user', 1, 1),
    (4, 1, 2, '新增用户', 3, 'system:user:add', '', '', 1, 1),
    (5, 1, 2, '编辑用户', 3, 'system:user:edit', '', '', 2, 1),
    (6, 1, 2, '删除用户', 3, 'system:user:delete', '', '', 3, 1),
    (7, 1, 2, '角色管理', 2, 'system:role:list', '/system/role', 'system/role', 4, 1)
ON CONFLICT (id) DO NOTHING;
