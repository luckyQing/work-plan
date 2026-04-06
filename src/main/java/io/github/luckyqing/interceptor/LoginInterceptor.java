package io.github.luckyqing.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.luckyqing.common.JwtUtil;
import io.github.luckyqing.common.R;
import io.github.luckyqing.common.UserContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录拦截器
 * 校验 JWT token，将 userId 存入 request 和 ThreadLocal
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        if (token == null || token.isEmpty()) {
            writeError(response, 401, "未登录");
            return false;
        }

        Long userId = JwtUtil.getUserId(token);
        if (userId == null) {
            writeError(response, 401, "登录已过期");
            return false;
        }

        request.setAttribute("userId", userId);
        UserContext.setUserId(userId);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        UserContext.clear();
    }

    private void writeError(HttpServletResponse response, int code, String msg) throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(200);
        response.getWriter().write(objectMapper.writeValueAsString(R.fail(code, msg)));
    }
}
