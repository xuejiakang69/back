# Spring Boot + MyBatis MySQL 项目

## 项目结构

```
src/main/java/org/example/
├── Application.java          # Spring Boot 启动类
├── entity/
│   └── User.java            # 用户实体类
├── mapper/
│   └── UserMapper.java      # MyBatis Mapper 接口
├── service/
│   └── UserService.java     # 用户服务类
└── controller/
    └── UserController.java  # 用户控制器

src/main/resources/
├── application.yml          # 配置文件
├── mapper/
│   └── UserMapper.xml       # MyBatis 映射文件
└── schema.sql              # 数据库初始化脚本
```

## 快速开始

### 1. 创建数据库

首先，确保 MySQL 正在运行，然后执行数据库初始化脚本：

```bash
mysql -u root -p < src/main/resources/schema.sql
```

或者手动在 MySQL 中执行 `schema.sql` 文件。

### 2. 修改数据库配置

编辑 `src/main/resources/application.yml`，修改数据库连接信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/test_db?useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
    username: root      # 你的 MySQL 用户名
    password: root      # 你的 MySQL 密码
```

### 3. 运行项目

```bash
# 使用 Maven
mvn spring-boot:run

# 或者打包后运行
mvn clean package -DskipTests
java -jar target/untitled-1.0-SNAPSHOT.jar
```

### 4. 测试 API

项目运行后，可以通过以下 API 端点进行测试：

```bash
# 获取所有用户
curl http://localhost:8080/api/users

# 根据 ID 获取用户
curl http://localhost:8080/api/users/1

# 创建用户
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"username":"newuser","email":"new@example.com","password":"pass123"}'

# 更新用户
curl -X PUT http://localhost:8080/api/users/1 \
  -H "Content-Type: application/json" \
  -d '{"username":"updated","email":"updated@example.com","password":"newpass"}'

# 删除用户
curl -X DELETE http://localhost:8080/api/users/1
```

## 功能特性

- ✅ Spring Boot 3.2.0
- ✅ MyBatis 3.0.3
- ✅ MySQL 数据库连接
- ✅ 完整的 CRUD 操作示例
- ✅ RESTful API 设计
- ✅ 驼峰命名自动转换
- ✅ SQL 日志输出

## 注意事项

1. 确保 MySQL 版本 5.7+ 或 8.0+
2. 默认数据库用户名和密码都是 `root`，请根据实际情况修改
3. 项目使用 Java 21，需要确保 JDK 版本匹配

## 常见问题

### 连接失败

检查：
- MySQL 服务是否启动
- 数据库用户名和密码是否正确
- 数据库 `test_db` 是否存在

### 端口冲突

如果 8080 端口被占用，在 `application.yml` 中修改：

```yaml
server:
  port: 8081
```

## 扩展

你可以基于这个示例继续扩展：

1. 添加更多实体和表
2. 实现分页查询
3. 添加参数校验
4. 集成 Swagger API 文档
5. 添加单元测试
6. 实现事务管理
