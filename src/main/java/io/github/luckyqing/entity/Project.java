package io.github.luckyqing.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 项目实体
 */
@Data
@TableName("project")
public class Project {

    /** 主键ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 项目名称 */
    private String projectName;

    /** 状态: 1进行中 0已结束 */
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
}
