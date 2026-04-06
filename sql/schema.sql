-- 导出 work_plan 的数据库结构
CREATE DATABASE IF NOT EXISTS `work_plan` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `work_plan`;

-- 表 work_plan.t_demand 结构
CREATE TABLE IF NOT EXISTS `t_demand` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `demand_name` varchar(500) COLLATE utf8mb4_general_ci NOT NULL COMMENT '需求名称',
    `demand_type` varchar(50) COLLATE utf8mb4_general_ci DEFAULT '需求' COMMENT '需求类型（字典值）',
    `project_id` bigint unsigned DEFAULT NULL COMMENT '所属项目ID',
    `description` text COLLATE utf8mb4_general_ci COMMENT '需求描述',
    `status` tinyint unsigned DEFAULT '0' COMMENT '状态（字典值）',
    `priority` tinyint unsigned DEFAULT '1' COMMENT '优先级（字典值）',
    `creator_id` bigint unsigned DEFAULT NULL COMMENT '需求提出人ID',
    `start_date` date DEFAULT NULL COMMENT '计划开始日期',
    `end_date` date DEFAULT NULL COMMENT '计划结束日期',
    `total_hours` decimal(6,1) unsigned DEFAULT '0.0' COMMENT '预估总工时',
    `product_members` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '产品人员（逗号分隔用户ID）',
    `test_members` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '测试人员（逗号分隔用户ID）',
    `dev_members` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '研发人员（逗号分隔用户ID）',
    `create_id` bigint unsigned DEFAULT NULL COMMENT '创建人ID',
    `update_id` bigint unsigned DEFAULT NULL COMMENT '修改人ID',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` tinyint unsigned DEFAULT '0',
    PRIMARY KEY (`id`),
    KEY `idx_demand_name` (`demand_name`)
    ) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='需求表';

-- work_plan.t_demand 的数据：~0 rows (大约)
INSERT INTO `t_demand` (`id`, `demand_name`, `demand_type`, `project_id`, `description`, `status`, `priority`, `creator_id`, `start_date`, `end_date`, `total_hours`, `product_members`, `test_members`, `dev_members`, `create_id`, `update_id`, `create_time`, `update_time`, `deleted`) VALUES
  (1, '组合策略', '10', 2, '组合策略下单', 20, 30, 1, '2026-04-06', '2026-04-24', 120.0, '1', '2', '2', 1, 1, '2026-04-06 15:54:28', '2026-04-06 21:08:30', 0),
  (2, '接口监控', '20', 2, '', 20, 20, 1, '2026-04-06', '2026-04-24', 30.0, '1', '4', '2,3', 1, 1, '2026-04-06 15:55:03', '2026-04-06 21:08:23', 0);

-- 表 work_plan.t_dict 结构
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
    `deleted` tinyint(1) DEFAULT '0',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_config_type_config_value` (`config_type`,`config_value`)
    ) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='字典配置表';

-- work_plan.t_dict 的数据：~0 rows (大约)
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
(16, 'demand_status', '已完成', '30', 3, 1, NULL, 1, '2026-04-06 15:45:54', '2026-04-06 15:48:27', 0);

-- 表 work_plan.t_permission 结构
CREATE TABLE IF NOT EXISTS `t_permission` (
 `id` bigint unsigned NOT NULL AUTO_INCREMENT,
 `perm_code` varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限标识，如 user:list、menu:demand',
    `perm_name` varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限名称',
    `perm_type` varchar(20) COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限类型: menu/page/api',
    `perm_path` varchar(200) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '路径（接口路径或页面路径）',
    `parent_id` bigint unsigned DEFAULT '0' COMMENT '父权限ID，0表示顶级',
    `sort_order` int unsigned DEFAULT '0' COMMENT '排序',
    `status` tinyint unsigned DEFAULT '1' COMMENT '状态: 1启用 0禁用',
    `create_id` bigint unsigned DEFAULT NULL COMMENT '创建人ID',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
    `update_id` bigint unsigned DEFAULT NULL COMMENT '修改人ID',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` tinyint unsigned DEFAULT '0',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_perm_code` (`perm_code`)
    ) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='权限表';

-- work_plan.t_permission 的数据：~20 rows (大约)
INSERT INTO `t_permission` (`id`, `perm_code`, `perm_name`, `perm_type`, `perm_path`, `parent_id`, `sort_order`, `status`, `create_id`, `create_time`, `update_id`, `update_time`, `deleted`) VALUES
    (1, 'menu:dashboard', '工作台', 'menu', 'dashboard.html', 0, 1, 1, NULL, '2026-04-06 18:17:22', NULL, '2026-04-06 18:17:22', 0),
    (2, 'menu:week_view', '周排期', 'menu', 'week-view.html', 0, 2, 1, NULL, '2026-04-06 18:17:22', NULL, '2026-04-06 18:17:22', 0),
    (3, 'menu:demand', '需求管理', 'menu', 'demand-manage.html', 0, 3, 1, NULL, '2026-04-06 18:17:22', NULL, '2026-04-06 18:17:22', 0),
    (4, 'menu:user', '人员管理', 'menu', 'user-manage.html', 0, 998, 1, NULL, '2026-04-06 18:17:22', 1, '2026-04-06 22:55:17', 0),
    (5, 'menu:config', '字典配置', 'menu', 'config-manage.html', 0, 10000, 1, NULL, '2026-04-06 18:17:22', 1, '2026-04-06 22:55:02', 0),
    (6, 'api:user:list', '查询用户列表', 'api', '/api/user/list', 0, 1, 1, NULL, '2026-04-06 18:17:22', NULL, '2026-04-06 19:05:39', 0),
    (7, 'api:user:add', '新增用户', 'api', '/api/user', 0, 2, 1, NULL, '2026-04-06 18:17:22', NULL, '2026-04-06 18:17:22', 0),
    (8, 'api:user:edit', '编辑用户', 'api', '/api/user', 0, 3, 1, NULL, '2026-04-06 18:17:22', NULL, '2026-04-06 18:17:22', 0),
    (9, 'api:user:delete', '删除用户', 'api', '/api/user/**', 0, 4, 1, NULL, '2026-04-06 18:17:22', NULL, '2026-04-06 18:17:22', 0),
    (10, 'api:demand:list', '查询需求列表', 'api', '/api/demand/list', 0, 5, 1, NULL, '2026-04-06 18:17:22', NULL, '2026-04-06 18:17:22', 0),
    (11, 'api:demand:add', '新增需求', 'api', '/api/demand', 0, 6, 1, NULL, '2026-04-06 18:17:22', NULL, '2026-04-06 18:17:22', 0),
    (12, 'api:demand:edit', '编辑需求', 'api', '/api/demand', 0, 7, 1, NULL, '2026-04-06 18:17:22', NULL, '2026-04-06 18:17:22', 0),
    (13, 'api:demand:delete', '删除需求', 'api', '/api/demand/**', 0, 8, 1, NULL, '2026-04-06 18:17:22', NULL, '2026-04-06 18:17:22', 0),
    (14, 'api:task:list', '查询任务列表', 'api', '/api/task/list', 0, 9, 1, NULL, '2026-04-06 18:17:22', NULL, '2026-04-06 18:17:22', 0),
    (15, 'api:task:add', '新增任务', 'api', '/api/task', 0, 10, 1, NULL, '2026-04-06 18:17:22', NULL, '2026-04-06 18:17:22', 0),
    (16, 'api:task:edit', '编辑任务', 'api', '/api/task', 0, 11, 1, NULL, '2026-04-06 18:17:22', NULL, '2026-04-06 18:17:22', 0),
    (17, 'api:task:delete', '删除任务', 'api', '/api/task/**', 0, 12, 1, NULL, '2026-04-06 18:17:22', NULL, '2026-04-06 18:17:22', 0),
    (18, 'api:config:manage', '字典配置管理', 'api', '/api/config/**', 0, 13, 1, NULL, '2026-04-06 18:17:22', NULL, '2026-04-06 18:17:22', 0),
    (19, 'api:worklog:manage', '工时录入管理', 'api', '/api/worklog/**', 0, 14, 1, NULL, '2026-04-06 18:17:22', NULL, '2026-04-06 18:17:22', 0),
    (20, 'api:schedule:view', '排期视图', 'api', '/api/schedule/**', 0, 15, 1, NULL, '2026-04-06 18:17:22', NULL, '2026-04-06 18:17:22', 0),
    (21, 'menu:role', '角色管理', 'menu', 'role-manage.html', 0, 999, 1, 1, '2026-04-06 22:51:53', 1, '2026-04-06 22:54:25', 0),
    (22, 'menu:permission', '权限管理', 'menu', 'permission-manage.html', 0, 1000, 1, 1, '2026-04-06 22:52:26', 1, '2026-04-06 22:54:36', 0);

-- 表 work_plan.t_project 结构
CREATE TABLE IF NOT EXISTS `t_project` (
   `id` bigint unsigned NOT NULL AUTO_INCREMENT,
   `project_name` varchar(200) COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目名称',
    `status` tinyint unsigned DEFAULT '1' COMMENT '状态: 1进行中 0已结束',
    `create_id` bigint unsigned DEFAULT NULL COMMENT '创建人ID',
    `update_id` bigint unsigned DEFAULT NULL COMMENT '修改人ID',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` tinyint unsigned DEFAULT '0',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_project_name` (`project_name`)
    ) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='项目表';

-- work_plan.t_project 的数据：~0 rows (大约)
INSERT INTO `t_project` (`id`, `project_name`, `status`, `create_id`, `update_id`, `create_time`, `update_time`, `deleted`) VALUES
   (1, 'US', 1, NULL, NULL, '2026-04-06 15:45:54', '2026-04-06 15:46:06', 0),
   (2, 'SG', 1, NULL, NULL, '2026-04-06 15:45:54', '2026-04-06 15:46:08', 0),
   (3, 'HK', 1, NULL, NULL, '2026-04-06 15:46:12', '2026-04-06 15:46:16', 0);

-- 表 work_plan.t_role 结构
CREATE TABLE IF NOT EXISTS `t_role` (
`id` bigint unsigned NOT NULL AUTO_INCREMENT,
`role_code` varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色标识，如 ADMIN/USER',
    `role_name` varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名称',
    `description` varchar(200) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '描述',
    `status` tinyint unsigned DEFAULT '1' COMMENT '状态: 1启用 0禁用',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
    `create_id` bigint unsigned DEFAULT NULL COMMENT '创建人ID',
    `update_id` bigint unsigned DEFAULT NULL COMMENT '修改人ID',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` tinyint unsigned DEFAULT '0',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_code` (`role_code`)
    ) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='角色表';

-- work_plan.t_role 的数据：~2 rows (大约)
INSERT INTO `t_role` (`id`, `role_code`, `role_name`, `description`, `status`, `create_time`, `create_id`, `update_id`, `update_time`, `deleted`) VALUES
(1, 'ADMIN', '管理员', '系统管理员，拥有所有权限', 1, '2026-04-06 18:17:22', NULL, NULL, '2026-04-06 18:17:22', 0),
(2, 'USER', '普通用户', '普通用户，基础操作权限', 1, '2026-04-06 18:17:22', NULL, NULL, '2026-04-06 18:17:22', 0),
(3, 'leader', '小组leader', '', 1, '2026-04-06 19:06:29', NULL, NULL, '2026-04-06 19:06:29', 0);

-- 表 work_plan.t_role_permission 结构
CREATE TABLE IF NOT EXISTS `t_role_permission` (
 `id` bigint unsigned NOT NULL AUTO_INCREMENT,
 `role_id` bigint unsigned NOT NULL COMMENT '角色ID',
 `permission_id` bigint unsigned NOT NULL COMMENT '权限ID',
 `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
 `create_id` bigint unsigned DEFAULT NULL COMMENT '创建人ID',
 `update_id` bigint unsigned DEFAULT NULL COMMENT '修改人ID',
 `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
 `deleted` tinyint unsigned DEFAULT '0',
 PRIMARY KEY (`id`),
    KEY `idx_role_perm` (`role_id`,`permission_id`) USING BTREE
    ) ENGINE=InnoDB AUTO_INCREMENT=130 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='角色权限关联表';

-- work_plan.t_role_permission 的数据：~30 rows (大约)
INSERT INTO `t_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `create_id`, `update_id`, `update_time`, `deleted`) VALUES
   (1, 1, 18, '2026-04-06 18:17:23', NULL, NULL, '2026-04-06 22:53:40', 1),
   (2, 1, 11, '2026-04-06 18:17:23', NULL, NULL, '2026-04-06 22:53:40', 1),
   (3, 1, 13, '2026-04-06 18:17:23', NULL, NULL, '2026-04-06 22:53:40', 1),
   (4, 1, 12, '2026-04-06 18:17:23', NULL, NULL, '2026-04-06 22:53:40', 1),
   (5, 1, 10, '2026-04-06 18:17:23', NULL, NULL, '2026-04-06 22:53:40', 1),
   (6, 1, 20, '2026-04-06 18:17:23', NULL, NULL, '2026-04-06 22:53:40', 1),
   (7, 1, 15, '2026-04-06 18:17:23', NULL, NULL, '2026-04-06 22:53:40', 1),
   (8, 1, 17, '2026-04-06 18:17:23', NULL, NULL, '2026-04-06 22:53:40', 1),
   (9, 1, 16, '2026-04-06 18:17:23', NULL, NULL, '2026-04-06 22:53:40', 1),
   (10, 1, 14, '2026-04-06 18:17:23', NULL, NULL, '2026-04-06 22:53:40', 1),
   (11, 1, 7, '2026-04-06 18:17:23', NULL, NULL, '2026-04-06 22:53:40', 1),
   (12, 1, 9, '2026-04-06 18:17:23', NULL, NULL, '2026-04-06 22:53:40', 1),
   (13, 1, 8, '2026-04-06 18:17:23', NULL, NULL, '2026-04-06 22:53:40', 1),
   (14, 1, 6, '2026-04-06 18:17:23', NULL, NULL, '2026-04-06 22:53:40', 1),
   (15, 1, 19, '2026-04-06 18:17:23', NULL, NULL, '2026-04-06 22:53:40', 1),
   (16, 1, 5, '2026-04-06 18:17:23', NULL, NULL, '2026-04-06 22:53:40', 1),
   (17, 1, 1, '2026-04-06 18:17:23', NULL, NULL, '2026-04-06 22:53:40', 1),
   (18, 1, 3, '2026-04-06 18:17:23', NULL, NULL, '2026-04-06 22:53:40', 1),
   (19, 1, 4, '2026-04-06 18:17:23', NULL, NULL, '2026-04-06 22:53:40', 1),
   (20, 1, 2, '2026-04-06 18:17:23', NULL, NULL, '2026-04-06 22:53:40', 1),
   (32, 2, 10, '2026-04-06 18:17:23', NULL, NULL, '2026-04-06 22:55:59', 1),
   (33, 2, 20, '2026-04-06 18:17:23', NULL, NULL, '2026-04-06 22:55:59', 1),
   (34, 2, 15, '2026-04-06 18:17:23', NULL, NULL, '2026-04-06 22:55:59', 1),
   (35, 2, 17, '2026-04-06 18:17:23', NULL, NULL, '2026-04-06 22:55:59', 1),
   (36, 2, 16, '2026-04-06 18:17:23', NULL, NULL, '2026-04-06 22:55:59', 1),
   (37, 2, 14, '2026-04-06 18:17:23', NULL, NULL, '2026-04-06 22:55:59', 1),
   (38, 2, 19, '2026-04-06 18:17:23', NULL, NULL, '2026-04-06 22:55:59', 1),
   (39, 2, 1, '2026-04-06 18:17:23', NULL, NULL, '2026-04-06 22:55:59', 1),
   (40, 2, 3, '2026-04-06 18:17:23', NULL, NULL, '2026-04-06 22:55:59', 1),
   (41, 2, 2, '2026-04-06 18:17:23', NULL, NULL, '2026-04-06 22:55:59', 1),
   (64, 3, 4, '2026-04-06 21:11:00', NULL, NULL, '2026-04-06 21:27:22', 1),
   (65, 3, 6, '2026-04-06 21:11:00', NULL, NULL, '2026-04-06 21:27:22', 1),
   (66, 3, 7, '2026-04-06 21:11:00', NULL, NULL, '2026-04-06 21:27:22', 1),
   (67, 3, 8, '2026-04-06 21:11:00', NULL, NULL, '2026-04-06 21:27:22', 1),
   (68, 3, 9, '2026-04-06 21:11:00', NULL, NULL, '2026-04-06 21:27:22', 1),
   (69, 3, 10, '2026-04-06 21:11:00', NULL, NULL, '2026-04-06 21:27:22', 1),
   (70, 3, 11, '2026-04-06 21:11:00', NULL, NULL, '2026-04-06 21:27:22', 1),
   (71, 3, 12, '2026-04-06 21:11:00', NULL, NULL, '2026-04-06 21:27:22', 1),
   (72, 3, 13, '2026-04-06 21:11:00', NULL, NULL, '2026-04-06 21:27:22', 1),
   (73, 3, 14, '2026-04-06 21:11:00', NULL, NULL, '2026-04-06 21:27:22', 1),
   (74, 3, 15, '2026-04-06 21:11:00', NULL, NULL, '2026-04-06 21:27:22', 1),
   (75, 3, 16, '2026-04-06 21:11:01', NULL, NULL, '2026-04-06 21:27:22', 1),
   (76, 3, 17, '2026-04-06 21:11:01', NULL, NULL, '2026-04-06 21:27:22', 1),
   (77, 3, 19, '2026-04-06 21:11:01', NULL, NULL, '2026-04-06 21:27:22', 1),
   (78, 3, 20, '2026-04-06 21:11:01', NULL, NULL, '2026-04-06 21:27:22', 1),
   (79, 3, 1, '2026-04-06 21:11:01', NULL, NULL, '2026-04-06 21:27:22', 1),
   (80, 3, 2, '2026-04-06 21:11:01', NULL, NULL, '2026-04-06 21:27:22', 1),
   (81, 3, 3, '2026-04-06 21:11:02', NULL, NULL, '2026-04-06 21:27:22', 1),
   (85, 3, 6, '2026-04-06 22:07:08', 1, 1, '2026-04-06 22:07:08', 0),
   (86, 3, 7, '2026-04-06 22:07:08', 1, 1, '2026-04-06 22:07:08', 0),
   (87, 3, 8, '2026-04-06 22:07:08', 1, 1, '2026-04-06 22:07:08', 0),
   (88, 3, 10, '2026-04-06 22:07:08', 1, 1, '2026-04-06 22:07:08', 0),
   (89, 3, 11, '2026-04-06 22:07:08', 1, 1, '2026-04-06 22:07:08', 0),
   (90, 3, 12, '2026-04-06 22:07:08', 1, 1, '2026-04-06 22:07:08', 0),
   (91, 3, 13, '2026-04-06 22:07:09', 1, 1, '2026-04-06 22:07:09', 0),
   (92, 3, 14, '2026-04-06 22:07:09', 1, 1, '2026-04-06 22:07:09', 0),
   (93, 3, 15, '2026-04-06 22:07:09', 1, 1, '2026-04-06 22:07:09', 0),
   (94, 3, 16, '2026-04-06 22:07:09', 1, 1, '2026-04-06 22:07:09', 0),
   (95, 3, 17, '2026-04-06 22:07:09', 1, 1, '2026-04-06 22:07:09', 0),
   (96, 3, 19, '2026-04-06 22:07:09', 1, 1, '2026-04-06 22:07:09', 0),
   (97, 3, 1, '2026-04-06 22:07:09', 1, 1, '2026-04-06 22:07:09', 0),
   (98, 3, 2, '2026-04-06 22:07:09', 1, 1, '2026-04-06 22:07:09', 0),
   (99, 3, 3, '2026-04-06 22:07:09', 1, 1, '2026-04-06 22:07:09', 0),
   (100, 1, 22, '2026-04-06 22:53:40', 1, 1, '2026-04-06 22:53:40', 0),
   (101, 1, 21, '2026-04-06 22:53:40', 1, 1, '2026-04-06 22:53:40', 0),
   (102, 1, 1, '2026-04-06 22:53:40', 1, 1, '2026-04-06 22:53:40', 0),
   (103, 1, 2, '2026-04-06 22:53:40', 1, 1, '2026-04-06 22:53:40', 0),
   (104, 1, 3, '2026-04-06 22:53:40', 1, 1, '2026-04-06 22:53:40', 0),
   (105, 1, 4, '2026-04-06 22:53:40', 1, 1, '2026-04-06 22:53:40', 0),
   (106, 1, 5, '2026-04-06 22:53:40', 1, 1, '2026-04-06 22:53:40', 0),
   (107, 1, 6, '2026-04-06 22:53:41', 1, 1, '2026-04-06 22:53:41', 0),
   (108, 1, 7, '2026-04-06 22:53:41', 1, 1, '2026-04-06 22:53:41', 0),
   (109, 1, 8, '2026-04-06 22:53:41', 1, 1, '2026-04-06 22:53:41', 0),
   (110, 1, 9, '2026-04-06 22:53:41', 1, 1, '2026-04-06 22:53:41', 0),
   (111, 1, 10, '2026-04-06 22:53:41', 1, 1, '2026-04-06 22:53:41', 0),
   (112, 1, 11, '2026-04-06 22:53:41', 1, 1, '2026-04-06 22:53:41', 0),
   (113, 1, 12, '2026-04-06 22:53:41', 1, 1, '2026-04-06 22:53:41', 0),
   (114, 1, 13, '2026-04-06 22:53:41', 1, 1, '2026-04-06 22:53:41', 0),
   (115, 1, 14, '2026-04-06 22:53:41', 1, 1, '2026-04-06 22:53:41', 0),
   (116, 1, 15, '2026-04-06 22:53:41', 1, 1, '2026-04-06 22:53:41', 0),
   (117, 1, 16, '2026-04-06 22:53:41', 1, 1, '2026-04-06 22:53:41', 0),
   (118, 1, 17, '2026-04-06 22:53:41', 1, 1, '2026-04-06 22:53:41', 0),
   (119, 1, 18, '2026-04-06 22:53:41', 1, 1, '2026-04-06 22:53:41', 0),
   (120, 1, 19, '2026-04-06 22:53:41', 1, 1, '2026-04-06 22:53:41', 0),
   (121, 1, 20, '2026-04-06 22:53:41', 1, 1, '2026-04-06 22:53:41', 0),
   (122, 2, 1, '2026-04-06 22:56:00', 1, 1, '2026-04-06 22:56:00', 0),
   (123, 2, 2, '2026-04-06 22:56:00', 1, 1, '2026-04-06 22:56:00', 0),
   (124, 2, 14, '2026-04-06 22:56:00', 1, 1, '2026-04-06 22:56:00', 0),
   (125, 2, 15, '2026-04-06 22:56:00', 1, 1, '2026-04-06 22:56:00', 0),
   (126, 2, 16, '2026-04-06 22:56:00', 1, 1, '2026-04-06 22:56:00', 0),
   (127, 2, 17, '2026-04-06 22:56:00', 1, 1, '2026-04-06 22:56:00', 0),
   (128, 2, 19, '2026-04-06 22:56:00', 1, 1, '2026-04-06 22:56:00', 0),
   (129, 2, 20, '2026-04-06 22:56:00', 1, 1, '2026-04-06 22:56:00', 0);

-- 表 work_plan.t_task 结构
CREATE TABLE IF NOT EXISTS `t_task` (
`id` bigint unsigned NOT NULL AUTO_INCREMENT,
`task_name` varchar(500) COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务名称',
    `task_type` varchar(50) COLLATE utf8mb4_general_ci DEFAULT '开发' COMMENT '任务类型（字典值）',
    `demand_id` bigint unsigned DEFAULT NULL COMMENT '所属需求ID',
    `assignee_id` bigint unsigned NOT NULL COMMENT '负责人ID',
    `start_date` date NOT NULL COMMENT '开始日期',
    `end_date` date NOT NULL COMMENT '结束日期',
    `total_hours` decimal(6,1) unsigned DEFAULT '0.0' COMMENT '预估工时',
    `status` tinyint unsigned DEFAULT '0' COMMENT '状态',
    `description` text COLLATE utf8mb4_general_ci COMMENT '任务描述',
    `create_id` bigint unsigned DEFAULT NULL COMMENT '创建人ID',
    `update_id` bigint unsigned DEFAULT NULL COMMENT '修改人ID',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` tinyint unsigned DEFAULT '0',
    PRIMARY KEY (`id`),
    KEY `idx_assignee_id` (`assignee_id`),
    KEY `idx_demand_id` (`demand_id`),
    KEY `idx_start_date_end_date` (`start_date`,`end_date`)
    ) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='任务表';

-- work_plan.t_task 的数据：~0 rows (大约)
INSERT INTO `t_task` (`id`, `task_name`, `task_type`, `demand_id`, `assignee_id`, `start_date`, `end_date`, `total_hours`, `status`, `description`, `create_id`, `update_id`, `create_time`, `update_time`, `deleted`) VALUES
    (1, '设计文档编写', '30', 1, 2, '2026-04-06', '2026-04-06', 4.0, 0, '', 1, 2, '2026-04-06 16:01:03', '2026-04-06 23:10:34', 1),
    (2, '设计评审', '30', 1, 2, '2026-04-06', '2026-04-06', 4.0, 0, '', 1, 1, '2026-04-06 16:02:00', '2026-04-06 16:02:00', 0),
    (3, '购买力修改', '10', 1, 2, '2026-04-07', '2026-04-09', 18.0, 0, '', 1, 1, '2026-04-06 16:02:24', '2026-04-06 16:02:24', 0),
    (4, '设计文档编写', '30', 2, 2, '2026-04-07', '2026-04-08', 4.0, 0, '', 1, 1, '2026-04-06 16:03:00', '2026-04-06 16:03:00', 0),
    (5, '设计文档编写', '10', 2, 2, '2026-04-05', '2026-04-05', 4.0, 0, '', 2, 2, '2026-04-06 17:45:44', '2026-04-06 17:45:44', 0);

-- 表 work_plan.t_user 结构
CREATE TABLE IF NOT EXISTS `t_user` (
`id` bigint unsigned NOT NULL AUTO_INCREMENT,
`username` varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '登录账号',
    `password` varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
    `real_name` varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '真实姓名',
    `dept` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '所属部门（字典值）',
    `role` varchar(20) COLLATE utf8mb4_general_ci DEFAULT 'USER' COMMENT '角色（字典值）',
    `status` tinyint unsigned DEFAULT '1' COMMENT '状态: 1启用 0禁用',
    `create_id` bigint unsigned DEFAULT NULL COMMENT '创建人ID',
    `update_id` bigint unsigned DEFAULT NULL COMMENT '修改人ID',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` tinyint unsigned DEFAULT '0',
    PRIMARY KEY (`id`),
    UNIQUE KEY `username` (`username`)
    ) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户表';

-- work_plan.t_user 的数据：~0 rows (大约)
INSERT INTO `t_user` (`id`, `username`, `password`, `real_name`, `dept`, `role`, `status`, `create_id`, `update_id`, `create_time`, `update_time`, `deleted`) VALUES
  (1, 'admin', 'e10adc3949ba59abbe56e057f20f883e', '管理员', '30', 'ADMIN', 1, NULL, 1, '2026-04-06 15:45:54', '2026-04-06 18:52:37', 0),
  (2, 'zhangsan', 'fcea920f7412b5da7be0cf42b8c93759', '张三', '20', 'USER', 1, NULL, 1, '2026-04-06 15:45:54', '2026-04-06 18:53:20', 0),
  (3, 'lisi', 'e10adc3949ba59abbe56e057f20f883e', '李四', '20', 'USER', 1, NULL, 1, '2026-04-06 15:45:54', '2026-04-06 18:53:24', 0),
  (4, 'wangwu', 'e10adc3949ba59abbe56e057f20f883e', '王五', '10', 'USER', 1, NULL, 1, '2026-04-06 15:45:54', '2026-04-06 18:53:27', 0);

-- 表 work_plan.t_user_role 结构
CREATE TABLE IF NOT EXISTS `t_user_role` (
`id` bigint unsigned NOT NULL AUTO_INCREMENT,
`user_id` bigint unsigned NOT NULL COMMENT '用户ID',
`role_id` bigint unsigned NOT NULL COMMENT '角色ID',
`create_time` datetime DEFAULT CURRENT_TIMESTAMP,
`create_id` bigint unsigned DEFAULT NULL COMMENT '创建人ID',
`update_id` bigint unsigned DEFAULT NULL COMMENT '修改人ID',
`update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
`deleted` tinyint unsigned DEFAULT '0',
PRIMARY KEY (`id`),
UNIQUE KEY `uk_user_role` (`user_id`,`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户角色关联表';

-- work_plan.t_user_role 的数据
INSERT INTO `t_user_role` (`id`, `user_id`, `role_id`, `create_time`, `create_id`, `update_id`, `update_time`, `deleted`) VALUES
 (1, 1, 1, '2026-04-06 18:17:23', NULL, NULL, '2026-04-06 21:19:41', 0),
 (2, 2, 2, '2026-04-06 18:17:23', NULL, NULL, '2026-04-06 21:19:41', 0),
 (3, 3, 2, '2026-04-06 18:17:23', NULL, NULL, '2026-04-06 21:19:41', 0),
 (4, 4, 2, '2026-04-06 18:17:23', NULL, NULL, '2026-04-06 21:19:41', 0);

-- 表 work_plan.t_work_log 结构
CREATE TABLE IF NOT EXISTS `t_work_log` (
    `id` bigint unsigned NOT NULL AUTO_INCREMENT,
    `task_id` bigint unsigned NOT NULL COMMENT '关联任务ID',
    `user_id` bigint unsigned NOT NULL COMMENT '录入人ID',
    `log_date` date NOT NULL COMMENT '工作日期',
    `hours` decimal(4,1) unsigned NOT NULL COMMENT '录入工时',
    `remark` varchar(500) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
    `create_id` bigint unsigned DEFAULT NULL COMMENT '创建人ID',
    `update_id` bigint unsigned DEFAULT NULL COMMENT '修改人ID',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` tinyint unsigned DEFAULT '0',
    PRIMARY KEY (`id`),
    KEY `idx_user_date` (`user_id`,`log_date`),
    KEY `idx_task_id` (`task_id`)
    ) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='工时录入表';

-- work_plan.t_work_log 的数据
INSERT INTO `t_work_log` (`id`, `task_id`, `user_id`, `log_date`, `hours`, `remark`, `create_id`, `update_id`, `create_time`, `update_time`, `deleted`) VALUES
 (1, 1, 2, '2026-04-06', 5.0, '', 2, 2, '2026-04-06 17:40:23', '2026-04-06 17:41:05', 1),
 (2, 1, 2, '2026-04-06', 3.0, '', 2, 2, '2026-04-06 17:41:11', '2026-04-06 17:41:11', 0),
 (3, 2, 2, '2026-04-06', 6.0, '', 2, 2, '2026-04-06 17:41:24', '2026-04-06 17:41:24', 0),
 (4, 5, 2, '2026-04-06', 3.0, '', 2, 2, '2026-04-06 17:52:45', '2026-04-06 17:52:45', 0);