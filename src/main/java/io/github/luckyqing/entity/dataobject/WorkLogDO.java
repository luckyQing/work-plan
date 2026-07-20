package io.github.luckyqing.entity.dataobject;

import io.github.luckyqing.entity.WorkLogEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 工时记录关联查询数据对象
 * 包含关联表字段（任务名称、任务类型）
 *
 * @author collin.li
 * @since 2026-07-20
 */
@Getter
@Setter
public class WorkLogDO extends WorkLogEntity {

    /** 任务名称 */
    private String taskName;

    /** 任务类型 */
    private String taskType;
}
