package io.github.luckyqing.vo.user;

import lombok.Data;

/**
 * 用户新增/修改请求参数
 */
@Data
public class UserSaveReqVO {

    /** 用户ID（修改时必传） */
    private Long id;

    /** 登录账号 */
    private String username;

    /** 登录密码（新增时必传，修改时为空则不更新） */
    private String password;

    /** 真实姓名 */
    private String realName;

    /** 所属部门ID */
    private Long deptId;

    /** 角色: ADMIN/USER */
    private String role;

    /** 状态: 1启用 0禁用 */
    private Integer status;
}
