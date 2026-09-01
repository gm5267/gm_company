package com.gm.ace.common.util;

import com.gm.ace.common.base.PageReq;
import com.mybatisflex.core.query.QueryWrapper;

import java.util.Collection;
import java.util.Set;

/**
 * 排序白名单工具，防止 order by 字段名 SQL 注入
 * <p>
 * order by 的字段名无法用 JDBC 参数绑定，客户端传入的列名必须落在白名单内，否则该排序项被忽略。
 * MyBatis-Flex 1.x 的排序通过 {@link QueryWrapper#orderBy(String, Boolean)} 设置。
 *
 * @author guoym
 */
public final class QueryKit {

    private QueryKit() {
    }

    /**
     * 把白名单内的排序项应用到查询条件
     *
     * @param qw        查询条件
     * @param orders    排序项（来自请求）
     * @param whiteList 字段白名单（下划线命名）
     */
    public static void applyOrders(QueryWrapper qw, Collection<PageReq.OrderItem> orders, Set<String> whiteList) {
        if (qw == null || orders == null || orders.isEmpty() || whiteList == null || whiteList.isEmpty()) {
            return;
        }
        for (PageReq.OrderItem item : orders) {
            String column = item.getColumn();
            if (column == null || column.isBlank() || !whiteList.contains(column)) {
                continue;
            }
            qw.orderBy(column, !Boolean.FALSE.equals(item.getAsc()));
        }
    }
}
