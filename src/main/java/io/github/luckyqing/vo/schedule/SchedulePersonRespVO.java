package io.github.luckyqing.vo.schedule;

import lombok.Data;

import java.util.List;

/**
 * 周排期视图 - 人员维度响应数据
 */
@Data
public class SchedulePersonRespVO {

    /** 用户ID */
    private Long userId;

    /** 人员姓名 */
    private String name;

    /** 该人员在当前周的任务列表 */
    private List<ScheduleTaskRespVO> tasks;
}
