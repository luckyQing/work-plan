package io.github.luckyqing.controller;

import io.github.luckyqing.common.R;
import io.github.luckyqing.entity.WorkLog;
import io.github.luckyqing.service.WorkLogService;
import io.github.luckyqing.vo.worklog.DashboardTaskVO;
import io.github.luckyqing.vo.worklog.WorkLogRespVO;
import io.github.luckyqing.vo.worklog.WorkLogSaveReqVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

/**
 * 工时录入接口
 */
@RestController
@RequestMapping("/api/worklog")
public class WorkLogController {

    @Autowired
    private WorkLogService workLogService;

    @Autowired
    private io.github.luckyqing.service.PermissionService permissionService;

    /**
     * 获取工作台任务卡片（今日任务）
     */
    @GetMapping("/dashboard")
    public R<List<DashboardTaskVO>> dashboard(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        String today = LocalDate.now().toString();
        return R.ok(workLogService.getDashboardTasks(userId, today));
    }

    /**
     * 查询今日工时记录
     */
    @GetMapping("/today")
    public R<List<WorkLogRespVO>> today(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        String today = LocalDate.now().toString();
        return R.ok(workLogService.listByUserAndDate(userId, today));
    }

    /**
     * 录入工时
     */
    @PostMapping
    public R<Void> add(@Valid @RequestBody WorkLogSaveReqVO reqVO, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        workLogService.addLog(reqVO, userId);
        return R.ok();
    }

    /**
     * 删除工时记录
     */
    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        // 非管理员只能删除自己的工时记录
        if (!permissionService.getUserRoles(userId).contains("ADMIN")) {
            WorkLog log = workLogService.getById(id);
            if (log != null && !log.getUserId().equals(userId)) {
                return R.fail("无权删除他人的工时记录");
            }
        }
        workLogService.removeById(id);
        return R.ok();
    }
}
