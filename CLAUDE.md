# CLAUDE.md - shop-api 项目文档

> 本文档用于 Claude Code 自动理解项目结构，每次调用时自动补全最新内容。

---

## 项目概览

| 项目 | 说明 |
|------|------|
| 名称 | shop-api |
| 框架 | Spring Boot 3.2.0 |
| ORM | MyBatis 3.0.3 |
| 数据库 | MySQL 8.x |
| JDK | 21 |
| 版本 | 1.0.3 |

---

## 目录结构

```
back/
├── src/main/java/org/example/
│   ├── Application.java          # 启动入口
│   ├── Main.java                 # IDE 模板（可删除）
│   ├── controller/
│   │   └── UserController.java   # REST 控制器
│   ├── service/
│   │   └── UserService.java      # 业务逻辑层
│   ├── mapper/
│   │   └── UserMapper.java       # MyBatis Mapper 接口
│   └── entity/
│       └── User.java             # 用户实体类
├── src/main/resources/
│   ├── application.yml           # 通用配置（MyBatis）
│   ├── application-local.yml     # 本地开发环境
│   ├── application-test.yml      # 测试环境
│   ├── application-prod.yml      # 生产环境
│   ├── mapper/
│   │   └── UserMapper.xml        # SQL 映射文件
│   └── schema.sql                # 数据库初始化脚本
├── .github/workflows/
│   └── deploy.yml                # GitHub Actions 自动部署
├── VERSION                       # 版本号文件
├── pom.xml                       # Maven 配置
└── CLAUDE.md                     # 本文档
```

---

## 模块详细解析

### 1. Application.java - 启动入口

**路径**: `src/main/java/org/example/Application.java`

**功能**:
- Spring Boot 应用启动类
- `@MapperScan("org.example.mapper")` 扫描 MyBatis Mapper 接口
- 启动时根据 profile 加载对应环境配置

**关键注解**:
```java
@SpringBootApplication
@MapperScan("org.example.mapper")
```

---

### 2. User.java - 实体层

**路径**: `src/main/java/org/example/entity/User.java`

**功能**:
- 用户数据模型，映射数据库 `user` 表
- 实现 `Serializable` 接口支持序列化

**字段定义**:
| 字段 | 类型 | 说明 |
|------|------|------|
| id | Long | 主键，自增 |
| username | String | 用户名，唯一 |
| email | String | 邮箱 |
| password | String | 密码 |

**构造方法**:
- `User()` - 无参构造
- `User(String username, String email, String password)` - 全参构造

**注意**: 数据库表有 `created_at` 和 `updated_at` 字段，但实体类未映射。

---

### 3. UserMapper.java - 数据访问层

**路径**: `src/main/java/org/example/mapper/UserMapper.java`

**功能**:
- MyBatis Mapper 接口，定义数据库操作方法
- 对应 XML 文件: `resources/mapper/UserMapper.xml`

**方法列表**:
| 方法 | 参数 | 返回值 | 说明 |
|------|------|--------|------|
| `selectById` | Long id | User | 按 ID 查询用户 |
| `selectByUsername` | String username | User | 按用户名查询 |
| `selectAll` | 无 | List\<User\> | 查询所有用户 |
| `insert` | User user | int | 插入用户（返回影响行数） |
| `update` | User user | int | 更新用户信息 |
| `deleteById` | Long id | int | 删除用户 |

---

### 4. UserMapper.xml - SQL 映射

**路径**: `src/main/resources/mapper/UserMapper.xml`

**功能**:
- 定义具体的 SQL 语句
- 命名空间: `org.example.mapper.UserMapper`
- 结果映射: `BaseResultMap`（id, username, email, password）
- SQL 片段: `Base_Column_List`

**表名**: `user`

**操作说明**:
- `insert` 使用 `useGeneratedKeys="true" keyProperty="id"` 自动回填主键
- `update` 只更新非空字段
- `selectAll` 返回所有用户（无分页）

---

### 5. UserService.java - 业务逻辑层

**路径**: `src/main/java/org/example/service/UserService.java`

**功能**:
- 业务逻辑处理，当前为简单 CRUD 代理
- 直接调用 Mapper 方法，无额外业务逻辑

**方法列表**:
| 方法 | 说明 |
|------|------|
| `getUserById(Long id)` | 查询单个用户 |
| `getUserByUsername(String username)` | 按用户名查询 |
| `getAllUsers()` | 获取所有用户 |
| `createUser(User user)` | 创建用户 |
| `updateUser(User user)` | 更新用户 |
| `deleteUser(Long id)` | 删除用户（返回 boolean） |

**注意**: 
- 没有事务管理（`@Transactional`）
- 没有参数校验
- 没有业务逻辑处理

---

### 6. UserController.java - 控制层

**路径**: `src/main/java/org/example/controller/UserController.java`

**功能**:
- RESTful API 控制器
- 基础路径: `/api/users`

**API 端点**:
| HTTP 方法 | 路径 | 参数 | 说明 |
|-----------|------|------|------|
| GET | `/api/users/{id}` | @PathVariable id | 获取单个用户 |
| GET | `/api/users` | 无 | 获取所有用户 |
| POST | `/api/users` | @RequestBody user | 创建用户 |
| PUT | `/api/users/{id}` | @PathVariable id, @RequestBody user | 更新用户 |
| DELETE | `/api/users/{id}` | @PathVariable id | 删除用户 |

**请求/响应示例**:
```json
// POST /api/users
{
  "username": "zhangsan",
  "email": "zhangsan@example.com",
  "password": "123456"
}

// GET /api/users/1
{
  "id": 1,
  "username": "zhangsan",
  "email": "zhangsan@example.com",
  "password": "123456"
}
```

**注意**: 
- 没有统一异常处理
- 没有参数校验（`@Valid`）
- 没有分页支持

---

### 7. schema.sql - 数据库初始化

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

### 8. 配置文件

#### application.yml（通用配置）
```yaml
mybatis:
  mapper-locations: classpath:mapper/*.xml
  type-aliases-package: org.example.entity
  configuration:
    map-underscore-to-camel-case: true
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
```

#### application-local.yml（本地开发）
- 端口: 8081
- 数据库: `mysql://47.103.116.90:3306/shop_test`
- 用户名: `shop_test`
- 自动执行 schema.sql

#### application-test.yml（测试环境）
- 端口: 8081
- 数据库: `mysql://47.103.116.90:3306/shop_test`
- 用户名: `shop_test`
- 自动执行 schema.sql

#### application-prod.yml（生产环境）
- 端口: 8080
- 数据库: `mysql://47.103.116.90:3306/shop`
- 用户名: `shop`
- 自动执行 schema.sql

---

### 9. deploy.yml - 自动部署

**路径**: `.github/workflows/deploy.yml`

**触发条件**:
- Push 到 `main` 分支 → 生产环境
- Push 到 `test` 分支 → 测试环境
- 手动触发 (workflow_dispatch)

**部署流程**:

| 步骤 | main 分支 | test 分支 |
|------|-----------|-----------|
| 环境判断 | production | test |
| 版本检查 | ✅ 检查版本是否存在 | ❌ |
| 上传位置 | `/www/wwwroot/java/java-prod/releases/v{版本}/` | `/www/wwwroot/java/java-test/` |
| 重启服务 | ✅ 自动重启 | ❌ |
| 清理旧版本 | ✅ 保留最近 4 个版本 | ❌ |

**服务器目录结构**:
```
/www/wwwroot/java/
├── java-prod/
│   ├── current/              # 当前运行版本
│   ├── releases/v1.0.x/     # 历史版本
│   └── logs/java.log         # 应用日志
└── java-test/
    └── app.jar               # 测试环境
```

**启动命令**:
```bash
JAVA_HOME=/www/server/java/jdk-21.0.2
nohup $JAVA_HOME/bin/java -jar app.jar --spring.profiles.active=$ENV > $APP/logs/java.log 2>&1 &
```

---

### 10. pom.xml - Maven 依赖

| 依赖 | 版本 | 用途 |
|------|------|------|
| spring-boot-starter-web | 3.2.0 | Web 框架，内嵌 Tomcat |
| mybatis-spring-boot-starter | 3.0.3 | MyBatis ORM 集成 |
| mysql-connector-j | runtime | MySQL JDBC 驱动 |
| spring-boot-starter-test | test | 测试框架 |

**构建插件**:
- `spring-boot-maven-plugin` - 打包可执行 jar

---

## 环境配置对照

| 配置项 | local | test | prod |
|--------|-------|------|------|
| 端口 | 8081 | 8081 | 8080 |
| 数据库地址 | 47.103.116.90 | 47.103.116.90 | 47.103.116.90 |
| 数据库名 | shop_test | shop_test | shop |
| 用户名 | shop_test | shop_test | shop |
| schema 初始化 | always | always | always |

---

## 启动命令

```bash
# 本地开发
mvn spring-boot:run -Dspring-boot.run.profiles=local

# 测试环境
mvn spring-boot:run -Dspring-boot.run.profiles=test

# 生产环境
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

---

## 开发注意事项

### 已知问题
1. **安全风险**: 数据库密码明文写在配置文件中
2. **schema.sql 每次启动执行**: 生产环境应改为 `never`
3. **实体类缺少时间字段映射**: `created_at` 和 `updated_at` 未在 User.java 中定义
4. **无输入校验**: Controller 层没有 `@Valid`
5. **无异常处理**: 没有全局 `@ControllerAdvice`
6. **无分页**: `selectAll` 查询所有用户

### 优化建议
1. 添加 `@Valid` 参数校验
2. 添加全局异常处理
3. 实现分页查询
4. 使用环境变量存储敏感信息
5. 删除无用的 `Main.java`

---

## Git 分支策略

| 分支 | 用途 | 部署目标 |
|------|------|----------|
| main | 生产分支 | 正式环境 |
| test | 测试分支 | 测试环境 |
| 其他 | 开发分支 | 本地 |

---

## 版本管理

- 版本号存储在 `VERSION` 文件中
- 每次部署前需更新版本号
- 生产环境会检查版本是否已存在
- 自动保留最近 4 个历史版本

---

## 数据库表结构

### user 表
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

## 最后更新

- 更新时间: 2026-07-15
- 更新内容: 初始版本，包含完整的项目结构和模块解析
