package io.github.luckyqing.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 需求实体
 */
@Data
@TableName("demand")
public class Demand {

    /** 主键ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 需求名称 */
    private String demandName;

    /** 需求类型: 需求/优化/Bug */
    private String demandType;

    /** 所属项目ID */
    private Long projectId;

    /** 需求描述 */
    private String description;

    /** 状态: 0待开始 1进行中 2已完成 */
    private Integer status;

    /** 优先级: 0低 1中 2高 */
    private Integer priority;

    /** 创建人ID */
    private Long creatorId;

    /** 计划开始日期 */
    private LocalDate startDate;

    /** 计划结束日期 */
    private LocalDate endDate;

    /** 预估总工时 */
    private BigDecimal totalHours;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /** 逻辑删除标识 */
    @TableLogic
    private Integer deleted;

    /** 项目名称（非数据库字段，关联查询用） */
    @TableField(exist = false)
    private String projectName;

    /** 创建人姓名（非数据库字段，关联查询用） */
    @TableField(exist = false)
    private String creatorName;
}
