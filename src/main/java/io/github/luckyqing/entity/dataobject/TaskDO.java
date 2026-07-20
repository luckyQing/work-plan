package io.github.luckyqing.entity.dataobject;

import io.github.luckyqing.entity.TaskEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 任务关联查询数据对象
 * 包含关联表字段（负责人姓名、需求名称）
 *
 * @author collin.li
 * @since 2026-07-20
 */
@Getter
@Setter
public class TaskDO extends TaskEntity {

    /** 负责人姓名 */
    private String assigneeName;

    /** 需求名称 */
    private String demandName;
}
