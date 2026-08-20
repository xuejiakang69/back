# 架构详细解析

## Application.java - 启动入口

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

## User.java - 实体层

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

## UserMapper.java - 数据访问层

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

## UserMapper.xml - SQL 映射

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

## UserService.java - 业务逻辑层

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

## UserController.java - 控制层

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
