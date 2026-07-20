package io.github.luckyqing.convert;

import io.github.luckyqing.entity.DemandEntity;
import io.github.luckyqing.entity.TaskEntity;
import io.github.luckyqing.entity.dataobject.DemandDO;
import io.github.luckyqing.entity.dataobject.TaskDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 实体对象转换器
 *
 * @author collin.li
 * @since 2026-07-20
 */
@Mapper
public interface EntityConvert {

    EntityConvert INSTANCE = Mappers.getMapper(EntityConvert.class);

    TaskDO toTaskDO(TaskEntity entity);

    List<TaskDO> toTaskDOList(List<TaskEntity> entities);

    DemandDO toDemandDO(DemandEntity entity);

    List<DemandDO> toDemandDOList(List<DemandEntity> entities);
}
