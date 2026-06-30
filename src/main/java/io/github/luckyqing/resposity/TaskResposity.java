package io.github.luckyqing.resposity;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.luckyqing.entity.TaskEntity;
import io.github.luckyqing.mapper.TaskMapper;
import io.github.luckyqing.vo.task.TaskListReqVO;
import io.github.luckyqing.vo.task.TaskRespVO;
import io.github.luckyqing.vo.task.TaskSaveReqVO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 任务管理服务
 * 提供任务的增删改查及按日期范围查询功能
 */
@Service
public class TaskResposity extends ServiceImpl<TaskMapper, TaskEntity> {

    /**
     * 查询任务列表
     * 可选按负责人和日期范围筛选
     *
     * @param reqVO 查询参数
     * @return 任务RespVO列表
     */
    public List<TaskRespVO> listTasks(TaskListReqVO reqVO) {
        List<TaskEntity> tasks;
        if (reqVO.getAssigneeId() != null && reqVO.getStartDate() != null && reqVO.getEndDate() != null) {
            tasks = baseMapper.selectByAssigneeAndDateRange(
                    reqVO.getAssigneeId(), reqVO.getStartDate(), reqVO.getEndDate());
        } else {
            tasks = list();
        }
        return tasks.stream().map(this::toRespVO).collect(Collectors.toList());
    }

    /**
     * 根据ID查询任务详情
     *
     * @param id 任务ID
     * @return 任务RespVO
     */
    public TaskRespVO getTaskById(Long id) {
        TaskEntity task = getById(id);
        return task != null ? toRespVO(task) : null;
    }

    /**
     * 根据日期范围查询所有任务（内部使用，供排期服务调用）
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 任务Entity列表
     */
    public List<TaskEntity> getByDateRange(String startDate, String endDate) {
        return baseMapper.selectByDateRange(startDate, endDate);
    }

    /**
     * 新增任务
     * 如果未指定负责人，默认为当前登录用户
     *
     * @param reqVO       任务信息
     * @param loginUserId 当前登录用户ID
     */
    public void addTask(TaskSaveReqVO reqVO, Long loginUserId) {
        TaskEntity task = toEntity(reqVO);
        // 未指定负责人时，默认为当前登录用户
        if (task.getAssigneeId() == null) {
            task.setAssigneeId(loginUserId);
        }
        save(task);
    }

    /**
     * 更新任务信息
     *
     * @param reqVO 任务信息
     */
    public void updateTask(TaskSaveReqVO reqVO) {
        TaskEntity task = toEntity(reqVO);
        updateById(task);
    }

    /**
     * 将任务标记为已完成（status=2）
     *
     * @param taskId 任务ID
     */
    public void completeTask(Long taskId) {
        TaskEntity task = new TaskEntity();
        task.setId(taskId);
        task.setStatus(2);
        updateById(task);
    }

    /**
     * Entity 转 RespVO
     */
    private TaskRespVO toRespVO(TaskEntity task) {
        TaskRespVO vo = new TaskRespVO();
        vo.setId(task.getId());
        vo.setTaskName(task.getTaskName());
        vo.setTaskType(task.getTaskType());
        vo.setDemandId(task.getDemandId());
        vo.setDemandName(task.getDemandName());
        vo.setAssigneeId(task.getAssigneeId());
        vo.setAssigneeName(task.getAssigneeName());
        vo.setStartDate(task.getStartDate());
        vo.setEndDate(task.getEndDate());
        vo.setTotalHours(task.getTotalHours());
        vo.setStatus(task.getStatus());
        vo.setDescription(task.getDescription());
        vo.setCreateTime(task.getCreateTime());
        return vo;
    }

    /**
     * ReqVO 转 Entity
     */
    private TaskEntity toEntity(TaskSaveReqVO reqVO) {
        TaskEntity task = new TaskEntity();
        task.setId(reqVO.getId());
        task.setTaskName(reqVO.getTaskName());
        task.setTaskType(reqVO.getTaskType());
        task.setDemandId(reqVO.getDemandId());
        task.setAssigneeId(reqVO.getAssigneeId());
        if (reqVO.getStartDate() != null) {
            task.setStartDate(LocalDate.parse(reqVO.getStartDate()));
        }
        if (reqVO.getEndDate() != null) {
            task.setEndDate(LocalDate.parse(reqVO.getEndDate()));
        }
        task.setTotalHours(reqVO.getTotalHours());
        task.setStatus(reqVO.getStatus());
        task.setDescription(reqVO.getDescription());
        return task;
    }
}
