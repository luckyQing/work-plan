package io.github.luckyqing.shiro;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.luckyqing.common.JwtUtil;
import io.github.luckyqing.common.R;
import io.github.luckyqing.common.UserContext;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.http.HttpStatus;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * JWT 过滤器
 * 从请求头提取 token，交给 Shiro 认证
 */
public class JwtFilter extends BasicHttpAuthenticationFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        String token = getToken((HttpServletRequest) request);
        return token != null;
    }

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        String token = getToken((HttpServletRequest) request);
        JwtToken jwtToken = new JwtToken(token);
        getSubject(request, response).login(jwtToken);
        // 将 userId 存入 ThreadLocal
        Long userId = JwtUtil.getUserId(token);
        if (userId != null) {
            UserContext.setUserId(userId);
            ((HttpServletRequest) request).setAttribute("userId", userId);
        }
        return true;
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (isLoginAttempt(request, response)) {
            try {
                executeLogin(request, response);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletResponse res = (HttpServletResponse) response;
        res.setContentType("application/json;charset=UTF-8");
        res.setStatus(HttpStatus.OK.value());
        res.getWriter().write(objectMapper.writeValueAsString(R.fail(401, "未登录或登录已过期")));
        return false;
    }

    @Override
    public void afterCompletion(ServletRequest request, ServletResponse response, Exception exception) throws Exception {
        UserContext.clear();
    }

    private String getToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}
