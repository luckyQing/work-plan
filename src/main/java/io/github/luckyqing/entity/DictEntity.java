package io.github.luckyqing.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 字典配置表
 * </p>
 *
 * @author collin.li
 * @since 2026-07-20
 */
@Getter
@Setter
@TableName("t_dict")
public class DictEntity extends BaseEntity {

    /**
     * 配置类别
     */
    @TableField("config_type")
    private String configType;

    /**
     * 显示名称
     */
    @TableField("config_label")
    private String configLabel;

    /**
     * 配置值
     */
    @TableField("config_value")
    private String configValue;

    /**
     * 排序号
     */
    @TableField("sort_order")
    private Integer sortOrder;

    /**
     * 状态: 1启用 0禁用
     */
    @TableField("status")
    private Integer status;

}
