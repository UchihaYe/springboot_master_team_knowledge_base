-- ========================================
-- 团队在线知识库 - MySQL 数据库表结构脚本
-- 版本: v1.0
-- 创建日期: 2025-09-28
-- 描述: 只包含表结构，不包含触发器
-- ========================================

-- 设置字符集和排序规则
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 删除已存在的数据库（谨慎使用）
-- DROP DATABASE IF EXISTS knowledge_management;

-- 创建数据库
CREATE DATABASE IF NOT EXISTS knowledge_management
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE knowledge_management;

-- ========================================
-- 1. 用户表 (users)
-- ========================================
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `email` VARCHAR(255) NOT NULL UNIQUE COMMENT '邮箱地址',
  `password_hash` VARCHAR(255) NOT NULL COMMENT '密码哈希',
  `display_name` VARCHAR(100) NOT NULL COMMENT '显示名称',
  `avatar_url` VARCHAR(500) NULL COMMENT '头像URL',
  `is_system_admin` BOOLEAN NOT NULL DEFAULT FALSE COMMENT '是否为系统管理员',
  `status` ENUM('ACTIVE', 'INACTIVE', 'SUSPENDED') NOT NULL DEFAULT 'ACTIVE' COMMENT '用户状态',
  `last_login_at` DATETIME NULL COMMENT '最后登录时间',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  INDEX `idx_email` (`email`),
  INDEX `idx_status` (`status`),
  INDEX `idx_created_at` (`created_at`)
) ENGINE=InnoDB COMMENT='用户表';

-- ========================================
-- 2. 知识空间表 (spaces)
-- ========================================
DROP TABLE IF EXISTS `spaces`;
CREATE TABLE `spaces` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '空间ID',
  `name` VARCHAR(200) NOT NULL COMMENT '空间名称',
  `description` TEXT NULL COMMENT '空间描述',
  `owner_id` BIGINT NOT NULL COMMENT '空间所有者ID',
  `status` ENUM('ACTIVE', 'ARCHIVED') NOT NULL DEFAULT 'ACTIVE' COMMENT '空间状态',
  `is_public` BOOLEAN NOT NULL DEFAULT FALSE COMMENT '是否公开空间',
  `settings` JSON NULL COMMENT '空间设置',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted_at` DATETIME NULL COMMENT '删除时间（软删除）',
  PRIMARY KEY (`id`),
  INDEX `idx_owner_id` (`owner_id`),
  INDEX `idx_status` (`status`),
  INDEX `idx_name` (`name`),
  INDEX `idx_deleted_at` (`deleted_at`),
  FOREIGN KEY (`owner_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB COMMENT='知识空间表';

-- ========================================
-- 3. 文档表 (documents)
-- ========================================
DROP TABLE IF EXISTS `documents`;
CREATE TABLE `documents` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '文档ID',
  `title` VARCHAR(500) NOT NULL COMMENT '文档标题',
  `content` LONGTEXT NULL COMMENT '文档内容（Markdown格式）',
  `content_type` ENUM('DOCUMENT', 'FOLDER') NOT NULL DEFAULT 'DOCUMENT' COMMENT '内容类型',
  `space_id` BIGINT NOT NULL COMMENT '所属空间ID',
  `parent_id` BIGINT NULL COMMENT '父文档ID（用于目录结构）',
  `creator_id` BIGINT NOT NULL COMMENT '创建者ID',
  `last_editor_id` BIGINT NULL COMMENT '最后编辑者ID',
  `status` ENUM('PUBLISHED', 'DRAFT', 'ARCHIVED') NOT NULL DEFAULT 'DRAFT' COMMENT '文档状态',
  `view_count` INT NOT NULL DEFAULT 0 COMMENT '查看次数',
  `like_count` INT NOT NULL DEFAULT 0 COMMENT '点赞次数',
  `sort_order` INT NOT NULL DEFAULT 0 COMMENT '排序序号',
  `is_pinned` BOOLEAN NOT NULL DEFAULT FALSE COMMENT '是否置顶',
  `next_review_at` DATETIME NULL COMMENT '下次审查时间',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted_at` DATETIME NULL COMMENT '删除时间（软删除）',
  PRIMARY KEY (`id`),
  INDEX `idx_space_id` (`space_id`),
  INDEX `idx_parent_id` (`parent_id`),
  INDEX `idx_creator_id` (`creator_id`),
  INDEX `idx_status` (`status`),
  INDEX `idx_title` (`title`),
  INDEX `idx_created_at` (`created_at`),
  INDEX `idx_updated_at` (`updated_at`),
  INDEX `idx_deleted_at` (`deleted_at`),
  FULLTEXT INDEX `idx_fulltext_search` (`title`, `content`),
  FOREIGN KEY (`space_id`) REFERENCES `spaces` (`id`) ON DELETE CASCADE,
  FOREIGN KEY (`parent_id`) REFERENCES `documents` (`id`) ON DELETE CASCADE,
  FOREIGN KEY (`creator_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  FOREIGN KEY (`last_editor_id`) REFERENCES `users` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB COMMENT='文档表';

-- ========================================
-- 4. 文档版本历史表 (doc_versions)
-- ========================================
DROP TABLE IF EXISTS `doc_versions`;
CREATE TABLE `doc_versions` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '版本ID',
  `document_id` BIGINT NOT NULL COMMENT '文档ID',
  `version_number` INT NOT NULL COMMENT '版本号',
  `title` VARCHAR(500) NOT NULL COMMENT '文档标题',
  `content` LONGTEXT NULL COMMENT '文档内容',
  `author_id` BIGINT NOT NULL COMMENT '作者ID',
  `comment` TEXT NULL COMMENT '版本说明',
  `change_type` ENUM('CREATE', 'UPDATE', 'RESTORE') NOT NULL DEFAULT 'UPDATE' COMMENT '变更类型',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  INDEX `idx_document_id` (`document_id`),
  INDEX `idx_author_id` (`author_id`),
  INDEX `idx_version_number` (`version_number`),
  INDEX `idx_created_at` (`created_at`),
  UNIQUE KEY `uk_document_version` (`document_id`, `version_number`),
  FOREIGN KEY (`document_id`) REFERENCES `documents` (`id`) ON DELETE CASCADE,
  FOREIGN KEY (`author_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB COMMENT='文档版本历史表';

-- ========================================
-- 5. 文档草稿表 (doc_drafts)
-- ========================================
DROP TABLE IF EXISTS `doc_drafts`;
CREATE TABLE `doc_drafts` (
  `document_id` BIGINT NOT NULL COMMENT '文档ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `title` VARCHAR(500) NOT NULL COMMENT '草稿标题',
  `content` LONGTEXT NULL COMMENT '草稿内容',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`document_id`, `user_id`),
  INDEX `idx_user_id` (`user_id`),
  INDEX `idx_updated_at` (`updated_at`),
  FOREIGN KEY (`document_id`) REFERENCES `documents` (`id`) ON DELETE CASCADE,
  FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB COMMENT='文档草稿表';

-- ========================================
-- 6. 附件表 (attachments)
-- ========================================
DROP TABLE IF EXISTS `attachments`;
CREATE TABLE `attachments` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '附件ID',
  `file_name` VARCHAR(255) NOT NULL COMMENT '文件名',
  `original_name` VARCHAR(255) NOT NULL COMMENT '原始文件名',
  `file_path` VARCHAR(500) NOT NULL COMMENT '文件路径',
  `file_url` VARCHAR(500) NULL COMMENT '文件URL',
  `mime_type` VARCHAR(100) NOT NULL COMMENT 'MIME类型',
  `size_bytes` BIGINT NOT NULL COMMENT '文件大小（字节）',
  `uploader_id` BIGINT NOT NULL COMMENT '上传者ID',
  `document_id` BIGINT NULL COMMENT '关联文档ID',
  `upload_type` ENUM('DOCUMENT', 'AVATAR', 'TEMP') NOT NULL DEFAULT 'DOCUMENT' COMMENT '上传类型',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  INDEX `idx_uploader_id` (`uploader_id`),
  INDEX `idx_document_id` (`document_id`),
  INDEX `idx_file_name` (`file_name`),
  INDEX `idx_upload_type` (`upload_type`),
  INDEX `idx_created_at` (`created_at`),
  FOREIGN KEY (`uploader_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  FOREIGN KEY (`document_id`) REFERENCES `documents` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB COMMENT='附件表';

-- ========================================
-- 7. 标签表 (tags)
-- ========================================
DROP TABLE IF EXISTS `tags`;
CREATE TABLE `tags` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '标签ID',
  `name` VARCHAR(50) NOT NULL UNIQUE COMMENT '标签名称',
  `color` VARCHAR(7) NULL COMMENT '标签颜色（HEX）',
  `description` TEXT NULL COMMENT '标签描述',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `uk_name` (`name`)
) ENGINE=InnoDB COMMENT='标签表';

-- ========================================
-- 8. 文档标签关联表 (document_tags)
-- ========================================
DROP TABLE IF EXISTS `document_tags`;
CREATE TABLE `document_tags` (
  `document_id` BIGINT NOT NULL COMMENT '文档ID',
  `tag_id` BIGINT NOT NULL COMMENT '标签ID',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`document_id`, `tag_id`),
  INDEX `idx_tag_id` (`tag_id`),
  FOREIGN KEY (`document_id`) REFERENCES `documents` (`id`) ON DELETE CASCADE,
  FOREIGN KEY (`tag_id`) REFERENCES `tags` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB COMMENT='文档标签关联表';

-- ========================================
-- 9. 评论表 (comments)
-- ========================================
DROP TABLE IF EXISTS `comments`;
CREATE TABLE `comments` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '评论ID',
  `content` TEXT NOT NULL COMMENT '评论内容',
  `document_id` BIGINT NOT NULL COMMENT '文档ID',
  `author_id` BIGINT NOT NULL COMMENT '作者ID',
  `parent_id` BIGINT NULL COMMENT '父评论ID（用于回复）',
  `like_count` INT NOT NULL DEFAULT 0 COMMENT '点赞数',
  `reply_count` INT NOT NULL DEFAULT 0 COMMENT '回复数',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted_at` DATETIME NULL COMMENT '删除时间（软删除）',
  PRIMARY KEY (`id`),
  INDEX `idx_document_id` (`document_id`),
  INDEX `idx_author_id` (`author_id`),
  INDEX `idx_parent_id` (`parent_id`),
  INDEX `idx_created_at` (`created_at`),
  INDEX `idx_deleted_at` (`deleted_at`),
  FOREIGN KEY (`document_id`) REFERENCES `documents` (`id`) ON DELETE CASCADE,
  FOREIGN KEY (`author_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  FOREIGN KEY (`parent_id`) REFERENCES `comments` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB COMMENT='评论表';

-- ========================================
-- 10. 用户组表 (groups)
-- ========================================
DROP TABLE IF EXISTS `groups`;
CREATE TABLE `groups` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户组ID',
  `space_id` BIGINT NOT NULL COMMENT '所属空间ID',
  `name` VARCHAR(100) NOT NULL COMMENT '用户组名称',
  `description` TEXT NULL COMMENT '用户组描述',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  INDEX `idx_space_id` (`space_id`),
  INDEX `idx_name` (`name`),
  UNIQUE KEY `uk_space_name` (`space_id`, `name`),
  FOREIGN KEY (`space_id`) REFERENCES `spaces` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB COMMENT='用户组表';

-- ========================================
-- 11. 用户组成员表 (group_members)
-- ========================================
DROP TABLE IF EXISTS `group_members`;
CREATE TABLE `group_members` (
  `group_id` BIGINT NOT NULL COMMENT '用户组ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `role` ENUM('MEMBER', 'ADMIN') NOT NULL DEFAULT 'MEMBER' COMMENT '组内角色',
  `joined_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '加入时间',
  PRIMARY KEY (`group_id`, `user_id`),
  INDEX `idx_user_id` (`user_id`),
  FOREIGN KEY (`group_id`) REFERENCES `groups` (`id`) ON DELETE CASCADE,
  FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB COMMENT='用户组成员表';

-- ========================================
-- 12. 权限表 (permissions)
-- ========================================
DROP TABLE IF EXISTS `permissions`;
CREATE TABLE `permissions` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '权限ID',
  `principal_type` ENUM('USER', 'GROUP') NOT NULL COMMENT '主体类型',
  `principal_id` BIGINT NOT NULL COMMENT '主体ID',
  `target_type` ENUM('SPACE', 'DOCUMENT') NOT NULL COMMENT '目标类型',
  `target_id` BIGINT NOT NULL COMMENT '目标ID',
  `permission_level` ENUM('read', 'write', 'admin', 'owner') NOT NULL COMMENT '权限级别',
  `granted_by` BIGINT NOT NULL COMMENT '授权者ID',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  INDEX `idx_principal` (`principal_type`, `principal_id`),
  INDEX `idx_target` (`target_type`, `target_id`),
  INDEX `idx_permission_level` (`permission_level`),
  UNIQUE KEY `uk_permission` (`principal_type`, `principal_id`, `target_type`, `target_id`),
  FOREIGN KEY (`granted_by`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB COMMENT='权限表';

-- ========================================
-- 13. 通知表 (notifications)
-- ========================================
DROP TABLE IF EXISTS `notifications`;
CREATE TABLE `notifications` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '通知ID',
  `recipient_id` BIGINT NOT NULL COMMENT '接收者ID',
  `actor_id` BIGINT NULL COMMENT '触发者ID',
  `action_type` ENUM('DOCUMENT_COMMENT', 'DOCUMENT_LIKE', 'DOCUMENT_MENTION', 'SPACE_INVITE', 'SYSTEM_NOTICE') NOT NULL COMMENT '操作类型',
  `target_type` ENUM('DOCUMENT', 'COMMENT', 'SPACE', 'USER') NOT NULL COMMENT '目标类型',
  `target_id` BIGINT NOT NULL COMMENT '目标ID',
  `title` VARCHAR(200) NOT NULL COMMENT '通知标题',
  `content` TEXT NULL COMMENT '通知内容',
  `is_read` BOOLEAN NOT NULL DEFAULT FALSE COMMENT '是否已读',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  INDEX `idx_recipient_id` (`recipient_id`),
  INDEX `idx_actor_id` (`actor_id`),
  INDEX `idx_action_type` (`action_type`),
  INDEX `idx_target` (`target_type`, `target_id`),
  INDEX `idx_is_read` (`is_read`),
  INDEX `idx_created_at` (`created_at`),
  FOREIGN KEY (`recipient_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  FOREIGN KEY (`actor_id`) REFERENCES `users` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB COMMENT='通知表';

-- ========================================
-- 14. 审计日志表 (audit_logs)
-- ========================================
DROP TABLE IF EXISTS `audit_logs`;
CREATE TABLE `audit_logs` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `actor_id` BIGINT NULL COMMENT '操作者ID',
  `action` VARCHAR(100) NOT NULL COMMENT '操作类型',
  `target_type` ENUM('DOCUMENT', 'SPACE', 'USER', 'COMMENT', 'ATTACHMENT') NOT NULL COMMENT '目标类型',
  `target_id` BIGINT NOT NULL COMMENT '目标ID',
  `details` JSON NULL COMMENT '详细信息',
  `ip_address` VARCHAR(45) NULL COMMENT 'IP地址',
  `user_agent` TEXT NULL COMMENT '用户代理',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  INDEX `idx_actor_id` (`actor_id`),
  INDEX `idx_action` (`action`),
  INDEX `idx_target` (`target_type`, `target_id`),
  INDEX `idx_created_at` (`created_at`),
  FOREIGN KEY (`actor_id`) REFERENCES `users` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB COMMENT='审计日志表';

-- ========================================
-- 15. 文档编辑锁表 (document_locks)
-- ========================================
DROP TABLE IF EXISTS `document_locks`;
CREATE TABLE `document_locks` (
  `document_id` BIGINT NOT NULL COMMENT '文档ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `lock_type` ENUM('EDIT', 'EXCLUSIVE') NOT NULL DEFAULT 'EDIT' COMMENT '锁类型',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `expires_at` DATETIME NOT NULL COMMENT '过期时间',
  PRIMARY KEY (`document_id`),
  INDEX `idx_user_id` (`user_id`),
  INDEX `idx_expires_at` (`expires_at`),
  FOREIGN KEY (`document_id`) REFERENCES `documents` (`id`) ON DELETE CASCADE,
  FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB COMMENT='文档编辑锁表';

-- ========================================
-- 16. 文档审查者表 (document_reviewers)
-- ========================================
DROP TABLE IF EXISTS `document_reviewers`;
CREATE TABLE `document_reviewers` (
  `document_id` BIGINT NOT NULL COMMENT '文档ID',
  `user_id` BIGINT NOT NULL COMMENT '审查者ID',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`document_id`, `user_id`),
  INDEX `idx_user_id` (`user_id`),
  FOREIGN KEY (`document_id`) REFERENCES `documents` (`id`) ON DELETE CASCADE,
  FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB COMMENT='文档审查者表';

-- ========================================
-- 17. 收藏表 (favorites)
-- ========================================
DROP TABLE IF EXISTS `favorites`;
CREATE TABLE `favorites` (
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `document_id` BIGINT NOT NULL COMMENT '文档ID',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
  PRIMARY KEY (`user_id`, `document_id`),
  INDEX `idx_document_id` (`document_id`),
  INDEX `idx_created_at` (`created_at`),
  FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  FOREIGN KEY (`document_id`) REFERENCES `documents` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB COMMENT='收藏表';

-- ========================================
-- 18. 点赞表 (likes)
-- ========================================
DROP TABLE IF EXISTS `likes`;
CREATE TABLE `likes` (
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `target_type` ENUM('DOCUMENT', 'COMMENT') NOT NULL COMMENT '目标类型',
  `target_id` BIGINT NOT NULL COMMENT '目标ID',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '点赞时间',
  PRIMARY KEY (`user_id`, `target_type`, `target_id`),
  INDEX `idx_target` (`target_type`, `target_id`),
  INDEX `idx_created_at` (`created_at`),
  FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB COMMENT='点赞表';

-- ========================================
-- 19. 文档模板表 (doc_templates)
-- ========================================
DROP TABLE IF EXISTS `doc_templates`;
CREATE TABLE `doc_templates` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '模板ID',
  `space_id` BIGINT NULL COMMENT '所属空间ID（NULL表示全局模板）',
  `name` VARCHAR(200) NOT NULL COMMENT '模板名称',
  `description` TEXT NULL COMMENT '模板描述',
  `content` LONGTEXT NOT NULL COMMENT '模板内容',
  `category` VARCHAR(50) NULL COMMENT '模板分类',
  `creator_id` BIGINT NOT NULL COMMENT '创建者ID',
  `is_public` BOOLEAN NOT NULL DEFAULT FALSE COMMENT '是否公开模板',
  `usage_count` INT NOT NULL DEFAULT 0 COMMENT '使用次数',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  INDEX `idx_space_id` (`space_id`),
  INDEX `idx_creator_id` (`creator_id`),
  INDEX `idx_category` (`category`),
  INDEX `idx_is_public` (`is_public`),
  FOREIGN KEY (`space_id`) REFERENCES `spaces` (`id`) ON DELETE CASCADE,
  FOREIGN KEY (`creator_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB COMMENT='文档模板表';

-- ========================================
-- 20. 全文搜索表 (documents_fts)
-- ========================================
DROP TABLE IF EXISTS `documents_fts`;
CREATE TABLE `documents_fts` (
  `document_id` BIGINT NOT NULL COMMENT '文档ID',
  `title` VARCHAR(500) NOT NULL COMMENT '文档标题',
  `content` LONGTEXT NOT NULL COMMENT '文档内容',
  `tags` TEXT NULL COMMENT '标签（逗号分隔）',
  `author_name` VARCHAR(100) NOT NULL COMMENT '作者名称',
  `space_name` VARCHAR(200) NOT NULL COMMENT '空间名称',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`document_id`),
  FULLTEXT INDEX `idx_fulltext_search` (`title`, `content`, `tags`, `author_name`, `space_name`),
  INDEX `idx_updated_at` (`updated_at`),
  FOREIGN KEY (`document_id`) REFERENCES `documents` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB COMMENT='全文搜索表';

-- 重新启用外键检查
SET FOREIGN_KEY_CHECKS = 1;

-- ========================================
-- 创建有用的视图
-- ========================================

-- 文档详情视图
CREATE OR REPLACE VIEW v_document_details AS
SELECT
  d.id,
  d.title,
  d.content_type,
  d.status,
  d.view_count,
  d.like_count,
  d.created_at,
  d.updated_at,
  s.name AS space_name,
  s.id AS space_id,
  creator.display_name AS creator_name,
  creator.id AS creator_id,
  editor.display_name AS last_editor_name,
  parent.title AS parent_title,
  (SELECT COUNT(*) FROM comments WHERE document_id = d.id AND deleted_at IS NULL) AS comment_count,
  (SELECT COUNT(*) FROM favorites WHERE document_id = d.id) AS favorite_count,
  (SELECT GROUP_CONCAT(t.name SEPARATOR ', ') FROM document_tags dt JOIN tags t ON dt.tag_id = t.id WHERE dt.document_id = d.id) AS tags
FROM documents d
JOIN spaces s ON d.space_id = s.id
JOIN users creator ON d.creator_id = creator.id
LEFT JOIN users editor ON d.last_editor_id = editor.id
LEFT JOIN documents parent ON d.parent_id = parent.id
WHERE d.deleted_at IS NULL;

-- 用户统计视图
CREATE OR REPLACE VIEW v_user_stats AS
SELECT
  u.id,
  u.display_name,
  u.email,
  u.created_at,
  (SELECT COUNT(*) FROM documents WHERE creator_id = u.id AND deleted_at IS NULL) AS document_count,
  (SELECT COUNT(*) FROM comments WHERE author_id = u.id AND deleted_at IS NULL) AS comment_count,
  (SELECT COUNT(*) FROM spaces WHERE owner_id = u.id AND deleted_at IS NULL) AS space_count,
  (SELECT SUM(view_count) FROM documents WHERE creator_id = u.id AND deleted_at IS NULL) AS total_views,
  (SELECT SUM(like_count) FROM documents WHERE creator_id = u.id AND deleted_at IS NULL) AS total_likes
FROM users u
WHERE u.status = 'ACTIVE';

-- 空间统计视图
CREATE OR REPLACE VIEW v_space_stats AS
SELECT
  s.id,
  s.name,
  s.description,
  s.status,
  s.created_at,
  owner.display_name AS owner_name,
  (SELECT COUNT(*) FROM documents WHERE space_id = s.id AND deleted_at IS NULL) AS document_count,
  (SELECT COUNT(*) FROM documents WHERE space_id = s.id AND content_type = 'FOLDER' AND deleted_at IS NULL) AS folder_count,
  (SELECT COUNT(DISTINCT creator_id) FROM documents WHERE space_id = s.id AND deleted_at IS NULL) AS contributor_count,
  (SELECT SUM(view_count) FROM documents WHERE space_id = s.id AND deleted_at IS NULL) AS total_views
FROM spaces s
JOIN users owner ON s.owner_id = owner.id
WHERE s.deleted_at IS NULL;

COMMIT;

-- ========================================
-- 脚本执行完成
-- ========================================
SELECT 'MySQL 数据库表结构创建完成！' AS message;