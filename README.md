# 团队在线知识库 (Team Knowledge Base)

一个基于Vue 3 + Spring Boot 3的现代化团队知识管理平台，以Markdown为核心，支持协作编辑、版本控制、权限管理等企业级功能。

## 🚀 项目概述

本项目是一个完整的团队在线知识库解决方案，旨在帮助团队更好地管理、分享和协作知识内容。

### 核心特性

- 📝 **Markdown编辑器**: 所见即所得的分栏式编辑器，支持实时预览
- 🔐 **用户认证**: 邮箱注册、JWT认证、密码重置
- 🏢 **知识空间**: 支持多空间管理，灵活的权限控制
- 📋 **版本控制**: 完整的文档版本历史，支持版本对比和回滚
- 🏷️ **标签系统**: 智能标签管理，便于内容分类和检索
- 🔍 **全文搜索**: 强大的搜索功能，支持内容、标签、作者等多维度搜索
- 💬 **评论系统**: 支持文档评论和回复，促进团队协作
- 👥 **权限管理**: 基于角色的权限控制，支持空间、目录、文档三级权限
- 📊 **仪表盘**: 个性化工作台，展示最近编辑、收藏、动态等信息
- 🔔 **通知系统**: 实时通知，支持站内消息和邮件提醒
- 📱 **响应式设计**: 支持桌面端和移动端访问

## 🏗️ 项目架构

```
claude_project/
├── frontend/          # Vue 3 前端应用
│   ├── src/
│   │   ├── components/    # 可复用组件
│   │   ├── views/         # 页面组件
│   │   ├── stores/        # Pinia状态管理
│   │   ├── router/        # Vue Router配置
│   │   ├── styles/        # 样式文件
│   │   └── utils/         # 工具函数
│   ├── e2e/              # E2E测试
│   └── package.json
├── backend/          # Spring Boot 后端服务
│   ├── src/main/java/com/teamkb/
│   │   ├── controller/    # REST控制器
│   │   ├── service/       # 业务逻辑层
│   │   ├── repository/    # 数据访问层
│   │   ├── entity/        # JPA实体
│   │   ├── dto/           # 数据传输对象
│   │   ├── config/        # 配置类
│   │   └── security/      # 安全组件
│   ├── src/main/resources/
│   └── pom.xml
├── docs/             # 项目文档
│   ├── 需求设计说明书.md
│   ├── 概要设计说明书.md
│   ├── 详细设计说明书.md
│   └── UI设计说明书.md
└── README.md         # 本文件
```

## 💻 技术栈

### 前端
- **框架**: Vue 3.5+ (Composition API)
- **UI库**: Element Plus 2.11+
- **状态管理**: Pinia 3+
- **路由**: Vue Router 4.5+
- **构建工具**: Vite 7+
- **语言**: TypeScript 5.8+
- **测试**: Vitest + Playwright

### 后端
- **框架**: Spring Boot 3.3.0
- **安全**: Spring Security + JWT
- **数据库**: SQLite + Spring Data JPA
- **文档**: OpenAPI 3 (Swagger)
- **邮件**: Spring Mail
- **缓存**: Spring Cache
- **监控**: Spring Actuator

## 🚀 快速开始

### 环境要求

- **Node.js**: 18.0+ (推荐 LTS 版本)
- **Java**: 17+ (JDK)
- **Maven**: 3.6+
- **Git**: 最新版本

### 前端启动

```bash
cd frontend
npm install
npm run dev
```

前端将在 `http://localhost:5173` 启动

### 后端启动

```bash
cd backend
mvn spring-boot:run
```

后端将在 `http://localhost:8080` 启动

### 快速启动脚本

**Windows:**
```bash
# 启动后端
cd backend
run.bat

# 启动前端
cd frontend
npm run dev
```

**Linux/macOS:**
```bash
# 启动后端
cd backend
./run.sh

# 启动前端
cd frontend
npm run dev
```

## 📱 核心功能

### 用户系统
- ✅ 邮箱注册/登录
- ✅ 邮箱验证
- ✅ 密码重置
- ✅ 个人资料管理
- ✅ 主题切换 (明亮/暗黑)

### 文档管理
- ✅ Markdown 编辑器 (分栏式)
- ✅ 文档创建/编辑/删除
- ✅ 无限层级目录结构
- ✅ 拖拽排序和移动
- ✅ 版本历史和对比
- ✅ 标签管理
- ✅ 收藏/点赞功能

### 协作功能
- ✅ 评论系统 (支持嵌套回复)
- ✅ @提及功能
- ✅ 实时通知
- ✅ 分享功能

### 搜索与发现
- ✅ 全局搜索
- ✅ 高级筛选
- ✅ 标签导航
- ✅ 最近浏览历史

### 管理功能
- ✅ 知识空间管理
- ✅ 成员权限管理
- ✅ 用户组管理
- ✅ 系统管理后台

## 🔧 开发指南

### 前端开发

```bash
cd frontend

# 安装依赖
npm install

# 启动开发服务器
npm run dev

# 构建生产版本
npm run build

# 类型检查
npm run type-check

# 代码格式化
npm run format

# 运行测试
npm run test:unit
npm run test:e2e
```

### 后端开发

```bash
cd backend

# 编译项目
mvn clean compile

# 运行应用
mvn spring-boot:run

# 运行测试
mvn test

# 打包应用
mvn clean package
```

### API文档

后端启动后访问：
- Swagger UI: http://localhost:8080/swagger-ui.html
- API文档: http://localhost:8080/v3/api-docs

## 📋 开发规范

### 代码风格
- 前端: 使用 ESLint + Prettier
- 后端: 遵循阿里巴巴Java开发规范
- 提交信息: 使用约定式提交格式

### 分支管理
- `main`: 主分支，用于生产环境
- `develop`: 开发分支，用于集成测试
- `feature/*`: 功能分支
- `hotfix/*`: 热修复分支

### 测试要求
- 前端: 组件单元测试覆盖率 > 80%
- 后端: 核心业务逻辑测试覆盖率 > 70%
- E2E测试覆盖主要用户流程

## 🚀 部署指南

### 前端部署

```bash
cd frontend
npm run build
# 将 dist/ 目录部署到静态文件服务器
```

### 后端部署

```bash
cd backend
mvn clean package
java -jar target/team-knowledge-base-1.0.0.jar
```

### Docker部署

```bash
# 构建前端
cd frontend && npm run build

# 构建后端
cd ../backend && mvn clean package

# 使用Docker Compose
docker-compose up -d
```

## 📈 功能路线图

### v1.1 (计划中)
- [ ] 实时协同编辑
- [ ] 文档模板系统
- [ ] 更多文件格式支持
- [ ] 移动端应用

### v1.2 (规划中)
- [ ] AI智能推荐
- [ ] 文档统计分析
- [ ] 第三方集成 (Slack, Teams)
- [ ] 多语言支持

## 🤝 贡献指南

我们欢迎任何形式的贡献！

1. Fork 本项目
2. 创建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建 Pull Request

### 开发环境设置

1. 克隆项目
```bash
git clone https://github.com/your-username/team-knowledge-base.git
cd team-knowledge-base
```

2. 安装依赖
```bash
# 前端
cd frontend && npm install

# 后端
cd ../backend && mvn clean compile
```

3. 启动开发环境
```bash
# 终端1: 启动后端
cd backend && mvn spring-boot:run

# 终端2: 启动前端
cd frontend && npm run dev
```

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。

## 👥 团队

- **项目负责人**: Team KB Development Team
- **前端开发**: Vue.js Team
- **后端开发**: Spring Boot Team
- **UI/UX设计**: Design Team

## 📞 联系我们

- 邮箱: dev@teamkb.com
- 项目主页: https://github.com/your-username/team-knowledge-base
- 问题反馈: https://github.com/your-username/team-knowledge-base/issues

## 🙏 致谢

感谢所有为这个项目做出贡献的开发者和用户！

---

⭐ 如果这个项目对你有帮助，请给我们一个星标！