package io.github.luckyqing.common;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 将请求包装为 {@link ContentCachingRequestWrapper}，
 * 以便在全局异常处理时能够读取已消费的请求体（如 JSON body）用于日志记录。
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestCachingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (request instanceof ContentCachingRequestWrapper) {
            filterChain.doFilter(request, response);
        } else {
            filterChain.doFilter(new ContentCachingRequestWrapper(request), response);
        }
    }
}
