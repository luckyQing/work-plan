package io.github.luckyqing.vo.user;

import lombok.Data;

/**
 * 用户列表查询请求参数
 */
@Data
public class UserListReqVO {

    /** 部门（字典值，可选） */
    private String dept;
}
