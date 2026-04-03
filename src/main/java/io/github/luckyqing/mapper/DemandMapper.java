package io.github.luckyqing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.luckyqing.entity.Demand;

import java.util.List;

/**
 * 需求 Mapper 接口
 */
public interface DemandMapper extends BaseMapper<Demand> {

    /**
     * 查询所有需求（关联项目名称、创建人姓名）
     *
     * @return 需求列表（含关联信息）
     */
    List<Demand> selectAllWithDetail();
}
