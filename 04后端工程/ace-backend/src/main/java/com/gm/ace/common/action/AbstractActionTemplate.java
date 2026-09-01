package com.gm.ace.common.action;

import com.gm.ace.common.result.R;

/**
 * 业务抽象模板，子类实现 executeAction 即可，一个类干一件事
 * <p>
 * 统一返回 {@link R}：
 * <ul>
 *   <li>返回值为 {@code Boolean} → 走 {@link R#status(boolean)}（删除/启用禁用等无数据场景）</li>
 *   <li>返回值为 {@code null} → 走 {@link R#ok()}（无数据场景）</li>
 *   <li>其余 → 走 {@link R#data(Object)}</li>
 * </ul>
 *
 * @param <Request>  入参类型
 * @param <Response> 出参类型
 * @author guoym
 */
public abstract class AbstractActionTemplate<Request, Response> {

    /** 前置处理，默认空实现，子类按需重写 */
    protected void before(Request request) {
    }

    /** 核心执行，子类必须实现 */
    protected abstract Response executeAction(Request request);

    /** 后置处理，默认空实现，子类按需重写 */
    protected void after(Request request, Response response) {
    }

    /** 模板入口：组装成统一返回体 */
    public R<Response> execute(Request request) {
        before(request);
        Response response = executeAction(request);
        R<Response> result = build(response);
        after(request, response);
        return result;
    }

    private R<Response> build(Response response) {
        if (response == null) {
            return R.ok();
        }
        if (response instanceof Boolean b) {
            return R.status(b);
        }
        return R.data(response);
    }
}
