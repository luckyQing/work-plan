package io.github.luckyqing.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.luckyqing.common.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录拦截器
 * 校验请求头中的 Authorization token，从 Redis 中验证登录状态
 * 验证通过后将 userId 存入 request attribute 供后续使用
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 放行 OPTIONS 预检请求
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        // 从请求头获取 token
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // token 为空，返回未登录
        if (token == null || token.isEmpty()) {
            writeError(response, 401, "未登录");
            return false;
        }

        // 从 Redis 校验 token 有效性
        Object userId = redisTemplate.opsForValue().get("token:" + token);
        if (userId == null) {
            writeError(response, 401, "登录已过期");
            return false;
        }

        // 将用户ID存入 request，供 Controller 使用
        request.setAttribute("userId", Long.valueOf(userId.toString()));
        return true;
    }

    /**
     * 向客户端写入错误响应
     */
    private void writeError(HttpServletResponse response, int code, String msg) throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(200);
        response.getWriter().write(objectMapper.writeValueAsString(R.fail(code, msg)));
    }
}
