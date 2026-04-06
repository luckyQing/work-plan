package io.github.luckyqing.vo.schedule;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 周排期视图 - 主任务（需求级别或独立任务）响应数据
 */
@Data
public class ScheduleTaskRespVO {

    /** 任务ID（独立任务时有值，需求级别时为null） */
    private Long taskId;

    /** 需求ID（需求级别时有值） */
    private Long demandId;

    /** 任务/需求名称 */
    private String taskName;

    /** 任务类型（需求/优化/Bug等） */
    private String taskType;

    /** 开始日期 yyyy-MM-dd */
    private String startDate;

    /** 结束日期 yyyy-MM-dd */
    private String endDate;

    /** 总工时 */
    private BigDecimal totalHours;

    /** 子任务列表（需求下的具体任务） */
    private List<ScheduleSubTaskRespVO> subTasks;
}
