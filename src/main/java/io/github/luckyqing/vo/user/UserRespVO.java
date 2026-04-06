package io.github.luckyqing.vo.user;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户信息响应数据
 */
@Data
public class UserRespVO {

    /** 用户ID */
    private Long id;

    /** 登录账号 */
    private String username;

    /** 真实姓名 */
    private String realName;

    /** 所属部门（字典值） */
    private String dept;

    /** 角色 */
    private String role;

    /** 状态: 1启用 0禁用 */
    private Integer status;

    /** 创建时间 */
    private LocalDateTime createTime;
}
