-- 创建用户表
CREATE TABLE IF NOT EXISTS user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL UNIQUE COMMENT '用户唯一标识（UUID），不可修改',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码（BCrypt加密）',
    real_name VARCHAR(50) DEFAULT '' COMMENT '真实姓名',
    phone VARCHAR(20) DEFAULT '' COMMENT '手机号',
    email VARCHAR(100) DEFAULT '' COMMENT '邮箱',
    department VARCHAR(100) DEFAULT '' COMMENT '部门',
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '账号状态：PENDING-待审批, APPROVED-已通过, REJECTED-已拒绝, DISABLED-已禁用',
    password_updated_at TIMESTAMP NULL COMMENT '上次修改密码时间',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 插入示例数据（超级管理员，已审批状态）
-- 密码：admin123（BCrypt加密）
INSERT IGNORE INTO user (user_id, username, password, real_name, email, status, password_updated_at) VALUES
('550e8400-e29b-41d4-a716-446655440000', 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '超级管理员', 'admin@example.com', 'APPROVED', NOW());
