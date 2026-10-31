package io.github.luckyqing.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 动态路由数据源：根据 {@link ShardingContext} 决定使用原始数据源还是 ShardingSphere 数据源。
 * <ul>
 *     <li>默认 → 原始 HikariCP 数据源（直连，零开销）</li>
 *     <li>分片模式 → ShardingSphere 数据源（分表路由）</li>
 * </ul>
 *
 * @author collin.li
 * @date 2026-07-25
 */
public class RoutingDataSource extends AbstractRoutingDataSource {

    /**
     * 数据源 key：原始数据源
     */
    public static final String DS_RAW = "raw";

    /**
     * 数据源 key：ShardingSphere 分片数据源
     */
    public static final String DS_SHARDING = "sharding";

    @Override
    protected Object determineCurrentLookupKey() {
        return ShardingContext.isSharding() ? DS_SHARDING : DS_RAW;
    }
}
