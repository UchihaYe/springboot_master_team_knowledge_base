# MySQL 数据库初始化快速修复指南

## 常见错误和解决方案

### 1. 触发器语法错误 [42000][1064]
```
错误: You have an error in your SQL syntax... near 'DELIMITER $$'
```

**解决方案**:
```sql
-- ✅ 使用这个文件（兼容所有客户端）
knowledge_management_schema.sql

-- ❌ 不要使用这个文件
knowledge_management_init.sql
```

### 2. 重复键错误 [23000][1062] ✅ 已修复
```
错误: Duplicate entry '1-COMMENT-5' for key 'likes.PRIMARY'
```

**解决方案**: 已在最新版本的 `test_data.sql` 中修复

---

## 推荐执行顺序

### 方案 A: GUI工具（Navicat/MySQL Workbench）
```sql
-- 1. 先执行表结构
source knowledge_management_schema.sql

-- 2. 再执行测试数据
source test_data.sql

-- 注意：不要执行 triggers.sql
```

### 方案 B: 命令行（如需触发器）
```bash
mysql -u root -p
mysql> source knowledge_management_schema.sql
mysql> source test_data.sql
mysql> source triggers.sql
```

---

## 数据验证

执行完成后运行以下查询验证：

```sql
-- 基础数据统计
SELECT
  (SELECT COUNT(*) FROM users) as users,
  (SELECT COUNT(*) FROM spaces) as spaces,
  (SELECT COUNT(*) FROM documents) as documents,
  (SELECT COUNT(*) FROM comments) as comments,
  (SELECT COUNT(*) FROM likes) as likes;

-- 应该返回: 7, 5, 13, 8, 38
```

---

## 清理和重置

如果需要重新开始：

```sql
-- 删除数据库（谨慎使用）
DROP DATABASE IF EXISTS knowledge_management;

-- 重新执行初始化脚本
source knowledge_management_schema.sql
source test_data.sql
```

---

## 文件状态

- ✅ `knowledge_management_schema.sql` - 推荐使用，兼容所有工具
- ✅ `test_data.sql` - 已修复重复数据问题
- ⚠️ `triggers.sql` - 仅命令行使用
- ❌ `knowledge_management_init.sql` - 已弃用，触发器语法问题

---

## 测试账号

**管理员**: admin@company.com / password123
**用户**: zhang.wei@company.com / password123

其他测试用户请查看 `SQL_README.md`