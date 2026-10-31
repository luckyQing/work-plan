package io.github.luckyqing.config;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.driver.api.ShardingSphereDataSourceFactory;
import org.apache.shardingsphere.infra.algorithm.core.config.AlgorithmConfiguration;
import org.apache.shardingsphere.infra.config.mode.ModeConfiguration;
import org.apache.shardingsphere.infra.config.rule.RuleConfiguration;
import org.apache.shardingsphere.mode.repository.standalone.StandalonePersistRepositoryConfiguration;
import org.apache.shardingsphere.sharding.api.config.ShardingRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.rule.ShardingTableRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.strategy.sharding.StandardShardingStrategyConfiguration;
import org.apache.shardingsphere.single.config.SingleRuleConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.StringJoiner;

/**
 * 数据源配置：通过 ShardingSphere 5.5.x Java API 编程式构建分片数据源，
 * 配合 {@link RoutingDataSource} 实现注解驱动的动态路由。
 * <ul>
 *     <li>rawDataSource —— 原始 HikariCP 数据源（直连）</li>
 *     <li>shardingDataSource —— ShardingSphere 分片数据源</li>
 *     <li>dataSource —— 路由数据源（Primary，供 MyBatis-Plus 使用）</li>
 * </ul>
 *
 * @author collin.li
 * @date 2026-07-25
 */
@Slf4j
@Configuration
public class DataSourceConfig {

    /** ShardingSphere 内部的逻辑数据源名 */
    private static final String DS_NAME = "ds";

    private static final String WORK_LOG_TABLE = "t_work_log";

    private static final String WORK_LOG_ALGORITHM = "work_log_interval";

    private static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private static final DateTimeFormatter SUFFIX_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Value("${spring.datasource.url}")
    private String jdbcUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${sharding.work-log.start-date}")
    private String startDate;

    @Value("${sharding.work-log.end-date}")
    private String endDate;

    @Value("${sharding.sql-show:false}")
    private boolean sqlShow;

    /**
     * 原始 HikariCP 数据源（非分片表直连）
     */
    @Bean(destroyMethod = "close")
    public HikariDataSource rawDataSource() {
        return createHikari("raw-pool");
    }

    /**
     * ShardingSphere 分片数据源（仅分片表使用）
     */
    @Bean(destroyMethod = "close")
    public DataSource shardingDataSource() throws SQLException {
        Map<String, DataSource> dataSourceMap = Collections.singletonMap(DS_NAME, createHikari("sharding-pool"));

        // Single 规则：加载数据源中所有非分片表，使 t_work_log 与 t_task 等单表的关联查询能正确路由
        SingleRuleConfiguration singleRule = new SingleRuleConfiguration();
        singleRule.setTables(Collections.singletonList("*.*"));
        singleRule.setDefaultDataSource(DS_NAME);

        Collection<RuleConfiguration> rules = Arrays.asList(buildShardingRule(), singleRule);

        // 运行模式：Standalone + 内存元数据仓库（元数据每次启动时从数据库加载）
        ModeConfiguration modeConfig = new ModeConfiguration("Standalone",
                new StandalonePersistRepositoryConfiguration("Memory", new Properties()));

        Properties props = new Properties();
        props.setProperty("sql-show", String.valueOf(sqlShow));

        return ShardingSphereDataSourceFactory.createDataSource(modeConfig, dataSourceMap, rules, props);
    }

    /**
     * 路由数据源（Primary），MyBatis-Plus / Spring TX 使用此数据源
     */
    @Primary
    @Bean
    public DataSource dataSource(@Qualifier("rawDataSource") DataSource rawDataSource,
                                 @Qualifier("shardingDataSource") DataSource shardingDataSource) {
        RoutingDataSource routing = new RoutingDataSource();
        Map<Object, Object> targetDataSources = new HashMap<>(4);
        targetDataSources.put(RoutingDataSource.DS_RAW, rawDataSource);
        targetDataSources.put(RoutingDataSource.DS_SHARDING, shardingDataSource);
        routing.setTargetDataSources(targetDataSources);
        routing.setDefaultTargetDataSource(rawDataSource);
        return routing;
    }

    private HikariDataSource createHikari(String poolName) {
        HikariDataSource ds = new HikariDataSource();
        ds.setPoolName(poolName);
        ds.setDriverClassName(driverClassName);
        ds.setJdbcUrl(jdbcUrl);
        ds.setUsername(username);
        ds.setPassword(password);
        return ds;
    }

    /**
     * 构建 t_work_log 按日分表规则：按 create_time 路由到 t_work_log_yyyyMMdd
     */
    private ShardingRuleConfiguration buildShardingRule() {
        // 实际数据节点必须列出所有分表，INTERVAL 算法只会在这些表中选择路由目标
        ShardingTableRuleConfiguration tableRule =
                new ShardingTableRuleConfiguration(WORK_LOG_TABLE, buildWorkLogDataNodes());
        tableRule.setTableShardingStrategy(
                new StandardShardingStrategyConfiguration("create_time", WORK_LOG_ALGORITHM));

        Properties algorithmProps = new Properties();
        algorithmProps.setProperty("datetime-pattern", DATETIME_PATTERN);
        algorithmProps.setProperty("datetime-lower", startDate);
        algorithmProps.setProperty("datetime-upper", endDate);
        algorithmProps.setProperty("sharding-suffix-pattern", "yyyyMMdd");
        algorithmProps.setProperty("datetime-interval-amount", "1");
        algorithmProps.setProperty("datetime-interval-unit", "DAYS");

        ShardingRuleConfiguration config = new ShardingRuleConfiguration();
        config.getTables().add(tableRule);
        config.getShardingAlgorithms().put(WORK_LOG_ALGORITHM, new AlgorithmConfiguration("INTERVAL", algorithmProps));
        return config;
    }

    /**
     * 生成 ds.t_work_log_yyyyMMdd 列表（逗号分隔），覆盖 [start-date, end-date] 的每一天
     */
    private String buildWorkLogDataNodes() {
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern(DATETIME_PATTERN);
        LocalDate first = LocalDateTime.parse(startDate, pattern).toLocalDate();
        LocalDate last = LocalDateTime.parse(endDate, pattern).toLocalDate();
        if (last.isBefore(first)) {
            throw new IllegalStateException("sharding.work-log.end-date 不能早于 start-date");
        }
        StringJoiner nodes = new StringJoiner(",");
        for (LocalDate day = first; !day.isAfter(last); day = day.plusDays(1)) {
            nodes.add(DS_NAME + "." + WORK_LOG_TABLE + "_" + day.format(SUFFIX_FORMATTER));
        }
        return nodes.toString();
    }
}
