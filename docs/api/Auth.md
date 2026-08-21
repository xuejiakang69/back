# Auth API

## 概述

认证模块 API，提供登录、修改密码、账号申请等功能。

**基础路径**: `/api/auth`

---

## 接口列表

### 用户登录

- **HTTP 方法**: `POST`
- **路径**: `/api/auth/login`
- **描述**: 用户名 + 密码登录

#### 请求参数

| 参数名 | 位置 | 类型 | 必填 | 说明 |
|--------|------|------|------|------|
| username | body | String | 是 | 用户名 |
| password | body | String | 是 | 密码 |

#### 请求示例

```json
{
  "username": "admin",
  "password": "admin123"
}
```

#### 响应结果

| 字段 | 类型 | 说明 |
|------|------|------|
| code | Integer | 状态码，200-成功 |
| message | String | 提示信息 |
| data.token | String | JWT Token |
| data.userInfo | Object | 用户信息 |
| data.userInfo.userId | String | 用户唯一标识（UUID） |
| data.userInfo.username | String | 用户名 |
| data.userInfo.realName | String | 真实姓名 |
| data.needChangePassword | Boolean | 是否需要修改密码 |

#### 响应示例

```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "userInfo": {
      "userId": "550e8400-e29b-41d4-a716-446655440000",
      "username": "admin",
      "realName": "超级管理员",
      "needChangePassword": false
    },
    "needChangePassword": false
  }
}
```

#### 错误码

| 错误码 | 说明 |
|--------|------|
| 400 | 用户名或密码错误 |
| 400 | 账号待审批 |
| 400 | 账号审批已被拒绝 |
| 400 | 账号已被禁用 |

---

### 修改密码

- **HTTP 方法**: `POST`
- **路径**: `/api/auth/change-password`
- **描述**: 修改当前用户密码

#### 请求参数

| 参数名 | 位置 | 类型 | 必填 | 说明 |
|--------|------|------|------|------|
| Authorization | header | String | 是 | Bearer Token |
| oldPassword | body | String | 是 | 旧密码 |
| newPassword | body | String | 是 | 新密码（6-20位） |

#### 请求示例

```json
{
  "oldPassword": "oldPass123",
  "newPassword": "newPass456"
}
```

#### 响应示例

```json
{
  "code": 200,
  "message": "密码修改成功",
  "data": null
}
```

#### 错误码

| 错误码 | 说明 |
|--------|------|
| 400 | 旧密码错误 |
| 400 | 新密码不能与旧密码相同 |
| 401 | 未授权 |

---

### 账号申请

- **HTTP 方法**: `POST`
- **路径**: `/api/auth/apply`
- **描述**: 申请创建账号，需管理员审批

#### 请求参数

| 参数名 | 位置 | 类型 | 必填 | 说明 |
|--------|------|------|------|------|
| username | body | String | 是 | 用户名（唯一） |
| realName | body | String | 是 | 真实姓名 |
| phone | body | String | 否 | 手机号 |
| email | body | String | 否 | 邮箱 |
| department | body | String | 否 | 部门 |

#### 请求示例

```json
{
  "username": "zhangsan",
  "realName": "张三",
  "phone": "13800138000",
  "email": "zhangsan@example.com",
  "department": "技术部"
}
```

#### 响应示例

```json
{
  "code": 200,
  "message": "申请已提交，请等待管理员审批",
  "data": null
}
```

#### 错误码

| 错误码 | 说明 |
|--------|------|
| 400 | 用户名已存在 |

---

### 获取当前用户信息

- **HTTP 方法**: `GET`
- **路径**: `/api/auth/userinfo`
- **描述**: 获取当前登录用户信息

#### 请求参数

| 参数名 | 位置 | 类型 | 必填 | 说明 |
|--------|------|------|------|------|
| Authorization | header | String | 是 | Bearer Token |

#### 响应示例

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "userId": "550e8400-e29b-41d4-a716-446655440000",
    "username": "admin",
    "realName": "超级管理员",
    "phone": "",
    "email": "admin@example.com",
    "department": "",
    "status": "APPROVED",
    "needChangePassword": false
  }
}
```

---

*文档创建时间：2026-08-21*
