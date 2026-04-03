package io.github.luckyqing.vo.project;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 项目信息响应数据
 */
@Data
public class ProjectRespVO {

    /** 项目ID */
    private Long id;

    /** 项目名称 */
    private String projectName;

    /** 状态: 1进行中 0已结束 */
    private Integer status;

    /** 创建时间 */
    private LocalDateTime createTime;
}
