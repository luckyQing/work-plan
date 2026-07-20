package io.github.luckyqing.mapper;

import io.github.luckyqing.entity.DemandEntity;
import io.github.luckyqing.entity.dataobject.DemandDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 需求表 Mapper 接口
 * </p>
 *
 * @author collin.li
 * @since 2026-07-20
 */
public interface DemandMapper extends BaseMapper<DemandEntity> {

    /**
     * 查询所有需求（关联项目名称、创建人姓名）
     *
     * @return 需求列表（含关联信息）
     */
    List<DemandDO> selectAllWithDetail();
}
