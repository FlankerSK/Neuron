CREATE USER 'root'@'%' IDENTIFIED BY 'glgjss17yfhbqz';
GRANT ALL PRIVILEGES ON *.* TO 'root'@'%';
FLUSH PRIVILEGES;
-- 创建数据库
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

-- 创建表[用户信息表]
DROP TABLE if EXISTS user_info;
CREATE TABLE user_info(
                          username VARCHAR(100) PRIMARY KEY,
                          name VARCHAR(100) NOT NULL DEFAULT '', -- 真实姓名
                          id_number VARCHAR(18) NOT NULL DEFAULT '', -- 身份证号
                          birthday DATETIME, -- 出生日期
                          gender VARCHAR(18) NOT NULL DEFAULT '', -- 性别
                          address VARCHAR(255) NOT NULL DEFAULT '', -- 住址
                          phone VARCHAR(20) NOT NULL DEFAULT '', -- 手机号
                          email VARCHAR(255)  NOT NULL DEFAULT '', -- 邮箱
                          education_level VARCHAR(50) NOT NULL DEFAULT '', -- 学历
                          graduation_institution VARCHAR(255) NOT NULL DEFAULT '', -- 毕业院校
                          graduation_major VARCHAR(255) NOT NULL DEFAULT '', -- 毕业专业
                          hire_date DATETIME, -- 入职时间
                          department VARCHAR(255) NOT NULL DEFAULT '', -- 就职部门
                          position VARCHAR(255) NOT NULL DEFAULT '' -- 职位
) DEFAULT CHARACTER SET 'utf8mb4';