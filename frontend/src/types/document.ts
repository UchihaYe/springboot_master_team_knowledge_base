// 文档状态枚举
export enum DocumentStatus {
  DRAFT = 'draft',           // 草稿
  PUBLISHED = 'published',   // 已发布
  REVIEW = 'review',         // 待审核
  ARCHIVED = 'archived',     // 已归档
  LOCKED = 'locked'          // 编辑锁定
}

// 文档权限枚举
export enum DocumentPermission {
  READ = 'read',           // 可查看
  COMMENT = 'comment',     // 可评论
  EDIT = 'edit',          // 可编辑
  MANAGE = 'manage'       // 可管理
}

// 用户角色枚举
export enum UserRole {
  VIEWER = 'viewer',           // 访客
  COMMENTER = 'commenter',     // 评论者
  EDITOR = 'editor',           // 编辑者
  SPACE_ADMIN = 'space_admin', // 空间管理员
  SYSTEM_ADMIN = 'system_admin' // 系统管理员
}

// 文档接口
export interface Document {
  id: string
  title: string
  content: string
  description?: string
  tags: string[]
  author: {
    id: string
    name: string
    avatar?: string
  }
  status: DocumentStatus
  createdAt: string
  updatedAt: string
  publishedAt?: string
  views: number
  likes: number
  comments: number
  liked: boolean
  favorited: boolean
  permissions: DocumentPermission[]
  spaceId: string
  spaceName: string
  reviewers?: Array<{
    id: string
    name: string
  }>
  reviewDeadline?: string
  version: number
  isLocked?: boolean
  lockedBy?: {
    id: string
    name: string
  }
  lockedAt?: string
}

// 文档创建/更新请求
export interface DocumentRequest {
  title: string
  content: string
  description?: string
  tags?: string[]
  status?: DocumentStatus
  spaceId: string
  reviewers?: string[]
  reviewDeadline?: string
}

// 文档状态变更请求
export interface DocumentStatusRequest {
  status: DocumentStatus
  reason?: string
}

// 权限检查结果
export interface PermissionCheckResult {
  canRead: boolean
  canComment: boolean
  canEdit: boolean
  canManage: boolean
  canPublish: boolean
  canArchive: boolean
  canDelete: boolean
  canLock: boolean
}

// 文档版本历史
export interface DocumentVersion {
  id: string
  version: number
  title: string
  content: string
  description?: string
  author: {
    id: string
    name: string
    avatar?: string
  }
  createdAt: string
  changeType: 'create' | 'update' | 'publish' | 'archive' | 'restore'
  changeDescription: string
  contentDiff?: string
  size: number
}