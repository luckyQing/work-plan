package io.github.luckyqing.controller;

import io.github.luckyqing.common.R;
import io.github.luckyqing.vo.schedule.SchedulePersonRespVO;
import io.github.luckyqing.vo.schedule.ScheduleReqVO;
import io.github.luckyqing.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 排期视图接口
 * 提供周排期数据查询
 */
@RestController
@RequestMapping("/api/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    /**
     * 获取周排期视图数据
     * 支持按部门、项目、关键词筛选，以及"只看自己"模式
     *
     * @param reqVO   查询参数
     * @param request HTTP请求（获取当前登录用户ID）
     * @return 人员排期列表
     */
    @GetMapping("/week")
    public R<List<SchedulePersonRespVO>> weekView(ScheduleReqVO reqVO, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return R.ok(scheduleService.getWeekSchedule(reqVO, userId));
    }
}
