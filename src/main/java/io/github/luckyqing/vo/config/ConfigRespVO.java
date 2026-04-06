package io.github.luckyqing.vo.config;

import lombok.Data;

/**
 * 配置项响应数据
 */
@Data
public class ConfigRespVO {

    /** 配置ID */
    private Long id;

    /** 配置类别 */
    private String configType;

    /** 显示名称 */
    private String configLabel;

    /** 配置值 */
    private String configValue;

    /** 排序号 */
    private Integer sortOrder;

    /** 状态: 1启用 0禁用 */
    private Integer status;
}
