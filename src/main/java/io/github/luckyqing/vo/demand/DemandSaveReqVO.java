package io.github.luckyqing.vo.demand;

import lombok.Data;
import javax.validation.constraints.NotBlank;
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

    private Long projectId;

    private String description;

    private Integer status;

    private Integer priority;

    private String startDate;

    private String endDate;

    private BigDecimal totalHours;
}
