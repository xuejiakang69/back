# 配置文件说明

## 配置文件结构

```
src/main/resources/
├── application.yml           # 通用配置（MyBatis）
├── application-local.yml     # 本地开发环境
├── application-test.yml      # 测试环境
└── application-prod.yml      # 生产环境
```

---

## application.yml（通用配置）

```yaml
mybatis:
  mapper-locations: classpath:mapper/*.xml
  type-aliases-package: org.example.entity
  configuration:
    map-underscore-to-camel-case: true
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
```

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

## application-local.yml（本地开发）

- 端口: 8081
- 数据库: `mysql://47.103.116.90:3306/shop_test`
- 用户名: `shop_test`
- 自动执行 schema.sql

---

## application-test.yml（测试环境）

- 端口: 8081
- 数据库: `mysql://47.103.116.90:3306/shop_test`
- 用户名: `shop_test`
- 自动执行 schema.sql

---

## application-prod.yml（生产环境）

- 端口: 8080
- 数据库: `mysql://47.103.116.90:3306/shop`
- 用户名: `shop`
- 自动执行 schema.sql

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
