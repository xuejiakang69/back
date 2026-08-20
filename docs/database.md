# 数据库文档

## schema.sql - 数据库初始化

**路径**: `src/main/resources/schema.sql`

**功能**:
- 自动创建 `user` 表（如果不存在）
- 插入示例数据（admin 和 test 用户）

**表结构**:
```sql
CREATE TABLE IF NOT EXISTS user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

**示例数据**:
```sql
INSERT IGNORE INTO user (username, email, password) VALUES
('admin', 'admin@example.com', 'password123'),
('test', 'test@example.com', 'test123');
```

---

## user 表结构

```sql
+------------+--------------+------+-----+-------------------+----------------+
| Field      | Type         | Null | Key | Default           | Extra          |
+------------+--------------+------+-----+-------------------+----------------+
| id         | bigint       | NO   | PRI | NULL              | auto_increment |
| username   | varchar(50)  | NO   | UNI | NULL              |                |
| email      | varchar(100) | NO   |     | NULL              |                |
| password   | varchar(100) | NO   |     | NULL              |                |
| created_at | timestamp    | YES  |     | CURRENT_TIMESTAMP |                |
| updated_at | timestamp    | YES  |     | CURRENT_TIMESTAMP |                |
+------------+--------------+------+-----+-------------------+----------------+
```

---

## 数据模型

### User 实体类

**路径**: `src/main/java/org/example/entity/User.java`

| 字段 | Java 类型 | 数据库类型 | 说明 |
|------|-----------|-----------|------|
| id | Long | BIGINT | 主键，自增 |
| username | String | VARCHAR(50) | 用户名，唯一 |
| email | String | VARCHAR(100) | 邮箱 |
| password | String | VARCHAR(100) | 密码 |

**注意**: 数据库有 `created_at` 和 `updated_at` 字段，但实体类未映射。
