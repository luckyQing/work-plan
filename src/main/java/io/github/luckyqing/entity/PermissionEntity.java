package io.github.luckyqing.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 权限表
 * </p>
 *
 * @author collin.li
 * @since 2026-07-20
 */
@Getter
@Setter
@TableName("t_permission")
public class PermissionEntity extends BaseEntity {

    /**
     * 权限标识，如 user:list、menu:demand
     */
    @TableField("perm_code")
    private String permCode;

    /**
     * 权限名称
     */
    @TableField("perm_name")
    private String permName;

    /**
     * 权限类型: menu/page/api
     */
    @TableField("perm_type")
    private String permType;

    /**
     * 路径（接口路径或页面路径）
     */
    @TableField("perm_path")
    private String permPath;

    /**
     * 父权限ID，0表示顶级
     */
    @TableField("parent_id")
    private Long parentId;

    /**
     * 排序
     */
    @TableField("sort_order")
    private Integer sortOrder;

    /**
     * 状态: 1启用 0禁用
     */
    @TableField("status")
    private Integer status;

}
