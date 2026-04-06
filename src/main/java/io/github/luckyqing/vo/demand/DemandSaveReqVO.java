package io.github.luckyqing.vo.demand;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 需求新增/修改请求参数
 */
@Data
public class DemandSaveReqVO {

    private Long id;

    @NotBlank(message = "需求名称不能为空")
    private String demandName;

    @NotBlank(message = "需求类型不能为空")
    private String demandType;

    @NotNull(message = "所属项目不能为空")
    private Long projectId;

    private String description;

    @NotNull(message = "状态不能为空")
    private Integer status;

    @NotNull(message = "优先级不能为空")
    private Integer priority;

    @NotBlank(message = "计划开始日期不能为空")
    private String startDate;

    @NotBlank(message = "计划结束日期不能为空")
    private String endDate;

    @NotNull(message = "预估工时不能为空")
    private BigDecimal totalHours;

    /** 产品人员（逗号分隔的用户ID） */
    private String productMembers;

    /** 测试人员（逗号分隔的用户ID） */
    private String testMembers;

    /** 研发人员（逗号分隔的用户ID） */
    private String devMembers;
}
