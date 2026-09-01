package com.gm.ace.common.result;

import com.mybatisflex.core.paginate.Page;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * 分页返回封装，与 MyBatis-Flex 的 {@link Page} 对接
 *
 * @param <T> 出参类型
 * @author guoym
 */
@Data
public class PageResult<T> implements Serializable {

    /** 总记录数 */
    private long total;

    /** 当前页码 */
    private long pageNumber;

    /** 每页条数 */
    private long pageSize;

    /** 总页数 */
    private long totalPage;

    /** 当前页数据 */
    private List<T> records;

    /**
     * 把 MyBatis-Flex 的分页对象转换为统一分页返回体
     *
     * @param page      分页查询结果
     * @param converter 实体到出参的转换函数（通常用 MapStruct 转换器方法引用）
     * @param <T>       出参类型
     * @param <E>       实体类型
     * @return 分页返回体
     */
    public static <T, E> PageResult<T> of(Page<E> page, Function<E, T> converter) {
        PageResult<T> result = new PageResult<>();
        result.setTotal(page.getTotalRow());
        result.setPageNumber(page.getPageNumber());
        result.setPageSize(page.getPageSize());
        result.setTotalPage(page.getTotalPage());
        List<E> source = page.getRecords();
        if (source == null || source.isEmpty()) {
            result.setRecords(new ArrayList<>());
            return result;
        }
        List<T> records = new ArrayList<>(source.size());
        for (E item : source) {
            records.add(converter.apply(item));
        }
        result.setRecords(records);
        return result;
    }
}
