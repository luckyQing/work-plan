package io.github.luckyqing.controller;

import io.github.luckyqing.common.R;
import io.github.luckyqing.common.RoleConstants;
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
        // 非管理员不允许修改负责人（防止将任务转给他人）
        if (!permissionService.getUserRoles(userId).contains(RoleConstants.ADMIN)) {
            reqVO.setAssigneeId(userId);
        }
        // 状态只能通过「完成」按钮操作，编辑接口不允许修改
        reqVO.setStatus(null);
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
     * 标记任务为已完成
     * 非管理员只能完成自己的任务
     */
    @PutMapping("/{id}/complete")
    public R<Void> complete(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        R<Void> check = checkOwnership(id, userId);
        if (check != null) {
            return check;
        }
        taskService.completeTask(id);
        return R.ok();
    }

    /**
     * 校验任务归属：非管理员只能操作自己的任务
     *
     * @return null 表示通过，否则返回错误
     */
    private R<Void> checkOwnership(Long taskId, Long userId) {
        if (taskId == null) {
            return R.fail("任务ID不能为空");
        }
        // 管理员不限制
        if (permissionService.getUserRoles(userId).contains(RoleConstants.ADMIN)) {
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
