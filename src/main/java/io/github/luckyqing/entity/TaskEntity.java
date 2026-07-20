package io.github.luckyqing.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * <p>
 * 任务表
 * </p>
 *
 * @author collin.li
 * @since 2026-07-20
 */
@Getter
@Setter
@TableName("t_task")
public class TaskEntity extends BaseEntity {

    /**
     * 任务名称
     */
    @TableField("task_name")
    private String taskName;

    /**
     * 任务类型（字典值）
     */
    @TableField("task_type")
    private String taskType;

    /**
     * 所属需求ID
     */
    @TableField("demand_id")
    private Long demandId;

    /**
     * 负责人ID
     */
    @TableField("assignee_id")
    private Long assigneeId;

    /**
     * 开始日期
     */
    @TableField("start_date")
    private LocalDate startDate;

    /**
     * 结束日期
     */
    @TableField("end_date")
    private LocalDate endDate;

    /**
     * 预估工时
     */
    @TableField("total_hours")
    private BigDecimal totalHours;

    /**
     * 状态
     */
    @TableField("status")
    private Integer status;

    /**
     * 任务描述
     */
    @TableField("description")
    private String description;

}
