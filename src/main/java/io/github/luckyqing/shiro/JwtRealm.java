package io.github.luckyqing.shiro;

import io.github.luckyqing.common.JwtUtil;
import io.github.luckyqing.service.PermissionService;
import io.jsonwebtoken.Claims;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

/**
 * Shiro JWT Realm
 * 认证：校验 JWT token 有效性
 * 授权：从 Redis 缓存加载用户角色和权限
 */
public class JwtRealm extends AuthorizingRealm {

    @Autowired
    private PermissionService permissionService;

    /** 只处理 JwtToken 类型 */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /** 授权：加载角色和权限 */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String token = (String) principals.getPrimaryPrincipal();
        Long userId = JwtUtil.getUserId(token);
        if (userId == null) return null;

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        // 从 Redis 缓存加载角色
        Set<String> roles = permissionService.getUserRoles(userId);
        info.setRoles(roles);
        // 从 Redis 缓存加载权限码
        Set<String> perms = permissionService.getUserPermCodes(userId);
        info.setStringPermissions(perms);
        return info;
    }

    /** 认证：校验 JWT */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authToken) throws AuthenticationException {
        String token = (String) authToken.getPrincipal();
        Claims claims = JwtUtil.parseToken(token);
        if (claims == null) {
            throw new AuthenticationException("Token 无效或已过期");
        }
        return new SimpleAuthenticationInfo(token, token, getName());
    }
}
