package io.github.luckyqing.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * <p>
 * 工时录入表
 * </p>
 *
 * @author collin.li
 * @since 2026-07-20
 */
@Getter
@Setter
@TableName("t_work_log")
public class WorkLogEntity extends BaseEntity {

    /**
     * 关联任务ID
     */
    @TableField("task_id")
    private Long taskId;

    /**
     * 录入人ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 工作日期
     */
    @TableField("log_date")
    private LocalDate logDate;

    /**
     * 录入工时
     */
    @TableField("hours")
    private BigDecimal hours;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

}
