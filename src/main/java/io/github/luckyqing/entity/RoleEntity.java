package io.github.luckyqing.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @author collin.li
 * @since 2026-07-20
 */
@Getter
@Setter
@TableName("t_role")
public class RoleEntity extends BaseEntity {

    /**
     * 角色标识，如 ADMIN/USER
     */
    @TableField("role_code")
    private String roleCode;

    /**
     * 角色名称
     */
    @TableField("role_name")
    private String roleName;

    /**
     * 描述
     */
    @TableField("description")
    private String description;

    /**
     * 状态: 1启用 0禁用
     */
    @TableField("status")
    private Integer status;

}
