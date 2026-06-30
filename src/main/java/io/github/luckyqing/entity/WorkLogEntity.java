package io.github.luckyqing.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 工时录入实体
 */
@Data
@TableName("t_work_log")
public class WorkLogEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 关联任务ID */
    private Long taskId;

    /** 录入人ID */
    private Long userId;

    /** 工作日期 */
    private LocalDate logDate;

    /** 录入工时 */
    private BigDecimal hours;

    /** 备注 */
    private String remark;

    @TableField(fill = FieldFill.INSERT)
    private Long createId;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;

    /** 任务名称（关联查询） */
    @TableField(exist = false)
    private String taskName;

    /** 任务类型（关联查询） */
    @TableField(exist = false)
    private String taskType;
}
