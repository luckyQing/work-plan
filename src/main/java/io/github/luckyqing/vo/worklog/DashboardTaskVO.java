package io.github.luckyqing.vo.worklog;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 工作台任务卡片数据
 */
@Data
public class DashboardTaskVO {

    /** 任务ID */
    private Long taskId;

    /** 任务名称 */
    private String taskName;

    /** 任务类型 */
    private String taskType;

    /** 需求名称 */
    private String demandName;

    /** 项目名称 */
    private String projectName;

    /** 开始日期 */
    private LocalDate startDate;

    /** 结束日期 */
    private LocalDate endDate;

    /** 计划工时 */
    private BigDecimal totalHours;

    /** 今日已录入工时 */
    private BigDecimal todayLoggedHours;

    /** 累计已录入工时 */
    private BigDecimal totalLoggedHours;

    /** 任务状态: 0待开始 1进行中 2已完成 */
    private Integer status;

    /** 完成进度百分比（0-100） */
    private Integer progressPercent;

    /** 是否超期未完成 */
    private Boolean overdue;
}
