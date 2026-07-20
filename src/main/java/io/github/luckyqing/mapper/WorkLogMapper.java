package io.github.luckyqing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.luckyqing.entity.WorkLogEntity;
import io.github.luckyqing.entity.dataobject.WorkLogDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 工时录入表 Mapper 接口
 * </p>
 *
 * @author collin.li
 * @since 2026-07-20
 */
public interface WorkLogMapper extends BaseMapper<WorkLogEntity> {

    /**
     * 查询指定用户指定日期的工时记录（含任务名称）
     */
    List<WorkLogDO> selectByUserAndDate(@Param("userId") Long userId, @Param("logDate") String logDate);
}
