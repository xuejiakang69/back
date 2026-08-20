# 部署文档

## deploy.yml - 自动部署

**路径**: `.github/workflows/deploy.yml`

---

## 触发条件

- Push 到 `main` 分支 → 生产环境
- Push 到 `test` 分支 → 测试环境
- 手动触发 (workflow_dispatch)

---

## 部署流程

| 步骤 | main 分支 | test 分支 |
|------|-----------|-----------|
| 环境判断 | production | test |
| 版本检查 | ✅ 检查版本是否存在 | ❌ |
| 上传位置 | `/www/wwwroot/java/java-prod/releases/v{版本}/` | `/www/wwwroot/java/java-test/` |
| 重启服务 | ✅ 自动重启 | ❌ |
| 清理旧版本 | ✅ 保留最近 4 个版本 | ❌ |

---

## 服务器目录结构

```
/www/wwwroot/java/
├── java-prod/
│   ├── current/              # 当前运行版本
│   ├── releases/v1.0.x/     # 历史版本
│   └── logs/java.log         # 应用日志
└── java-test/
    └── app.jar               # 测试环境
```

---

## 启动命令

```bash
JAVA_HOME=/www/server/java/jdk-21.0.2
nohup $JAVA_HOME/bin/java -jar app.jar --spring.profiles.active=$ENV > $APP/logs/java.log 2>&1 &
```

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

## Maven 依赖

| 依赖 | 版本 | 用途 |
|------|------|------|
| spring-boot-starter-web | 3.2.0 | Web 框架，内嵌 Tomcat |
| mybatis-spring-boot-starter | 3.0.3 | MyBatis ORM 集成 |
| mysql-connector-j | runtime | MySQL JDBC 驱动 |
| spring-boot-starter-test | test | 测试框架 |

**构建插件**:
- `spring-boot-maven-plugin` - 打包可执行 jar
