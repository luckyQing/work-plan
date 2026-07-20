package io.github.luckyqing.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * <p>
 * 需求表
 * </p>
 *
 * @author collin.li
 * @since 2026-07-20
 */
@Getter
@Setter
@TableName("t_demand")
public class DemandEntity extends BaseEntity {

    /**
     * 需求名称
     */
    @TableField("demand_name")
    private String demandName;

    /**
     * 需求类型（字典值）
     */
    @TableField("demand_type")
    private String demandType;

    /**
     * 所属项目ID
     */
    @TableField("project_id")
    private Long projectId;

    /**
     * 需求描述
     */
    @TableField("description")
    private String description;

    /**
     * 状态（字典值）
     */
    @TableField("status")
    private Integer status;

    /**
     * 优先级（字典值）
     */
    @TableField("priority")
    private Integer priority;

    /**
     * 需求提出人ID
     */
    @TableField("creator_id")
    private Long creatorId;

    /**
     * 计划开始日期
     */
    @TableField("start_date")
    private LocalDate startDate;

    /**
     * 计划结束日期
     */
    @TableField("end_date")
    private LocalDate endDate;

    /**
     * 预估总工时
     */
    @TableField("total_hours")
    private BigDecimal totalHours;

    /**
     * 产品人员（逗号分隔用户ID）
     */
    @TableField("product_members")
    private String productMembers;

    /**
     * 测试人员（逗号分隔用户ID）
     */
    @TableField("test_members")
    private String testMembers;

    /**
     * 研发人员（逗号分隔用户ID）
     */
    @TableField("dev_members")
    private String devMembers;

}
