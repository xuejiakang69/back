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
│   ├── controller/
│   │   └── UserController.java   # REST 控制器
│   ├── service/
│   │   └── UserService.java      # 业务逻辑层
│   ├── mapper/
│   │   └── UserMapper.java       # MyBatis Mapper 接口
│   └── entity/
│       └── User.java             # 用户实体类
├── src/main/resources/
│   ├── application*.yml          # 配置文件
│   ├── mapper/UserMapper.xml     # SQL 映射文件
│   └── schema.sql                # 数据库初始化脚本
├── docs/                         # 📚 详细文档目录
│   ├── api/                      # API 接口文档（按模块）
│   │   └── User.md              # 用户模块 API
│   ├── modules/                  # 模块说明文档（按模块）
│   │   └── User.md              # 用户模块说明
│   ├── architecture.md           # 架构详细解析
│   ├── database.md               # 数据库文档
│   ├── configuration.md          # 配置文件说明
│   ├── deployment.md             # 部署文档
│   ├── known-issues.md           # 已知问题与优化建议
│   └── rules.md                  # 文档同步规则
├── .github/workflows/
│   └── deploy.yml                # GitHub Actions 自动部署
├── VERSION                       # 版本号文件
└── pom.xml                       # Maven 配置
```

---

## 快速导航

### API 文档（按模块）

| 模块 | 文档 |
|------|------|
| 用户管理 | [User API](docs/api/User.md) |

### 模块说明（按模块）

| 模块 | 文档 |
|------|------|
| 用户管理 | [User 模块](docs/modules/User.md) |

### 通用文档

| 文档 | 说明 |
|------|------|
| [架构详细解析](docs/architecture.md) | Application、Entity、Mapper、Service、Controller 详解 |
| [数据库文档](docs/database.md) | 表结构、数据模型、schema.sql |
| [配置文件说明](docs/configuration.md) | 各环境配置对照、启动命令 |
| [部署文档](docs/deployment.md) | 自动部署流程、Git 分支策略、版本管理 |
| [已知问题](docs/known-issues.md) | 已知问题、风险等级、优化建议 |
| [文档同步规则](docs/rules.md) | 代码与文档同步的核心规则和模板 |

---

## 核心 API

| HTTP 方法 | 路径 | 说明 |
|-----------|------|------|
| GET | `/api/users/{id}` | 获取单个用户 |
| GET | `/api/users` | 获取所有用户 |
| POST | `/api/users` | 创建用户 |
| PUT | `/api/users/{id}` | 更新用户 |
| DELETE | `/api/users/{id}` | 删除用户 |

---

## 环境配置

| 配置项 | local | test | prod |
|--------|-------|------|------|
| 端口 | 8088 | 8088 | 8080 |
| 数据库 | shop_test | shop_test | shop |

---

## 文档同步规则

> **核心原则**：代码与文档必须实时同步。

详细规则请查看 [文档同步规则](docs/rules.md)

---

## 最后更新

- 更新时间: 2026-08-20
- 更新内容: 模块化拆分文档，创建 docs/ 目录存放详细文档
