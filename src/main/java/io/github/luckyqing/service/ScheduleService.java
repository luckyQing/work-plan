package io.github.luckyqing.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.github.luckyqing.entity.Demand;
import io.github.luckyqing.entity.Project;
import io.github.luckyqing.entity.User;
import io.github.luckyqing.entity.Task;
import io.github.luckyqing.vo.schedule.SchedulePersonRespVO;
import io.github.luckyqing.vo.schedule.ScheduleReqVO;
import io.github.luckyqing.vo.schedule.ScheduleSubTaskRespVO;
import io.github.luckyqing.vo.schedule.ScheduleTaskRespVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 排期服务
 * 组装周视图所需的人员-任务数据结构
 */
@Service
public class ScheduleService {

    @Autowired
    private UserService userService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private DemandService demandService;

    @Autowired
    private ProjectService projectService;

    /**
     * 获取周排期视图数据
     * 按人员维度组装任务，需求下的任务作为子任务展示，独立任务单独展示
     *
     * @param reqVO      查询参数（日期范围、部门、项目、关键词等）
     * @param selfUserId 当前登录用户ID（用于"只看自己"筛选）
     * @return 人员排期RespVO列表
     */
    public List<SchedulePersonRespVO> getWeekSchedule(ScheduleReqVO reqVO, Long selfUserId) {
        // 1. 确定人员范围
        List<User> users = resolveUsers(reqVO, selfUserId);

        // 2. 查询日期范围内的所有任务
        List<Task> allTasks = taskService.getByDateRange(reqVO.getStartDate(), reqVO.getEndDate());

        // 3. 如果有项目筛选，获取该项目下的需求ID集合
        Set<Long> demandIdsInProject = resolveDemandIdsByProject(reqVO.getProjectId());

        // 4. 按人员组装排期数据
        List<SchedulePersonRespVO> result = new ArrayList<>();
        for (User user : users) {
            List<Task> userTasks = filterUserTasks(allTasks, user, demandIdsInProject, reqVO.getKeyword());

            // 有关键词筛选时，跳过无任务的人员
            if (userTasks.isEmpty() && reqVO.getKeyword() != null && !reqVO.getKeyword().isEmpty()) {
                continue;
            }

            SchedulePersonRespVO personVO = buildPersonRespVO(user, userTasks, reqVO);
            result.add(personVO);
        }
        return result;
    }

    /**
     * 根据查询条件确定人员范围
     */
    private List<User> resolveUsers(ScheduleReqVO reqVO, Long selfUserId) {
        if (reqVO.isOnlySelf() && selfUserId != null) {
            User self = userService.getById(selfUserId);
            return self != null ? Collections.singletonList(self) : Collections.emptyList();
        } else if (reqVO.getDept() != null && !reqVO.getDept().isEmpty()) {
            return userService.listByDept(reqVO.getDept());
        } else {
            return userService.list();
        }
    }

    /**
     * 根据项目ID获取该项目下的需求ID集合
     *
     * @return 需求ID集合，无项目筛选时返回 null
     */
    private Set<Long> resolveDemandIdsByProject(Long projectId) {
        if (projectId == null) {
            return null;
        }
        List<Demand> demands = demandService.list(
                new LambdaQueryWrapper<Demand>().eq(Demand::getProjectId, projectId));
        return demands.stream().map(Demand::getId).collect(Collectors.toSet());
    }

    /**
     * 筛选某个用户在当前条件下的任务
     */
    private List<Task> filterUserTasks(List<Task> allTasks, User user,
                                       Set<Long> demandIdsInProject, String keyword) {
        return allTasks.stream()
                .filter(t -> t.getAssigneeId().equals(user.getId()))
                .filter(t -> {
                    if (demandIdsInProject != null && t.getDemandId() != null) {
                        return demandIdsInProject.contains(t.getDemandId());
                    }
                    return demandIdsInProject == null;
                })
                .filter(t -> {
                    if (keyword != null && !keyword.isEmpty()) {
                        return t.getTaskName().contains(keyword) || user.getRealName().contains(keyword);
                    }
                    return true;
                })
                .collect(Collectors.toList());
    }

    /**
     * 组装单个人员的排期RespVO
     * 将任务按需求分组：有需求的任务归入需求主任务下作为子任务，无需求的作为独立任务
     */
    private SchedulePersonRespVO buildPersonRespVO(User user, List<Task> userTasks, ScheduleReqVO reqVO) {
        // 按需求ID分组
        Map<Long, List<Task>> tasksByDemand = new LinkedHashMap<>();
        List<Task> independentTasks = new ArrayList<>();
        for (Task t : userTasks) {
            if (t.getDemandId() != null) {
                tasksByDemand.computeIfAbsent(t.getDemandId(), k -> new ArrayList<>()).add(t);
            } else {
                independentTasks.add(t);
            }
        }

        List<ScheduleTaskRespVO> taskVOList = new ArrayList<>();

        // 需求级别的主任务（含子任务）
        for (Map.Entry<Long, List<Task>> entry : tasksByDemand.entrySet()) {
            Demand demand = demandService.getById(entry.getKey());
            if (demand == null) {
                continue;
            }
            taskVOList.add(buildDemandTaskRespVO(demand, entry.getValue(), reqVO));
        }

        // 独立任务（无关联需求）
        for (Task t : independentTasks) {
            taskVOList.add(buildIndependentTaskRespVO(t));
        }

        SchedulePersonRespVO personVO = new SchedulePersonRespVO();
        personVO.setUserId(user.getId());
        personVO.setName(user.getRealName());
        personVO.setTasks(taskVOList);
        return personVO;
    }

    /**
     * 构建需求级别的主任务RespVO
     * 日期范围取子任务的最小开始日期和最大结束日期，工时取子任务之和
     */
    private ScheduleTaskRespVO buildDemandTaskRespVO(Demand demand, List<Task> subTasks, ScheduleReqVO reqVO) {
        String minStart = subTasks.stream().map(t -> t.getStartDate().toString())
                .min(String::compareTo).orElse(reqVO.getStartDate());
        String maxEnd = subTasks.stream().map(t -> t.getEndDate().toString())
                .max(String::compareTo).orElse(reqVO.getEndDate());
        BigDecimal totalH = subTasks.stream().map(Task::getTotalHours).reduce(BigDecimal.ZERO, BigDecimal::add);

        ScheduleTaskRespVO taskVO = new ScheduleTaskRespVO();
        taskVO.setDemandId(demand.getId());
        taskVO.setTaskName(demand.getDemandName());
        taskVO.setTaskType(demand.getDemandType());
        // 设置项目名称
        if (demand.getProjectId() != null) {
            Project project = projectService.getById(demand.getProjectId());
            if (project != null) taskVO.setProjectName(project.getProjectName());
        }
        taskVO.setStartDate(minStart);
        taskVO.setEndDate(maxEnd);
        taskVO.setTotalHours(totalH);

        List<ScheduleSubTaskRespVO> subList = new ArrayList<>();
        for (Task st : subTasks) {
            ScheduleSubTaskRespVO subVO = new ScheduleSubTaskRespVO();
            subVO.setTaskId(st.getId());
            subVO.setName(st.getTaskName());
            subVO.setType(st.getTaskType());
            subVO.setStartDate(st.getStartDate().toString());
            subVO.setEndDate(st.getEndDate().toString());
            subVO.setTotalHours(st.getTotalHours());
            subList.add(subVO);
        }
        taskVO.setSubTasks(subList);
        return taskVO;
    }

    /**
     * 构建独立任务RespVO（无关联需求的任务）
     */
    private ScheduleTaskRespVO buildIndependentTaskRespVO(Task task) {
        ScheduleTaskRespVO taskVO = new ScheduleTaskRespVO();
        taskVO.setTaskId(task.getId());
        taskVO.setTaskName(task.getTaskName());
        taskVO.setTaskType(task.getTaskType());
        taskVO.setStartDate(task.getStartDate().toString());
        taskVO.setEndDate(task.getEndDate().toString());
        taskVO.setTotalHours(task.getTotalHours());
        taskVO.setSubTasks(Collections.emptyList());
        return taskVO;
    }
}
