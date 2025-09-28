# MySQL 数据库初始化说明

本目录包含团队在线知识库项目的MySQL数据库初始化脚本和测试数据。

## 文件说明

### 1. `knowledge_management_schema.sql` （推荐使用）
- **用途**: 数据库表结构初始化（无触发器版本）
- **内容**: 包含所有20个数据表的完整结构定义
- **功能**:
  - 创建数据库和所有表
  - 定义外键关系
  - 创建索引（包括全文索引）
  - 创建有用的视图
- **兼容性**: 适用于所有MySQL客户端工具

### 1.1. `knowledge_management_init.sql` （已弃用）
- **状态**: 由于触发器语法兼容性问题，已替换为上述文件
- **问题**: 某些MySQL客户端不支持DELIMITER语句

### 2. `test_data.sql`
- **用途**: 测试数据初始化
- **内容**: 包含完整的测试数据集
- **数据量**:
  - 7个测试用户（包括管理员）
  - 5个知识空间
  - 13个文档（包括文件夹和具体文档）
  - 10个标签
  - 8条评论
  - 多条点赞、收藏、权限等数据

### 3. `triggers.sql` （可选）
- **用途**: 数据库触发器（自动维护计数字段）
- **内容**: 点赞数、回复数等自动计算触发器
- **使用**: 仅在命令行MySQL客户端中执行
- **注意**: GUI工具可能不支持DELIMITER语句

### 4. `SQL_README.md`
- **用途**: 使用说明文档
- **内容**: 详细的使用指南和数据说明

## 使用方法

### 方法一：命令行执行（推荐）

```bash
# 1. 连接MySQL数据库
mysql -u root -p

# 2. 执行表结构初始化
mysql> source /path/to/knowledge_management_schema.sql

# 3. 执行测试数据插入
mysql> source /path/to/test_data.sql

# 4. 可选：执行触发器创建
mysql> source /path/to/triggers.sql
```

### 方法二：使用MySQL客户端工具
1. 打开Navicat/MySQL Workbench等客户端工具
2. 连接到MySQL服务器
3. 执行表结构文件: `knowledge_management_schema.sql`
4. 执行测试数据文件: `test_data.sql`
5. **注意**: 不要在GUI工具中执行 `triggers.sql`

### 方法三：Docker环境自动初始化
在docker-compose配置中已经设置了自动初始化：

```yaml
mysql:
  image: mysql:8.0
  volumes:
    - ./docs/knowledge_management_schema.sql:/docker-entrypoint-initdb.d/01-schema.sql
    - ./docs/test_data.sql:/docker-entrypoint-initdb.d/02-data.sql
    - ./docs/triggers.sql:/docker-entrypoint-initdb.d/03-triggers.sql
```

## 测试用户账号

### 管理员账号
- **邮箱**: `admin@company.com`
- **密码**: `password123`
- **权限**: 系统管理员

### 普通用户账号
| 邮箱 | 姓名 | 密码 | 角色 |
|------|------|------|------|
| zhang.wei@company.com | 张伟 | password123 | 产品经理 |
| li.na@company.com | 李娜 | password123 | UI设计师 |
| wang.ming@company.com | 王明 | password123 | 前端开发 |
| chen.hui@company.com | 陈慧 | password123 | 运营专员 |
| liu.qiang@company.com | 刘强 | password123 | 后端开发 |

> **注意**: 密码哈希值是使用BCrypt加密的`password123`

## 数据库表结构概览

### 核心业务表
1. **users** - 用户表
2. **spaces** - 知识空间表
3. **documents** - 文档表
4. **doc_versions** - 文档版本历史表
5. **doc_drafts** - 文档草稿表

### 内容管理表
6. **attachments** - 附件表
7. **tags** - 标签表
8. **document_tags** - 文档标签关联表
9. **comments** - 评论表
10. **doc_templates** - 文档模板表

### 权限管理表
11. **groups** - 用户组表
12. **group_members** - 用户组成员表
13. **permissions** - 权限表

### 交互功能表
14. **favorites** - 收藏表
15. **likes** - 点赞表
16. **notifications** - 通知表

### 系统功能表
17. **audit_logs** - 审计日志表
18. **document_locks** - 文档编辑锁表
19. **document_reviewers** - 文档审查者表
20. **documents_fts** - 全文搜索表

## 重要特性

### 1. 软删除支持
- 文档和空间支持软删除（deleted_at字段）
- 可以实现回收站功能

### 2. 全文搜索
- 基于MySQL的FULLTEXT索引
- 支持中英文混合搜索
- documents_fts表用于优化搜索性能

### 3. 权限系统
- 支持用户和用户组权限
- 多级权限：read, write, admin, owner
- 支持空间级和文档级权限控制

### 4. 版本控制
- 文档版本历史记录
- 支持版本恢复
- 草稿系统支持多用户协作

### 5. 数据一致性
- 外键约束保证数据完整性
- 触发器维护计数字段
- 事务支持保证操作原子性

## 性能优化

### 索引策略
- 主键和外键自动索引
- 常用查询字段创建单列索引
- 复合查询创建组合索引
- 全文搜索使用FULLTEXT索引

### 触发器维护
- 自动维护like_count、reply_count等计数字段
- 自动同步全文搜索表数据
- 减少应用层计算压力

### 视图优化
- v_document_details: 文档详情聚合视图
- v_user_stats: 用户统计信息视图
- v_space_stats: 空间统计信息视图

## 数据校验

执行完初始化脚本后，可以运行以下查询验证数据：

```sql
-- 检查基础数据
SELECT
  '数据统计' as category,
  (SELECT COUNT(*) FROM users) as user_count,
  (SELECT COUNT(*) FROM spaces) as space_count,
  (SELECT COUNT(*) FROM documents) as document_count,
  (SELECT COUNT(*) FROM comments) as comment_count;

-- 检查权限数据
SELECT COUNT(*) as permission_count FROM permissions;

-- 检查全文搜索数据
SELECT COUNT(*) as fts_count FROM documents_fts;

-- 检查触发器是否正常工作
SELECT id, title, like_count, view_count FROM documents WHERE content_type = 'DOCUMENT';
```

## 注意事项

1. **字符集**: 确保MySQL服务器支持utf8mb4字符集
2. **时区**: 建议设置为UTC或本地时区
3. **权限**: 确保MySQL用户有CREATE、INSERT、UPDATE、DELETE权限
4. **内存**: InnoDB缓冲池建议设置为可用内存的70-80%
5. **备份**: 建议定期备份数据库

## 故障排除

### 常见问题

1. **触发器语法错误 [42000][1064]**
   ```
   错误: You have an error in your SQL syntax... near 'DELIMITER $$'
   ```
   **解决方案**:
   - 使用 `knowledge_management_schema.sql` 而不是原始的 init.sql
   - 在命令行MySQL客户端中执行 `triggers.sql`
   - 不要在Navicat、MySQL Workbench等GUI工具中执行触发器文件

2. **外键约束错误**
   ```sql
   SET FOREIGN_KEY_CHECKS = 0;
   -- 执行问题语句
   SET FOREIGN_KEY_CHECKS = 1;
   ```

3. **字符集问题**
   ```sql
   ALTER DATABASE knowledge_management CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

4. **权限不足**
   ```sql
   GRANT ALL PRIVILEGES ON knowledge_management.* TO 'your_user'@'%';
   FLUSH PRIVILEGES;
   ```

## 更新记录

- **v1.0** (2025-09-28): 初始版本，包含完整的表结构和测试数据