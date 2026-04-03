package io.github.luckyqing.controller;

import io.github.luckyqing.common.R;
import io.github.luckyqing.vo.task.TaskListReqVO;
import io.github.luckyqing.vo.task.TaskRespVO;
import io.github.luckyqing.vo.task.TaskSaveReqVO;
import io.github.luckyqing.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 任务管理接口
 * 提供个人任务的录入、查看、修改、删除功能
 */
@RestController
@RequestMapping("/api/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    /**
     * 查询任务列表
     * 可选按负责人和日期范围筛选
     *
     * @param reqVO 查询参数
     * @return 任务列表
     */
    @GetMapping("/list")
    public R<List<TaskRespVO>> list(TaskListReqVO reqVO) {
        return R.ok(taskService.listTasks(reqVO));
    }

    /**
     * 根据ID查询任务详情
     *
     * @param id 任务ID
     * @return 任务信息
     */
    @GetMapping("/{id}")
    public R<TaskRespVO> getById(@PathVariable Long id) {
        return R.ok(taskService.getTaskById(id));
    }

    /**
     * 新增任务
     * 如果未指定负责人，默认为当前登录用户
     *
     * @param reqVO   任务信息
     * @param request HTTP请求（获取当前登录用户ID）
     * @return 操作结果
     */
    @PostMapping
    public R<Void> add(@RequestBody TaskSaveReqVO reqVO, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        taskService.addTask(reqVO, userId);
        return R.ok();
    }

    /**
     * 更新任务信息
     *
     * @param reqVO 任务信息
     * @return 操作结果
     */
    @PutMapping
    public R<Void> update(@RequestBody TaskSaveReqVO reqVO) {
        taskService.updateTask(reqVO);
        return R.ok();
    }

    /**
     * 删除任务（逻辑删除）
     *
     * @param id 任务ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        taskService.removeById(id);
        return R.ok();
    }
}
