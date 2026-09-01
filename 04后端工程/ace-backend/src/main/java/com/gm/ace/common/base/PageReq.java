package com.gm.ace.common.base;

import com.mybatisflex.core.paginate.Page;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页请求基类
 *
 * @author guoym
 */
@Data
public class PageReq {

    /** 页码，从 1 开始 */
    @NotNull(message = "{valid.page.number.notnull}")
    @Min(value = 1, message = "{valid.page.number.min}")
    private Integer pageNumber = 1;

    /** 每页条数，上限 500 */
    @NotNull(message = "{valid.page.size.notnull}")
    @Min(value = 1, message = "{valid.page.size.min}")
    @Max(value = 500, message = "{valid.page.size.max}")
    private Integer pageSize = 10;

    /** 排序条件，字段名必须来自字段白名单（见 QueryKit），否则被忽略 */
    @Valid
    private List<OrderItem> orders = new ArrayList<>();

    /** 构造 MyBatis-Flex 分页对象 */
    public <T> Page<T> toFlexPage() {
        return new Page<>(pageNumber, pageSize);
    }

    /** 排序项 */
    @Data
    public static class OrderItem {

        /** 下划线命名字段名 */
        private String column;

        /** 是否升序，默认升序 */
        private Boolean asc = Boolean.TRUE;
    }
}
