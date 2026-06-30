package io.github.luckyqing.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 字典配置实体
 */
@Data
@TableName("t_dict")
public class DictEntity {

    /** 主键ID */
    @TableId(type = IdType.AUTO)
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

    /** 创建人ID */
    @TableField(fill = FieldFill.INSERT)
    private Long createId;

    /** 修改人ID */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateId;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /** 逻辑删除标识 */
    @TableLogic
    private Integer deleted;
}
