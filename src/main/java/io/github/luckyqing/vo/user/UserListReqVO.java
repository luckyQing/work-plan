package io.github.luckyqing.vo.user;

import lombok.Data;

/**
 * 用户列表查询请求参数
 */
@Data
public class UserListReqVO {

    /** 部门ID（可选，不传则查全部） */
    private Long deptId;
}
