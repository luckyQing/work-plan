package io.github.luckyqing.vo.dept;

import lombok.Data;

/**
 * 部门新增/修改请求参数
 */
@Data
public class DeptSaveReqVO {

    /** 部门ID（修改时必传） */
    private Long id;

    /** 部门名称 */
    private String deptName;
}
