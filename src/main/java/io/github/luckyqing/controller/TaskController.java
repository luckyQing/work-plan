package io.github.luckyqing.controller;

import io.github.luckyqing.common.R;
import io.github.luckyqing.entity.Task;
import io.github.luckyqing.service.PermissionService;
import io.github.luckyqing.vo.task.TaskListReqVO;
import io.github.luckyqing.vo.task.TaskRespVO;
import io.github.luckyqing.vo.task.TaskSaveReqVO;
import io.github.luckyqing.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * 任务管理接口
 */
@RestController
@RequestMapping("/api/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private PermissionService permissionService;

    @GetMapping("/list")
    public R<List<TaskRespVO>> list(TaskListReqVO reqVO) {
        return R.ok(taskService.listTasks(reqVO));
    }

    @GetMapping("/{id}")
    public R<TaskRespVO> getById(@PathVariable Long id) {
        return R.ok(taskService.getTaskById(id));
    }

    @PostMapping
    public R<Void> add(@Valid @RequestBody TaskSaveReqVO reqVO, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        taskService.addTask(reqVO, userId);
        return R.ok();
    }

    @PutMapping
    public R<Void> update(@Valid @RequestBody TaskSaveReqVO reqVO, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        // 非管理员只能编辑自己的任务
        R<Void> check = checkOwnership(reqVO.getId(), userId);
        if (check != null) {
            return check;
        }
        taskService.updateTask(reqVO);
        return R.ok();
    }

    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        // 非管理员只能删除自己的任务
        R<Void> check = checkOwnership(id, userId);
        if (check != null) {
            return check;
        }
        taskService.removeById(id);
        return R.ok();
    }

    /**
     * 校验任务归属：非管理员只能操作自己的任务
     *
     * @return null 表示通过，否则返回错误
     */
    private R<Void> checkOwnership(Long taskId, Long userId) {
        if (taskId == null) {
            return null;
        }
        // 管理员不限制
        if (permissionService.getUserRoles(userId).contains("ADMIN")) {
            return null;
        }
        Task task = taskService.getById(taskId);
        if (task == null) {
            return R.fail("任务不存在");
        }
        if (!task.getAssigneeId().equals(userId)) {
            return R.fail("无权操作他人的任务");
        }
        return null;
    }
}
