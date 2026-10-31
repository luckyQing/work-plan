package io.github.luckyqing.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 拦截标注了 {@link ShardingTable} 的 Mapper 接口方法，
 * 在执行前开启分片路由，执行后恢复。
 * <p>
 * MyBatis Mapper 运行时是 JDK 动态代理（$ProxyN），接口上的注解不会被实现类继承，
 * 且 insert/selectById 等方法声明在 BaseMapper 上，因此不能用 {@code @within} 匹配，
 * 这里拦截所有 Mapper 方法，再检查目标对象实现的接口上是否有 {@link ShardingTable}。
 * </p>
 *
 * @author collin.li
 * @date 2026-07-25
 */
@Slf4j
@Aspect
@Component
public class ShardingTableAspect {

    /** 目标类 → 是否需要分片路由 */
    private final Map<Class<?>, Boolean> shardingCache = new ConcurrentHashMap<>();

    /**
     * 拦截所有 MyBatis-Plus Mapper 方法（含 BaseMapper 继承来的方法）
     */
    @Around("execution(* com.baomidou.mybatisplus.core.mapper.Mapper+.*(..))")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object target = joinPoint.getTarget();
        // 已处于分片路由中（嵌套调用）或目标未标注注解时直接执行
        if (target == null || ShardingContext.isSharding() || !isShardingMapper(target.getClass())) {
            return joinPoint.proceed();
        }
        ShardingContext.enableSharding();
        try {
            return joinPoint.proceed();
        } finally {
            ShardingContext.disableSharding();
        }
    }

    private boolean isShardingMapper(Class<?> targetClass) {
        return shardingCache.computeIfAbsent(targetClass, clazz -> {
            for (Class<?> iface : ClassUtils.getAllInterfacesForClassAsSet(clazz)) {
                if (iface.isAnnotationPresent(ShardingTable.class)) {
                    return Boolean.TRUE;
                }
            }
            return Boolean.FALSE;
        });
    }
}
