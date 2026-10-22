# SideBy API

API responses use the following envelope:

```json
{
  "code": "OK",
  "message": "success",
  "data": {}
}
```

HTTP status codes remain meaningful. An error response does not use HTTP 200 merely to fit the envelope.

## Available

### `GET /api/system/status`

Runs a lightweight `SELECT 1` against MySQL. A successful response is HTTP 200:

```json
{
  "code": "OK",
  "message": "success",
  "data": {
    "application": "SideBy",
    "status": "UP",
    "database": "UP"
  }
}
```

If the database probe fails after the application has started, the endpoint returns HTTP 503 without exposing SQL, credentials, or a stack trace:

```json
{
  "code": "DATABASE_UNAVAILABLE",
  "message": "Database is currently unavailable",
  "data": null
}
```

## 当前实现状态

- 注册接口已经实现，并且已经完成数据库写入和密码哈希保存；
- 登录、当前用户、退出正在按照 `docs/auth-guide/05-00-登录Session和CSRF总入口.md` 分步骤实现；
- 刷新接口暂不单独实现，本阶段使用服务端 Session，浏览器刷新页面后直接调用当前用户接口恢复状态；
- 在 `05-10-手工验证登录闭环.md` 完成前，不要把登录接口当作已经验收完成。

下面的认证接口契约是目标契约。代码实现过程中如果遇到错误，优先以对应教程中的“文件完整内容”和验证结果为准。

以下业务接口仍未实现：

- Activity 创建、列表、详情、更新、取消；
- Activity 加入和退出；
- 评论；
- 通知；
- 文件上传。

不要在这些接口真正实现前，让前端依赖它们。

## API 契约

编码前先更新 `docs/api.md`。前后端都以这一份契约为准。

### 3.1 获取 CSRF Token

```http
GET /api/auth/csrf
```

成功响应：

```json
{
  "code": "OK",
  "message": "success",
  "data": {
    "headerName": "X-CSRF-TOKEN",
    "token": "..."
  }
}
```

### 3.2 注册

```http
POST /api/auth/register
Content-Type: application/json
X-CSRF-TOKEN: ...
```

请求：

```json
{
  "username": "sideby_user",
  "email": "user@example.com",
  "password": "example-password",
  "displayName": "SideBy 用户"
}
```

成功：HTTP 201。

```json
{
  "code": "OK",
  "message": "success",
  "data": {
    "id": "1",
    "username": "sideby_user",
    "email": "user@example.com",
    "displayName": "SideBy 用户",
    "avatarUrl": null,
    "bio": null
  }
}
```

注意：ID 必须是 JSON 字符串，不是 JavaScript number。

### 3.3 登录

```http
POST /api/auth/login
Content-Type: application/json
X-CSRF-TOKEN: ...
```

请求：

```json
{
  "identifier": "sideby_user",
  "password": "example-password"
}
```

成功：HTTP 200，同时响应中设置 Session Cookie。

```json
{
  "code": "OK",
  "message": "success",
  "data": {
    "id": "1",
    "username": "sideby_user",
    "email": "user@example.com",
    "displayName": "SideBy 用户",
    "avatarUrl": null,
    "bio": null
  }
}
```

失败：HTTP 401。

```json
{
  "code": "INVALID_CREDENTIALS",
  "message": "Username, email, or password is incorrect",
  "data": null
}
```

不要告诉调用方究竟是账号不存在还是密码错误，否则会泄露账号是否存在。

### 3.4 获取当前用户

```http
GET /api/users/me
```

登录成功时返回用户资料；未登录时返回 HTTP 401：

```json
{
  "code": "UNAUTHENTICATED",
  "message": "Authentication is required",
  "data": null
}
```

### 3.5 退出

```http
POST /api/auth/logout
X-CSRF-TOKEN: ...
```

成功响应：

```json
{
  "code": "OK",
  "message": "success",
  "data": null
}
```

退出后服务端 Session 失效，浏览器的 Session Cookie 被清理。

### 3.6 第一阶段错误码

| HTTP | code | 使用场景 |
| --- | --- | --- |
| 400 | `VALIDATION_FAILED` | 注册字段不合法 |
| 401 | `INVALID_CREDENTIALS` | 登录账号或密码错误 |
| 401 | `UNAUTHENTICATED` | 未登录访问受保护接口 |
| 403 | `FORBIDDEN` | 已登录但无权限，或安全校验失败 |
| 409 | `USERNAME_ALREADY_EXISTS` | 用户名重复 |
| 409 | `EMAIL_ALREADY_EXISTS` | 邮箱重复 |
| 409 | `REGISTRATION_CONFLICT` | 并发注册导致唯一键冲突 |
| 500 | `INTERNAL_ERROR` | 未预料的服务端错误
