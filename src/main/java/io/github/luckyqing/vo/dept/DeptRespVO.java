package io.github.luckyqing.vo.dept;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 部门信息响应数据
 */
@Data
public class DeptRespVO {

    /** 部门ID */
    private Long id;

    /** 部门名称 */
    private String deptName;

    /** 创建时间 */
    private LocalDateTime createTime;
}
