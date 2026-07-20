package io.github.luckyqing.entity.dataobject;

import io.github.luckyqing.entity.DemandEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 需求关联查询数据对象
 * 包含关联表字段（项目名称、创建人姓名）
 *
 * @author collin.li
 * @since 2026-07-20
 */
@Getter
@Setter
public class DemandDO extends DemandEntity {

    /** 项目名称 */
    private String projectName;

    /** 创建人姓名 */
    private String creatorName;
}
