package io.github.luckyqing.vo.auth;

import lombok.Data;

/**
 * 登录成功返回数据
 */
@Data
public class LoginRespVO {

    /** 登录令牌 */
    private String token;

    /** 用户ID */
    private Long userId;

    /** 用户真实姓名 */
    private String realName;

    /** 用户角色: ADMIN/USER */
    private String role;
}
