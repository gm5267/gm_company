package com.gm.ace.common.trace;

import cn.hutool.core.util.IdUtil;
import com.gm.ace.tenant.LoginUserContext;
import com.gm.ace.tenant.TenantContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 链路追踪 ID 过滤器（最外层）
 * <p>
 * 每个请求生成（或透传）traceId 写入 MDC 供日志与 {@link com.gm.ace.common.result.R} 使用，
 * 并回写响应头；请求结束后清理租户/登录用户线程上下文，避免线程池串号。
 *
 * @author guoym
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TraceIdFilter extends OncePerRequestFilter {

    /** MDC key 与响应头名 */
    public static final String TRACE_ID = "traceId";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String tid = request.getHeader(TRACE_ID);
        if (tid == null || tid.isBlank()) {
            tid = IdUtil.fastUUID();
        }
        MDC.put(TRACE_ID, tid);
        try {
            response.setHeader(TRACE_ID, tid);
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(TRACE_ID);
            TenantContext.clear();
            LoginUserContext.clear();
        }
    }
}
