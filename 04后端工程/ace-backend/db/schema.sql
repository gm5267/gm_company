-- ============================================================================
--  GM 公司多租户 SaaS 后台 - 数据库初始化脚本（PostgreSQL）
--  模式：gm_company
--  说明：逻辑删除统一使用 is_deleted（0 正常 / 1 删除），唯一约束配合部分索引
--        WHERE is_deleted = 0，保证已删除记录不占用唯一约束。
--  执行：psql -h localhost -p 5432 -U postgres -d postgres -f db/schema.sql
-- ============================================================================

CREATE SCHEMA IF NOT EXISTS gm_company;
SET search_path TO gm_company, public;

-- ============================== 租户表（主数据，无 tenant_id） ==============================
CREATE TABLE IF NOT EXISTS sys_tenant (
    id           BIGINT       NOT NULL,
    tenant_name  VARCHAR(64)  NOT NULL,
    tenant_code  VARCHAR(64)  NOT NULL,
    contact      VARCHAR(32),
    phone        VARCHAR(32),
    status       SMALLINT     NOT NULL DEFAULT 1,
    expire_time  TIMESTAMP,
    create_by    BIGINT,
    create_time  TIMESTAMP    NOT NULL DEFAULT now(),
    update_by    BIGINT,
    update_time  TIMESTAMP    NOT NULL DEFAULT now(),
    is_deleted   SMALLINT     NOT NULL DEFAULT 0,
    CONSTRAINT pk_sys_tenant PRIMARY KEY (id)
);
COMMENT ON TABLE  sys_tenant IS '租户表';
COMMENT ON COLUMN sys_tenant.id IS '主键';
COMMENT ON COLUMN sys_tenant.tenant_name IS '租户名称';
COMMENT ON COLUMN sys_tenant.tenant_code IS '租户编码（唯一）';
COMMENT ON COLUMN sys_tenant.contact IS '联系人';
COMMENT ON COLUMN sys_tenant.phone IS '联系电话';
COMMENT ON COLUMN sys_tenant.status IS '状态（1 启用 0 停用）';
COMMENT ON COLUMN sys_tenant.expire_time IS '有效期至';
COMMENT ON COLUMN sys_tenant.create_by IS '创建人';
COMMENT ON COLUMN sys_tenant.create_time IS '创建时间';
COMMENT ON COLUMN sys_tenant.update_by IS '修改人';
COMMENT ON COLUMN sys_tenant.update_time IS '修改时间';
COMMENT ON COLUMN sys_tenant.is_deleted IS '逻辑删除（0 正常 1 删除）';
CREATE UNIQUE INDEX IF NOT EXISTS uk_tenant_code ON sys_tenant (tenant_code) WHERE is_deleted = 0;

-- ============================== 用户表 ==============================
CREATE TABLE IF NOT EXISTS sys_user (
    id          BIGINT       NOT NULL,
    tenant_id   BIGINT       NOT NULL,
    username    VARCHAR(64)  NOT NULL,
    password    VARCHAR(100) NOT NULL,
    nickname    VARCHAR(64),
    real_name   VARCHAR(64),
    email       VARCHAR(128),
    phone       VARCHAR(32),
    avatar      VARCHAR(255),
    gender      SMALLINT     DEFAULT 0,
    status      SMALLINT     NOT NULL DEFAULT 1,
    dept_id     BIGINT,
    remark      VARCHAR(255),
    create_by   BIGINT,
    create_time TIMESTAMP    NOT NULL DEFAULT now(),
    update_by   BIGINT,
    update_time TIMESTAMP    NOT NULL DEFAULT now(),
    is_deleted  SMALLINT     NOT NULL DEFAULT 0,
    CONSTRAINT pk_sys_user PRIMARY KEY (id)
);
COMMENT ON TABLE  sys_user IS '用户表';
COMMENT ON COLUMN sys_user.id IS '主键';
COMMENT ON COLUMN sys_user.tenant_id IS '租户ID';
COMMENT ON COLUMN sys_user.username IS '登录账号';
COMMENT ON COLUMN sys_user.password IS '密码（bcrypt 加密）';
COMMENT ON COLUMN sys_user.nickname IS '昵称';
COMMENT ON COLUMN sys_user.real_name IS '真实姓名';
COMMENT ON COLUMN sys_user.email IS '邮箱';
COMMENT ON COLUMN sys_user.phone IS '手机号';
COMMENT ON COLUMN sys_user.avatar IS '头像';
COMMENT ON COLUMN sys_user.gender IS '性别（0 未知 1 男 2 女）';
COMMENT ON COLUMN sys_user.status IS '状态（1 启用 0 停用）';
COMMENT ON COLUMN sys_user.dept_id IS '部门ID';
COMMENT ON COLUMN sys_user.remark IS '备注';
COMMENT ON COLUMN sys_user.create_by IS '创建人';
COMMENT ON COLUMN sys_user.create_time IS '创建时间';
COMMENT ON COLUMN sys_user.update_by IS '修改人';
COMMENT ON COLUMN sys_user.update_time IS '修改时间';
COMMENT ON COLUMN sys_user.is_deleted IS '逻辑删除（0 正常 1 删除）';
CREATE UNIQUE INDEX IF NOT EXISTS uk_user_tenant_username ON sys_user (tenant_id, username) WHERE is_deleted = 0;
CREATE INDEX IF NOT EXISTS idx_user_tenant ON sys_user (tenant_id);

-- ============================== 角色表 ==============================
CREATE TABLE IF NOT EXISTS sys_role (
    id          BIGINT       NOT NULL,
    tenant_id   BIGINT       NOT NULL,
    role_code   VARCHAR(64)  NOT NULL,
    role_name   VARCHAR(64)  NOT NULL,
    remark      VARCHAR(255),
    status      SMALLINT     NOT NULL DEFAULT 1,
    create_by   BIGINT,
    create_time TIMESTAMP    NOT NULL DEFAULT now(),
    update_by   BIGINT,
    update_time TIMESTAMP    NOT NULL DEFAULT now(),
    is_deleted  SMALLINT     NOT NULL DEFAULT 0,
    CONSTRAINT pk_sys_role PRIMARY KEY (id)
);
COMMENT ON TABLE  sys_role IS '角色表';
COMMENT ON COLUMN sys_role.id IS '主键';
COMMENT ON COLUMN sys_role.tenant_id IS '租户ID';
COMMENT ON COLUMN sys_role.role_code IS '角色编码（唯一）';
COMMENT ON COLUMN sys_role.role_name IS '角色名称';
COMMENT ON COLUMN sys_role.remark IS '备注';
COMMENT ON COLUMN sys_role.status IS '状态（1 启用 0 停用）';
COMMENT ON COLUMN sys_role.create_by IS '创建人';
COMMENT ON COLUMN sys_role.create_time IS '创建时间';
COMMENT ON COLUMN sys_role.update_by IS '修改人';
COMMENT ON COLUMN sys_role.update_time IS '修改时间';
COMMENT ON COLUMN sys_role.is_deleted IS '逻辑删除（0 正常 1 删除）';
CREATE UNIQUE INDEX IF NOT EXISTS uk_role_tenant_code ON sys_role (tenant_id, role_code) WHERE is_deleted = 0;
CREATE INDEX IF NOT EXISTS idx_role_tenant ON sys_role (tenant_id);

-- ============================== 菜单表 ==============================
CREATE TABLE IF NOT EXISTS sys_menu (
    id          BIGINT       NOT NULL,
    tenant_id   BIGINT       NOT NULL,
    parent_id   BIGINT       NOT NULL DEFAULT 0,
    menu_name   VARCHAR(64)  NOT NULL,
    menu_type   SMALLINT     NOT NULL DEFAULT 1,
    permission  VARCHAR(128),
    path        VARCHAR(255),
    component   VARCHAR(255),
    icon        VARCHAR(64),
    sort        INT          NOT NULL DEFAULT 0,
    status      SMALLINT     NOT NULL DEFAULT 1,
    create_by   BIGINT,
    create_time TIMESTAMP    NOT NULL DEFAULT now(),
    update_by   BIGINT,
    update_time TIMESTAMP    NOT NULL DEFAULT now(),
    is_deleted  SMALLINT     NOT NULL DEFAULT 0,
    CONSTRAINT pk_sys_menu PRIMARY KEY (id)
);
COMMENT ON TABLE  sys_menu IS '菜单/权限表';
COMMENT ON COLUMN sys_menu.id IS '主键';
COMMENT ON COLUMN sys_menu.tenant_id IS '租户ID';
COMMENT ON COLUMN sys_menu.parent_id IS '父菜单ID（0 表示根）';
COMMENT ON COLUMN sys_menu.menu_name IS '菜单名称';
COMMENT ON COLUMN sys_menu.menu_type IS '类型（1 目录 2 菜单 3 按钮）';
COMMENT ON COLUMN sys_menu.permission IS '权限标识（按钮级 @PreAuthorize 使用）';
COMMENT ON COLUMN sys_menu.path IS '路由路径';
COMMENT ON COLUMN sys_menu.component IS '前端组件';
COMMENT ON COLUMN sys_menu.icon IS '图标';
COMMENT ON COLUMN sys_menu.sort IS '排序';
COMMENT ON COLUMN sys_menu.status IS '状态（1 启用 0 停用）';
COMMENT ON COLUMN sys_menu.create_by IS '创建人';
COMMENT ON COLUMN sys_menu.create_time IS '创建时间';
COMMENT ON COLUMN sys_menu.update_by IS '修改人';
COMMENT ON COLUMN sys_menu.update_time IS '修改时间';
COMMENT ON COLUMN sys_menu.is_deleted IS '逻辑删除（0 正常 1 删除）';
CREATE INDEX IF NOT EXISTS idx_menu_tenant ON sys_menu (tenant_id);

-- ============================== 用户-角色关联 ==============================
CREATE TABLE IF NOT EXISTS sys_user_role (
    id          BIGINT       NOT NULL,
    tenant_id   BIGINT       NOT NULL,
    user_id     BIGINT       NOT NULL,
    role_id     BIGINT       NOT NULL,
    create_by   BIGINT,
    create_time TIMESTAMP    NOT NULL DEFAULT now(),
    update_by   BIGINT,
    update_time TIMESTAMP    NOT NULL DEFAULT now(),
    is_deleted  SMALLINT     NOT NULL DEFAULT 0,
    CONSTRAINT pk_sys_user_role PRIMARY KEY (id)
);
COMMENT ON TABLE  sys_user_role IS '用户角色关联表';
COMMENT ON COLUMN sys_user_role.id IS '主键';
COMMENT ON COLUMN sys_user_role.tenant_id IS '租户ID';
COMMENT ON COLUMN sys_user_role.user_id IS '用户ID';
COMMENT ON COLUMN sys_user_role.role_id IS '角色ID';
COMMENT ON COLUMN sys_user_role.create_by IS '创建人';
COMMENT ON COLUMN sys_user_role.create_time IS '创建时间';
COMMENT ON COLUMN sys_user_role.update_by IS '修改人';
COMMENT ON COLUMN sys_user_role.update_time IS '修改时间';
COMMENT ON COLUMN sys_user_role.is_deleted IS '逻辑删除（0 正常 1 删除）';
CREATE INDEX IF NOT EXISTS idx_ur_user ON sys_user_role (user_id);
CREATE INDEX IF NOT EXISTS idx_ur_role ON sys_user_role (role_id);
CREATE INDEX IF NOT EXISTS idx_ur_tenant ON sys_user_role (tenant_id);

-- ============================== 角色-菜单关联 ==============================
CREATE TABLE IF NOT EXISTS sys_role_menu (
    id          BIGINT       NOT NULL,
    tenant_id   BIGINT       NOT NULL,
    role_id     BIGINT       NOT NULL,
    menu_id     BIGINT       NOT NULL,
    create_by   BIGINT,
    create_time TIMESTAMP    NOT NULL DEFAULT now(),
    update_by   BIGINT,
    update_time TIMESTAMP    NOT NULL DEFAULT now(),
    is_deleted  SMALLINT     NOT NULL DEFAULT 0,
    CONSTRAINT pk_sys_role_menu PRIMARY KEY (id)
);
COMMENT ON TABLE  sys_role_menu IS '角色菜单关联表';
COMMENT ON COLUMN sys_role_menu.id IS '主键';
COMMENT ON COLUMN sys_role_menu.tenant_id IS '租户ID';
COMMENT ON COLUMN sys_role_menu.role_id IS '角色ID';
COMMENT ON COLUMN sys_role_menu.menu_id IS '菜单ID';
COMMENT ON COLUMN sys_role_menu.create_by IS '创建人';
COMMENT ON COLUMN sys_role_menu.create_time IS '创建时间';
COMMENT ON COLUMN sys_role_menu.update_by IS '修改人';
COMMENT ON COLUMN sys_role_menu.update_time IS '修改时间';
COMMENT ON COLUMN sys_role_menu.is_deleted IS '逻辑删除（0 正常 1 删除）';
CREATE INDEX IF NOT EXISTS idx_rm_role ON sys_role_menu (role_id);
CREATE INDEX IF NOT EXISTS idx_rm_menu ON sys_role_menu (menu_id);
CREATE INDEX IF NOT EXISTS idx_rm_tenant ON sys_role_menu (tenant_id);

-- ============================== 登录日志（追加写，无逻辑删除） ==============================
CREATE TABLE IF NOT EXISTS sys_login_log (
    id           BIGINT       NOT NULL,
    tenant_id    BIGINT,
    user_id      BIGINT,
    username     VARCHAR(64),
    ip           VARCHAR(64),
    user_agent   VARCHAR(512),
    login_time   TIMESTAMP    NOT NULL DEFAULT now(),
    login_status SMALLINT     NOT NULL DEFAULT 1,
    msg          VARCHAR(255),
    CONSTRAINT pk_sys_login_log PRIMARY KEY (id)
);
COMMENT ON TABLE  sys_login_log IS '登录日志（仅追加，不做逻辑删除）';
COMMENT ON COLUMN sys_login_log.id IS '主键';
COMMENT ON COLUMN sys_login_log.tenant_id IS '租户ID';
COMMENT ON COLUMN sys_login_log.user_id IS '用户ID';
COMMENT ON COLUMN sys_login_log.username IS '登录账号';
COMMENT ON COLUMN sys_login_log.ip IS '登录IP';
COMMENT ON COLUMN sys_login_log.user_agent IS '浏览器UA';
COMMENT ON COLUMN sys_login_log.login_time IS '登录时间';
COMMENT ON COLUMN sys_login_log.login_status IS '状态（1 成功 0 失败）';
COMMENT ON COLUMN sys_login_log.msg IS '备注/失败原因';
