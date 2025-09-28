-- ========================================
-- 团队在线知识库 - MySQL 触发器脚本
-- 版本: v1.0
-- 创建日期: 2025-09-28
-- 描述: 用于自动维护计数字段的触发器
-- 注意: 请在命令行MySQL客户端中执行，某些GUI工具可能不支持
-- ========================================

USE knowledge_management;

-- ========================================
-- 创建触发器用于维护计数字段
-- ========================================

-- 删除已存在的触发器（如果有）
DROP TRIGGER IF EXISTS tr_document_like_count_insert;
DROP TRIGGER IF EXISTS tr_document_like_count_delete;
DROP TRIGGER IF EXISTS tr_comment_like_count_insert;
DROP TRIGGER IF EXISTS tr_comment_like_count_delete;
DROP TRIGGER IF EXISTS tr_comment_reply_count_insert;
DROP TRIGGER IF EXISTS tr_comment_reply_count_delete;
DROP TRIGGER IF EXISTS tr_documents_fts_insert;
DROP TRIGGER IF EXISTS tr_documents_fts_update;
DROP TRIGGER IF EXISTS tr_documents_fts_delete;

-- 文档点赞数维护触发器
DELIMITER $$

CREATE TRIGGER tr_document_like_count_insert
AFTER INSERT ON likes
FOR EACH ROW
BEGIN
  IF NEW.target_type = 'DOCUMENT' THEN
    UPDATE documents SET like_count = like_count + 1 WHERE id = NEW.target_id;
  END IF;
END$$

CREATE TRIGGER tr_document_like_count_delete
AFTER DELETE ON likes
FOR EACH ROW
BEGIN
  IF OLD.target_type = 'DOCUMENT' THEN
    UPDATE documents SET like_count = like_count - 1 WHERE id = OLD.target_id;
  END IF;
END$$

-- 评论点赞数维护触发器
CREATE TRIGGER tr_comment_like_count_insert
AFTER INSERT ON likes
FOR EACH ROW
BEGIN
  IF NEW.target_type = 'COMMENT' THEN
    UPDATE comments SET like_count = like_count + 1 WHERE id = NEW.target_id;
  END IF;
END$$

CREATE TRIGGER tr_comment_like_count_delete
AFTER DELETE ON likes
FOR EACH ROW
BEGIN
  IF OLD.target_type = 'COMMENT' THEN
    UPDATE comments SET like_count = like_count - 1 WHERE id = OLD.target_id;
  END IF;
END$$

-- 评论回复数维护触发器
CREATE TRIGGER tr_comment_reply_count_insert
AFTER INSERT ON comments
FOR EACH ROW
BEGIN
  IF NEW.parent_id IS NOT NULL THEN
    UPDATE comments SET reply_count = reply_count + 1 WHERE id = NEW.parent_id;
  END IF;
END$$

CREATE TRIGGER tr_comment_reply_count_delete
AFTER DELETE ON comments
FOR EACH ROW
BEGIN
  IF OLD.parent_id IS NOT NULL THEN
    UPDATE comments SET reply_count = reply_count - 1 WHERE id = OLD.parent_id;
  END IF;
END$$

-- 全文搜索表同步触发器
CREATE TRIGGER tr_documents_fts_insert
AFTER INSERT ON documents
FOR EACH ROW
BEGIN
  IF NEW.content_type = 'DOCUMENT' AND NEW.status = 'PUBLISHED' THEN
    INSERT INTO documents_fts (document_id, title, content, author_name, space_name)
    SELECT NEW.id, NEW.title, COALESCE(NEW.content, ''), u.display_name, s.name
    FROM users u, spaces s
    WHERE u.id = NEW.creator_id AND s.id = NEW.space_id;
  END IF;
END$$

CREATE TRIGGER tr_documents_fts_update
AFTER UPDATE ON documents
FOR EACH ROW
BEGIN
  IF NEW.content_type = 'DOCUMENT' THEN
    IF NEW.status = 'PUBLISHED' THEN
      INSERT INTO documents_fts (document_id, title, content, author_name, space_name)
      SELECT NEW.id, NEW.title, COALESCE(NEW.content, ''), u.display_name, s.name
      FROM users u, spaces s
      WHERE u.id = NEW.creator_id AND s.id = NEW.space_id
      ON DUPLICATE KEY UPDATE
        title = VALUES(title),
        content = VALUES(content),
        author_name = VALUES(author_name),
        space_name = VALUES(space_name);
    ELSE
      DELETE FROM documents_fts WHERE document_id = NEW.id;
    END IF;
  END IF;
END$$

CREATE TRIGGER tr_documents_fts_delete
AFTER DELETE ON documents
FOR EACH ROW
BEGIN
  DELETE FROM documents_fts WHERE document_id = OLD.id;
END$$

DELIMITER ;

-- ========================================
-- 验证触发器创建成功
-- ========================================

SELECT
  'MySQL 触发器创建完成！' AS message,
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.TRIGGERS WHERE TRIGGER_SCHEMA = 'knowledge_management') AS trigger_count;

-- 显示所有创建的触发器
SELECT
  TRIGGER_NAME as '触发器名称',
  EVENT_MANIPULATION as '事件类型',
  EVENT_OBJECT_TABLE as '表名'
FROM INFORMATION_SCHEMA.TRIGGERS
WHERE TRIGGER_SCHEMA = 'knowledge_management'
ORDER BY EVENT_OBJECT_TABLE, EVENT_MANIPULATION;