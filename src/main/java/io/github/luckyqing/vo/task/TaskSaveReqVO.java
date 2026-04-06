package io.github.luckyqing.vo.task;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 任务新增/修改请求参数
 */
@Data
public class TaskSaveReqVO {

    private Long id;

    @NotBlank(message = "任务名称不能为空")
    private String taskName;

    @NotBlank(message = "任务类型不能为空")
    private String taskType;

    private Long demandId;

    private Long assigneeId;

    @NotBlank(message = "开始日期不能为空")
    private String startDate;

    @NotBlank(message = "结束日期不能为空")
    private String endDate;

    private BigDecimal totalHours;

    private Integer status;

    private String description;
}
