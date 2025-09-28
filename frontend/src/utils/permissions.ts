import { UserRole, DocumentStatus, DocumentPermission, type PermissionCheckResult } from '@/types/document'

// 获取用户在文档上的权限
export function checkDocumentPermissions(
  userRole: UserRole,
  documentStatus: DocumentStatus,
  isAuthor: boolean = false,
  isLocked: boolean = false,
  lockedByCurrentUser: boolean = false
): PermissionCheckResult {
  const permissions: PermissionCheckResult = {
    canRead: false,
    canComment: false,
    canEdit: false,
    canManage: false,
    canPublish: false,
    canArchive: false,
    canDelete: false,
    canLock: false
  }

  // 系统管理员拥有所有权限
  if (userRole === UserRole.SYSTEM_ADMIN) {
    return {
      canRead: true,
      canComment: true,
      canEdit: !isLocked || lockedByCurrentUser,
      canManage: true,
      canPublish: true,
      canArchive: true,
      canDelete: true,
      canLock: true
    }
  }

  // 空间管理员权限
  if (userRole === UserRole.SPACE_ADMIN) {
    permissions.canRead = true
    permissions.canComment = documentStatus === DocumentStatus.PUBLISHED
    permissions.canEdit = !isLocked || lockedByCurrentUser
    permissions.canManage = true
    permissions.canPublish = true
    permissions.canArchive = true
    permissions.canDelete = true
    permissions.canLock = true
    return permissions
  }

  // 编辑者权限
  if (userRole === UserRole.EDITOR) {
    permissions.canRead = true
    permissions.canComment = documentStatus === DocumentStatus.PUBLISHED
    permissions.canEdit = (documentStatus === DocumentStatus.DRAFT || documentStatus === DocumentStatus.REVIEW) &&
                         (!isLocked || lockedByCurrentUser) &&
                         (isAuthor || documentStatus === DocumentStatus.DRAFT)
    permissions.canManage = isAuthor
    permissions.canPublish = isAuthor && documentStatus === DocumentStatus.DRAFT
    permissions.canDelete = isAuthor && documentStatus === DocumentStatus.DRAFT
    permissions.canLock = isAuthor
    return permissions
  }

  // 评论者权限
  if (userRole === UserRole.COMMENTER) {
    permissions.canRead = documentStatus === DocumentStatus.PUBLISHED ||
                         documentStatus === DocumentStatus.ARCHIVED
    permissions.canComment = documentStatus === DocumentStatus.PUBLISHED
    return permissions
  }

  // 访客权限
  if (userRole === UserRole.VIEWER) {
    permissions.canRead = documentStatus === DocumentStatus.PUBLISHED ||
                         documentStatus === DocumentStatus.ARCHIVED
    return permissions
  }

  return permissions
}

// 检查文档是否可以点赞
export function canLikeDocument(
  userRole: UserRole,
  documentStatus: DocumentStatus
): boolean {
  // 只有发布状态的文档才能被点赞
  if (documentStatus !== DocumentStatus.PUBLISHED) {
    return false
  }

  // 评论者及以上角色都可以点赞
  return [
    UserRole.COMMENTER,
    UserRole.EDITOR,
    UserRole.SPACE_ADMIN,
    UserRole.SYSTEM_ADMIN
  ].includes(userRole)
}

// 检查文档是否可以评论
export function canCommentDocument(
  userRole: UserRole,
  documentStatus: DocumentStatus
): boolean {
  // 只有发布状态的文档才能被评论
  if (documentStatus !== DocumentStatus.PUBLISHED) {
    return false
  }

  // 评论者及以上角色都可以评论
  return [
    UserRole.COMMENTER,
    UserRole.EDITOR,
    UserRole.SPACE_ADMIN,
    UserRole.SYSTEM_ADMIN
  ].includes(userRole)
}

// 获取文档状态显示信息
export function getDocumentStatusInfo(status: DocumentStatus) {
  const statusMap = {
    [DocumentStatus.DRAFT]: {
      label: '草稿',
      color: 'info',
      description: '文档正在编辑中，尚未发布'
    },
    [DocumentStatus.PUBLISHED]: {
      label: '已发布',
      color: 'success',
      description: '文档已发布，对有权限的用户可见'
    },
    [DocumentStatus.REVIEW]: {
      label: '待审核',
      color: 'warning',
      description: '文档等待审核中'
    },
    [DocumentStatus.ARCHIVED]: {
      label: '已归档',
      color: 'info',
      description: '文档已归档，只读状态'
    },
    [DocumentStatus.LOCKED]: {
      label: '编辑锁定',
      color: 'danger',
      description: '文档被锁定，暂时无法编辑'
    }
  }

  return statusMap[status] || {
    label: '未知',
    color: 'info',
    description: '未知状态'
  }
}

// 获取用户角色显示信息
export function getUserRoleInfo(role: UserRole) {
  const roleMap = {
    [UserRole.VIEWER]: {
      label: '访客',
      color: 'info',
      description: '只能查看已发布的内容'
    },
    [UserRole.COMMENTER]: {
      label: '评论者',
      color: 'primary',
      description: '可以查看和评论已发布的内容'
    },
    [UserRole.EDITOR]: {
      label: '编辑者',
      color: 'success',
      description: '可以创建、编辑和发布内容'
    },
    [UserRole.SPACE_ADMIN]: {
      label: '空间管理员',
      color: 'warning',
      description: '可以管理空间内的所有内容和成员'
    },
    [UserRole.SYSTEM_ADMIN]: {
      label: '系统管理员',
      color: 'danger',
      description: '拥有系统的所有权限'
    }
  }

  return roleMap[role] || {
    label: '未知',
    color: 'info',
    description: '未知角色'
  }
}