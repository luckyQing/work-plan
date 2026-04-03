package io.github.luckyqing.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 部门实体
 */
@Data
@TableName("sys_dept")
public class SysDept {

    /** 主键ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 部门名称 */
    private String deptName;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /** 逻辑删除标识: 0未删除 1已删除 */
    @TableLogic
    private Integer deleted;
}
