-- ========================================
-- 团队在线知识库 - 测试数据脚本
-- 版本: v1.0
-- 创建日期: 2025-09-28
-- 描述: 包含完整的测试数据
-- ========================================

USE knowledge_management;

-- 确保外键检查已启用
SET FOREIGN_KEY_CHECKS = 1;

-- ========================================
-- 1. 插入测试用户数据
-- ========================================

-- 管理员用户
INSERT INTO `users` (`id`, `email`, `password_hash`, `display_name`, `avatar_url`, `is_system_admin`, `status`, `last_login_at`) VALUES
(1, 'admin@company.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8ioctKk85jEeZYyBl1o9g1e9PnsWy', '系统管理员', 'https://i.pravatar.cc/150?img=1', true, 'ACTIVE', NOW()),
(2, 'zhang.wei@company.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8ioctKk85jEeZYyBl1o9g1e9PnsWy', '张伟', 'https://i.pravatar.cc/150?img=2', false, 'ACTIVE', NOW()),
(3, 'li.na@company.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8ioctKk85jEeZYyBl1o9g1e9PnsWy', '李娜', 'https://i.pravatar.cc/150?img=3', false, 'ACTIVE', NOW()),
(4, 'wang.ming@company.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8ioctKk85jEeZYyBl1o9g1e9PnsWy', '王明', 'https://i.pravatar.cc/150?img=4', false, 'ACTIVE', NOW()),
(5, 'chen.hui@company.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8ioctKk85jEeZYyBl1o9g1e9PnsWy', '陈慧', 'https://i.pravatar.cc/150?img=5', false, 'ACTIVE', NOW()),
(6, 'liu.qiang@company.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8ioctKk85jEeZYyBl1o9g1e9PnsWy', '刘强', 'https://i.pravatar.cc/150?img=6', false, 'ACTIVE', NOW()),
(7, 'test.user@company.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8ioctKk85jEeZYyBl1o9g1e9PnsWy', '测试用户', 'https://i.pravatar.cc/150?img=7', false, 'INACTIVE', '2025-09-01 10:30:00');

-- ========================================
-- 2. 插入测试空间数据
-- ========================================

INSERT INTO `spaces` (`id`, `name`, `description`, `owner_id`, `status`, `is_public`, `settings`) VALUES
(1, '产品开发文档', '产品团队的核心开发文档和技术规范', 2, 'ACTIVE', true, '{"theme": "blue", "auto_backup": true, "allow_comments": true}'),
(2, '运营指南', '运营团队的工作流程和最佳实践', 3, 'ACTIVE', false, '{"theme": "green", "auto_backup": false, "allow_comments": true}'),
(3, '技术分享', '技术团队的学习笔记和经验分享', 4, 'ACTIVE', true, '{"theme": "purple", "auto_backup": true, "allow_comments": true}'),
(4, '公司政策', '人力资源和公司政策文档', 1, 'ACTIVE', true, '{"theme": "orange", "auto_backup": true, "allow_comments": false}'),
(5, '项目归档', '已完成项目的归档文档', 5, 'ARCHIVED', false, '{"theme": "gray", "auto_backup": false, "allow_comments": false}');

-- ========================================
-- 3. 插入测试标签数据
-- ========================================

INSERT INTO `tags` (`id`, `name`, `color`, `description`) VALUES
(1, 'API', '#FF6B6B', 'API接口相关文档'),
(2, 'React', '#4ECDC4', 'React前端技术'),
(3, 'Spring Boot', '#45B7D1', 'Spring Boot后端框架'),
(4, '数据库', '#96CEB4', '数据库设计和优化'),
(5, '部署', '#FECA57', '部署和运维相关'),
(6, '设计规范', '#FF9FF3', 'UI/UX设计规范'),
(7, '测试', '#74B9FF', '测试相关文档'),
(8, '安全', '#FD79A8', '安全和权限管理'),
(9, '性能', '#FDCB6E', '性能优化相关'),
(10, '文档', '#A29BFE', '文档规范和模板');

-- ========================================
-- 4. 插入测试文档数据
-- ========================================

-- 根目录文档（文件夹）
INSERT INTO `documents` (`id`, `title`, `content`, `content_type`, `space_id`, `parent_id`, `creator_id`, `last_editor_id`, `status`, `view_count`, `sort_order`, `is_pinned`) VALUES
(1, '📚 API文档', NULL, 'FOLDER', 1, NULL, 2, NULL, 'PUBLISHED', 0, 1, true),
(2, '🎨 设计规范', NULL, 'FOLDER', 1, NULL, 3, NULL, 'PUBLISHED', 0, 2, false),
(3, '📋 运营流程', NULL, 'FOLDER', 2, NULL, 3, NULL, 'PUBLISHED', 0, 1, true),
(4, '💡 技术分享', NULL, 'FOLDER', 3, NULL, 4, NULL, 'PUBLISHED', 0, 1, false),
(5, '📜 公司制度', NULL, 'FOLDER', 4, NULL, 1, NULL, 'PUBLISHED', 0, 1, true);

-- 具体文档
INSERT INTO `documents` (`id`, `title`, `content`, `content_type`, `space_id`, `parent_id`, `creator_id`, `last_editor_id`, `status`, `view_count`, `sort_order`, `is_pinned`) VALUES
(6, '用户认证API', '# 用户认证API文档

## 概述
本文档描述了用户认证相关的API接口。

## 登录接口

### POST /api/v1/auth/login

**请求参数：**
```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

**响应示例：**
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "user": {
      "id": 1,
      "email": "user@example.com",
      "displayName": "张三"
    }
  }
}
```

## 注册接口

### POST /api/v1/auth/register

**请求参数：**
```json
{
  "email": "newuser@example.com",
  "password": "password123",
  "displayName": "新用户"
}
```

**注意事项：**
- 密码长度至少8位
- 邮箱格式必须正确
- 显示名称不能为空', 'DOCUMENT', 1, 1, 2, 2, 'PUBLISHED', 156, 1, false),

(7, '文档管理API', '# 文档管理API

## 概述
文档相关的CRUD操作接口。

## 获取文档列表

### GET /api/v1/spaces/{spaceId}/documents

**查询参数：**
- `page`: 页码（默认1）
- `size`: 每页大小（默认20）
- `type`: 文档类型（可选）

**响应示例：**
```json
{
  "code": 200,
  "data": {
    "content": [
      {
        "id": 1,
        "title": "示例文档",
        "contentType": "DOCUMENT",
        "status": "PUBLISHED",
        "createdAt": "2025-09-28T10:00:00"
      }
    ],
    "totalElements": 1,
    "totalPages": 1
  }
}
```

## 创建文档

### POST /api/v1/spaces/{spaceId}/documents

**请求体：**
```json
{
  "title": "新文档标题",
  "content": "# 文档内容\n\n这是一个新文档。",
  "contentType": "DOCUMENT",
  "parentId": null
}
```', 'DOCUMENT', 1, 1, 4, 4, 'PUBLISHED', 89, 2, false),

(8, 'UI设计规范', '# UI设计规范

## 颜色规范

### 主色调
- **主蓝色**: #1890ff
- **成功绿**: #52c41a
- **警告橙**: #faad14
- **错误红**: #ff4d4f

### 中性色
- **文本黑**: #262626
- **描述灰**: #8c8c8c
- **边框灰**: #d9d9d9
- **背景灰**: #fafafa

## 字体规范

### 字体大小
- **标题1**: 32px (font-size: 2rem)
- **标题2**: 24px (font-size: 1.5rem)
- **标题3**: 20px (font-size: 1.25rem)
- **正文**: 14px (font-size: 0.875rem)
- **小文本**: 12px (font-size: 0.75rem)

### 字体权重
- **粗体**: 600
- **中等**: 500
- **常规**: 400

## 间距规范

### 标准间距
- **xs**: 4px
- **sm**: 8px
- **md**: 16px
- **lg**: 24px
- **xl**: 32px

## 组件规范

### 按钮
- **主按钮**: 蓝色背景，白色文字
- **次要按钮**: 白色背景，蓝色边框
- **危险按钮**: 红色背景，白色文字

### 表单
- **输入框高度**: 32px
- **标签间距**: 8px
- **表单组间距**: 16px', 'DOCUMENT', 1, 2, 3, 3, 'PUBLISHED', 234, 1, true),

(9, '用户运营策略', '# 用户运营策略

## 用户获取策略

### 1. 内容营销
- 高质量技术博客
- 开源项目推广
- 社区活动参与

### 2. 合作推广
- 技术会议赞助
- KOL合作
- 媒体报道

### 3. 产品优化
- 用户体验提升
- 功能持续迭代
- 客户反馈收集

## 用户留存策略

### 1. 产品价值
- 核心功能完善
- 用户需求满足
- 使用体验优化

### 2. 用户服务
- 及时客服支持
- 用户培训
- 问题快速解决

### 3. 社区建设
- 用户交流群
- 最佳实践分享
- 用户成功案例

## 数据指标

### 关键指标
- **DAU**: 日活跃用户数
- **MAU**: 月活跃用户数
- **留存率**: 7日/30日留存
- **转化率**: 注册到激活转化

### 监控方法
- Google Analytics
- 用户行为分析
- A/B测试
- 用户访谈', 'DOCUMENT', 2, 3, 3, 3, 'PUBLISHED', 67, 1, false),

(10, 'React Hooks最佳实践', '# React Hooks最佳实践

## useState使用技巧

### 1. 合理拆分状态
```jsx
// ❌ 不推荐：将所有状态放在一个对象中
const [userInfo, setUserInfo] = useState({
  name: "",
  email: "",
  age: 0,
  avatar: ""
});

// ✅ 推荐：按逻辑分组拆分状态
const [name, setName] = useState("");
const [email, setEmail] = useState("");
const [profile, setProfile] = useState({ age: 0, avatar: "" });
```

### 2. 使用函数式更新
```jsx
// ❌ 可能有问题
setCount(count + 1);

// ✅ 推荐
setCount(prev => prev + 1);
```

## useEffect使用技巧

### 1. 正确设置依赖数组
```jsx
// ❌ 缺少依赖
useEffect(() => {
  fetchUserData(userId);
}, []);

// ✅ 正确依赖
useEffect(() => {
  fetchUserData(userId);
}, [userId]);
```

### 2. 清理副作用
```jsx
useEffect(() => {
  const timer = setInterval(() => {
    // 定时器逻辑
  }, 1000);

  // 清理函数
  return () => clearInterval(timer);
}, []);
```

## 自定义Hook设计原则

### 1. 单一职责
每个Hook只负责一个特定的逻辑

### 2. 可复用性
设计时考虑在不同组件中的复用

### 3. 命名规范
以"use"开头，语义化命名

```jsx
// 示例：用户数据管理Hook
function useUserData(userId) {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchUser(userId)
      .then(setUser)
      .catch(setError)
      .finally(() => setLoading(false));
  }, [userId]);

  return { user, loading, error };
}
```', 'DOCUMENT', 3, 4, 4, 4, 'PUBLISHED', 312, 1, true),

(11, 'MySQL性能优化指南', '# MySQL性能优化指南

## 索引优化

### 1. 选择合适的索引类型
- **B-Tree索引**: 最常用，适合等值和范围查询
- **哈希索引**: 适合等值查询，Memory引擎支持
- **全文索引**: 用于文本搜索
- **空间索引**: 用于地理数据

### 2. 创建复合索引
```sql
-- 复合索引顺序很重要
CREATE INDEX idx_user_status_created ON users (status, created_at);

-- 可以使用的查询
SELECT * FROM users WHERE status = "ACTIVE";
SELECT * FROM users WHERE status = "ACTIVE" AND created_at > "2025-01-01";

-- 无法使用索引的查询
SELECT * FROM users WHERE created_at > "2025-01-01";
```

### 3. 避免索引失效
```sql
-- ❌ 函数操作导致索引失效
SELECT * FROM users WHERE YEAR(created_at) = 2025;

-- ✅ 使用范围查询
SELECT * FROM users WHERE created_at >= "2025-01-01" AND created_at < "2026-01-01";
```

## 查询优化

### 1. 使用EXPLAIN分析查询
```sql
EXPLAIN SELECT * FROM documents d
JOIN users u ON d.creator_id = u.id
WHERE d.status = "PUBLISHED"
ORDER BY d.created_at DESC
LIMIT 10;
```

### 2. 避免SELECT *
```sql
-- ❌ 查询所有字段
SELECT * FROM documents WHERE space_id = 1;

-- ✅ 只查询需要的字段
SELECT id, title, creator_id, created_at FROM documents WHERE space_id = 1;
```

### 3. 合理使用分页
```sql
-- ❌ 深分页性能差
SELECT * FROM documents ORDER BY id LIMIT 10000, 10;

-- ✅ 使用游标分页
SELECT * FROM documents WHERE id > 10000 ORDER BY id LIMIT 10;
```

## 配置优化

### 1. InnoDB缓冲池
```ini
# 设置为服务器内存的70-80%
innodb_buffer_pool_size = 4G
innodb_buffer_pool_instances = 4
```

### 2. 查询缓存
```ini
# MySQL 8.0已移除查询缓存
# 建议使用应用层缓存（Redis）
```

### 3. 连接池配置
```ini
max_connections = 200
max_connect_errors = 100000
```', 'DOCUMENT', 3, 4, 6, 6, 'PUBLISHED', 198, 2, false),

(12, '员工手册', '# 员工手册

## 工作时间与考勤

### 标准工作时间
- **工作日**: 周一至周五
- **工作时间**: 9:00-18:00
- **午休时间**: 12:00-13:00

### 弹性工作制
- **核心时间**: 10:00-16:00必须在岗
- **弹性时间**: 8:00-10:00, 16:00-20:00
- **每日工作**: 满8小时即可

### 考勤管理
- 上下班需打卡
- 迟到早退需申请
- 月度考勤统计

## 薪酬福利

### 薪资结构
- **基本工资**: 根据岗位和级别确定
- **绩效奖金**: 根据个人和团队绩效
- **年终奖金**: 根据公司业绩和个人表现

### 福利待遇
- **五险一金**: 按国家标准缴纳
- **年假**: 5-15天（根据工龄）
- **病假**: 按国家规定执行
- **节日福利**: 传统节日礼品

### 培训发展
- **新员工培训**: 入职培训计划
- **技能培训**: 内部/外部培训机会
- **职业发展**: 晋升通道和规划

## 行为准则

### 职业道德
- 诚实守信
- 保守商业秘密
- 尊重同事
- 团队协作

### 禁止行为
- 违法违规行为
- 泄露公司机密
- 损害公司利益
- 骚扰他人

## 办公环境

### 办公设备
- 每人配备办公电脑
- 必要的办公软件许可
- 网络和通讯设备

### 环境管理
- 保持工作区域整洁
- 节约用电用水
- 垃圾分类处理
- 安全防护措施

## 联系方式

### 人事部门
- **电话**: 400-123-4567
- **邮箱**: hr@company.com
- **地址**: 北京市朝阳区xxx大厦

### 紧急联系
- **安保**: 内线8888
- **医务**: 内线8899
- **IT支持**: 内线8877', 'DOCUMENT', 4, 5, 1, 1, 'PUBLISHED', 445, 1, true),

(13, '项目A总结报告', '# 项目A总结报告

## 项目概述
项目A是我们公司2024年的重点项目，旨在开发新一代客户管理系统。

## 项目成果
- 成功上线新系统
- 用户满意度达到95%
- 性能提升50%

## 经验教训
### 成功因素
1. 团队协作良好
2. 需求分析充分
3. 技术选型合适

### 需要改进
1. 测试覆盖率不足
2. 文档维护滞后
3. 风险评估不够

## 后续计划
- 系统维护和优化
- 用户培训计划
- 经验总结分享', 'DOCUMENT', 5, NULL, 5, 5, 'PUBLISHED', 23, 1, false);

-- ========================================
-- 5. 插入文档版本历史数据
-- ========================================

INSERT INTO `doc_versions` (`document_id`, `version_number`, `title`, `content`, `author_id`, `comment`, `change_type`) VALUES
(6, 1, '用户认证API', '# 用户认证API文档\n\n初始版本的API文档。', 2, '创建API文档', 'CREATE'),
(6, 2, '用户认证API', '# 用户认证API文档\n\n## 概述\n本文档描述了用户认证相关的API接口。', 2, '添加概述部分', 'UPDATE'),
(7, 1, '文档管理API', '# 文档管理API\n\n基础API文档。', 4, '初始创建', 'CREATE'),
(8, 1, 'UI设计规范', '# UI设计规范\n\n基础设计规范。', 3, '创建设计规范', 'CREATE'),
(10, 1, 'React Hooks最佳实践', '# React Hooks最佳实践\n\n基础内容。', 4, '初始版本', 'CREATE'),
(10, 2, 'React Hooks最佳实践', '# React Hooks最佳实践\n\n## useState使用技巧\n\n添加了useState相关内容。', 4, '添加useState章节', 'UPDATE');

-- ========================================
-- 6. 插入文档标签关联数据
-- ========================================

INSERT INTO `document_tags` (`document_id`, `tag_id`) VALUES
(6, 1), (6, 3),          -- 用户认证API: API, Spring Boot
(7, 1), (7, 3),          -- 文档管理API: API, Spring Boot
(8, 6), (8, 10),         -- UI设计规范: 设计规范, 文档
(9, 10),                 -- 用户运营策略: 文档
(10, 2), (10, 10),       -- React Hooks: React, 文档
(11, 4), (11, 9),        -- MySQL性能优化: 数据库, 性能
(12, 10);                -- 员工手册: 文档

-- ========================================
-- 7. 插入评论数据
-- ========================================

INSERT INTO `comments` (`id`, `content`, `document_id`, `author_id`, `parent_id`, `like_count`) VALUES
(1, '这个API文档写得很详细，对新人很有帮助！', 6, 3, NULL, 5),
(2, '建议添加更多的错误码说明', 6, 4, NULL, 2),
(3, '同意楼上的建议，错误处理确实需要完善', 6, 5, 2, 1),
(4, 'UI规范很棒，但是缺少移动端的适配说明', 8, 2, NULL, 3),
(5, '这个Hook的例子很实用，已经在项目中使用了', 10, 6, NULL, 7),
(6, '性能优化的建议很实用，特别是索引部分', 11, 2, NULL, 4),
(7, '能否添加一些具体的优化案例？', 11, 3, 6, 2),
(8, '员工手册需要更新一下联系方式', 12, 4, NULL, 1);

-- ========================================
-- 8. 插入点赞数据
-- ========================================

INSERT INTO `likes` (`user_id`, `target_type`, `target_id`) VALUES
-- 文档点赞
(2, 'DOCUMENT', 6), (3, 'DOCUMENT', 6), (4, 'DOCUMENT', 6), (5, 'DOCUMENT', 6),
(1, 'DOCUMENT', 8), (2, 'DOCUMENT', 8), (4, 'DOCUMENT', 8), (6, 'DOCUMENT', 8),
(2, 'DOCUMENT', 10), (3, 'DOCUMENT', 10), (5, 'DOCUMENT', 10), (6, 'DOCUMENT', 10),
(1, 'DOCUMENT', 11), (3, 'DOCUMENT', 11), (5, 'DOCUMENT', 11),
-- 评论点赞
(2, 'COMMENT', 1), (4, 'COMMENT', 1), (5, 'COMMENT', 1), (6, 'COMMENT', 1), (1, 'COMMENT', 1),
(3, 'COMMENT', 2), (5, 'COMMENT', 2),
(6, 'COMMENT', 3),
(3, 'COMMENT', 4), (5, 'COMMENT', 4), (6, 'COMMENT', 4),
(2, 'COMMENT', 5), (3, 'COMMENT', 5), (4, 'COMMENT', 5), (5, 'COMMENT', 5), (1, 'COMMENT', 5), (7, 'COMMENT', 5), (6, 'COMMENT', 5),
(1, 'COMMENT', 6), (4, 'COMMENT', 6), (5, 'COMMENT', 6), (6, 'COMMENT', 6),
(4, 'COMMENT', 7), (5, 'COMMENT', 7),
(5, 'COMMENT', 8);

-- ========================================
-- 9. 插入收藏数据
-- ========================================

INSERT INTO `favorites` (`user_id`, `document_id`) VALUES
(2, 6), (2, 8), (2, 10),     -- 张伟收藏
(3, 6), (3, 11), (3, 12),    -- 李娜收藏
(4, 8), (4, 10), (4, 11),    -- 王明收藏
(5, 6), (5, 9), (5, 10),     -- 陈慧收藏
(6, 10), (6, 11);            -- 刘强收藏

-- ========================================
-- 10. 插入用户组数据
-- ========================================

INSERT INTO `groups` (`id`, `space_id`, `name`, `description`) VALUES
(1, 1, '产品团队', '负责产品开发和设计的核心团队'),
(2, 1, '测试团队', '负责产品质量保证的团队'),
(3, 2, '运营团队', '负责产品运营和推广的团队'),
(4, 3, '技术团队', '技术研发和架构设计团队'),
(5, 4, '管理团队', '公司管理层和行政团队');

INSERT INTO `group_members` (`group_id`, `user_id`, `role`) VALUES
(1, 2, 'ADMIN'), (1, 3, 'MEMBER'),           -- 产品团队
(2, 4, 'ADMIN'), (2, 6, 'MEMBER'),           -- 测试团队
(3, 3, 'ADMIN'), (3, 5, 'MEMBER'),           -- 运营团队
(4, 4, 'ADMIN'), (4, 6, 'MEMBER'), (4, 2, 'MEMBER'),  -- 技术团队
(5, 1, 'ADMIN');                             -- 管理团队

-- ========================================
-- 11. 插入权限数据
-- ========================================

INSERT INTO `permissions` (`principal_type`, `principal_id`, `target_type`, `target_id`, `permission_level`, `granted_by`) VALUES
-- 空间权限
('USER', 2, 'SPACE', 1, 'owner', 1),     -- 张伟是产品开发文档空间的所有者
('USER', 3, 'SPACE', 2, 'owner', 1),     -- 李娜是运营指南空间的所有者
('USER', 4, 'SPACE', 3, 'owner', 1),     -- 王明是技术分享空间的所有者
('GROUP', 1, 'SPACE', 1, 'write', 2),    -- 产品团队对产品开发文档有写权限
('GROUP', 4, 'SPACE', 3, 'write', 4),    -- 技术团队对技术分享有写权限
-- 文档权限
('USER', 4, 'DOCUMENT', 6, 'write', 2),  -- 王明对API文档有写权限
('USER', 5, 'DOCUMENT', 9, 'write', 3),  -- 陈慧对用户运营策略有写权限
('GROUP', 2, 'DOCUMENT', 7, 'read', 2);  -- 测试团队对文档管理API有读权限

-- ========================================
-- 12. 插入通知数据
-- ========================================

INSERT INTO `notifications` (`recipient_id`, `actor_id`, `action_type`, `target_type`, `target_id`, `title`, `content`, `is_read`) VALUES
(2, 3, 'DOCUMENT_COMMENT', 'DOCUMENT', 6, '李娜评论了您的文档', '李娜在"用户认证API"中添加了评论', true),
(2, 4, 'DOCUMENT_COMMENT', 'DOCUMENT', 6, '王明评论了您的文档', '王明在"用户认证API"中添加了评论', false),
(3, 2, 'DOCUMENT_LIKE', 'DOCUMENT', 8, '张伟点赞了您的文档', '张伟点赞了您的文档"UI设计规范"', true),
(4, 6, 'DOCUMENT_COMMENT', 'DOCUMENT', 10, '刘强评论了您的文档', '刘强在"React Hooks最佳实践"中添加了评论', false),
(1, NULL, 'SYSTEM_NOTICE', 'USER', 1, '系统升级通知', '系统将于今晚22:00-24:00进行升级维护', false),
(3, 5, 'DOCUMENT_LIKE', 'DOCUMENT', 9, '陈慧点赞了您的文档', '陈慧点赞了您的文档"用户运营策略"', true);

-- ========================================
-- 13. 插入文档模板数据
-- ========================================

INSERT INTO `doc_templates` (`id`, `space_id`, `name`, `description`, `content`, `category`, `creator_id`, `is_public`, `usage_count`) VALUES
(1, NULL, 'API文档模板', '标准的API接口文档模板', '# {API_NAME} API文档

## 概述
{API_DESCRIPTION}

## 接口地址
`{METHOD} {URL}`

## 请求参数
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| {PARAM_NAME} | {PARAM_TYPE} | {REQUIRED} | {PARAM_DESC} |

## 响应示例
```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

## 错误码说明
| 错误码 | 说明 |
|--------|------|
| 400 | 请求参数错误 |
| 401 | 未授权 |
| 500 | 服务器内部错误 |', 'API', 2, true, 15),

(2, 1, '功能设计文档', '产品功能设计文档模板', '# {FEATURE_NAME} 功能设计

## 功能概述
{FEATURE_OVERVIEW}

## 用户故事
作为一个{USER_ROLE}，我希望{USER_GOAL}，以便{USER_BENEFIT}。

## 功能需求
### 核心功能
1. {CORE_FEATURE_1}
2. {CORE_FEATURE_2}

### 扩展功能
1. {EXTENDED_FEATURE_1}
2. {EXTENDED_FEATURE_2}

## UI/UX设计
### 页面布局
{UI_LAYOUT_DESC}

### 交互流程
{INTERACTION_FLOW}

## 技术实现
### 前端实现
{FRONTEND_IMPLEMENTATION}

### 后端实现
{BACKEND_IMPLEMENTATION}

## 测试计划
### 功能测试
{FUNCTIONAL_TEST}

### 性能测试
{PERFORMANCE_TEST}

## 上线计划
{RELEASE_PLAN}', '产品设计', 3, false, 8),

(3, 3, '技术分享模板', '技术分享文档的标准模板', '# {TOPIC_NAME} 技术分享

## 分享者
**姓名**: {AUTHOR_NAME}
**职位**: {AUTHOR_POSITION}
**时间**: {SHARE_DATE}

## 背景介绍
{BACKGROUND}

## 核心内容
### 1. {MAIN_POINT_1}
{CONTENT_1}

### 2. {MAIN_POINT_2}
{CONTENT_2}

### 3. {MAIN_POINT_3}
{CONTENT_3}

## 代码示例
```{LANGUAGE}
{CODE_EXAMPLE}
```

## 最佳实践
1. {BEST_PRACTICE_1}
2. {BEST_PRACTICE_2}
3. {BEST_PRACTICE_3}

## 注意事项
- {WARNING_1}
- {WARNING_2}

## 参考资料
- [{REFERENCE_1}]({URL_1})
- [{REFERENCE_2}]({URL_2})

## Q&A
**Q**: {QUESTION_1}
**A**: {ANSWER_1}

## 总结
{SUMMARY}', '技术', 4, true, 12);

-- ========================================
-- 14. 插入审计日志数据
-- ========================================

INSERT INTO `audit_logs` (`actor_id`, `action`, `target_type`, `target_id`, `details`, `ip_address`, `user_agent`) VALUES
(2, 'CREATE_DOCUMENT', 'DOCUMENT', 6, '{"title": "用户认证API", "space_id": 1}', '192.168.1.100', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36'),
(3, 'UPDATE_DOCUMENT', 'DOCUMENT', 8, '{"title": "UI设计规范", "change_type": "content_update"}', '192.168.1.101', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36'),
(4, 'CREATE_COMMENT', 'COMMENT', 2, '{"document_id": 6, "content": "建议添加更多的错误码说明"}', '192.168.1.102', 'Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36'),
(1, 'CREATE_SPACE', 'SPACE', 4, '{"name": "公司政策", "description": "人力资源和公司政策文档"}', '192.168.1.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36'),
(5, 'DELETE_DOCUMENT', 'DOCUMENT', 13, '{"title": "项目A总结报告", "action": "soft_delete"}', '192.168.1.103', 'Mozilla/5.0 (iPhone; CPU iPhone OS 15_0 like Mac OS X) AppleWebKit/605.1.15');

-- ========================================
-- 15. 插入全文搜索数据
-- ========================================

INSERT INTO `documents_fts` (`document_id`, `title`, `content`, `tags`, `author_name`, `space_name`) VALUES
(6, '用户认证API', '用户认证API文档 概述 本文档描述了用户认证相关的API接口 登录接口 POST /api/v1/auth/login', 'API,Spring Boot', '张伟', '产品开发文档'),
(7, '文档管理API', '文档管理API 概述 文档相关的CRUD操作接口 获取文档列表 GET /api/v1/spaces/{spaceId}/documents', 'API,Spring Boot', '王明', '产品开发文档'),
(8, 'UI设计规范', 'UI设计规范 颜色规范 主色调 字体规范 字体大小 间距规范 标准间距 组件规范 按钮 表单', '设计规范,文档', '李娜', '产品开发文档'),
(9, '用户运营策略', '用户运营策略 用户获取策略 内容营销 合作推广 产品优化 用户留存策略 产品价值 用户服务 社区建设', '文档', '李娜', '运营指南'),
(10, 'React Hooks最佳实践', 'React Hooks最佳实践 useState使用技巧 合理拆分状态 函数式更新 useEffect使用技巧 正确设置依赖数组 清理副作用', 'React,文档', '王明', '技术分享'),
(11, 'MySQL性能优化指南', 'MySQL性能优化指南 索引优化 选择合适的索引类型 创建复合索引 避免索引失效 查询优化 使用EXPLAIN分析查询', '数据库,性能', '刘强', '技术分享'),
(12, '员工手册', '员工手册 工作时间与考勤 标准工作时间 弹性工作制 考勤管理 薪酬福利 薪资结构 福利待遇 培训发展', '文档', '系统管理员', '公司政策');

-- ========================================
-- 16. 更新文档的点赞数和查看数
-- ========================================

UPDATE `documents` SET
  `like_count` = (SELECT COUNT(*) FROM `likes` WHERE `target_type` = 'DOCUMENT' AND `target_id` = `documents`.`id`),
  `view_count` = FLOOR(RAND() * 500) + 50
WHERE `content_type` = 'DOCUMENT';

-- ========================================
-- 插入测试数据完成
-- ========================================

COMMIT;

-- 验证数据插入
SELECT
  '数据插入完成！' as status,
  (SELECT COUNT(*) FROM users) as user_count,
  (SELECT COUNT(*) FROM spaces) as space_count,
  (SELECT COUNT(*) FROM documents) as document_count,
  (SELECT COUNT(*) FROM comments) as comment_count,
  (SELECT COUNT(*) FROM likes) as like_count,
  (SELECT COUNT(*) FROM tags) as tag_count;