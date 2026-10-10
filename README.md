# SideBy

[中文](#中文说明) · [English](#english)

## 中文说明

SideBy 是一个面向异地实习、异地工作和初到陌生城市人群的活动搭子平台。本仓库提供可运行的工程基础：当前只实现了一条从浏览器到数据库的真实联调链路，账号和活动等业务能力留待后续开发。

### 已完成内容

- React 移动端优先的应用外壳，并提供桌面端导航布局
- 活动广场、发布活动、登录、个人中心、通知、活动详情和 404 路由
- 使用真实的占位和空状态，不展示伪造业务数据
- 类型化 Fetch 客户端，以及 TanStack Query 的加载、成功、失败和重试状态
- Vite `/api` 开发代理
- 基于 `com.sideby` 包结构的 Spring Boot 模块化单体骨架
- `GET /api/system/status`，通过 MySQL `SELECT 1` 真实检查数据库
- 统一 API 响应、业务异常、参数校验、安全的 5xx 响应和基础日志
- 用户、活动和报名关系的 Flyway 初始迁移
- MySQL Compose 服务，以及可选的 Redis profile
- 最小化 Actuator 健康检查暴露和 Nginx SPA/API 代理模板

注册登录、活动 CRUD、报名退出、评论、通知、上传、Redis 业务接入和 Capacitor 打包目前均**未实现**。

### 技术栈与固定版本

| 范围       | 技术                                               |
| ---------- | -------------------------------------------------- |
| 前端       | React 19.3.0、TypeScript 6.0.3、Vite 8.3.4         |
| 路由与请求 | React Router 7.18.4、TanStack Query 5.104.1        |
| 样式与质量 | CSS Modules、ESLint 10、Prettier 3                 |
| 后端       | Java 21 目标版本、Spring Boot 4.1.1、Maven Wrapper |
| 持久化     | MyBatis-Plus 3.5.17、MySQL 8.4、Flyway             |
| 基础设施   | Docker Compose、Spring Boot Actuator、Nginx 模板   |

前端锁文件固定了完整依赖树。后端使用 Spring Boot 依赖管理，并显式固定 MyBatis-Plus 版本。

### 仓库结构

```text
frontend/       React 应用和浏览器 API 客户端
backend/        Spring Boot 应用和 Flyway 迁移
deploy/nginx/   生产环境 Nginx 配置模板
docs/           API、数据库及后续集成约定
compose.yaml    本地 MySQL 和可选 Redis
```

前端按应用初始化、页面、共享组件、布局、API 基础能力和样式组织。当某个业务模块真正开始开发时，再创建 `features/<business>`；当前没有生成空业务目录。

后端按业务领域组织包。已经实现的 `system` 模块包含 controller、service、mapper 和 VO；通用响应及异常放在 `common`。后续开发时再添加 `auth`、`user`、`activity` 和 `notification`。

### 环境要求

- JDK 21 或更高版本，项目编译为 Java 21 字节码
- Node.js 22.12+ 或 20.19+
- pnpm 10+
- Docker 与 Docker Compose

### 环境变量

| 变量                    | 默认值                  | 使用方             |
| ----------------------- | ----------------------- | ------------------ |
| `MYSQL_DATABASE`        | `sideby`                | Compose            |
| `MYSQL_USER`            | `sideby`                | Compose            |
| `MYSQL_PASSWORD`        | 本地开发值              | Compose            |
| `MYSQL_ROOT_PASSWORD`   | 本地开发值              | Compose            |
| `MYSQL_PORT`            | `3306`                  | Compose 宿主机端口 |
| `DB_HOST`               | `127.0.0.1`             | Spring Boot        |
| `DB_PORT`               | `3306`                  | Spring Boot        |
| `DB_NAME`               | `sideby`                | Spring Boot        |
| `DB_USERNAME`           | `sideby`                | Spring Boot        |
| `DB_PASSWORD`           | `sideby_local_password` | Spring Boot        |
| `SERVER_PORT`           | `8080`                  | Spring Boot        |
| `VITE_DEV_PORT`         | `5173`                  | Vite               |
| `VITE_API_PROXY_TARGET` | `http://localhost:8080` | Vite               |

修改 Compose 默认值前，请将 `.env.example` 复制为 `.env`。**Compose 会读取 `.env`，但在宿主机上启动的 Java 进程不会自动读取它。** 必须在启动 Spring Boot 的终端中设置 `DB_*`，并确保 Compose 的 `MYSQL_*` 与后端数据库账号密码一致。

Spring Boot 在宿主机运行时使用 `DB_HOST=127.0.0.1`。如果以后将后端也容器化，并加入相同的 Compose 网络，则使用服务名 `DB_HOST=mysql`。

### 本地启动——Windows PowerShell

在仓库根目录执行：

```powershell
Copy-Item .env.example .env
docker compose up -d mysql
docker compose ps
```

在新的 PowerShell 窗口启动后端：

```powershell
cd backend
$env:DB_HOST = '127.0.0.1'
$env:DB_PORT = '3306'
$env:DB_NAME = 'sideby'
$env:DB_USERNAME = 'sideby'
$env:DB_PASSWORD = 'change_me'
./mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=dev"
```

在另一个窗口启动前端：

```powershell
cd frontend
pnpm install --frozen-lockfile
pnpm dev
```

打开 <http://localhost:5173>，状态卡片应显示 `API UP · Database UP`。

以上账号密码与 `.env.example` 一致。如果不复制示例文件，而是保留 Compose 内置默认值，请将后端 `DB_PASSWORD` 设置为 `sideby_local_password`。

### 本地启动——macOS/Linux

```bash
cp .env.example .env
docker compose up -d mysql
docker compose ps
```

在后端终端执行：

```bash
cd backend
export DB_HOST=127.0.0.1 DB_PORT=3306 DB_NAME=sideby
export DB_USERNAME=sideby DB_PASSWORD=change_me
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

在前端终端执行：

```bash
cd frontend
pnpm install --frozen-lockfile
pnpm dev
```

### 验证前后端联调

确保 MySQL 和后端已经运行，然后执行：

```powershell
Invoke-RestMethod http://localhost:8080/api/system/status
Invoke-RestMethod http://localhost:5173/api/system/status
```

第一个请求直接检查 Spring Boot；第二个请求检查 Vite 代理链路。后端运行时停止 MySQL，状态接口应返回 HTTP 503，前端应显示可以重试的错误状态。

Actuator 只在 `/actuator` 下暴露 `health` 和 `info`，不会公开健康检查详情。

### 构建与检查

前端：

```powershell
cd frontend
pnpm typecheck
pnpm lint
pnpm format:check
pnpm build
```

后端：

```powershell
cd backend
./mvnw.cmd verify
```

后端连接空数据库时，Flyway 会自动执行迁移。可以通过 `flyway_schema_history` 表检查迁移记录。

### 常见问题

#### 3306、8080 或 5173 端口被占用

根据需要修改 `MYSQL_PORT`、`SERVER_PORT` 或 `VITE_DEV_PORT`。如果后端端口发生变化，还需要将 `VITE_API_PROXY_TARGET` 设置为对应地址。

#### MySQL 容器已经启动但尚未就绪

执行 `docker compose ps`，等待 MySQL 变为 healthy 后再启动后端。可以使用 `docker compose logs mysql` 查看诊断日志。

#### 数据库拒绝访问，或环境变量没有生效

Compose `.env` 中的值不会自动导出到启动 Java 的 PowerShell/bash 进程。请在该终端显式设置 `DB_USERNAME` 和 `DB_PASSWORD`，并确保它们与首次创建 MySQL volume 时使用的账号密码一致。

修改 `.env` 不会重写已有数据库 volume 中的账号密码。只有在确定可以丢弃全部本地数据时，才应删除 volume 并重新初始化。

#### 前端状态卡片显示错误

依次检查 MySQL、后端直连地址 <http://localhost:8080/api/system/status>，以及代理地址 <http://localhost:5173/api/system/status>。

### 生产部署模板

本地 Vite 开发服务器不适用于生产环境。执行 `pnpm build` 后，通过 Nginx 托管 `frontend/dist`，并根据部署环境修改 `deploy/nginx/sideby.conf` 中的后端地址。模板已经包含 SPA 路由回退，并在代理时保留 `/api/...` 路径。

不要将 MySQL 暴露到公网。生产密钥应通过部署平台注入，不要提交真实 `.env`。

### 建议开发顺序

1. 注册登录、密码哈希、认证和授权
2. 活动发布、列表、详情和筛选
3. 事务化报名和退出
4. 评论和通知
5. 图片上传和存储
6. 在确有需求时接入 Redis
7. 生产部署和可观测性
8. Capacitor Android/iOS 打包

后端从 `backend/src/main/java/com/sideby` 开始添加实际业务包及相关 DTO、VO、Service、Mapper 和 Entity。前端从 `frontend/src/features` 开始添加业务模块，路由组合保留在 `src/app`，通用请求能力保留在 `src/lib`。

更多信息请查看 [API 文档](docs/api.md)、[数据库约定](docs/database.md) 和 [Capacitor 后续接入说明](docs/capacitor.md)。

---

## English

SideBy is an activity-companion platform for people interning, working, or newly arriving in an unfamiliar city. This repository provides a runnable foundation: it implements one real browser-to-database integration path while leaving account and activity business features for later development.

### What is ready

- React mobile-first application shell with a desktop navigation layout
- Routes for activities, creation, login, profile, notifications, activity details, and 404
- Honest placeholders and empty states instead of simulated business data
- Typed Fetch client and TanStack Query loading, success, failure, and retry states
- Vite `/api` development proxy
- Spring Boot modular-monolith foundation under `com.sideby`
- `GET /api/system/status`, backed by a real MySQL `SELECT 1`
- Unified API envelope, business exceptions, validation handling, safe 5xx responses, and basic logging
- Flyway migration for users, activities, and participation
- MySQL Compose service plus an optional Redis profile
- Minimal Actuator health exposure and an Nginx SPA/API proxy template

Registration, login, activity CRUD, joining, withdrawal, comments, notifications, uploads, Redis business integration, and Capacitor packaging are **not implemented**.

### Stack and pinned versions

| Area                | Technology                                           |
| ------------------- | ---------------------------------------------------- |
| Frontend            | React 19.3.0, TypeScript 6.0.3, Vite 8.3.4           |
| Routing and data    | React Router 7.18.4, TanStack Query 5.104.1          |
| Styling and quality | CSS Modules, ESLint 10, Prettier 3                   |
| Backend             | Java 21 target, Spring Boot 4.1.1, Maven Wrapper     |
| Persistence         | MyBatis-Plus 3.5.17, MySQL 8.4, Flyway               |
| Infrastructure      | Docker Compose, Spring Boot Actuator, Nginx template |

The frontend lock file pins the complete dependency graph. The backend uses Spring Boot dependency management and explicitly pins MyBatis-Plus.

### Repository layout

```text
frontend/       React application and browser API client
backend/        Spring Boot application and Flyway migrations
deploy/nginx/   Production Nginx configuration template
docs/           API, database, and future integration decisions
compose.yaml    Local MySQL and optional Redis
```

Frontend code is organized by application setup, pages, shared components, layouts, API infrastructure, and styles. Create a `features/<business>` directory when that business slice has real code; empty feature directories are intentionally omitted.

Backend packages are organized by business area. The implemented `system` slice contains controller, service, mapper, and VO layers; shared responses and errors stay under `common`. Add `auth`, `user`, `activity`, and `notification` when those features are implemented.

### Requirements

- JDK 21 or newer; the project compiles to Java 21 bytecode
- Node.js 22.12+ or 20.19+
- pnpm 10+
- Docker with Docker Compose

### Environment variables

| Variable                | Default                 | Used by              |
| ----------------------- | ----------------------- | -------------------- |
| `MYSQL_DATABASE`        | `sideby`                | Compose              |
| `MYSQL_USER`            | `sideby`                | Compose              |
| `MYSQL_PASSWORD`        | local development value | Compose              |
| `MYSQL_ROOT_PASSWORD`   | local development value | Compose              |
| `MYSQL_PORT`            | `3306`                  | Compose host binding |
| `DB_HOST`               | `127.0.0.1`             | Spring Boot          |
| `DB_PORT`               | `3306`                  | Spring Boot          |
| `DB_NAME`               | `sideby`                | Spring Boot          |
| `DB_USERNAME`           | `sideby`                | Spring Boot          |
| `DB_PASSWORD`           | `sideby_local_password` | Spring Boot          |
| `SERVER_PORT`           | `8080`                  | Spring Boot          |
| `VITE_DEV_PORT`         | `5173`                  | Vite                 |
| `VITE_API_PROXY_TARGET` | `http://localhost:8080` | Vite                 |

Copy `.env.example` to `.env` before changing the Compose defaults. **Compose reads `.env`, but a Java process started on the host does not.** Set `DB_*` in the shell that starts Spring Boot, and keep the Compose `MYSQL_*` values aligned with the backend credentials.

When Spring Boot runs on the host, use `DB_HOST=127.0.0.1`. If the backend is later containerized in the same Compose network, use the Compose service name `DB_HOST=mysql`.

### Start locally — Windows PowerShell

From the repository root:

```powershell
Copy-Item .env.example .env
docker compose up -d mysql
docker compose ps
```

Start the backend in a new PowerShell window:

```powershell
cd backend
$env:DB_HOST = '127.0.0.1'
$env:DB_PORT = '3306'
$env:DB_NAME = 'sideby'
$env:DB_USERNAME = 'sideby'
$env:DB_PASSWORD = 'change_me'
./mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=dev"
```

Start the frontend in another window:

```powershell
cd frontend
pnpm install --frozen-lockfile
pnpm dev
```

Open <http://localhost:5173>. The status card should show `API UP · Database UP`.

The values above match `.env.example`. If you retain the built-in Compose defaults instead of copying the example, use `sideby_local_password` as `DB_PASSWORD`.

### Start locally — macOS/Linux

```bash
cp .env.example .env
docker compose up -d mysql
docker compose ps
```

In a backend terminal:

```bash
cd backend
export DB_HOST=127.0.0.1 DB_PORT=3306 DB_NAME=sideby
export DB_USERNAME=sideby DB_PASSWORD=change_me
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

In a frontend terminal:

```bash
cd frontend
pnpm install --frozen-lockfile
pnpm dev
```

### Verify the integration

With MySQL and the backend running:

```powershell
Invoke-RestMethod http://localhost:8080/api/system/status
Invoke-RestMethod http://localhost:5173/api/system/status
```

The first request checks Spring Boot directly. The second checks the Vite proxy path. Stopping MySQL while the backend is running should make the status request return HTTP 503, and the frontend should show a retryable error state.

Actuator exposes only `health` and `info` under `/actuator`; health details are not publicly shown.

### Build and checks

Frontend:

```powershell
cd frontend
pnpm typecheck
pnpm lint
pnpm format:check
pnpm build
```

Backend:

```powershell
cd backend
./mvnw.cmd verify
```

Flyway runs automatically when the backend starts against an empty database. Inspect migration history in the `flyway_schema_history` table.

### Common problems

#### Port 3306, 8080, or 5173 is occupied

Change `MYSQL_PORT`, `SERVER_PORT`, or `VITE_DEV_PORT` as appropriate. When the backend port changes, also set `VITE_API_PROXY_TARGET` to the matching URL.

#### The MySQL container is running but not ready

Use `docker compose ps` until MySQL is healthy, then start the backend. Use `docker compose logs mysql` for diagnostics.

#### Access is denied or environment variables appear to be ignored

Values in the Compose `.env` file are not exported to the PowerShell/bash process that launches Java. Set `DB_USERNAME` and `DB_PASSWORD` explicitly in that terminal, and ensure they match the credentials used when the MySQL volume was first created.

Changing `.env` does not rewrite credentials in an existing database volume. Remove the volume only when you intentionally want to discard all local data.

#### The frontend status card reports an error

Check MySQL first, then the backend directly at <http://localhost:8080/api/system/status>, and finally the proxied URL at <http://localhost:5173/api/system/status>.

### Production template

The local Vite server is not a production server. Run `pnpm build`, serve `frontend/dist` through Nginx, and adapt `deploy/nginx/sideby.conf` so its backend upstream matches the deployment environment. The template includes SPA history fallback and preserves the `/api/...` path while proxying.

Do not expose MySQL to the public network. Supply production secrets through the deployment platform rather than a committed `.env` file.

### Suggested implementation order

1. Registration/login, password hashing, authentication, and authorization
2. Activity publishing, listing, details, and filtering
3. Transactional joining and withdrawal
4. Comments and notifications
5. Image upload and storage
6. Redis-backed features where there is a measured need
7. Production deployment and observability
8. Capacitor Android/iOS packaging

Start backend feature work from `backend/src/main/java/com/sideby`, adding only the relevant business packages, DTOs, VOs, services, mappers, and entities. Start frontend feature work from `frontend/src/features`, keeping route composition under `src/app` and reusable transport code under `src/lib`.

See the [API documentation](docs/api.md), [database conventions](docs/database.md), and [future Capacitor notes](docs/capacitor.md).
