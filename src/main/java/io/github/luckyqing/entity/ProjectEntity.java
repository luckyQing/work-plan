package io.github.luckyqing.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 项目表
 * </p>
 *
 * @author collin.li
 * @since 2026-07-20
 */
@Getter
@Setter
@TableName("t_project")
public class ProjectEntity extends BaseEntity {

    /**
     * 项目名称
     */
    @TableField("project_name")
    private String projectName;

    /**
     * 状态: 1进行中 0已结束
     */
    @TableField("status")
    private Integer status;

}
