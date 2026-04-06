-- 导出 work_plan 的数据库结构
CREATE DATABASE IF NOT EXISTS `work_plan` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `work_plan`;

-- 导出  表 work_plan.t_demand 结构
CREATE TABLE IF NOT EXISTS `t_demand` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `demand_name` varchar(500) COLLATE utf8mb4_general_ci NOT NULL COMMENT '需求名称',
    `demand_type` varchar(50) COLLATE utf8mb4_general_ci DEFAULT '需求' COMMENT '需求类型（字典值）',
    `project_id` bigint DEFAULT NULL COMMENT '所属项目ID',
    `description` text COLLATE utf8mb4_general_ci COMMENT '需求描述',
    `status` tinyint DEFAULT '0' COMMENT '状态（字典值）',
    `priority` tinyint DEFAULT '1' COMMENT '优先级（字典值）',
    `creator_id` bigint DEFAULT NULL COMMENT '需求提出人ID',
    `start_date` date DEFAULT NULL COMMENT '计划开始日期',
    `end_date` date DEFAULT NULL COMMENT '计划结束日期',
    `total_hours` decimal(6,1) DEFAULT '0.0' COMMENT '预估总工时',
    `create_id` bigint DEFAULT NULL COMMENT '创建人ID',
    `update_id` bigint DEFAULT NULL COMMENT '修改人ID',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` tinyint DEFAULT '0',
    PRIMARY KEY (`id`),
    KEY `idx_demand_name` (`demand_name`)
    ) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='需求表';

-- 正在导出表  work_plan.t_demand 的数据：~0 rows (大约)
INSERT INTO `t_demand` (`id`, `demand_name`, `demand_type`, `project_id`, `description`, `status`, `priority`, `creator_id`, `start_date`, `end_date`, `total_hours`, `create_id`, `update_id`, `create_time`, `update_time`, `deleted`) VALUES
  (1, '组合策略', '10', 2, '组合策略下单', 10, 30, 1, '2026-04-06', '2026-04-24', 120.0, 1, 1, '2026-04-06 15:54:28', '2026-04-06 15:54:28', 0),
  (2, '接口监控', '20', 2, '', 10, 20, 1, '2026-04-06', '2026-04-24', 30.0, 1, 1, '2026-04-06 15:55:03', '2026-04-06 15:55:03', 0);

-- 导出  表 work_plan.t_dict 结构
CREATE TABLE IF NOT EXISTS `t_dict` (
`id` bigint NOT NULL AUTO_INCREMENT,
`config_type` varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '配置类别',
    `config_label` varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '显示名称',
    `config_value` varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '配置值',
    `sort_order` int DEFAULT '0' COMMENT '排序号',
    `status` tinyint DEFAULT '1' COMMENT '状态: 1启用 0禁用',
    `create_id` bigint DEFAULT NULL COMMENT '创建人ID',
    `update_id` bigint DEFAULT NULL COMMENT '修改人ID',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` tinyint DEFAULT '0',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_config_type_config_value` (`config_type`,`config_value`)
    ) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='字典配置表';

-- 正在导出表  work_plan.t_dict 的数据：~0 rows (大约)
INSERT INTO `t_dict` (`id`, `config_type`, `config_label`, `config_value`, `sort_order`, `status`, `create_id`, `update_id`, `create_time`, `update_time`, `deleted`) VALUES
(1, 'dept', '前端开发部', '10', 1, 1, NULL, 1, '2026-04-06 15:45:54', '2026-04-06 15:47:24', 0),
(2, 'dept', '后端开发部', '20', 2, 1, NULL, 1, '2026-04-06 15:45:54', '2026-04-06 15:47:27', 0),
(3, 'dept', '测试部', '30', 3, 1, NULL, 1, '2026-04-06 15:45:54', '2026-04-06 15:47:31', 0),
(4, 'task_type', '开发', '10', 1, 1, NULL, 1, '2026-04-06 15:45:54', '2026-04-06 15:47:42', 0),
(5, 'task_type', '测试', '20', 2, 1, NULL, 1, '2026-04-06 15:45:54', '2026-04-06 15:47:45', 0),
(6, 'task_type', '设计', '30', 3, 1, NULL, 1, '2026-04-06 15:45:54', '2026-04-06 15:47:48', 0),
(7, 'task_type', '其他', '40', 4, 1, NULL, 1, '2026-04-06 15:45:54', '2026-04-06 15:47:51', 0),
(8, 'demand_type', '需求', '10', 1, 1, NULL, 1, '2026-04-06 15:45:54', '2026-04-06 15:47:56', 0),
(9, 'demand_type', '优化', '20', 2, 1, NULL, 1, '2026-04-06 15:45:54', '2026-04-06 15:47:59', 0),
(10, 'demand_type', 'Bug', '30', 3, 1, NULL, 1, '2026-04-06 15:45:54', '2026-04-06 15:48:02', 0),
(11, 'priority', '低', '10', 1, 1, NULL, 1, '2026-04-06 15:45:54', '2026-04-06 15:48:10', 0),
(12, 'priority', '中', '20', 2, 1, NULL, 1, '2026-04-06 15:45:54', '2026-04-06 15:48:13', 0),
(13, 'priority', '高', '30', 3, 1, NULL, 1, '2026-04-06 15:45:54', '2026-04-06 15:48:16', 0),
(14, 'demand_status', '待开始', '10', 1, 1, NULL, 1, '2026-04-06 15:45:54', '2026-04-06 15:48:22', 0),
(15, 'demand_status', '进行中', '20', 2, 1, NULL, 1, '2026-04-06 15:45:54', '2026-04-06 15:48:24', 0),
(16, 'demand_status', '已完成', '30', 3, 1, NULL, 1, '2026-04-06 15:45:54', '2026-04-06 15:48:27', 0),
(17, 'role', '普通用户', '20', 1, 1, NULL, 1, '2026-04-06 15:45:54', '2026-04-06 15:48:33', 0),
(18, 'role', '管理员', '10', 2, 1, NULL, 1, '2026-04-06 15:45:54', '2026-04-06 15:48:36', 0);

-- 导出  表 work_plan.t_project 结构
CREATE TABLE IF NOT EXISTS `t_project` (
   `id` bigint NOT NULL AUTO_INCREMENT,
   `project_name` varchar(200) COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目名称',
    `status` tinyint DEFAULT '1' COMMENT '状态: 1进行中 0已结束',
    `create_id` bigint DEFAULT NULL COMMENT '创建人ID',
    `update_id` bigint DEFAULT NULL COMMENT '修改人ID',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` tinyint DEFAULT '0',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_project_name` (`project_name`)
    ) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='项目表';

-- 正在导出表  work_plan.t_project 的数据：~0 rows (大约)
INSERT INTO `t_project` (`id`, `project_name`, `status`, `create_id`, `update_id`, `create_time`, `update_time`, `deleted`) VALUES
   (1, 'US', 1, NULL, NULL, '2026-04-06 15:45:54', '2026-04-06 15:46:06', 0),
   (2, 'SG', 1, NULL, NULL, '2026-04-06 15:45:54', '2026-04-06 15:46:08', 0),
   (3, 'HK', 1, NULL, NULL, '2026-04-06 15:46:12', '2026-04-06 15:46:16', 0);

-- 导出  表 work_plan.t_task 结构
CREATE TABLE IF NOT EXISTS `t_task` (
`id` bigint NOT NULL AUTO_INCREMENT,
`task_name` varchar(500) COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务名称',
    `task_type` varchar(50) COLLATE utf8mb4_general_ci DEFAULT '开发' COMMENT '任务类型（字典值）',
    `demand_id` bigint DEFAULT NULL COMMENT '所属需求ID',
    `assignee_id` bigint NOT NULL COMMENT '负责人ID',
    `start_date` date NOT NULL COMMENT '开始日期',
    `end_date` date NOT NULL COMMENT '结束日期',
    `total_hours` decimal(6,1) DEFAULT '0.0' COMMENT '预估工时',
    `status` tinyint DEFAULT '0' COMMENT '状态',
    `description` text COLLATE utf8mb4_general_ci COMMENT '任务描述',
    `create_id` bigint DEFAULT NULL COMMENT '创建人ID',
    `update_id` bigint DEFAULT NULL COMMENT '修改人ID',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` tinyint DEFAULT '0',
    PRIMARY KEY (`id`),
    KEY `idx_assignee_id` (`assignee_id`),
    KEY `idx_demand_id` (`demand_id`),
    KEY `idx_start_date_end_date` (`start_date`,`end_date`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='任务表';

-- 正在导出表  work_plan.t_task 的数据：~0 rows (大约)

-- 导出  表 work_plan.t_user 结构
CREATE TABLE IF NOT EXISTS `t_user` (
`id` bigint NOT NULL AUTO_INCREMENT,
`username` varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '登录账号',
    `password` varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
    `real_name` varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '真实姓名',
    `dept` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '所属部门（字典值）',
    `role` varchar(20) COLLATE utf8mb4_general_ci DEFAULT 'USER' COMMENT '角色（字典值）',
    `status` tinyint DEFAULT '1' COMMENT '状态: 1启用 0禁用',
    `create_id` bigint DEFAULT NULL COMMENT '创建人ID',
    `update_id` bigint DEFAULT NULL COMMENT '修改人ID',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` tinyint DEFAULT '0',
    PRIMARY KEY (`id`),
    UNIQUE KEY `username` (`username`)
    ) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户表';

-- 正在导出表  work_plan.t_user 的数据：~4 rows (大约)
INSERT INTO `t_user` (`id`, `username`, `password`, `real_name`, `dept`, `role`, `status`, `create_id`, `update_id`, `create_time`, `update_time`, `deleted`) VALUES
  (1, 'admin', 'e10adc3949ba59abbe56e057f20f883e', '管理员', '30', '10', 1, NULL, 1, '2026-04-06 15:45:54', '2026-04-06 15:53:00', 0),
  (2, 'zhangsan', 'e10adc3949ba59abbe56e057f20f883e', '张三', '20', '20', 1, NULL, 1, '2026-04-06 15:45:54', '2026-04-06 15:53:12', 0),
  (3, 'lisi', 'e10adc3949ba59abbe56e057f20f883e', '李四', '20', '20', 1, NULL, 1, '2026-04-06 15:45:54', '2026-04-06 15:53:27', 0),
  (4, 'wangwu', 'e10adc3949ba59abbe56e057f20f883e', '王五', '10', '20', 1, NULL, 1, '2026-04-06 15:45:54', '2026-04-06 15:53:36', 0);

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
