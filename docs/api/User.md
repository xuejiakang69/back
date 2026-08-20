# User API

## 概述

用户管理模块 API，提供用户的增删改查功能。

**基础路径**: `/api/users`

---

## 接口列表

### 获取用户详情

- **HTTP 方法**: `GET`
- **路径**: `/api/users/{id}`
- **描述**: 根据用户 ID 获取用户详细信息

#### 请求参数

| 参数名 | 位置 | 类型 | 必填 | 说明 |
|--------|------|------|------|------|
| id | path | Long | 是 | 用户 ID |

#### 请求示例

```
GET /api/users/1
```

#### 响应结果

| 字段 | 类型 | 说明 |
|------|------|------|
| id | Long | 用户 ID |
| username | String | 用户名 |
| email | String | 邮箱 |
| password | String | 密码 |

#### 响应示例

```json
{
  "id": 1,
  "username": "admin",
  "email": "admin@example.com",
  "password": "password123"
}
```

#### 错误码

| 错误码 | 说明 |
|--------|------|
| 404 | 用户不存在 |
| 500 | 服务器内部错误 |

#### 前置条件

- 无

#### 权限要求

- 无需认证

#### 关联接口

- [获取所有用户](#获取所有用户)
- [创建用户](#创建用户)

---

### 获取所有用户

- **HTTP 方法**: `GET`
- **路径**: `/api/users`
- **描述**: 获取所有用户列表

#### 请求参数

无

#### 请求示例

```
GET /api/users
```

#### 响应结果

| 字段 | 类型 | 说明 |
|------|------|------|
| [] | Array | 用户数组 |
| [].id | Long | 用户 ID |
| [].username | String | 用户名 |
| [].email | String | 邮箱 |
| [].password | String | 密码 |

#### 响应示例

```json
[
  {
    "id": 1,
    "username": "admin",
    "email": "admin@example.com",
    "password": "password123"
  },
  {
    "id": 2,
    "username": "test",
    "email": "test@example.com",
    "password": "test123"
  }
]
```

#### 错误码

| 错误码 | 说明 |
|--------|------|
| 500 | 服务器内部错误 |

#### 前置条件

- 无

#### 权限要求

- 无需认证

#### 关联接口

- [获取用户详情](#获取用户详情)

---

### 创建用户

- **HTTP 方法**: `POST`
- **路径**: `/api/users`
- **描述**: 创建新用户

#### 请求参数

| 参数名 | 位置 | 类型 | 必填 | 说明 |
|--------|------|------|------|------|
| username | body | String | 是 | 用户名（唯一） |
| email | body | String | 是 | 邮箱 |
| password | body | String | 是 | 密码 |

#### 请求示例

```json
{
  "username": "zhangsan",
  "email": "zhangsan@example.com",
  "password": "123456"
}
```

#### 响应结果

| 字段 | 类型 | 说明 |
|------|------|------|
| id | Long | 用户 ID |
| username | String | 用户名 |
| email | String | 邮箱 |
| password | String | 密码 |

#### 响应示例

```json
{
  "id": 3,
  "username": "zhangsan",
  "email": "zhangsan@example.com",
  "password": "123456"
}
```

#### 错误码

| 错误码 | 说明 |
|--------|------|
| 400 | 请求参数错误 |
| 409 | 用户名已存在 |
| 500 | 服务器内部错误 |

#### 前置条件

- 无

#### 权限要求

- 无需认证

#### 关联接口

- [获取用户详情](#获取用户详情)
- [更新用户](#更新用户)

---

### 更新用户

- **HTTP 方法**: `PUT`
- **路径**: `/api/users/{id}`
- **描述**: 更新用户信息

#### 请求参数

| 参数名 | 位置 | 类型 | 必填 | 说明 |
|--------|------|------|------|------|
| id | path | Long | 是 | 用户 ID |
| username | body | String | 否 | 用户名 |
| email | body | String | 否 | 邮箱 |
| password | body | String | 否 | 密码 |

#### 请求示例

```json
{
  "email": "newemail@example.com"
}
```

#### 响应结果

| 字段 | 类型 | 说明 |
|------|------|------|
| id | Long | 用户 ID |
| username | String | 用户名 |
| email | String | 邮箱 |
| password | String | 密码 |

#### 响应示例

```json
{
  "id": 1,
  "username": "admin",
  "email": "newemail@example.com",
  "password": "password123"
}
```

#### 错误码

| 错误码 | 说明 |
|--------|------|
| 400 | 请求参数错误 |
| 404 | 用户不存在 |
| 500 | 服务器内部错误 |

#### 前置条件

- 用户存在

#### 权限要求

- 无需认证

#### 关联接口

- [获取用户详情](#获取用户详情)
- [删除用户](#删除用户)

---

### 删除用户

- **HTTP 方法**: `DELETE`
- **路径**: `/api/users/{id}`
- **描述**: 删除指定用户

#### 请求参数

| 参数名 | 位置 | 类型 | 必填 | 说明 |
|--------|------|------|------|------|
| id | path | Long | 是 | 用户 ID |

#### 请求示例

```
DELETE /api/users/1
```

#### 响应结果

| 字段 | 类型 | 说明 |
|------|------|------|
| - | Boolean | 删除成功返回 true |

#### 响应示例

```json
true
```

#### 错误码

| 错误码 | 说明 |
|--------|------|
| 404 | 用户不存在 |
| 500 | 服务器内部错误 |

#### 前置条件

- 用户存在

#### 权限要求

- 无需认证

#### 关联接口

- [获取用户详情](#获取用户详情)
- [创建用户](#创建用户)
