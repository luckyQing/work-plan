package io.github.luckyqing.vo.schedule;

import lombok.Data;

/**
 * 周排期查询请求参数
 */
@Data
public class ScheduleReqVO {

    /** 周开始日期 yyyy-MM-dd */
    private String startDate;

    /** 周结束日期 yyyy-MM-dd */
    private String endDate;

    /** 部门ID（可选） */
    private Long deptId;

    /** 项目ID（可选） */
    private Long projectId;

    /** 是否只看自己 */
    private boolean onlySelf;

    /** 搜索关键词（可选） */
    private String keyword;
}
