---
name: ace-backend 骨架工程
overview: 在空目录 04后端工程/ace-backend 中从零搭建可直接启动的 Spring Boot 4.1.1 后端骨架：MyBatis-Flex(Boot4 starter) + PostgreSQL + Redis + Spring Security 7.1 + JWT(Nimbus) + MapStruct + Lombok + Hutool。核心工程规范为「一接口一 Action 类 + Req/Resp 命名 + Repository 层 + 租户隔离 + 实体通用字段自动注入」，落地统一返回体 R、全局异常与国际化、Apifox 可识别的注释式文档、RBAC + 租户表结构 DDL，并提供多环境 profile 与 jar 启动脚本。
todos:
  - id: scaffold-project
    content: 搭建 ace-backend 单模块 Maven 工程与 pom 版本矩阵，建立分层包结构与启动类
    status: completed
  - id: verify-apt-jackson
    content: 配置四段注解处理器链并验证 APT 在 JDK 23 下生效，同时验证 Jackson 3 兼容性并确定降级方案
    status: completed
    dependencies:
      - scaffold-project
  - id: build-tenant-base
    content: 实现 TenantContext、TenantFactory、BaseEntity 与全局填充监听器，产出七表 DDL
    status: completed
    dependencies:
      - verify-apt-jackson
  - id: build-common-core
    content: 实现泛型统一返回体 R、AbstractActionTemplate、异常体系、全局异常处理、分组校验国际化与链路追踪
    status: completed
    dependencies:
      - build-tenant-base
  - id: build-infra-config
    content: 实现 MyBatis-Flex 配置与 repository 包扫描、Redis 序列化配置、Jackson 兼容配置
    status: completed
    dependencies:
      - build-common-core
  - id: build-security
    content: 实现 Security 无状态鉴权与 Nimbus JWT（claim 携带 tenant_id）、白名单、401/403、方法级鉴权
    status: completed
    dependencies:
      - build-infra-config
  - id: build-system-module
    content: 编写实体、repository、convert、Action 与极薄 Controller，示范完整请求链路
    status: completed
    dependencies:
      - build-security
  - id: integrate-apifox-and-deliver
    content: 接入 SpringDoc、制定 Javadoc 规范、补齐多环境配置、日志、启动脚本与 README，跑通编译与 jar 启动冒烟
    status: completed
    dependencies:
      - build-system-module
---

## 产品概述

在 `04后端工程/ace-backend`（当前空目录）从零构建一个可直接启动的企业级 Java 后端骨架工程，作为公司多租户 SaaS 后台管理系统的服务端基座。工程以 Spring Boot 4.1.1 为核心，整合 MyBatis-Flex + PostgreSQL、Redis、Spring Security 7.1 + JWT、MapStruct + Lombok、Hutool，并确立一套贯穿全工程的编码范式与数据隔离边界。

## 核心特性

- **工程基座**：Maven 单模块工程，JDK 23.0.2 编译目标，本机 Maven 3.9.12 构建，多环境 profile（dev/test/prod），Windows 与 Linux 启动脚本，日志含 MDC 链路追踪 ID
- **租户隔离**：共享库共享表 + `tenant_id` 列隔离。租户 ID 只能来自验签后的 JWT，经 TenantContext 传递，由 MyBatis-Flex 在增删改查时自动追加租户条件，开发人员无需手写 `WHERE tenant_id = ?`
- **实体基类自动注入**：`BaseEntity` 统一承载 `id`（自增）、`create_by`、`create_time`、`update_by`、`update_time`、`tenant_id`、`is_deleted`，审计字段由全局填充监听器自动注入；**乐观锁 `version` 不进基类**（使用频率低，有特殊需要在具体实体上单独声明 `@Column(version = true)`）
- **Action 编排范式**：每个 API 对应一个 `AbstractActionTemplate` 实现类（`#{业务名称}Action`），模板统一封装 `before` → `executeAction` → `after` 流程并自动包装统一返回体，一个类只干一件事
- **DTO 规范**：Controller 入参出参统一 `#{业务名称}Req` / `#{业务名称}Resp` 后缀，实体与 DTO 之间由 MapStruct 编译期转换
- **数据访问**：数据层包名与类名统一为 `repository`（`XxxRepository extends BaseMapper<XxxEntity>`），内置分页、逻辑删除；乐观锁按需要在具体实体上单独声明
- **认证授权**：Spring Security 7.1 无状态鉴权，Nimbus JWT 签发校验（claim 携带 `tenant_id`），白名单放行，401/403 统一处理，方法级 `@PreAuthorize` 生效，RBAC + 租户表结构骨架
- **接口规范**：统一返回体 `R<T>`（泛型静态工厂以适配 Action 模板）、业务与系统异常体系、全局异常兜底、`@Valid` 分组校验 + 国际化错误文案
- **接口文档**：以 Javadoc 注释为零侵入主方案（Apifox IDEA 插件识别），辅以 SpringDoc 生成 `/v3/api-docs` 供 Apifox URL 导入，生产环境关闭

## 技术栈选型

| 组件 | 选型 | 版本（已核实 Maven Central / 官方文档） |
| --- | --- | --- |
| 构建 | Maven 单模块 | 本机 **3.9.12**（`D:\apache-maven-3.9.12`） |
| JDK | Oracle JDK | **23.0.2**（`release=23`，不得高于已安装版本） |
| 框架 | Spring Boot | **4.1.1**（2026-08-20 发布，要求 JDK 17+，兼容至 JDK 26） |
| Web | `spring-boot-starter-web` | 坐标不变，Boot 4.1.1 下存在 |
| ORM | `com.mybatis-flex:mybatis-flex-spring-boot4-starter` | **1.11.8**（2026-07-01） |
| APT | `com.mybatis-flex:mybatis-flex-processor` | 1.11.8 |
| 数据库 | PostgreSQL | 驱动交由 Boot BOM 管理，不写版本 |
| 缓存 | `spring-boot-starter-data-redis` | 坐标不变，Boot 4.1.1 下存在 |
| 安全 | `spring-boot-starter-security` + `spring-boot-starter-oauth2-resource-server` | 由 Boot BOM 管理，实得 **Spring Security 7.1.0** |
| JWT | Nimbus JOSE（随 oauth2-resource-server 引入） | 由 Boot BOM 管理 |
| 对象转换 | `org.mapstruct:mapstruct` + `mapstruct-processor` | **1.6.3**（1.7.0 仍为 Beta2，不用） |
| 简化代码 | `org.projectlombok:lombok` | **1.18.46**（覆盖 JDK 23/25/26） |
| 处理器桥接 | `org.projectlombok:lombok-mapstruct-binding` | 0.2.0 |
| 工具包 | `cn.hutool:hutool-bom` | 取最新稳定版 |
| 接口文档 | `org.springdoc:springdoc-openapi-starter-webmvc-api` | **3.1.0**（2026-08-01，Boot 4 线） |


### 关键决策与理由

1. **MyBatis-Flex 必须用 Boot 4 专用 starter**：`mybatis-flex-spring-boot4-starter`；通用的 `mybatis-flex-spring-boot-starter` 属 Boot 3 线，混用导致自动配置失效。
2. **JWT 用 Nimbus JOSE 而非 jjwt**：`jjwt-jackson` 强依赖 Jackson 2，而 Boot 4 默认已是 Jackson 3，引入 jjwt 会直接触发依赖冲突；Nimbus 随 `oauth2-resource-server` 一并引入，零额外依赖。
3. **springdoc 用 3.x 而非 2.8.x**：已验证 3.1.0 的 pom 依赖 Boot 4 新增的 `spring-boot-webmvc`、`spring-boot-web-server` 模块，2.8.17 属 Boot 3 线。
4. **租户隔离用框架原生机制而非填充监听器**：只有 `@Column(tenantId = true)` + `TenantManager` 才能在**查询时**自动追加 `tenant_id` 条件；`InsertListener` 只能管写入、管不了查询，用它做租户就是漏数据的开始。
5. **审计字段用全局 `registerInsertListener(listener, BaseEntity.class)`**：官方支持注册到基类即对全部子类生效，避免在每张表上重复配注解。
6. **单模块优先**：Boot 4 + JDK 23 + Jackson 3 组合破坏性变更多，单模块可将依赖冲突与 APT 问题排查成本降到最低，包结构已按分层与业务模块内聚预留。
7. **层级边界**：Controller 极薄只做转发；Action 为 application 层，一接口一类，负责编排与事务边界；Service 为 domain 层，仅放跨 Action 复用的领域逻辑；简单查询类 Action 可直接调 Repository，不强制套 Service。

## 实现方案

请求链路（含租户上下文与 Action 编排层）：

```mermaid
flowchart TD
    A[客户端请求] --> B[JwtAuthenticationFilter 验签]
    B --> C[JWT claim 取 tenant_id 写入 TenantContext]
    C --> D[SecurityContext 装配 LoginUser]
    D --> E[Controller 接收 XxxReq 并 Validated 分组校验]
    E --> F[XxxAction.execute req]
    F --> G[before 业务前置校验]
    G --> H[executeAction 编排业务逻辑]
    H --> I[Service 可选 领域逻辑]
    I --> J[Repository MyBatis-Flex 访问 PostgreSQL]
    J --> K[TenantFactory 自动追加 tenant_id 条件]
    H --> L[RedisTemplate 读写缓存]
    K --> M[MapStruct Convert 实体转 XxxResp]
    L --> M
    M --> N[Action 自动包装 R.data 或 R.status]
    N --> O[统一返回体 R XxxResp]
    P[异常抛出] --> Q[GlobalExceptionHandler 兜底]
    Q --> S[I18n MessageSource 翻译文案]
    S --> O
    O --> T[请求结束 TenantContext.clear 与 MDC 清理]
```

**要点说明**

- **注解处理器链是编译成败的命门**：本机正是 JDK 23.0.2，而自 JDK 23 起 javac 默认等同 `-proc:none`，Lombok、MapStruct、MyBatis-Flex 的 APT 会**静默失效**（表现为大量 `cannot find symbol`），必须在 `maven-compiler-plugin` 显式声明 `annotationProcessorPaths` 并开启 `<proc>full</proc>`。
- **租户三条红线**（来自参考文章，必须成为团队规范）：① `tenantId` 只信验签后的 JWT，不接受任何请求头/请求参数；② 原生 SQL（XML mapper、`Db + Row`、手写复杂 SQL）**不会**自动隔离，属强制审查项；③ 异步任务/MQ/定时任务必须显式传播租户上下文。
- **Jackson 3 兼容需先验证再定稿**：Boot 4 默认 Jackson 3（包名 `tools.jackson.*`），而 Hutool 等三方库仍依赖 Jackson 2。实施时先做最小验证，再决定沿用或降级共存，结论与开关写入 README。
- **Apifox 双通道**：主通道为 Javadoc 注释 + Apifox IDEA 插件，零运行时依赖、零注解侵入，规避 `therapi-runtime-javadoc`（最新版仍停在 2022 年的 0.15.0）在高版本 JDK 上的兼容风险；辅通道为 springdoc 暴露 `/v3/api-docs`，prod 默认关闭。

## 实施注意事项

- **注解处理器顺序不可调换**：`lombok` → `lombok-mapstruct-binding` → `mapstruct-processor` → `mybatis-flex-processor`；Lombok 依赖声明为 `provided`。
- **APT 生效必须确证**：编译后检查 `target/generated-sources` 下是否生成 `*MapperImpl`（MapStruct）与 MyBatis-Flex TableDef 产物，**仅"编译通过"不足以证明 APT 生效**。
- **`R` 必须提供泛型静态工厂**：用户给定模板中 `return R.status((Boolean) response);` 要求 `R.status` 能返回 `R<Response>`，若返回 `R<Boolean>` 则编译期类型不兼容，这是模板能否落地的硬门槛。
- **租户上下文必须清理**：`JwtAuthenticationFilter` 的 `finally` 中 `TenantContext.clear()` 与 `MDC.clear()`，否则线程池复用会串租户、串 traceId。
- **MyBatis-Flex 填充监听器只对 Mapper 生效**：通过 XML mapper 或 `Db + Row` 插入**不会**触发 `onInsert`/`onUpdate`，需在 README 列为强制审查项。
- **索引规范**：所有业务表索引 `tenant_id` 前置；原唯一索引 `uk_x(business_key)` 改为 `uk_tenant_x(tenant_id, business_key)`，否则不同租户的业务单号会互相冲突。
- **Redis 序列化**：禁用 JDK 原生序列化，统一 String + JSON，保证 redis-cli 可读、跨语言可解析，规避反序列化安全风险。
- **PostgreSQL 规范**：表名与字段名统一小写加下划线；主键用 `bigint GENERATED BY DEFAULT AS IDENTITY` 配 `@Id(keyType = KeyType.Auto)`；jsonb 字段注册类型处理器。
- **国际化**：`spring.messages.basename` 指向 i18n 目录，校验注解 message 统一使用占位符键，由全局异常处理器经 MessageSource 翻译后返回，杜绝在 DTO 中硬编码中文。
- **安全边界**：JWT 密钥从配置项读取、禁止硬编码；白名单仅放行登录、健康检查、文档等必要路径；`MethodArgumentNotValidException` 的返回体不得回显敏感字段值。
- **影响面控制**：对象存储、Excel 导入导出、限流、可观测性、MQ 本次不实现，仅预留包结构位置，不引入依赖、不写实现（但租户跨线程传播工具方法必须提供，这是安全边界）。

## 架构设计

四层分层 + 租户上下文 + Action 编排层：

- **common 层**：统一返回体与返回码、分页封装、`AbstractActionTemplate` 业务模板、实体积基类、全局填充监听器、异常体系、校验分组、链路追踪
- **config 层**：MyBatis-Flex、Redis、Security、SpringDoc、国际化、Jackson 兼容配置
- **security 层**：令牌签发与校验、认证过滤器、登录用户模型与上下文、未认证与未授权处理器
- **tenant 层**：租户上下文（ThreadLocal）、TenantFactory 实现、跨线程传播工具
- **module 层**：按业务模块划分，模块内 `controller / action / service / repository / entity / dto.req / dto.resp / convert` 自成一格

## 目录结构

```
d:/gm-workspace/gm-company/04后端工程/ace-backend/
├── pom.xml                                    # [NEW] 单模块工程自身即父 POM。定义 Boot 4.1.1 parent、版本属性（java.version=23）、依赖清单；核心是 release=23、proc=full 与 annotationProcessorPaths 四段处理器链。
├── README.md                                  # [NEW] 启动步骤、JDK23/Maven3.9.12 环境要求、版本矩阵、Jackson 3 风险与降级说明、Apifox 双通道用法、Action/Req/Resp/Repository 编码规范、租户三条红线、索引 tenant_id 前置规范、已知风险清单。
├── db/
│   └── schema.sql                             # [NEW] 七表 DDL：sys_tenant、sys_user、sys_role、sys_menu、sys_user_role、sys_role_menu 及示例业务表。小写下划线，id 用 identity 自增，逻辑删除列统一 is_deleted（不用 deleted），带 tenant_id 的注释与联合索引（tenant_id 前置）；默认不建 version 列，需要乐观锁的表单独加。
├── script/
│   ├── start.sh                               # [NEW] Linux 启动脚本，支持 start/stop/restart/status，含 JVM 参数与 profile 传参。
│   └── start.bat                              # [NEW] Windows 启动脚本，功能同上，适配本机环境。
└── src/main/
    ├── java/com/gm/ace/
    │   ├── AceBackendApplication.java         # [NEW] 启动类，@MapperScan 指向 **.repository 包。
    │   ├── common/
    │   │   ├── result/R.java                  # [NEW] 统一返回体。承载 code/message/data/timestamp/traceId；必须提供泛型静态工厂 data/status/ok/fail 以适配 Action 模板。
    │   │   ├── result/PageResult.java         # [NEW] 分页返回封装，与 MyBatis-Flex Page 对接。
    │   │   ├── result/ResultCode.java         # [NEW] 返回码枚举：成功、业务失败、参数错误、未认证、未授权、系统异常。
    │   │   ├── action/AbstractActionTemplate.java # [NEW] 业务抽象模板。保留用户给定签名，修正泛型不兼容，增强 after 钩子与模式匹配写法。
    │   │   ├── base/BaseEntity.java           # [NEW] 实体基类：id 自增、createBy/createTime/updateBy/updateTime、@Column(tenantId=true) tenantId、@Column(isLogicDelete=true) isDeleted。不含乐观锁（按需在具体实体单独声明）。
    │   │   ├── fill/BaseEntityInsertListener.java # [NEW] 全局插入监听器，注入 createBy/createTime/updateBy/updateTime。
    │   │   ├── fill/BaseEntityUpdateListener.java # [NEW] 全局更新监听器，注入 updateBy/updateTime。
    │   │   ├── exception/BizException.java    # [NEW] 业务异常，携带返回码。
    │   │   ├── exception/SystemException.java # [NEW] 系统异常，日志记全量栈，响应仅返回泛化提示。
    │   │   ├── exception/GlobalExceptionHandler.java # [NEW] 全局兜底：参数校验、业务异常、系统异常、未认证、未授权、404、方法不支持，经 MessageSource 翻译后包装为 R。
    │   │   ├── validate/AddGroup.java         # [NEW] 新增分组校验标识。
    │   │   ├── validate/UpdateGroup.java      # [NEW] 更新分组校验标识。
    │   │   ├── validate/EmptyReq.java         # [NEW] 无入参接口的约定 Req 类型，避免 execute() 传裸 null。
    │   │   └── trace/TraceIdFilter.java       # [NEW] 生成 traceId 写入 MDC，透传至响应头，注入 R 的 traceId，并在 finally 清理 MDC。
    │   ├── config/
    │   │   ├── MybatisFlexConfig.java         # [NEW] 分页方言、逻辑删除、PostgreSQL 主键策略；构造器中通过 FlexGlobalConfig 注册 BaseEntity 的全局 insert/update 监听器。
    │   │   ├── RedisConfig.java               # [NEW] RedisTemplate 序列化配置（String + JSON，禁用 JDK 原生序列化）。
    │   │   ├── SecurityConfig.java            # [NEW] 无状态会话、白名单、过滤器链、密码编码器、开启方法级鉴权。
    │   │   ├── OpenApiConfig.java             # [NEW] SpringDoc 元信息与 Bearer 安全方案，prod 默认关闭。
    │   │   ├── MessageSourceConfig.java       # [NEW] 国际化资源与校验消息源打通。
    │   │   └── JacksonCompatConfig.java       # [NEW] Jackson 3 兼容适配，按验证结论决定沿用或降级，结论写入 README。
    │   ├── security/
    │   │   ├── JwtProperties.java             # [NEW] 令牌配置项，密钥与有效期从配置读取，禁止硬编码。
    │   │   ├── JwtTokenService.java           # [NEW] 基于 Nimbus 的签发、解析与校验，签发时写入 tenant_id claim。
    │   │   ├── JwtAuthenticationFilter.java   # [NEW] 验签后从 claim 取 tenant_id 写入 TenantContext，装配认证信息；finally 中清理。
    │   │   ├── LoginUser.java                 # [NEW] 登录用户模型，承载用户标识、租户 ID、角色与权限集合。
    │   │   ├── LoginUserContext.java          # [NEW] 当前登录用户上下文，供填充监听器取 createBy/updateBy。
    │   │   └── handler/                       # [NEW] 未认证与未授权处理器，返回统一返回体 R。
    │   ├── tenant/
    │   │   ├── TenantContext.java             # [NEW] ThreadLocal 租户上下文，提供 set/get/clear 与 runWith 模板方法（try/finally 保证清理），供异步/MQ 显式传播。
    │   │   └── TenantFactoryImpl.java         # [NEW] TenantFactory 实现，只从 TenantContext 读取租户 ID，严禁读请求头或请求参数。
    │   └── module/system/                     # [NEW] RBAC + 租户示范模块，完整示范工程范式，不实现全套业务 CRUD。
    │       ├── controller/SysUserController.java  # [NEW] 极薄控制器：接收 XxxReq + @Validated 分组，一行转发给对应 Action。
    │       ├── action/UserPageAction.java     # [NEW] Action 范式示范（分页查询），extends AbstractActionTemplate<UserPageReq, PageResult<UserPageResp>>。
    │       ├── action/UserDetailAction.java   # [NEW] Action 范式示范（单条查询），示范 @PreAuthorize 鉴权与 Repository 直调。
    │       ├── service/SysUserService.java    # [NEW] 领域服务骨架，示范跨 Action 复用逻辑的落点。
    │       ├── repository/SysUserRepository.java  # [NEW] 数据层（原 mapper 层改名），extends BaseMapper<SysUser>。
    │       ├── repository/SysRoleRepository.java  # [NEW] 角色数据层骨架。
    │       ├── repository/SysMenuRepository.java  # [NEW] 菜单数据层骨架。
    │       ├── entity/SysTenant.java          # [NEW] 租户实体，自身不带 tenant_id。
    │       ├── entity/SysUser.java            # [NEW] 用户实体，继承 BaseEntity。
    │       ├── entity/SysRole.java            # [NEW] 角色实体，继承 BaseEntity。
    │       ├── entity/SysMenu.java            # [NEW] 菜单实体，平台级不带 tenant_id。
    │       ├── dto/req/UserPageReq.java       # [NEW] 入参 DTO，字段带 Javadoc（供 Apifox 解析），校验注解使用国际化占位符键。
    │       ├── dto/req/UserDetailReq.java     # [NEW] 入参 DTO。
    │       ├── dto/resp/UserPageResp.java     # [NEW] 出参 DTO，字段带 Javadoc。
    │       ├── dto/resp/UserDetailResp.java   # [NEW] 出参 DTO。
    │       └── convert/SysUserConvert.java    # [NEW] MapStruct 转换器（@Mapper(componentModel = "spring")），验证 APT 生效，实体转 Resp。
    └── resources/
        ├── application.yml                    # [NEW] 主配置，公共项与 profile 激活。
        ├── application-dev.yml                # [NEW] 开发环境：数据源、Redis、日志级别、SQL 打印、SpringDoc 开启。
        ├── application-test.yml               # [NEW] 测试环境配置。
        ├── application-prod.yml               # [NEW] 生产环境配置：SpringDoc 关闭、日志降级、敏感项外置。
        ├── i18n/messages.properties           # [NEW] 默认国际化资源。
        ├── i18n/messages_zh_CN.properties     # [NEW] 中文资源，含校验与异常文案。
        ├── i18n/messages_en_US.properties     # [NEW] 英文资源。
        └── logback-spring.xml                 # [NEW] 日志配置，按 profile 区分，控制台输出含 MDC traceId。
```

## 关键代码结构

**1. 统一返回体的泛型静态工厂（Action 模板能否编译通过的前提）**

```java
public final class R<T> implements Serializable {
    private int code;
    private String message;
    private T data;
    private long timestamp;
    private String traceId;

    /** 泛型静态工厂：返回 R<T> 而非 R<Boolean>，否则 AbstractActionTemplate 中 R.status(...) 无法赋值给 R<Response> */
    public static <T> R<T> data(T data);
    public static <T> R<T> status(boolean success);
    public static <T> R<T> ok();
    public static <T> R<T> fail(ResultCode resultCode);
    public static <T> R<T> fail(ResultCode resultCode, String message);
}
```

**2. 业务抽象模板（保留用户给定签名，做兼容性修正与最小增强）**

```java
/**
 * 业务抽象模板，子类实现execute即可，一个类干一件事
 *
 * @author guoym
 * @param <Request>  入参类型，约定为 #{业务名称}Req；无入参场景使用 EmptyReq
 * @param <Response> 出参类型，约定为 #{业务名称}Resp
 */
public abstract class AbstractActionTemplate<Request, Response> {

    protected void before(Request request) {}

    /** 业务编排主体，由子类按需标注 @Transactional 控制事务边界 */
    protected abstract Response executeAction(Request request);

    /** 后置钩子，用于埋点、操作日志、后置缓存，默认空实现 */
    protected void after(Request request, Response response) {}

    public R<Response> execute(Request request) {
        before(request);
        Response response = executeAction(request);
        after(request, response);
        // JDK 16+ 模式匹配；response 为 null 时走 R.data(null)，语义为成功但无数据
        if (response instanceof Boolean bool) {
            return R.status(bool);
        }
        return R.data(response);
    }

    /** 无入参场景：约定 Request 为 EmptyReq，禁止直接传裸 null */
    public R<Response> execute() {
        return execute(null);
    }
}
```

**3. 实体基类与租户上下文（自动注入 + 数据隔离的核心）**

```java
/** 实体基类：通用字段统一管理，审计字段由全局监听器自动注入 */
public abstract class BaseEntity implements Serializable {
    @Id(keyType = KeyType.Auto)
    private Long id;                                   // bigint GENERATED BY DEFAULT AS IDENTITY
    private Long createBy;
    private LocalDateTime createTime;
    private Long updateBy;
    private LocalDateTime updateTime;
    @Column(tenantId = true)
    private Long tenantId;                             // 由 TenantFactory 在增删改查时强制处理
    /** 逻辑删除标记，列名 is_deleted（不用 deleted，避免与 SQL 关键字/构造器语义混淆） */
    @Column(value = "is_deleted", isLogicDelete = true)
    private Integer isDeleted;
    // 注意：乐观锁 version 不在此处声明，需要的实体自行添加 @Column(version = true)
}

/** 租户上下文：租户 ID 只由 JwtAuthenticationFilter 在验签通过后写入，严禁取自请求头或请求参数 */
public final class TenantContext {
    private static final ThreadLocal<Long> HOLDER = new ThreadLocal<>();

    public static void set(Long tenantId);
    public static Long get();
    public static void clear();
    /** 异步/MQ/定时任务必须用它显式传播租户，内部 try/finally 保证清理 */
    public static <T> T runWith(Long tenantId, Supplier<T> supplier);
}

/** 注册到基类，所有继承 BaseEntity 的子类均生效 */
public class BaseEntityInsertListener implements InsertListener {
    @Override
    public void onInsert(Object entity) {
        if (entity instanceof BaseEntity base) {
            Long userId = LoginUserContext.getUserId();
            base.setCreateBy(userId);
            base.setUpdateBy(userId);
            base.setCreateTime(LocalDateTime.now());
            base.setUpdateTime(LocalDateTime.now());
        }
    }
}
```

**4. 编译插件的注解处理器链（顺序不可调换，JDK 23 下 APT 生效的唯一保障）**

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <configuration>
        <release>23</release>
        <proc>full</proc>
        <annotationProcessorPaths>
            <path>
                <groupId>org.projectlombok</groupId>
                <artifactId>lombok</artifactId>
                <version>1.18.46</version>
            </path>
            <path>
                <groupId>org.projectlombok</groupId>
                <artifactId>lombok-mapstruct-binding</artifactId>
                <version>0.2.0</version>
            </path>
            <path>
                <groupId>org.mapstruct</groupId>
                <artifactId>mapstruct-processor</artifactId>
                <version>1.6.3</version>
            </path>
            <path>
                <groupId>com.mybatis-flex</groupId>
                <artifactId>mybatis-flex-processor</artifactId>
                <version>1.11.8</version>
            </path>
        </annotationProcessorPaths>
    </configuration>
</plugin>
```