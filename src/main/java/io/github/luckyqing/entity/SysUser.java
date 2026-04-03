package io.github.luckyqing.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户实体
 */
@Data
@TableName("sys_user")
public class SysUser {

    /** 主键ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 登录账号 */
    private String username;

    /** 登录密码（MD5加密） */
    private String password;

    /** 真实姓名 */
    private String realName;

    /** 所属部门ID */
    private Long deptId;

    /** 角色: ADMIN-管理员 USER-普通用户 */
    private String role;

    /** 状态: 1启用 0禁用 */
    private Integer status;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /** 逻辑删除标识 */
    @TableLogic
    private Integer deleted;

    /** 部门名称（非数据库字段，关联查询用） */
    @TableField(exist = false)
    private String deptName;
}
