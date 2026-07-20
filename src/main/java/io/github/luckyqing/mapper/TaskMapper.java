package io.github.luckyqing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.luckyqing.entity.TaskEntity;
import io.github.luckyqing.entity.dataobject.TaskDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 任务表 Mapper 接口
 * </p>
 *
 * @author collin.li
 * @since 2026-07-20
 */
public interface TaskMapper extends BaseMapper<TaskEntity> {

    /**
     * 根据负责人ID和日期范围查询任务
     *
     * @param assigneeId 负责人ID
     * @param startDate  开始日期 yyyy-MM-dd
     * @param endDate    结束日期 yyyy-MM-dd
     * @return 任务列表（含负责人姓名、需求名称）
     */
    List<TaskDO> selectByAssigneeAndDateRange(@Param("assigneeId") Long assigneeId,
                                            @Param("startDate") String startDate,
                                            @Param("endDate") String endDate);

    /**
     * 根据日期范围查询所有任务
     *
     * @param startDate 开始日期 yyyy-MM-dd
     * @param endDate   结束日期 yyyy-MM-dd
     * @return 任务列表（含负责人姓名、需求名称）
     */
    List<TaskDO> selectByDateRange(@Param("startDate") String startDate,
                                 @Param("endDate") String endDate);
}
