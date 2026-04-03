package io.github.luckyqing.vo.task;

import lombok.Data;

/**
 * 任务列表查询请求参数
 */
@Data
public class TaskListReqVO {

    /** 负责人ID（可选） */
    private Long assigneeId;

    /** 开始日期 yyyy-MM-dd（可选） */
    private String startDate;

    /** 结束日期 yyyy-MM-dd（可选） */
    private String endDate;
}
