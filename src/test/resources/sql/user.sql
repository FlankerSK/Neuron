-- 创建数据库
DROP DATABASE IF EXISTS neuron;
CREATE DATABASE neuron DEFAULT CHARACTER SET utf8mb4;

-- 使用数据数据
USE neuron;

-- 创建表[用户表]
DROP TABLE if EXISTS users;
CREATE TABLE users(
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(10) NOT NULL CHECK (role in ('ROLE_ADMIN', 'ROLE_USER')),
    account_non_locked NUMERIC(1) NOT NULL DEFAULT 1,
    enabled NUMERIC(1) NOT NULL DEFAULT 1
) DEFAULT CHARACTER SET 'utf8mb4';

-- 创建表[安全绑定信息表]
DROP TABLE if EXISTS security_info;
CREATE TABLE security_info(
    username VARCHAR(100) PRIMARY KEY,
    email VARCHAR(100) NOT NULL DEFAULT '',
    phone VARCHAR(15) NOT NULL DEFAULT ''
) DEFAULT CHARACTER SET 'utf8mb4';

-- 添加用户
INSERT INTO users (username, password, role)
VALUES
    ('test1', '$2a$10$9y3amVDM9YPlIvOnLWEujuTTtFLMr4JIRnP/WdA3M3sYDruaBg3Qe', 'ROLE_ADMIN'),
    ('test2', '$2a$10$wphfu8lbfN5ka9JsHyCdtOucaxOPia6nnM0aewC883OTRk7oMuJn2', 'ROLE_USER');

INSERT INTO security_info (username, email)
VALUES
    ('test1', 'communicationGCY@outlook.com'),
    ('test2', 'communicationGCY@outlook.com')
