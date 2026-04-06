package io.github.luckyqing.common;

/**
 * 当前登录用户上下文（基于 ThreadLocal）
 * 在拦截器中设置，在 MetaObjectHandler 中读取
 */
public class UserContext {

    private static final ThreadLocal<Long> CURRENT_USER_ID = new ThreadLocal<>();

    public static void setUserId(Long userId) {
        CURRENT_USER_ID.set(userId);
    }

    public static Long getUserId() {
        return CURRENT_USER_ID.get();
    }

    public static void clear() {
        CURRENT_USER_ID.remove();
    }
}
