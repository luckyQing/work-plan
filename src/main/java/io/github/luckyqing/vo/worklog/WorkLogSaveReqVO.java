package io.github.luckyqing.vo.worklog;

import lombok.Data;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 工时录入请求参数
 */
@Data
public class WorkLogSaveReqVO {

    /** 关联任务ID */
    @NotNull(message = "任务不能为空")
    private Long taskId;

    /** 工作日期 yyyy-MM-dd */
    @NotNull(message = "工作日期不能为空")
    private String logDate;

    /** 录入工时 */
    @NotNull(message = "工时不能为空")
    private BigDecimal hours;

    /** 备注 */
    private String remark;
}
