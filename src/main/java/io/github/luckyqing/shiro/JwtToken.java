package io.github.luckyqing.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * 自定义 Shiro Token，封装 JWT 字符串
 */
public class JwtToken implements AuthenticationToken {

    private final String token;

    public JwtToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
