package io.github.luckyqing.vo.demand;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 需求信息响应数据
 */
@Data
public class DemandRespVO {

    /** 需求ID */
    private Long id;

    /** 需求名称 */
    private String demandName;

    /** 需求类型 */
    private String demandType;

    /** 所属项目ID */
    private Long projectId;

    /** 项目名称 */
    private String projectName;

    /** 需求描述 */
    private String description;

    /** 状态: 0待开始 1进行中 2已完成 */
    private Integer status;

    /** 优先级: 0低 1中 2高 */
    private Integer priority;

    /** 创建人ID */
    private Long creatorId;

    /** 创建人姓名 */
    private String creatorName;

    /** 计划开始日期 */
    private LocalDate startDate;

    /** 计划结束日期 */
    private LocalDate endDate;

    /** 预估总工时 */
    private BigDecimal totalHours;

    /** 产品人员（逗号分隔的用户ID） */
    private String productMembers;

    /** 测试人员（逗号分隔的用户ID） */
    private String testMembers;

    /** 研发人员（逗号分隔的用户ID） */
    private String devMembers;

    /** 创建时间 */
    private LocalDateTime createTime;
}
