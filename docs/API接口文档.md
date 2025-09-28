# 团队在线知识库 API 接口文档

## 文档信息

- **项目名称**: 团队在线知识库
- **API版本**: v1.0
- **文档版本**: v1.0
- **更新时间**: 2025-09-28
- **基础URL**: `https://api.knowledge.example.com/api/v1`

## 概述

本文档描述了团队在线知识库系统的RESTful API接口。系统采用JWT认证，所有接口均返回JSON格式数据。

### API设计原则

- 遵循RESTful架构风格
- 使用标准HTTP状态码
- 统一的响应格式
- 支持分页查询
- 完善的错误处理

### 认证方式

除公开接口外，所有API均需要在请求头中携带JWT令牌：

```
Authorization: Bearer {access_token}
```

### 统一响应格式

#### 成功响应格式

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    // 具体数据
  },
  "timestamp": "2025-09-28T10:30:00"
}
```

#### 分页响应格式

```json
{
  "code": 200,
  "message": "查询成功",
  "data": {
    "content": [],
    "totalElements": 100,
    "totalPages": 10,
    "size": 10,
    "number": 0,
    "first": true,
    "last": false
  },
  "timestamp": "2025-09-28T10:30:00"
}
```

#### 错误响应格式

```json
{
  "code": 400,
  "message": "请求参数错误",
  "errors": [
    {
      "field": "email",
      "message": "邮箱格式不正确"
    }
  ],
  "timestamp": "2025-09-28T10:30:00",
  "path": "/api/v1/auth/register"
}
```

## 错误码说明

| 错误码 | HTTP状态码 | 说明 |
|--------|------------|------|
| 200 | 200 | 请求成功 |
| 201 | 201 | 创建成功 |
| 400 | 400 | 请求参数错误 |
| 401 | 401 | 未认证或认证失败 |
| 403 | 403 | 权限不足 |
| 404 | 404 | 资源不存在 |
| 409 | 409 | 资源冲突 |
| 422 | 422 | 参数验证失败 |
| 429 | 429 | 请求频率限制 |
| 500 | 500 | 服务器内部错误 |

---

## 1. 认证管理模块

### 1.1 用户注册

**POST** `/auth/register`

**描述**: 新用户注册账户

**请求体**:
```json
{
  "email": "user@example.com",
  "password": "password123",
  "displayName": "张三"
}
```

**请求参数说明**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| email | string | 是 | 用户邮箱，需唯一 |
| password | string | 是 | 密码，至少8位包含字母和数字 |
| displayName | string | 是 | 显示名称，2-50字符 |

**成功响应**:
```json
{
  "code": 201,
  "message": "注册成功",
  "data": {
    "id": 12345,
    "email": "user@example.com",
    "displayName": "张三",
    "avatarUrl": null,
    "isSystemAdmin": false,
    "status": "ACTIVE",
    "createdAt": "2025-09-28T10:30:00"
  }
}
```

**错误响应**:
- `409` - 邮箱已存在
- `422` - 参数验证失败

### 1.2 用户登录

**POST** `/auth/login`

**描述**: 用户登录获取访问令牌

**请求体**:
```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

**成功响应**:
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "refresh_token_here",
    "tokenType": "Bearer",
    "expiresIn": 3600,
    "user": {
      "id": 12345,
      "email": "user@example.com",
      "displayName": "张三",
      "avatarUrl": "https://example.com/avatar.jpg",
      "isSystemAdmin": false
    }
  }
}
```

**错误响应**:
- `401` - 邮箱或密码错误
- `403` - 账户被禁用

### 1.3 刷新令牌

**POST** `/auth/refresh`

**请求体**:
```json
{
  "refreshToken": "refresh_token_here"
}
```

**成功响应**:
```json
{
  "code": 200,
  "message": "令牌刷新成功",
  "data": {
    "accessToken": "new_access_token",
    "expiresIn": 3600
  }
}
```

### 1.4 用户登出

**POST** `/auth/logout`

**请求头**: `Authorization: Bearer {accessToken}`

**成功响应**:
```json
{
  "code": 200,
  "message": "登出成功"
}
```

### 1.5 忘记密码

**POST** `/auth/forgot-password`

**请求体**:
```json
{
  "email": "user@example.com"
}
```

**成功响应**:
```json
{
  "code": 200,
  "message": "重置密码邮件已发送"
}
```

### 1.6 重置密码

**POST** `/auth/reset-password`

**请求体**:
```json
{
  "token": "reset_token",
  "newPassword": "newpassword123"
}
```

**成功响应**:
```json
{
  "code": 200,
  "message": "密码重置成功"
}
```

---

## 2. 用户管理模块

### 2.1 获取当前用户信息

**GET** `/users/me`

**权限要求**: 已认证用户

**成功响应**:
```json
{
  "code": 200,
  "data": {
    "id": 12345,
    "email": "user@example.com",
    "displayName": "张三",
    "avatarUrl": "https://example.com/avatar.jpg",
    "isSystemAdmin": false,
    "status": "ACTIVE",
    "lastLoginAt": "2025-09-28T09:00:00",
    "createdAt": "2025-09-01T10:00:00",
    "preferences": {
      "theme": "light",
      "language": "zh-CN",
      "emailNotifications": true
    }
  }
}
```

### 2.2 更新用户信息

**PUT** `/users/me`

**请求体**:
```json
{
  "displayName": "李四",
  "preferences": {
    "theme": "dark",
    "language": "zh-CN",
    "emailNotifications": false
  }
}
```

**成功响应**:
```json
{
  "code": 200,
  "message": "用户信息更新成功",
  "data": {
    "id": 12345,
    "displayName": "李四",
    "preferences": {
      "theme": "dark",
      "language": "zh-CN",
      "emailNotifications": false
    }
  }
}
```

### 2.3 更新密码

**PUT** `/users/me/password`

**请求体**:
```json
{
  "currentPassword": "oldpassword123",
  "newPassword": "newpassword456"
}
```

**成功响应**:
```json
{
  "code": 200,
  "message": "密码更新成功"
}
```

### 2.4 上传头像

**POST** `/users/me/avatar`

**请求**: `multipart/form-data`
- `file`: 图片文件（支持PNG、JPG、GIF，最大5MB）

**成功响应**:
```json
{
  "code": 200,
  "message": "头像上传成功",
  "data": {
    "avatarUrl": "https://example.com/avatars/12345.jpg"
  }
}
```

---

## 3. 知识空间管理模块

### 3.1 创建知识空间

**POST** `/spaces`

**权限要求**: 已认证用户

**请求体**:
```json
{
  "name": "技术文档空间",
  "description": "用于存放技术相关文档",
  "isPublic": false,
  "settings": {
    "theme": "blue",
    "allowComments": true,
    "autoBackup": true
  }
}
```

**成功响应**:
```json
{
  "code": 201,
  "message": "空间创建成功",
  "data": {
    "id": 1001,
    "name": "技术文档空间",
    "description": "用于存放技术相关文档",
    "ownerId": 12345,
    "ownerName": "张三",
    "isPublic": false,
    "status": "ACTIVE",
    "settings": {
      "theme": "blue",
      "allowComments": true,
      "autoBackup": true
    },
    "memberCount": 1,
    "documentCount": 0,
    "createdAt": "2025-09-28T10:30:00"
  }
}
```

### 3.2 获取空间列表

**GET** `/spaces`

**查询参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | integer | 否 | 页码，默认0 |
| size | integer | 否 | 每页大小，默认20 |
| keyword | string | 否 | 搜索关键词 |
| status | string | 否 | 空间状态：ACTIVE, ARCHIVED |

**成功响应**:
```json
{
  "code": 200,
  "data": {
    "content": [
      {
        "id": 1001,
        "name": "技术文档空间",
        "description": "用于存放技术相关文档",
        "ownerName": "张三",
        "isPublic": false,
        "memberCount": 5,
        "documentCount": 25,
        "lastActiveAt": "2025-09-28T09:00:00",
        "role": "OWNER"
      }
    ],
    "totalElements": 1,
    "totalPages": 1,
    "size": 20,
    "number": 0
  }
}
```

### 3.3 获取空间详情

**GET** `/spaces/{spaceId}`

**权限要求**: 空间成员

**成功响应**:
```json
{
  "code": 200,
  "data": {
    "id": 1001,
    "name": "技术文档空间",
    "description": "用于存放技术相关文档",
    "ownerId": 12345,
    "ownerName": "张三",
    "isPublic": false,
    "status": "ACTIVE",
    "settings": {
      "theme": "blue",
      "allowComments": true,
      "autoBackup": true
    },
    "memberCount": 5,
    "documentCount": 25,
    "createdAt": "2025-09-01T10:00:00",
    "updatedAt": "2025-09-28T09:00:00",
    "permission": {
      "canRead": true,
      "canWrite": true,
      "canAdmin": true,
      "canDelete": true
    }
  }
}
```

### 3.4 更新空间信息

**PUT** `/spaces/{spaceId}`

**权限要求**: 空间管理员

**请求体**:
```json
{
  "name": "技术文档空间（更新）",
  "description": "更新后的描述",
  "isPublic": true,
  "settings": {
    "theme": "green",
    "allowComments": true,
    "autoBackup": false
  }
}
```

### 3.5 删除空间

**DELETE** `/spaces/{spaceId}`

**权限要求**: 空间所有者

**成功响应**:
```json
{
  "code": 200,
  "message": "空间删除成功"
}
```

### 3.6 空间成员管理

#### 3.6.1 获取空间成员列表

**GET** `/spaces/{spaceId}/members`

**查询参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | integer | 否 | 页码，默认0 |
| size | integer | 否 | 每页大小，默认20 |
| keyword | string | 否 | 搜索关键词 |
| role | string | 否 | 角色筛选：OWNER, ADMIN, MEMBER |

**成功响应**:
```json
{
  "code": 200,
  "data": {
    "content": [
      {
        "userId": 12345,
        "displayName": "张三",
        "email": "zhang@example.com",
        "avatarUrl": "https://example.com/avatar1.jpg",
        "role": "OWNER",
        "joinedAt": "2025-09-01T10:00:00",
        "lastActiveAt": "2025-09-28T09:00:00"
      }
    ],
    "totalElements": 5,
    "totalPages": 1
  }
}
```

#### 3.6.2 邀请成员

**POST** `/spaces/{spaceId}/members`

**权限要求**: 空间管理员

**请求体**:
```json
{
  "emails": ["user1@example.com", "user2@example.com"],
  "role": "MEMBER",
  "message": "邀请您加入我们的知识空间"
}
```

**成功响应**:
```json
{
  "code": 200,
  "message": "邀请发送成功",
  "data": {
    "invited": 2,
    "failed": 0,
    "details": [
      {
        "email": "user1@example.com",
        "status": "sent"
      },
      {
        "email": "user2@example.com",
        "status": "sent"
      }
    ]
  }
}
```

#### 3.6.3 移除成员

**DELETE** `/spaces/{spaceId}/members/{userId}`

**权限要求**: 空间管理员

---

## 4. 文档管理模块

### 4.1 创建文档

**POST** `/spaces/{spaceId}/documents`

**权限要求**: 空间内CREATE权限

**请求体**:
```json
{
  "title": "API设计规范",
  "content": "# API设计规范\n\n本文档描述了API设计的最佳实践...",
  "contentType": "DOCUMENT",
  "parentId": null,
  "tags": ["API", "规范"],
  "templateId": null
}
```

**请求参数说明**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| title | string | 是 | 文档标题，1-500字符 |
| content | string | 否 | 文档内容（Markdown格式） |
| contentType | string | 是 | 类型：DOCUMENT, FOLDER |
| parentId | integer | 否 | 父文档ID |
| tags | array | 否 | 标签数组 |
| templateId | integer | 否 | 模板ID |

**成功响应**:
```json
{
  "code": 201,
  "message": "文档创建成功",
  "data": {
    "id": 2001,
    "title": "API设计规范",
    "contentType": "DOCUMENT",
    "spaceId": 1001,
    "parentId": null,
    "creatorId": 12345,
    "creatorName": "张三",
    "status": "PUBLISHED",
    "viewCount": 0,
    "likeCount": 0,
    "createdAt": "2025-09-28T10:30:00",
    "updatedAt": "2025-09-28T10:30:00"
  }
}
```

### 4.2 获取文档详情

**GET** `/documents/{documentId}`

**权限要求**: 文档READ权限

**成功响应**:
```json
{
  "code": 200,
  "data": {
    "id": 2001,
    "title": "API设计规范",
    "content": "# API设计规范\n\n本文档描述了API设计的最佳实践...",
    "contentType": "DOCUMENT",
    "spaceId": 1001,
    "spaceName": "技术文档空间",
    "parentId": null,
    "parentTitle": null,
    "creatorId": 12345,
    "creatorName": "张三",
    "creatorAvatar": "https://example.com/avatar.jpg",
    "lastEditorId": 12345,
    "lastEditorName": "张三",
    "status": "PUBLISHED",
    "viewCount": 156,
    "likeCount": 8,
    "isLiked": false,
    "isFavorited": true,
    "tags": [
      {
        "id": 101,
        "name": "API",
        "color": "#FF5722"
      },
      {
        "id": 102,
        "name": "规范",
        "color": "#4CAF50"
      }
    ],
    "attachments": [
      {
        "id": 3001,
        "fileName": "api-example.png",
        "originalName": "api示例.png",
        "fileSize": 152048,
        "mimeType": "image/png",
        "url": "/api/v1/attachments/3001/download"
      }
    ],
    "createdAt": "2025-09-20T10:00:00",
    "updatedAt": "2025-09-28T09:30:00",
    "permission": {
      "canRead": true,
      "canEdit": true,
      "canComment": true,
      "canDelete": false,
      "canShare": true
    }
  }
}
```

### 4.3 更新文档

**PUT** `/documents/{documentId}`

**权限要求**: 文档EDIT权限

**请求体**:
```json
{
  "title": "API设计规范（更新版）",
  "content": "# API设计规范（更新版）\n\n本文档描述了API设计的最佳实践...",
  "tags": ["API", "规范", "2024"],
  "versionComment": "添加了新的示例和最佳实践"
}
```

**成功响应**:
```json
{
  "code": 200,
  "message": "文档更新成功",
  "data": {
    "id": 2001,
    "title": "API设计规范（更新版）",
    "version": 3,
    "updatedAt": "2025-09-28T10:30:00"
  }
}
```

### 4.4 删除文档

**DELETE** `/documents/{documentId}`

**权限要求**: 文档DELETE权限

**成功响应**:
```json
{
  "code": 200,
  "message": "文档已移至回收站"
}
```

### 4.5 获取文档树

**GET** `/spaces/{spaceId}/documents/tree`

**权限要求**: 空间READ权限

**查询参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| parentId | integer | 否 | 父节点ID，null为根节点 |
| depth | integer | 否 | 树深度，默认3 |

**成功响应**:
```json
{
  "code": 200,
  "data": [
    {
      "id": 2001,
      "title": "API设计规范",
      "contentType": "DOCUMENT",
      "parentId": null,
      "sortOrder": 1,
      "isPinned": true,
      "hasPermission": true,
      "children": [],
      "createdAt": "2025-09-20T10:00:00",
      "updatedAt": "2025-09-28T09:30:00"
    },
    {
      "id": 2002,
      "title": "技术文档",
      "contentType": "FOLDER",
      "parentId": null,
      "sortOrder": 2,
      "isPinned": false,
      "hasPermission": true,
      "children": [
        {
          "id": 2003,
          "title": "前端开发指南",
          "contentType": "DOCUMENT",
          "parentId": 2002,
          "sortOrder": 1,
          "isPinned": false,
          "hasPermission": true,
          "children": []
        }
      ]
    }
  ]
}
```

### 4.6 移动文档

**PUT** `/documents/{documentId}/move`

**权限要求**: 文档EDIT权限和目标位置CREATE权限

**请求体**:
```json
{
  "targetParentId": 2002,
  "targetSpaceId": 1001,
  "sortOrder": 1
}
```

### 4.7 文档排序

**PUT** `/documents/reorder`

**请求体**:
```json
{
  "parentId": 2002,
  "documentIds": [2003, 2004, 2005]
}
```

---

## 5. 版本管理模块

### 5.1 获取文档版本列表

**GET** `/documents/{documentId}/versions`

**权限要求**: 文档READ权限

**查询参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | integer | 否 | 页码，默认0 |
| size | integer | 否 | 每页大小，默认10 |

**成功响应**:
```json
{
  "code": 200,
  "data": {
    "content": [
      {
        "id": 30001,
        "versionNumber": 3,
        "title": "API设计规范（更新版）",
        "authorId": 12345,
        "authorName": "张三",
        "authorAvatar": "https://example.com/avatar.jpg",
        "comment": "添加了新的示例和最佳实践",
        "changeType": "UPDATE",
        "isCurrent": true,
        "createdAt": "2025-09-28T10:30:00"
      },
      {
        "id": 30002,
        "versionNumber": 2,
        "title": "API设计规范",
        "authorId": 12345,
        "authorName": "张三",
        "authorAvatar": "https://example.com/avatar.jpg",
        "comment": "添加了详细说明",
        "changeType": "UPDATE",
        "isCurrent": false,
        "createdAt": "2025-09-25T14:20:00"
      }
    ],
    "totalElements": 3,
    "totalPages": 1
  }
}
```

### 5.2 获取版本详情

**GET** `/documents/{documentId}/versions/{versionId}`

**成功响应**:
```json
{
  "code": 200,
  "data": {
    "id": 30001,
    "versionNumber": 3,
    "title": "API设计规范（更新版）",
    "content": "# API设计规范（更新版）\n\n本文档描述了API设计的最佳实践...",
    "authorId": 12345,
    "authorName": "张三",
    "comment": "添加了新的示例和最佳实践",
    "changeType": "UPDATE",
    "createdAt": "2025-09-28T10:30:00"
  }
}
```

### 5.3 版本对比

**GET** `/documents/{documentId}/versions/compare`

**查询参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| v1 | integer | 是 | 版本1的ID |
| v2 | integer | 是 | 版本2的ID |

**成功响应**:
```json
{
  "code": 200,
  "data": {
    "v1": {
      "id": 30001,
      "versionNumber": 2,
      "title": "API设计规范",
      "authorName": "张三",
      "createdAt": "2025-09-25T14:20:00"
    },
    "v2": {
      "id": 30002,
      "versionNumber": 3,
      "title": "API设计规范（更新版）",
      "authorName": "张三",
      "createdAt": "2025-09-28T10:30:00"
    },
    "diff": {
      "title": {
        "old": "API设计规范",
        "new": "API设计规范（更新版）",
        "changed": true
      },
      "content": [
        {
          "lineNumber": 1,
          "type": "UNCHANGED",
          "content": "# API设计规范"
        },
        {
          "lineNumber": 2,
          "type": "ADDED",
          "content": "## 新增示例"
        },
        {
          "lineNumber": 3,
          "type": "REMOVED",
          "content": "## 旧的内容"
        }
      ]
    }
  }
}
```

### 5.4 恢复版本

**POST** `/documents/{documentId}/versions/{versionId}/restore`

**权限要求**: 文档EDIT权限

**成功响应**:
```json
{
  "code": 200,
  "message": "版本恢复成功",
  "data": {
    "newVersionId": 30003,
    "versionNumber": 4
  }
}
```

---

## 6. 搜索模块

### 6.1 全局搜索

**GET** `/search`

**查询参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| q | string | 是 | 搜索关键词 |
| page | integer | 否 | 页码，默认0 |
| size | integer | 否 | 每页大小，默认10 |
| spaceId | integer | 否 | 限定空间ID |
| authorId | integer | 否 | 限定作者ID |
| tags | array | 否 | 标签筛选 |
| startDate | string | 否 | 开始日期（YYYY-MM-DD） |
| endDate | string | 否 | 结束日期（YYYY-MM-DD） |
| sortBy | string | 否 | 排序方式：relevance, date, title |
| order | string | 否 | 排序顺序：asc, desc |

**成功响应**:
```json
{
  "code": 200,
  "data": {
    "query": "API设计",
    "totalElements": 15,
    "totalPages": 2,
    "content": [
      {
        "id": 2001,
        "title": "API设计规范",
        "summary": "本文档描述<em>API设计</em>规范，包含RESTful API的最佳实践...",
        "spaceId": 1001,
        "spaceName": "技术文档空间",
        "authorId": 12345,
        "authorName": "张三",
        "authorAvatar": "https://example.com/avatar.jpg",
        "score": 0.95,
        "tags": ["API", "规范"],
        "createdAt": "2025-09-20T10:00:00",
        "updatedAt": "2025-09-28T09:30:00"
      }
    ],
    "facets": {
      "spaces": [
        {
          "spaceId": 1001,
          "spaceName": "技术文档空间",
          "count": 12
        }
      ],
      "authors": [
        {
          "authorId": 12345,
          "authorName": "张三",
          "count": 8
        }
      ],
      "tags": [
        {
          "tagName": "API",
          "count": 15
        }
      ]
    }
  }
}
```

### 6.2 搜索建议

**GET** `/search/suggest`

**查询参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| q | string | 是 | 搜索关键词 |
| limit | integer | 否 | 返回数量，默认10 |

**成功响应**:
```json
{
  "code": 200,
  "data": [
    "API设计规范",
    "API文档模板",
    "API测试指南",
    "API性能优化"
  ]
}
```

### 6.3 高级搜索

**POST** `/search/advanced`

**请求体**:
```json
{
  "query": {
    "must": [
      {
        "match": {
          "content": "API设计"
        }
      }
    ],
    "filter": [
      {
        "terms": {
          "tags": ["API", "规范"]
        }
      },
      {
        "range": {
          "createdAt": {
            "gte": "2025-01-01",
            "lte": "2025-12-31"
          }
        }
      }
    ]
  },
  "sort": [
    {
      "score": "desc"
    },
    {
      "updatedAt": "desc"
    }
  ],
  "page": 0,
  "size": 10
}
```

---

## 7. 评论和交互模块

### 7.1 获取文档评论

**GET** `/documents/{documentId}/comments`

**权限要求**: 文档READ权限

**查询参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | integer | 否 | 页码，默认0 |
| size | integer | 否 | 每页大小，默认20 |
| sortBy | string | 否 | 排序方式：date, likes |
| order | string | 否 | 排序顺序：asc, desc |

**成功响应**:
```json
{
  "code": 200,
  "data": {
    "content": [
      {
        "id": 4001,
        "content": "这个API文档写得很详细，对新人很有帮助！",
        "documentId": 2001,
        "authorId": 12346,
        "authorName": "李四",
        "authorAvatar": "https://example.com/avatar2.jpg",
        "parentId": null,
        "likeCount": 5,
        "replyCount": 2,
        "isLiked": false,
        "canEdit": false,
        "canDelete": false,
        "createdAt": "2025-09-28T09:00:00",
        "updatedAt": "2025-09-28T09:00:00",
        "replies": [
          {
            "id": 4002,
            "content": "同意，特别是示例部分很实用。",
            "authorId": 12347,
            "authorName": "王五",
            "authorAvatar": "https://example.com/avatar3.jpg",
            "parentId": 4001,
            "likeCount": 1,
            "replyCount": 0,
            "isLiked": true,
            "canEdit": false,
            "canDelete": false,
            "createdAt": "2025-09-28T09:15:00"
          }
        ]
      }
    ],
    "totalElements": 8,
    "totalPages": 1
  }
}
```

### 7.2 添加评论

**POST** `/documents/{documentId}/comments`

**权限要求**: 文档COMMENT权限

**请求体**:
```json
{
  "content": "这个文档很有帮助！@张三 可以看一下。",
  "parentId": null
}
```

**成功响应**:
```json
{
  "code": 201,
  "message": "评论添加成功",
  "data": {
    "id": 4003,
    "content": "这个文档很有帮助！@张三 可以看一下。",
    "documentId": 2001,
    "authorId": 12345,
    "authorName": "当前用户",
    "parentId": null,
    "likeCount": 0,
    "replyCount": 0,
    "isLiked": false,
    "canEdit": true,
    "canDelete": true,
    "createdAt": "2025-09-28T10:30:00",
    "mentions": [
      {
        "userId": 12345,
        "userName": "张三"
      }
    ]
  }
}
```

### 7.3 更新评论

**PUT** `/comments/{commentId}`

**权限要求**: 评论作者

**请求体**:
```json
{
  "content": "这个文档很有帮助！（已更新）"
}
```

### 7.4 删除评论

**DELETE** `/comments/{commentId}`

**权限要求**: 评论作者或管理员

### 7.5 点赞/取消点赞

#### 7.5.1 点赞文档

**POST** `/documents/{documentId}/like`

**成功响应**:
```json
{
  "code": 200,
  "message": "点赞成功",
  "data": {
    "isLiked": true,
    "likeCount": 9
  }
}
```

#### 7.5.2 取消点赞文档

**DELETE** `/documents/{documentId}/like`

#### 7.5.3 点赞评论

**POST** `/comments/{commentId}/like`

#### 7.5.4 取消点赞评论

**DELETE** `/comments/{commentId}/like`

### 7.6 收藏管理

#### 7.6.1 收藏文档

**POST** `/documents/{documentId}/favorite`

**成功响应**:
```json
{
  "code": 200,
  "message": "收藏成功",
  "data": {
    "isFavorited": true,
    "favoriteCount": 15
  }
}
```

#### 7.6.2 取消收藏

**DELETE** `/documents/{documentId}/favorite`

#### 7.6.3 获取收藏列表

**GET** `/favorites`

**查询参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | integer | 否 | 页码，默认0 |
| size | integer | 否 | 每页大小，默认20 |

---

## 8. 通知模块

### 8.1 获取通知列表

**GET** `/notifications`

**查询参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| unread | boolean | 否 | 只显示未读通知 |
| type | string | 否 | 通知类型筛选 |
| page | integer | 否 | 页码，默认0 |
| size | integer | 否 | 每页大小，默认20 |

**成功响应**:
```json
{
  "code": 200,
  "data": {
    "content": [
      {
        "id": 5001,
        "type": "DOCUMENT_COMMENT",
        "title": "您的文档收到新评论",
        "content": "李四在文档《API设计规范》中评论了：这个规范很实用！",
        "isRead": false,
        "actorId": 12346,
        "actorName": "李四",
        "actorAvatar": "https://example.com/avatar2.jpg",
        "targetType": "DOCUMENT",
        "targetId": 2001,
        "targetTitle": "API设计规范",
        "createdAt": "2025-09-28T11:00:00"
      },
      {
        "id": 5002,
        "type": "DOCUMENT_MENTION",
        "title": "您在评论中被提及",
        "content": "王五在文档《技术分享》的评论中提及了您",
        "isRead": true,
        "actorId": 12347,
        "actorName": "王五",
        "actorAvatar": "https://example.com/avatar3.jpg",
        "targetType": "COMMENT",
        "targetId": 4005,
        "targetTitle": "技术分享",
        "createdAt": "2025-09-28T10:30:00"
      }
    ],
    "totalElements": 25,
    "totalPages": 2,
    "unreadCount": 8
  }
}
```

### 8.2 标记通知为已读

**PUT** `/notifications/{notificationId}/read`

**成功响应**:
```json
{
  "code": 200,
  "message": "通知已标记为已读"
}
```

### 8.3 批量标记已读

**PUT** `/notifications/mark-all-read`

**成功响应**:
```json
{
  "code": 200,
  "message": "所有通知已标记为已读"
}
```

### 8.4 删除通知

**DELETE** `/notifications/{notificationId}`

---

## 9. 标签管理模块

### 9.1 创建标签

**POST** `/tags`

**权限要求**: 已认证用户

**请求体**:
```json
{
  "name": "API设计",
  "color": "#FF5722",
  "description": "API设计相关的文档"
}
```

**成功响应**:
```json
{
  "code": 201,
  "message": "标签创建成功",
  "data": {
    "id": 101,
    "name": "api-设计",
    "color": "#FF5722",
    "description": "API设计相关的文档",
    "usageCount": 0,
    "createdAt": "2025-09-28T10:30:00",
    "canEdit": true,
    "canDelete": true
  }
}
```

### 9.2 获取标签列表

**GET** `/tags`

**查询参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | integer | 否 | 页码，默认0 |
| size | integer | 否 | 每页大小，默认20 |
| name | string | 否 | 标签名称筛选 |
| sortBy | string | 否 | 排序方式：name, usage, date |
| order | string | 否 | 排序顺序：asc, desc |

**成功响应**:
```json
{
  "code": 200,
  "data": {
    "content": [
      {
        "id": 101,
        "name": "api-设计",
        "color": "#FF5722",
        "description": "API设计相关的文档",
        "usageCount": 15,
        "createdAt": "2025-09-20T10:00:00",
        "canEdit": true,
        "canDelete": false
      }
    ],
    "totalElements": 50,
    "totalPages": 3
  }
}
```

### 9.3 标签自动补全

**GET** `/tags/suggest`

**查询参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| q | string | 是 | 搜索关键词 |
| limit | integer | 否 | 返回数量，默认10 |

**成功响应**:
```json
{
  "code": 200,
  "data": [
    {
      "id": 101,
      "name": "API",
      "usageCount": 25
    },
    {
      "id": 102,
      "name": "API设计",
      "usageCount": 15
    },
    {
      "id": 103,
      "name": "API文档",
      "usageCount": 12
    }
  ]
}
```

### 9.4 获取热门标签

**GET** `/tags/popular`

**查询参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| limit | integer | 否 | 返回数量，默认20 |
| period | string | 否 | 时间范围：week, month, year |

**成功响应**:
```json
{
  "code": 200,
  "data": [
    {
      "id": 101,
      "name": "API",
      "color": "#FF5722",
      "usageCount": 25,
      "trendingScore": 8.5
    },
    {
      "id": 102,
      "name": "前端",
      "color": "#2196F3",
      "usageCount": 20,
      "trendingScore": 7.2
    }
  ]
}
```

### 9.5 为文档添加标签

**POST** `/documents/{documentId}/tags`

**权限要求**: 文档编辑权限

**请求体**:
```json
{
  "tags": ["API", "设计", "规范"]
}
```

### 9.6 移除文档标签

**DELETE** `/documents/{documentId}/tags/{tagId}`

### 9.7 获取文档标签

**GET** `/documents/{documentId}/tags`

---

## 10. 附件管理模块

### 10.1 上传附件

**POST** `/attachments/upload`

**请求**: `multipart/form-data`
- `file`: 文件（必填）
- `documentId`: 关联文档ID（可选）
- `uploadType`: 上传类型（DOCUMENT, AVATAR, TEMP）

**成功响应**:
```json
{
  "code": 201,
  "message": "文件上传成功",
  "data": {
    "id": 3001,
    "fileName": "20250928_103000_api-example.png",
    "originalName": "api示例.png",
    "filePath": "/uploads/2025/09/28/20250928_103000_api-example.png",
    "fileUrl": "/api/v1/attachments/3001/download",
    "mimeType": "image/png",
    "fileSize": 152048,
    "uploadType": "DOCUMENT",
    "documentId": 2001,
    "uploaderId": 12345,
    "createdAt": "2025-09-28T10:30:00"
  }
}
```

### 10.2 下载附件

**GET** `/attachments/{attachmentId}/download`

**成功响应**: 返回文件流

### 10.3 获取附件信息

**GET** `/attachments/{attachmentId}`

**成功响应**:
```json
{
  "code": 200,
  "data": {
    "id": 3001,
    "fileName": "20250928_103000_api-example.png",
    "originalName": "api示例.png",
    "fileSize": 152048,
    "mimeType": "image/png",
    "uploadType": "DOCUMENT",
    "documentId": 2001,
    "documentTitle": "API设计规范",
    "uploaderId": 12345,
    "uploaderName": "张三",
    "createdAt": "2025-09-28T10:30:00"
  }
}
```

### 10.4 删除附件

**DELETE** `/attachments/{attachmentId}`

**权限要求**: 附件上传者或管理员

---

## 11. 权限管理模块

### 11.1 获取资源权限

**GET** `/permissions`

**查询参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| targetType | string | 是 | 资源类型：SPACE, DOCUMENT |
| targetId | integer | 是 | 资源ID |

**成功响应**:
```json
{
  "code": 200,
  "data": {
    "targetType": "DOCUMENT",
    "targetId": 2001,
    "permissions": [
      {
        "id": 6001,
        "principalType": "USER",
        "principalId": 12345,
        "principalName": "张三",
        "permissionLevel": "OWNER",
        "grantedBy": 12345,
        "grantedByName": "张三",
        "createdAt": "2025-09-20T10:00:00"
      },
      {
        "id": 6002,
        "principalType": "GROUP",
        "principalId": 7001,
        "principalName": "技术团队",
        "permissionLevel": "WRITE",
        "grantedBy": 12345,
        "grantedByName": "张三",
        "createdAt": "2025-09-25T14:00:00"
      }
    ],
    "inheritance": {
      "enabled": true,
      "source": "SPACE",
      "sourceId": 1001,
      "sourceName": "技术文档空间"
    }
  }
}
```

### 11.2 设置权限

**POST** `/permissions`

**权限要求**: 资源管理员

**请求体**:
```json
{
  "targetType": "DOCUMENT",
  "targetId": 2001,
  "permissions": [
    {
      "principalType": "USER",
      "principalId": 12346,
      "permissionLevel": "WRITE"
    },
    {
      "principalType": "GROUP",
      "principalId": 7001,
      "permissionLevel": "READ"
    }
  ]
}
```

### 11.3 检查权限

**GET** `/permissions/check`

**查询参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| targetType | string | 是 | 资源类型 |
| targetId | integer | 是 | 资源ID |
| action | string | 是 | 操作：READ, WRITE, DELETE, ADMIN |

**成功响应**:
```json
{
  "code": 200,
  "data": {
    "hasPermission": true,
    "permissionLevel": "WRITE",
    "reason": "USER_PERMISSION"
  }
}
```

---

## 12. 用户组管理模块

### 12.1 创建用户组

**POST** `/spaces/{spaceId}/groups`

**权限要求**: 空间管理员

**请求体**:
```json
{
  "name": "前端开发组",
  "description": "负责前端开发的团队成员"
}
```

**成功响应**:
```json
{
  "code": 201,
  "message": "用户组创建成功",
  "data": {
    "id": 7001,
    "spaceId": 1001,
    "name": "前端开发组",
    "description": "负责前端开发的团队成员",
    "memberCount": 0,
    "createdAt": "2025-09-28T10:30:00"
  }
}
```

### 12.2 获取空间用户组列表

**GET** `/spaces/{spaceId}/groups`

**权限要求**: 空间成员

**成功响应**:
```json
{
  "code": 200,
  "data": [
    {
      "id": 7001,
      "name": "前端开发组",
      "description": "负责前端开发的团队成员",
      "memberCount": 5,
      "createdAt": "2025-09-20T10:00:00"
    }
  ]
}
```

### 12.3 添加组成员

**POST** `/groups/{groupId}/members`

**请求体**:
```json
{
  "userIds": [12346, 12347],
  "role": "MEMBER"
}
```

### 12.4 移除组成员

**DELETE** `/groups/{groupId}/members/{userId}`

---

## 13. 文档模板模块

### 13.1 创建文档模板

**POST** `/spaces/{spaceId}/templates`

**权限要求**: 空间管理员

**请求体**:
```json
{
  "name": "API文档模板",
  "description": "用于创建API文档的标准模板",
  "content": "# {API_NAME} API文档\n\n## 概述\n{API_DESCRIPTION}\n\n## 接口列表\n\n### {METHOD} {URL}\n\n**描述**: {DESCRIPTION}\n\n**请求参数**:\n\n| 参数名 | 类型 | 必填 | 说明 |\n|--------|------|------|------|\n| {PARAM_NAME} | {PARAM_TYPE} | {REQUIRED} | {PARAM_DESC} |\n\n**响应示例**:\n\n```json\n{\n  \"code\": 200,\n  \"message\": \"success\",\n  \"data\": {}\n}\n```",
  "category": "API",
  "isPublic": false
}
```

**成功响应**:
```json
{
  "code": 201,
  "message": "模板创建成功",
  "data": {
    "id": 8001,
    "spaceId": 1001,
    "name": "API文档模板",
    "description": "用于创建API文档的标准模板",
    "category": "API",
    "creatorId": 12345,
    "creatorName": "张三",
    "isPublic": false,
    "usageCount": 0,
    "createdAt": "2025-09-28T10:30:00"
  }
}
```

### 13.2 获取模板列表

**GET** `/templates`

**查询参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| spaceId | integer | 否 | 空间ID |
| category | string | 否 | 模板分类 |
| isPublic | boolean | 否 | 是否公开模板 |
| page | integer | 否 | 页码，默认0 |
| size | integer | 否 | 每页大小，默认20 |

**成功响应**:
```json
{
  "code": 200,
  "data": {
    "content": [
      {
        "id": 8001,
        "name": "API文档模板",
        "description": "用于创建API文档的标准模板",
        "category": "API",
        "creatorName": "张三",
        "isPublic": false,
        "usageCount": 15,
        "createdAt": "2025-09-20T10:00:00"
      }
    ],
    "totalElements": 10,
    "totalPages": 1
  }
}
```

### 13.3 基于模板创建文档

**POST** `/templates/{templateId}/create-document`

**权限要求**: 空间内创建文档权限

**请求体**:
```json
{
  "spaceId": 1001,
  "parentId": null,
  "title": "用户管理API文档",
  "variables": {
    "API_NAME": "用户管理",
    "API_DESCRIPTION": "用户注册、登录、信息管理相关接口",
    "METHOD": "POST",
    "URL": "/api/v1/users",
    "DESCRIPTION": "创建新用户",
    "PARAM_NAME": "email",
    "PARAM_TYPE": "string",
    "REQUIRED": "是",
    "PARAM_DESC": "用户邮箱"
  }
}
```

**成功响应**:
```json
{
  "code": 201,
  "message": "基于模板创建文档成功",
  "data": {
    "id": 2005,
    "title": "用户管理API文档",
    "content": "# 用户管理 API文档\n\n## 概述\n用户注册、登录、信息管理相关接口\n\n## 接口列表\n\n### POST /api/v1/users\n\n**描述**: 创建新用户\n\n**请求参数**:\n\n| 参数名 | 类型 | 必填 | 说明 |\n|--------|------|------|------|\n| email | string | 是 | 用户邮箱 |\n\n**响应示例**:\n\n```json\n{\n  \"code\": 200,\n  \"message\": \"success\",\n  \"data\": {}\n}\n```",
    "spaceId": 1001,
    "createdAt": "2025-09-28T10:30:00"
  }
}
```

---

## 14. 数据导入导出模块

### 14.1 导入Markdown文件

**POST** `/spaces/{spaceId}/import/markdown`

**权限要求**: 空间内创建文档权限

**请求**: `multipart/form-data`
- `file`: Markdown文件
- `parentId`: 父目录ID（可选）
- `preserveStructure`: 是否保持目录结构（boolean）

**成功响应**:
```json
{
  "code": 200,
  "message": "Markdown文件导入成功",
  "data": {
    "importedDocuments": [
      {
        "id": 2006,
        "title": "导入的文档",
        "status": "SUCCESS"
      }
    ],
    "failedDocuments": [],
    "totalCount": 1,
    "successCount": 1,
    "failedCount": 0
  }
}
```

### 14.2 导入ZIP文件

**POST** `/spaces/{spaceId}/import/zip`

**权限要求**: 空间内创建文档权限

**请求**: `multipart/form-data`
- `file`: ZIP文件
- `parentId`: 父目录ID（可选）

### 14.3 导出文档为PDF

**GET** `/documents/{documentId}/export/pdf`

**权限要求**: 文档查看权限

**成功响应**: 返回PDF文件流

### 14.4 导出文档为Markdown

**GET** `/documents/{documentId}/export/markdown`

**成功响应**: 返回Markdown文件流

### 14.5 批量导出空间文档

**POST** `/spaces/{spaceId}/export`

**请求体**:
```json
{
  "format": "ZIP",
  "includeAttachments": true,
  "documentIds": [2001, 2002, 2003]
}
```

**成功响应**:
```json
{
  "code": 200,
  "message": "导出任务已创建",
  "data": {
    "taskId": "export_task_123456",
    "estimatedTime": "2分钟",
    "downloadUrl": null
  }
}
```

### 14.6 获取导出任务状态

**GET** `/export-tasks/{taskId}`

**成功响应**:
```json
{
  "code": 200,
  "data": {
    "taskId": "export_task_123456",
    "status": "COMPLETED",
    "progress": 100,
    "downloadUrl": "/api/v1/download/export_task_123456.zip",
    "createdAt": "2025-09-28T10:30:00",
    "completedAt": "2025-09-28T10:32:15"
  }
}
```

---

## 15. 回收站模块

### 15.1 获取回收站内容

**GET** `/spaces/{spaceId}/recycle-bin`

**权限要求**: 空间管理员

**查询参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | integer | 否 | 页码，默认0 |
| size | integer | 否 | 每页大小，默认20 |
| type | string | 否 | 类型：DOCUMENT, FOLDER |

**成功响应**:
```json
{
  "code": 200,
  "data": {
    "content": [
      {
        "id": 2010,
        "title": "已删除的文档",
        "type": "DOCUMENT",
        "deletedBy": "张三",
        "deletedAt": "2025-09-28T10:30:00",
        "originalPath": "/技术文档/API设计",
        "canRestore": true,
        "autoDeleteAt": "2025-10-28T10:30:00"
      }
    ],
    "totalElements": 5,
    "totalPages": 1
  }
}
```

### 15.2 恢复文档

**POST** `/recycle-bin/restore`

**权限要求**: 空间管理员

**请求体**:
```json
{
  "documentIds": [2010],
  "targetParentId": null
}
```

**成功响应**:
```json
{
  "code": 200,
  "message": "文档恢复成功",
  "data": {
    "restored": [
      {
        "id": 2010,
        "title": "已删除的文档",
        "newPath": "/技术文档/API设计"
      }
    ],
    "failed": []
  }
}
```

### 15.3 彻底删除文档

**DELETE** `/recycle-bin/documents/{documentId}`

**权限要求**: 空间管理员

**成功响应**:
```json
{
  "code": 200,
  "message": "文档已彻底删除"
}
```

### 15.4 清空回收站

**DELETE** `/spaces/{spaceId}/recycle-bin`

**权限要求**: 空间管理员

---

## 16. 分享模块

### 16.1 内部分享文档

**POST** `/documents/{documentId}/share/internal`

**权限要求**: 文档查看权限

**请求体**:
```json
{
  "userIds": [12346, 12347],
  "message": "分享一个有用的文档给大家"
}
```

**成功响应**:
```json
{
  "code": 200,
  "message": "分享成功",
  "data": {
    "sharedTo": 2,
    "notificationsSent": 2
  }
}
```

### 16.2 邮件分享文档

**POST** `/documents/{documentId}/share/email`

**权限要求**: 文档查看权限

**请求体**:
```json
{
  "emails": ["colleague@example.com"],
  "subject": "分享文档：API设计规范",
  "message": "这是一个很有价值的技术文档，推荐阅读。"
}
```

### 16.3 创建公开分享链接

**POST** `/documents/{documentId}/share/public`

**权限要求**: 文档分享权限

**请求体**:
```json
{
  "expiresAt": "2025-10-28T10:30:00",
  "password": "share123",
  "allowDownload": true
}
```

**成功响应**:
```json
{
  "code": 200,
  "message": "分享链接创建成功",
  "data": {
    "shareId": "abc123def456",
    "shareUrl": "https://knowledge.example.com/share/abc123def456",
    "expiresAt": "2025-10-28T10:30:00",
    "hasPassword": true,
    "allowDownload": true,
    "createdAt": "2025-09-28T10:30:00"
  }
}
```

### 16.4 获取分享链接列表

**GET** `/documents/{documentId}/shares`

**成功响应**:
```json
{
  "code": 200,
  "data": [
    {
      "shareId": "abc123def456",
      "shareUrl": "https://knowledge.example.com/share/abc123def456",
      "expiresAt": "2025-10-28T10:30:00",
      "hasPassword": true,
      "allowDownload": true,
      "viewCount": 25,
      "createdAt": "2025-09-28T10:30:00",
      "lastAccessAt": "2025-09-28T15:20:00"
    }
  ]
}
```

### 16.5 访问分享链接

**GET** `/share/{shareId}`

**查询参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| password | string | 否 | 分享密码（如果设置了密码） |

**成功响应**:
```json
{
  "code": 200,
  "data": {
    "document": {
      "title": "API设计规范",
      "content": "# API设计规范\n\n...",
      "authorName": "张三",
      "createdAt": "2025-09-20T10:00:00",
      "updatedAt": "2025-09-28T09:30:00"
    },
    "allowDownload": true,
    "watermark": "通过分享链接访问"
  }
}
```

---

## 17. 系统管理模块

### 17.1 获取系统统计

**GET** `/admin/stats`

**权限要求**: 系统管理员

**成功响应**:
```json
{
  "code": 200,
  "data": {
    "overview": {
      "totalUsers": 1250,
      "totalSpaces": 85,
      "totalDocuments": 5680,
      "totalComments": 1250,
      "totalAttachments": 850,
      "storageUsed": "2.5GB",
      "storageLimit": "100GB"
    },
    "growth": {
      "newUsersThisMonth": 45,
      "newDocumentsThisMonth": 120,
      "activeUsersThisMonth": 680
    },
    "popular": {
      "topSpaces": [
        {
          "id": 1001,
          "name": "技术文档空间",
          "documentCount": 250,
          "memberCount": 15
        }
      ],
      "topDocuments": [
        {
          "id": 2001,
          "title": "API设计规范",
          "viewCount": 580,
          "likeCount": 45
        }
      ]
    }
  }
}
```

### 17.2 用户管理

#### 17.2.1 获取用户列表

**GET** `/admin/users`

**查询参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | integer | 否 | 页码，默认0 |
| size | integer | 否 | 每页大小，默认20 |
| keyword | string | 否 | 搜索关键词 |
| status | string | 否 | 用户状态 |

**成功响应**:
```json
{
  "code": 200,
  "data": {
    "content": [
      {
        "id": 12345,
        "email": "user@example.com",
        "displayName": "张三",
        "status": "ACTIVE",
        "isSystemAdmin": false,
        "documentCount": 25,
        "lastLoginAt": "2025-09-28T09:00:00",
        "createdAt": "2025-09-01T10:00:00"
      }
    ],
    "totalElements": 1250,
    "totalPages": 63
  }
}
```

#### 17.2.2 更新用户状态

**PUT** `/admin/users/{userId}/status`

**请求体**:
```json
{
  "status": "SUSPENDED",
  "reason": "违反使用条款"
}
```

#### 17.2.3 设置系统管理员

**PUT** `/admin/users/{userId}/admin`

**请求体**:
```json
{
  "isSystemAdmin": true
}
```

### 17.3 空间管理

#### 17.3.1 获取所有空间

**GET** `/admin/spaces`

#### 17.3.2 转移空间所有权

**PUT** `/admin/spaces/{spaceId}/owner`

**请求体**:
```json
{
  "newOwnerId": 12346,
  "reason": "原所有者离职"
}
```

### 17.4 系统配置

#### 17.4.1 获取系统配置

**GET** `/admin/config`

**成功响应**:
```json
{
  "code": 200,
  "data": {
    "upload": {
      "maxFileSize": "10MB",
      "allowedTypes": ["image/png", "image/jpeg", "application/pdf"]
    },
    "security": {
      "passwordMinLength": 8,
      "jwtExpirationTime": 3600,
      "maxLoginAttempts": 5
    },
    "notification": {
      "emailEnabled": true,
      "smtpHost": "smtp.example.com"
    }
  }
}
```

#### 17.4.2 更新系统配置

**PUT** `/admin/config`

**请求体**:
```json
{
  "upload": {
    "maxFileSize": "20MB"
  },
  "security": {
    "passwordMinLength": 10
  }
}
```

---

## 18. 审计日志模块

### 18.1 获取审计日志

**GET** `/admin/audit-logs`

**权限要求**: 系统管理员

**查询参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | integer | 否 | 页码，默认0 |
| size | integer | 否 | 每页大小，默认20 |
| action | string | 否 | 操作类型筛选 |
| userId | integer | 否 | 用户ID筛选 |
| startDate | string | 否 | 开始日期 |
| endDate | string | 否 | 结束日期 |

**成功响应**:
```json
{
  "code": 200,
  "data": {
    "content": [
      {
        "id": 9001,
        "actorId": 12345,
        "actorName": "张三",
        "action": "CREATE_DOCUMENT",
        "targetType": "DOCUMENT",
        "targetId": 2001,
        "details": {
          "title": "API设计规范",
          "spaceId": 1001
        },
        "ipAddress": "192.168.1.100",
        "userAgent": "Mozilla/5.0...",
        "createdAt": "2025-09-28T10:30:00"
      }
    ],
    "totalElements": 5000,
    "totalPages": 250
  }
}
```

### 18.2 导出审计日志

**POST** `/admin/audit-logs/export`

**请求体**:
```json
{
  "format": "CSV",
  "startDate": "2025-09-01",
  "endDate": "2025-09-30",
  "actions": ["CREATE_DOCUMENT", "DELETE_DOCUMENT"]
}
```

---

## 19. 仪表盘模块

### 19.1 获取个人仪表盘

**GET** `/dashboard`

**权限要求**: 已认证用户

**成功响应**:
```json
{
  "code": 200,
  "data": {
    "recentEdited": [
      {
        "id": 2001,
        "title": "API设计规范",
        "spaceName": "技术文档空间",
        "updatedAt": "2025-09-28T09:30:00"
      }
    ],
    "recentViewed": [
      {
        "id": 2002,
        "title": "前端开发指南",
        "spaceName": "技术文档空间",
        "viewedAt": "2025-09-28T08:45:00"
      }
    ],
    "favorites": [
      {
        "id": 2001,
        "title": "API设计规范",
        "spaceName": "技术文档空间",
        "favoritedAt": "2025-09-27T16:20:00"
      }
    ],
    "teamActivity": [
      {
        "type": "DOCUMENT_CREATED",
        "actorName": "李四",
        "targetTitle": "新功能开发文档",
        "spaceName": "技术文档空间",
        "createdAt": "2025-09-28T09:00:00"
      }
    ],
    "statistics": {
      "myDocuments": 25,
      "myComments": 45,
      "documentsLiked": 120,
      "spacesJoined": 8
    }
  }
}
```

### 19.2 获取空间仪表盘

**GET** `/spaces/{spaceId}/dashboard`

**权限要求**: 空间成员

**成功响应**:
```json
{
  "code": 200,
  "data": {
    "overview": {
      "totalDocuments": 250,
      "totalMembers": 15,
      "totalComments": 420,
      "totalViews": 8500
    },
    "recentActivity": [
      {
        "type": "DOCUMENT_UPDATED",
        "actorName": "张三",
        "targetTitle": "API设计规范",
        "createdAt": "2025-09-28T09:30:00"
      }
    ],
    "popularDocuments": [
      {
        "id": 2001,
        "title": "API设计规范",
        "viewCount": 580,
        "likeCount": 45,
        "commentCount": 12
      }
    ],
    "activeMembers": [
      {
        "userId": 12345,
        "displayName": "张三",
        "documentCount": 25,
        "commentCount": 45
      }
    ]
  }
}
```

---

## 20. 内容生命周期模块

### 20.1 设置文档审查策略

**POST** `/documents/{documentId}/review-policy`

**权限要求**: 文档编辑权限

**请求体**:
```json
{
  "reviewCycle": "QUARTERLY",
  "reviewers": [12346, 12347],
  "nextReviewDate": "2025-12-28",
  "isActive": true
}
```

**成功响应**:
```json
{
  "code": 200,
  "message": "审查策略设置成功",
  "data": {
    "documentId": 2001,
    "reviewCycle": "QUARTERLY",
    "nextReviewDate": "2025-12-28",
    "reviewers": [
      {
        "userId": 12346,
        "displayName": "李四"
      }
    ]
  }
}
```

### 20.2 获取我的审查任务

**GET** `/review-tasks`

**权限要求**: 已认证用户

**查询参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| status | string | 否 | 任务状态：PENDING, OVERDUE, COMPLETED |
| page | integer | 否 | 页码，默认0 |
| size | integer | 否 | 每页大小，默认20 |

**成功响应**:
```json
{
  "code": 200,
  "data": {
    "content": [
      {
        "documentId": 2001,
        "documentTitle": "API设计规范",
        "spaceName": "技术文档空间",
        "dueDate": "2025-12-28",
        "isOverdue": false,
        "priority": "HIGH",
        "lastReviewDate": "2025-09-28",
        "assignedAt": "2025-09-01T10:00:00"
      }
    ],
    "totalElements": 8,
    "totalPages": 1,
    "overdueCount": 2
  }
}
```

### 20.3 完成审查任务

**POST** `/review-tasks/{documentId}/complete`

**请求体**:
```json
{
  "comment": "文档内容已更新，符合最新标准",
  "nextReviewDate": "2026-03-28",
  "recommendedActions": ["UPDATE_CONTENT", "ADD_EXAMPLES"]
}
```

### 20.4 文档归档

**POST** `/documents/{documentId}/archive`

**权限要求**: 文档管理权限

**请求体**:
```json
{
  "reason": "文档已过时，不再维护",
  "preserveAccess": true
}
```

---

## 请求示例

### cURL示例

```bash
# 用户登录
curl -X POST "https://api.knowledge.example.com/api/v1/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "password123"
  }'

# 创建文档
curl -X POST "https://api.knowledge.example.com/api/v1/spaces/1001/documents" \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "新文档",
    "content": "# 标题\n\n文档内容...",
    "contentType": "DOCUMENT",
    "tags": ["标签1", "标签2"]
  }'

# 搜索文档
curl -X GET "https://api.knowledge.example.com/api/v1/search?q=API&page=0&size=10" \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

### JavaScript示例

```javascript
// 使用 fetch API
const response = await fetch('/api/v1/auth/login', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
  },
  body: JSON.stringify({
    email: 'user@example.com',
    password: 'password123'
  })
});

const result = await response.json();
if (result.code === 200) {
  localStorage.setItem('accessToken', result.data.accessToken);
}

// 使用 axios
import axios from 'axios';

const apiClient = axios.create({
  baseURL: '/api/v1',
  headers: {
    'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
  }
});

// 创建文档
const createDocument = async (spaceId, documentData) => {
  try {
    const response = await apiClient.post(`/spaces/${spaceId}/documents`, documentData);
    return response.data;
  } catch (error) {
    console.error('创建文档失败:', error.response.data);
    throw error;
  }
};
```

## 错误处理最佳实践

### 1. 客户端错误处理

```javascript
const handleApiError = (error) => {
  if (error.response) {
    const { code, message, errors } = error.response.data;

    switch (code) {
      case 401:
        // 重新登录
        redirectToLogin();
        break;
      case 403:
        // 显示权限不足提示
        showPermissionError();
        break;
      case 422:
        // 显示表单验证错误
        showValidationErrors(errors);
        break;
      default:
        // 显示通用错误
        showError(message);
    }
  } else {
    // 网络错误
    showNetworkError();
  }
};
```

### 2. 重试机制

```javascript
const apiWithRetry = async (apiCall, maxRetries = 3) => {
  for (let i = 0; i < maxRetries; i++) {
    try {
      return await apiCall();
    } catch (error) {
      if (i === maxRetries - 1 || error.response?.status < 500) {
        throw error;
      }
      await new Promise(resolve => setTimeout(resolve, 1000 * Math.pow(2, i)));
    }
  }
};
```

## 版本更新说明

### v1.0 (2025-09-28)
- 初始版本发布
- 包含所有核心功能模块
- 支持完整的知识库管理功能

---

**注意**: 本文档持续更新中，如有疑问请联系开发团队。