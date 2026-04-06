package io.github.luckyqing.vo.worklog;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 工时录入响应数据
 */
@Data
public class WorkLogRespVO {

    private Long id;

    /** 关联任务ID */
    private Long taskId;

    /** 任务名称 */
    private String taskName;

    /** 任务类型 */
    private String taskType;

    /** 工作日期 */
    private LocalDate logDate;

    /** 录入工时 */
    private BigDecimal hours;

    /** 备注 */
    private String remark;

    /** 录入时间 */
    private LocalDateTime createTime;
}
