package com.gm.ace.common.base;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 实体基类：所有业务表共有字段（不含租户ID，见 {@link TenantEntity}）
 * <ul>
 *   <li>id：雪花主键（由 {@link com.gm.ace.common.fill.BaseEntityInsertListener} 生成）</li>
 *   <li>createBy/createTime/updateBy/updateTime：审计字段</li>
 *   <li>isDeleted：逻辑删除（标记 {@code @Column(isLogicDelete = true)}）</li>
 * </ul>
 *
 * @author guoym
 */
@Data
public abstract class BaseEntity implements Serializable {

    @Id(keyType = KeyType.None)
    private Long id;

    @Column("create_by")
    private Long createBy;

    @Column("create_time")
    private LocalDateTime createTime;

    @Column("update_by")
    private Long updateBy;

    @Column("update_time")
    private LocalDateTime updateTime;

    @Column(value = "is_deleted", isLogicDelete = true)
    private Integer isDeleted;
}
