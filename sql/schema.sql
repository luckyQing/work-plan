-- 数据库初始化脚本
CREATE DATABASE IF NOT EXISTS work_plan DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_general_ci;
USE work_plan;

-- 部门表
CREATE TABLE IF NOT EXISTS sys_dept (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    dept_name VARCHAR(100) NOT NULL COMMENT '部门名称',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0
) COMMENT '部门表';

-- 用户表
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '登录账号',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    real_name VARCHAR(50) NOT NULL COMMENT '真实姓名',
    dept_id BIGINT COMMENT '部门ID',
    role VARCHAR(20) DEFAULT 'USER' COMMENT '角色: ADMIN/USER',
    status TINYINT DEFAULT 1 COMMENT '状态: 1启用 0禁用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0
) COMMENT '用户表';

-- 项目表
CREATE TABLE IF NOT EXISTS project (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    project_name VARCHAR(200) NOT NULL COMMENT '项目名称',
    status TINYINT DEFAULT 1 COMMENT '状态: 1进行中 0已结束',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0
) COMMENT '项目表';

-- 需求表
CREATE TABLE IF NOT EXISTS demand (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    demand_name VARCHAR(500) NOT NULL COMMENT '需求名称',
    demand_type VARCHAR(50) DEFAULT '需求' COMMENT '需求类型',
    project_id BIGINT COMMENT '所属项目ID',
    description TEXT COMMENT '需求描述',
    status TINYINT DEFAULT 0 COMMENT '状态: 0待开始 1进行中 2已完成',
    priority TINYINT DEFAULT 1 COMMENT '优先级: 0低 1中 2高',
    creator_id BIGINT COMMENT '创建人ID',
    start_date DATE COMMENT '计划开始日期',
    end_date DATE COMMENT '计划结束日期',
    total_hours DECIMAL(6,1) DEFAULT 0 COMMENT '预估总工时',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0
) COMMENT '需求表';

-- 任务表（个人任务/子任务）
CREATE TABLE IF NOT EXISTS task (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    task_name VARCHAR(500) NOT NULL COMMENT '任务名称',
    task_type VARCHAR(50) DEFAULT '开发' COMMENT '任务类型: 开发/测试/设计/其他',
    demand_id BIGINT COMMENT '所属需求ID（可为空，表示独立任务）',
    assignee_id BIGINT NOT NULL COMMENT '负责人ID',
    start_date DATE NOT NULL COMMENT '开始日期',
    end_date DATE NOT NULL COMMENT '结束日期',
    total_hours DECIMAL(6,1) DEFAULT 0 COMMENT '预估工时',
    status TINYINT DEFAULT 0 COMMENT '状态: 0待开始 1进行中 2已完成',
    description TEXT COMMENT '任务描述',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0
) COMMENT '任务表';

-- 初始数据
INSERT INTO sys_dept (dept_name) VALUES ('前端开发部'), ('后端开发部'), ('测试部');
INSERT INTO sys_user (username, password, real_name, dept_id, role) VALUES
('admin', 'e10adc3949ba59abbe56e057f20f883e', '管理员', 1, 'ADMIN'),
('zhangsan', 'e10adc3949ba59abbe56e057f20f883e', '张三', 1, 'USER'),
('lisi', 'e10adc3949ba59abbe56e057f20f883e', '李四', 2, 'USER'),
('wangwu', 'e10adc3949ba59abbe56e057f20f883e', '王五', 3, 'USER');
INSERT INTO project (project_name) VALUES ('花旗项目'), ('微软件项目');
