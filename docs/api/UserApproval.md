# 用户审批 API

## 概述

用户审批模块 API，提供待审批用户列表和审批功能。

**基础路径**: `/api/users`

---

## 接口列表

### 获取待审批用户列表

- **HTTP 方法**: `GET`
- **路径**: `/api/users/pending`
- **描述**: 获取所有待审批用户

#### 请求参数

| 参数名 | 位置 | 类型 | 必填 | 说明 |
|--------|------|------|------|------|
| Authorization | header | String | 是 | Bearer Token |

#### 响应示例

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 2,
      "userId": "660e8400-e29b-41d4-a716-446655440001",
      "username": "zhangsan",
      "realName": "张三",
      "phone": "13800138000",
      "email": "zhangsan@example.com",
      "department": "技术部",
      "status": "PENDING",
      "createdAt": "2026-08-21T10:00:00"
    }
  ]
}
```

---

### 审批账号

- **HTTP 方法**: `POST`
- **路径**: `/api/users/approve/{userId}`
- **描述**: 审批用户账号申请

#### 请求参数

| 参数名 | 位置 | 类型 | 必填 | 说明 |
|--------|------|------|------|------|
| userId | path | String | 是 | 用户唯一标识 |
| Authorization | header | String | 是 | Bearer Token |
| action | body | String | 是 | 审批动作：APPROVE-通过，REJECT-拒绝 |

#### 请求示例

```json
{
  "action": "APPROVE"
}
```

#### 响应示例（通过）

```json
{
  "code": 200,
  "message": "审批成功",
  "data": {
    "initialPassword": "Abc12345",
    "message": "审批通过，初始密码已生成"
  }
}
```

#### 响应示例（拒绝）

```json
{
  "code": 200,
  "message": "审批成功",
  "data": {
    "message": "已拒绝该账号申请"
  }
}
```

#### 错误码

| 错误码 | 说明 |
|--------|------|
| 400 | 该账号不在待审批状态 |
| 401 | 未授权 |

---

*文档创建时间：2026-08-21*
