package io.github.luckyqing.controller;

import io.github.luckyqing.common.R;
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

    /**
     * 更新任务信息
     *
     * @param reqVO 任务信息
     * @return 操作结果
     */
    @PutMapping
    public R<Void> update(@Valid @RequestBody TaskSaveReqVO reqVO) {
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
