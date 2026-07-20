package io.github.luckyqing.resposity;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.luckyqing.entity.DemandEntity;
import io.github.luckyqing.entity.ProjectEntity;
import io.github.luckyqing.entity.TaskEntity;
import io.github.luckyqing.entity.WorkLogEntity;
import io.github.luckyqing.entity.dataobject.TaskDO;
import io.github.luckyqing.entity.dataobject.WorkLogDO;
import io.github.luckyqing.mapper.WorkLogMapper;
import io.github.luckyqing.vo.worklog.DashboardTaskVO;
import io.github.luckyqing.vo.worklog.WorkLogRespVO;
import io.github.luckyqing.vo.worklog.WorkLogSaveReqVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 工时录入表 服务实现类
 * </p>
 *
 * @author collin.li
 * @since 2026-07-20
 */
@Service
public class WorkLogResposity extends ServiceImpl<WorkLogMapper, WorkLogEntity> {

    @Autowired
    private TaskResposity taskService;

    @Autowired
    private DemandResposity demandService;

    @Autowired
    private ProjectResposity projectService;

    /**
     * 录入工时
     */
    public void addLog(WorkLogSaveReqVO reqVO, Long userId) {
        WorkLogEntity log = new WorkLogEntity();
        log.setTaskId(reqVO.getTaskId());
        log.setUserId(userId);
        log.setLogDate(LocalDate.parse(reqVO.getLogDate()));
        log.setHours(reqVO.getHours());
        log.setRemark(reqVO.getRemark());
        save(log);
    }

    /**
     * 查询指定用户指定日期的工时记录
     */
    public List<WorkLogRespVO> listByUserAndDate(Long userId, String logDate) {
        List<WorkLogDO> logs = baseMapper.selectByUserAndDate(userId, logDate);
        return logs.stream().map(this::toRespVO).collect(Collectors.toList());
    }

    /**
     * 获取工作台数据：今日任务卡片列表
     * 包含今日进行中的任务 + 超期未完成的任务（end_date < today 且 status != 2）
     * 超期任务排在后面，通过 overdue 字段标记
     */
    public List<DashboardTaskVO> getDashboardTasks(Long userId, String today) {
        // 1. 今日进行中的任务（start_date <= today <= end_date）
        List<TaskDO> todayTasks = taskService.getByDateRange(today, today).stream()
                .filter(t -> t.getAssigneeId().equals(userId))
                .collect(Collectors.toList());

        // 2. 超期未完成任务（end_date < today 且 status != 2）
        // 查询 end_date 在 today 之前的任务，再过滤未完成
        List<TaskDO> overdueTasks = taskService.getByDateRange("2000-01-01", today).stream()
                .filter(t -> t.getAssigneeId().equals(userId))
                .filter(t -> t.getEndDate() != null && t.getEndDate().toString().compareTo(today) < 0)
                .filter(t -> t.getStatus() == null || t.getStatus() != 2)
                .collect(Collectors.toList());

        // 合并：今日任务在前，超期任务在后
        List<TaskDO> tasks = new ArrayList<>(todayTasks);
        // 去重（避免今日任务和超期任务重复）
        Set<Long> todayIds = todayTasks.stream()
                .map(TaskDO::getId)
                .collect(Collectors.toSet());
        overdueTasks.stream().filter(t -> !todayIds.contains(t.getId())).forEach(tasks::add);

        if (tasks.isEmpty()) {
            return new ArrayList<>();
        }

        // 查询今日工时记录，按 taskId 分组
        List<WorkLogEntity> todayLogs = list(new LambdaQueryWrapper<WorkLogEntity>()
                .eq(WorkLogEntity::getUserId, userId)
                .eq(WorkLogEntity::getLogDate, LocalDate.parse(today)));
        Map<Long, BigDecimal> todayHoursMap = todayLogs.stream()
                .collect(Collectors.groupingBy(WorkLogEntity::getTaskId,
                        Collectors.reducing(BigDecimal.ZERO, WorkLogEntity::getHours, BigDecimal::add)));

        // 查询累计工时记录，按 taskId 分组
        List<WorkLogEntity> allLogs = list(new LambdaQueryWrapper<WorkLogEntity>()
                .eq(WorkLogEntity::getUserId, userId)
                .in(WorkLogEntity::getTaskId, tasks.stream().map(TaskEntity::getId).collect(Collectors.toList())));
        Map<Long, BigDecimal> totalHoursMap = allLogs.stream()
                .collect(Collectors.groupingBy(WorkLogEntity::getTaskId,
                        Collectors.reducing(BigDecimal.ZERO, WorkLogEntity::getHours, BigDecimal::add)));

        List<DashboardTaskVO> result = new ArrayList<>();
        for (TaskDO task : tasks) {
            DashboardTaskVO vo = new DashboardTaskVO();
            vo.setTaskId(task.getId());
            vo.setTaskName(task.getTaskName());
            vo.setTaskType(task.getTaskType());
            vo.setStartDate(task.getStartDate());
            vo.setEndDate(task.getEndDate());
            vo.setTotalHours(task.getTotalHours());
            vo.setStatus(task.getStatus());

            BigDecimal todayH = todayHoursMap.getOrDefault(task.getId(), BigDecimal.ZERO);
            BigDecimal totalH = totalHoursMap.getOrDefault(task.getId(), BigDecimal.ZERO);
            vo.setTodayLoggedHours(todayH);
            vo.setTotalLoggedHours(totalH);

            // 计算进度百分比
            if (task.getTotalHours() != null && task.getTotalHours().compareTo(BigDecimal.ZERO) > 0) {
                int pct = totalH.multiply(BigDecimal.valueOf(100))
                        .divide(task.getTotalHours(), 0, java.math.RoundingMode.HALF_UP).intValue();
                vo.setProgressPercent(Math.min(pct, 100));
            } else {
                vo.setProgressPercent(task.getStatus() != null && task.getStatus() == 2 ? 100 : 0);
            }

            // 标记超期未完成
            boolean isOverdue = task.getEndDate() != null
                    && task.getEndDate().toString().compareTo(today) < 0
                    && (task.getStatus() == null || task.getStatus() != 2);
            vo.setOverdue(isOverdue);
            if (task.getDemandId() != null) {
                DemandEntity demand = demandService.getById(task.getDemandId());
                if (demand != null) {
                    vo.setDemandName(demand.getDemandName());
                    if (demand.getProjectId() != null) {
                        ProjectEntity project = projectService.getById(demand.getProjectId());
                        if (project != null) {
                            vo.setProjectName(project.getProjectName());
                        }
                    }
                }
            }
            result.add(vo);
        }
        return result;
    }

    private WorkLogRespVO toRespVO(WorkLogDO log) {
        WorkLogRespVO vo = new WorkLogRespVO();
        vo.setId(log.getId());
        vo.setTaskId(log.getTaskId());
        vo.setTaskName(log.getTaskName());
        vo.setTaskType(log.getTaskType());
        vo.setLogDate(log.getLogDate());
        vo.setHours(log.getHours());
        vo.setRemark(log.getRemark());
        vo.setCreateTime(log.getCreateTime());
        return vo;
    }
}