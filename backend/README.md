# Team Knowledge Base - Backend

基于Spring Boot 3的团队在线知识库后端服务。

## 功能特性

- 用户认证与授权 (JWT)
- 文档管理与版本控制
- 知识空间管理
- 评论与互动系统
- 全文搜索
- 标签系统
- 通知系统
- RESTful API
- 权限管理
- 邮件集成

## 技术栈

- **框架**: Spring Boot 3.3.0
- **安全**: Spring Security + JWT
- **数据库**: SQLite + Spring Data JPA
- **验证**: Bean Validation
- **文档**: OpenAPI 3 (Swagger)
- **邮件**: Spring Mail
- **缓存**: Spring Cache
- **监控**: Spring Actuator

## 项目结构

```
backend/
├── src/main/java/com/teamkb/
│   ├── controller/         # REST控制器
│   ├── service/           # 业务逻辑层
│   ├── repository/        # 数据访问层
│   ├── entity/           # JPA实体类
│   ├── dto/              # 数据传输对象
│   ├── config/           # 配置类
│   ├── security/         # 安全相关
│   ├── exception/        # 异常处理
│   └── util/             # 工具类
├── src/main/resources/
│   ├── application.yml    # 应用配置
│   └── application-prod.yml # 生产环境配置
├── data/                 # SQLite数据库文件
├── logs/                 # 日志文件
└── pom.xml               # Maven依赖配置
```

## 快速开始

### 前置要求

- JDK 17+
- Maven 3.6+

### 运行应用

1. 克隆项目
```bash
git clone <repository-url>
cd backend
```

2. 构建项目
```bash
mvn clean compile
```

3. 运行应用
```bash
mvn spring-boot:run
```

应用将在 `http://localhost:8080` 启动。

### API文档

启动应用后，访问以下地址查看API文档：
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs

### 健康检查

- 健康状态: http://localhost:8080/actuator/health

## 环境配置

### 开发环境

默认使用内嵌SQLite数据库，无需额外配置。

### 生产环境

设置以下环境变量：

```bash
# 数据库配置
export DATABASE_URL=jdbc:sqlite:/path/to/production.db

# JWT配置
export JWT_SECRET=your-production-jwt-secret-key
export JWT_EXPIRATION=86400000

# 邮件配置
export MAIL_HOST=smtp.your-domain.com
export MAIL_USERNAME=your-email@domain.com
export MAIL_PASSWORD=your-email-password

# 应用URL
export BASE_URL=https://your-domain.com
```

启动生产环境：
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

## API 概览

### 认证 API
- `POST /api/v1/auth/register` - 用户注册
- `POST /api/v1/auth/login` - 用户登录
- `POST /api/v1/auth/logout` - 用户登出
- `GET /api/v1/auth/verify-email` - 邮箱验证
- `POST /api/v1/auth/forgot-password` - 忘记密码
- `POST /api/v1/auth/reset-password` - 重置密码

### 用户管理 API
- `GET /api/v1/users` - 获取用户列表
- `GET /api/v1/users/{id}` - 获取用户详情
- `PUT /api/v1/users/{id}` - 更新用户信息
- `DELETE /api/v1/users/{id}` - 删除用户
- `POST /api/v1/users/{id}/change-password` - 修改密码
- `GET /api/v1/users/search` - 搜索用户

### 文档管理 API
- `POST /api/v1/documents` - 创建文档
- `GET /api/v1/documents/{id}` - 获取文档
- `GET /api/v1/documents/{id}/view` - 查看文档（增加浏览量）
- `PUT /api/v1/documents/{id}` - 更新文档
- `DELETE /api/v1/documents/{id}` - 删除文档
- `POST /api/v1/documents/{id}/archive` - 归档文档
- `POST /api/v1/documents/{id}/unarchive` - 取消归档
- `GET /api/v1/documents/space/{spaceId}` - 获取空间文档
- `GET /api/v1/documents/search` - 搜索文档
- `GET /api/v1/documents/tag/{tagName}` - 按标签获取文档
- `GET /api/v1/documents/review-needed` - 获取需要审核的文档

## 数据模型

### 核心实体

- **User**: 用户信息
- **Space**: 知识空间
- **Document**: 文档
- **Comment**: 评论
- **Tag**: 标签
- **Notification**: 通知

### 关系实体

- **SpaceMember**: 空间成员关系
- **DocumentLike**: 文档点赞
- **DocumentBookmark**: 文档收藏
- **DocumentVersion**: 文档版本
- **DocumentPermission**: 文档权限

## 安全特性

- JWT令牌认证
- 基于角色的访问控制 (RBAC)
- 密码加密存储 (BCrypt)
- CORS支持
- 请求速率限制
- SQL注入防护
- XSS防护

## 开发指南

### 添加新的API端点

1. 在相应的Controller中添加方法
2. 实现Service层业务逻辑
3. 添加必要的DTO类
4. 更新Repository层（如需要）
5. 添加单元测试

### 数据库迁移

数据库表结构由JPA自动管理。生产环境建议使用 `ddl-auto: validate` 并手动执行数据库迁移脚本。

### 缓存策略

应用使用Spring Cache进行缓存管理，可以在Service方法上添加 `@Cacheable` 注解。

## 监控与日志

- 使用Spring Actuator进行应用监控
- 日志文件位于 `logs/teamkb.log`
- 生产环境建议集成ELK Stack或类似的日志管理系统

## 部署

### Docker部署

```dockerfile
FROM openjdk:17-jre-slim
COPY target/team-knowledge-base-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### 构建Docker镜像

```bash
mvn clean package
docker build -t team-kb-backend .
docker run -p 8080:8080 team-kb-backend
```

## 贡献指南

1. Fork项目
2. 创建特性分支
3. 提交变更
4. 推送到分支
5. 创建Pull Request

## 许可证

MIT License