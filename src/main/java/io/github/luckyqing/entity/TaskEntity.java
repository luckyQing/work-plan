package io.github.luckyqing.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 任务实体（个人任务/子任务）
 */
@Data
@TableName("t_task")
public class TaskEntity {

    /** 主键ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 任务名称 */
    private String taskName;

    /** 任务类型: 开发/测试/设计/其他 */
    private String taskType;

    /** 所属需求ID（为空表示独立任务） */
    private Long demandId;

    /** 负责人ID */
    private Long assigneeId;

    /** 开始日期 */
    private LocalDate startDate;

    /** 结束日期 */
    private LocalDate endDate;

    /** 预估工时 */
    private BigDecimal totalHours;

    /** 状态: 0待开始 1进行中 2已完成 */
    private Integer status;

    /** 任务描述 */
    private String description;

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

    /** 负责人姓名（非数据库字段，关联查询用） */
    @TableField(exist = false)
    private String assigneeName;

    /** 需求名称（非数据库字段，关联查询用） */
    @TableField(exist = false)
    private String demandName;
}
