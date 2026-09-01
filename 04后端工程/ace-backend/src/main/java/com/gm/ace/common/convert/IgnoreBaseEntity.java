package com.gm.ace.common.convert;

import org.mapstruct.Mapping;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 添加到 MapStruct 的 Mapper 方法上，忽略 BaseEntity 的字段，避免复制时覆盖主键、审计字段与租户字段。
 * <p>
 * 字段清单已对齐本项目 BaseEntity（createUser→createBy，updateUser→updateBy，新增 tenantId，移除无用的 createDept）。
 * <b>注意</b>：目标类型必须是 BaseEntity 子类，否则编译报 Unknown property。
 *
 * @author guoym
 */
@Documented
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Mapping(target = "id", ignore = true)
@Mapping(target = "createBy", ignore = true)
@Mapping(target = "createTime", ignore = true)
@Mapping(target = "updateBy", ignore = true)
@Mapping(target = "updateTime", ignore = true)
@Mapping(target = "tenantId", ignore = true)
@Mapping(target = "isDeleted", ignore = true)
public @interface IgnoreBaseEntity {
}
