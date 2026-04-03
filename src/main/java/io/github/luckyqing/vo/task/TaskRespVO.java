package io.github.luckyqing.vo.task;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 任务信息响应数据
 */
@Data
public class TaskRespVO {

    /** 任务ID */
    private Long id;

    /** 任务名称 */
    private String taskName;

    /** 任务类型 */
    private String taskType;

    /** 所属需求ID */
    private Long demandId;

    /** 需求名称 */
    private String demandName;

    /** 负责人ID */
    private Long assigneeId;

    /** 负责人姓名 */
    private String assigneeName;

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

    /** 创建时间 */
    private LocalDateTime createTime;
}
