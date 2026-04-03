package io.github.luckyqing.vo.task;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 任务新增/修改请求参数
 */
@Data
public class TaskSaveReqVO {

    /** 任务ID（修改时必传） */
    private Long id;

    /** 任务名称 */
    private String taskName;

    /** 任务类型: 开发/测试/设计/其他 */
    private String taskType;

    /** 所属需求ID（为空表示独立任务） */
    private Long demandId;

    /** 负责人ID（为空则默认当前登录用户） */
    private Long assigneeId;

    /** 开始日期 yyyy-MM-dd */
    private String startDate;

    /** 结束日期 yyyy-MM-dd */
    private String endDate;

    /** 预估工时 */
    private BigDecimal totalHours;

    /** 状态: 0待开始 1进行中 2已完成 */
    private Integer status;

    /** 任务描述 */
    private String description;
}
