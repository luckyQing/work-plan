package io.github.luckyqing.common;

/**
 * 角色标识常量
 * 对应 t_role 表 role_code 字段，用于权限判断和 Shiro 注解
 */
public final class RoleConstants {

    private RoleConstants() {
    }

    /** 系统管理员 */
    public static final String ADMIN = "ADMIN";

    /** 普通用户 */
    public static final String USER = "USER";
}
