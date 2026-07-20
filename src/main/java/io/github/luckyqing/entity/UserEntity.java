package io.github.luckyqing.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author collin.li
 * @since 2026-07-20
 */
@Getter
@Setter
@TableName("t_user")
public class UserEntity extends BaseEntity {

    /**
     * 登录账号
     */
    @TableField("username")
    private String username;

    /**
     * 密码
     */
    @TableField("password")
    private String password;

    /**
     * 真实姓名
     */
    @TableField("real_name")
    private String realName;

    /**
     * 所属部门（字典值）
     */
    @TableField("dept")
    private String dept;

    /**
     * 角色（字典值）
     */
    @TableField("role")
    private String role;

    /**
     * 状态: 1启用 0禁用
     */
    @TableField("status")
    private Integer status;

}
