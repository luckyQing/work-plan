package io.github.luckyqing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.luckyqing.entity.WorkLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 工时录入 Mapper
 */
public interface WorkLogMapper extends BaseMapper<WorkLog> {

    /**
     * 查询指定用户指定日期的工时记录（含任务名称）
     */
    List<WorkLog> selectByUserAndDate(@Param("userId") Long userId, @Param("logDate") String logDate);
}
