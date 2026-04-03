package io.github.luckyqing.vo.project;

import lombok.Data;

/**
 * 项目新增/修改请求参数
 */
@Data
public class ProjectSaveReqVO {

    /** 项目ID（修改时必传） */
    private Long id;

    /** 项目名称 */
    private String projectName;

    /** 状态: 1进行中 0已结束 */
    private Integer status;
}
