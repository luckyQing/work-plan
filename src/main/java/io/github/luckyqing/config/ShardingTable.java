package io.github.luckyqing.config;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标注在 Mapper 接口上，表示该 Mapper 的 SQL 需要经过 ShardingSphere 分片路由。
 * <p>未标注此注解的 Mapper 默认直连数据库，零开销。</p>
 *
 * @author collin.li
 * @date 2026-07-25
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ShardingTable {
}