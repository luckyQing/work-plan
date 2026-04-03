package io.github.luckyqing.vo.schedule;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 周排期视图 - 子任务响应数据
 */
@Data
public class ScheduleSubTaskRespVO {

    /** 子任务名称 */
    private String name;

    /** 子任务类型（开发/测试/设计等） */
    private String type;

    /** 开始日期 yyyy-MM-dd */
    private String startDate;

    /** 结束日期 yyyy-MM-dd */
    private String endDate;

    /** 预估工时 */
    private BigDecimal totalHours;
}
