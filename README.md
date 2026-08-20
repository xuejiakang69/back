# shop-api

Spring Boot + MyBatis 用户管理 API 服务

## 技术栈

- JDK 21
- Spring Boot 3.2.0
- MyBatis 3.0.3
- MySQL 8.x

## 启动项目

### 前置条件

- JDK 21
- Maven

### 启动命令

```bash
# 本地开发环境（端口 8081）
mvn spring-boot:run -Dspring-boot.run.profiles=local

# 或打包后运行
mvn clean package -DskipTests
java -jar target/shop-api-1.0-SNAPSHOT.jar --spring.profiles.active=local
```

### 环境配置

| 环境 | 端口 | 启动参数 |
|------|------|----------|
| local | 8081 | `--spring.profiles.active=local` |
| test | 8081 | `--spring.profiles.active=test` |
| prod | 8080 | `--spring.profiles.active=prod` |

## API 端点

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/users` | 获取所有用户 |
| GET | `/api/users/{id}` | 获取单个用户 |
| POST | `/api/users` | 创建用户 |
| PUT | `/api/users/{id}` | 更新用户 |
| DELETE | `/api/users/{id}` | 删除用户 |
