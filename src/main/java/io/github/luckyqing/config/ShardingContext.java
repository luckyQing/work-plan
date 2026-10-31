package io.github.luckyqing.config;

/**
 * 分片路由上下文，基于 ThreadLocal 标识当前线程是否走 ShardingSphere 分片数据源。
 *
 * @author collin.li
 * @date 2026-07-25
 */
public final class ShardingContext {

    private static final ThreadLocal<Boolean> SHARDING_FLAG = ThreadLocal.withInitial(() -> Boolean.FALSE);

    private ShardingContext() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * 开启分片路由
     */
    public static void enableSharding() {
        SHARDING_FLAG.set(Boolean.TRUE);
    }

    /**
     * 关闭分片路由
     */
    public static void disableSharding() {
        SHARDING_FLAG.remove();
    }

    /**
     * 当前线程是否启用分片
     */
    public static boolean isSharding() {
        return Boolean.TRUE.equals(SHARDING_FLAG.get());
    }
}
