package io.github.luckyqing.vo.demand;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 需求新增/修改请求参数
 */
@Data
public class DemandSaveReqVO {

    /** 需求ID（修改时必传） */
    private Long id;

    /** 需求名称 */
    private String demandName;

    /** 需求类型: 需求/优化/Bug */
    private String demandType;

    /** 所属项目ID */
    private Long projectId;

    /** 需求描述 */
    private String description;

    /** 状态: 0待开始 1进行中 2已完成 */
    private Integer status;

    /** 优先级: 0低 1中 2高 */
    private Integer priority;

    /** 计划开始日期 yyyy-MM-dd */
    private String startDate;

    /** 计划结束日期 yyyy-MM-dd */
    private String endDate;

    /** 预估总工时 */
    private BigDecimal totalHours;
}
