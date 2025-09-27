<template>
  <div class="document-view">
    <!-- 文档头部 -->
    <div class="document-header">
      <div class="header-left">
        <el-button @click="$router.back()" text class="back-btn">
          <el-icon><ArrowLeft /></el-icon>
          返回
        </el-button>
        <div class="document-meta">
          <div class="title-section">
            <h1 class="document-title">{{ document.title }}</h1>
            <el-tag
              :type="statusInfo.color"
              class="status-tag"
              size="small"
            >
              {{ statusInfo.label }}
            </el-tag>
          </div>
          <div class="document-info">
            <span class="author">
              <el-icon><User /></el-icon>
              {{ document.author }}
            </span>
            <span class="created-time">
              <el-icon><Clock /></el-icon>
              {{ document.createdAt }}
            </span>
            <span class="updated-time">
              <el-icon><Refresh /></el-icon>
              最后更新: {{ document.updatedAt }}
            </span>
            <span class="space-info">
              <el-icon><Folder /></el-icon>
              {{ document.spaceName }}
            </span>
          </div>
        </div>
      </div>
      <div class="header-right">
        <div class="document-stats">
          <span class="stat-item">
            <el-icon><View /></el-icon>
            {{ document.views }} 浏览
          </span>
          <span class="stat-item">
            <el-icon><ChatLineRound /></el-icon>
            {{ document.comments }} 评论
          </span>
          <LikeButton
            v-if="canLike"
            :liked="document.liked"
            :count="document.likes"
            @like="handleLike"
            @unlike="handleUnlike"
          />
          <span v-else class="stat-item">
            <el-icon><SuitHeart /></el-icon>
            {{ document.likes }} 点赞
          </span>
        </div>
        <el-button-group class="action-buttons">
          <el-button @click="toggleFavorite">
            <el-icon :class="{ 'is-favorite': document.favorited }">
              <Star />
            </el-icon>
            {{ document.favorited ? '已收藏' : '收藏' }}
          </el-button>
          <el-button
            v-if="permissions.canEdit"
            @click="editDocument"
          >
            <el-icon><Edit /></el-icon>
            编辑
          </el-button>
          <el-dropdown @command="handleAction">
            <el-button>
              <el-icon><MoreFilled /></el-icon>
            </el-button>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="share">
                  <el-icon><Share /></el-icon>
                  分享
                </el-dropdown-item>
                <el-dropdown-item command="export">
                  <el-icon><Download /></el-icon>
                  导出
                </el-dropdown-item>
                <el-dropdown-item command="history">
                  <el-icon><Timer /></el-icon>
                  版本历史
                </el-dropdown-item>
                <el-dropdown-item
                  v-if="permissions.canEdit && document.status === 'draft'"
                  command="submitReview"
                  divided
                >
                  <el-icon><View /></el-icon>
                  提交审核
                </el-dropdown-item>
                <el-dropdown-item
                  v-if="permissions.canPublish && document.status === 'review'"
                  command="approveReview"
                >
                  <el-icon><Check /></el-icon>
                  审核通过
                </el-dropdown-item>
                <el-dropdown-item
                  v-if="permissions.canPublish && document.status === 'review'"
                  command="rejectReview"
                >
                  <el-icon><Close /></el-icon>
                  审核拒绝
                </el-dropdown-item>
                <el-dropdown-item
                  v-if="permissions.canPublish && document.status === 'draft'"
                  command="publish"
                >
                  <el-icon><Upload /></el-icon>
                  直接发布
                </el-dropdown-item>
                <el-dropdown-item
                  v-if="permissions.canArchive && document.status === 'published'"
                  command="archive"
                >
                  <el-icon><FolderOpened /></el-icon>
                  归档文档
                </el-dropdown-item>
                <el-dropdown-item
                  v-if="permissions.canLock"
                  :command="document.isLocked ? 'unlock' : 'lock'"
                >
                  <el-icon>
                    <Unlock v-if="document.isLocked" />
                    <Lock v-else />
                  </el-icon>
                  {{ document.isLocked ? '解锁文档' : '锁定文档' }}
                </el-dropdown-item>
                <el-dropdown-item
                  v-if="permissions.canDelete"
                  command="delete"
                  divided
                  class="danger-item"
                >
                  <el-icon><Delete /></el-icon>
                  删除文档
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </el-button-group>
      </div>
    </div>

    <!-- 审核信息 -->
    <div v-if="document.status === 'review'" class="review-info">
      <el-alert
        title="文档审核中"
        type="warning"
        :closable="false"
        show-icon
      >
        <template #default>
          <div class="review-details">
            <p>审核人员：{{ document.reviewers?.map(r => r.name).join(', ') || '待指定' }}</p>
            <p v-if="document.reviewDeadline">
              审核截止时间：{{ new Date(document.reviewDeadline).toLocaleDateString() }}
            </p>
          </div>
        </template>
      </el-alert>
    </div>

    <!-- 审核结果 -->
    <div v-if="document.reviewResult" class="review-result">
      <el-alert
        :title="document.reviewResult.approved ? '审核已通过' : '审核已拒绝'"
        :type="document.reviewResult.approved ? 'success' : 'error'"
        :closable="false"
        show-icon
      >
        <template #default>
          <div class="review-result-details">
            <p>审核人：{{ document.reviewResult.reviewer }}</p>
            <p>审核时间：{{ new Date(document.reviewResult.reviewedAt).toLocaleString() }}</p>
            <p>审核意见：{{ document.reviewResult.comment }}</p>
          </div>
        </template>
      </el-alert>
    </div>

    <!-- 文档标签 -->
    <div class="document-tags" v-if="document.tags && document.tags.length > 0">
      <el-tag
        v-for="tag in document.tags"
        :key="tag"
        class="tag-item"
        @click="searchByTag(tag)"
      >
        {{ tag }}
      </el-tag>
    </div>

    <!-- 文档内容 -->
    <div class="document-content">
      <div class="content-wrapper">
        <div v-html="renderedContent" class="markdown-content"></div>
      </div>

      <!-- 目录导航 -->
      <div class="content-toc" v-if="tocItems.length > 0">
        <div class="toc-header">
          <h3>目录</h3>
        </div>
        <div class="toc-items">
          <a
            v-for="item in tocItems"
            :key="item.id"
            :href="`#${item.id}`"
            :class="['toc-item', `toc-level-${item.level}`]"
            @click="scrollToHeading(item.id)"
          >
            {{ item.text }}
          </a>
        </div>
      </div>
    </div>

    <!-- 相关文档推荐 -->
    <div class="related-documents" v-if="relatedDocs.length > 0">
      <h3 class="section-title">相关文档</h3>
      <div class="related-list">
        <div
          v-for="doc in relatedDocs"
          :key="doc.id"
          class="related-item"
          @click="navigateToDocument(doc.id)"
        >
          <div class="related-info">
            <h4 class="related-title">{{ doc.title }}</h4>
            <p class="related-description">{{ doc.description }}</p>
            <div class="related-meta">
              <span>{{ doc.author }}</span>
              <span>{{ doc.updatedAt }}</span>
            </div>
          </div>
          <div class="related-stats">
            <span><el-icon><View /></el-icon> {{ doc.views }}</span>
            <span><el-icon><Star /></el-icon> {{ doc.likes }}</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 评论区 -->
    <CommentSection
      v-if="canComment"
      :document-id="document.id"
      :comments="comments"
      @comment-added="handleCommentAdded"
      @comment-updated="handleCommentUpdated"
      @comment-deleted="handleCommentDeleted"
    />

    <!-- 无法评论时的提示 -->
    <div v-else-if="document.status !== 'published'" class="comment-disabled">
      <el-alert
        title="评论功能暂不可用"
        :description="`只有已发布的文档才能进行评论。当前文档状态：${statusInfo.label}`"
        type="info"
        show-icon
        :closable="false"
      />
    </div>

    <!-- 版本历史侧边栏 -->
    <el-drawer
      v-model="showHistory"
      title="版本历史"
      direction="rtl"
      size="50%"
      :before-close="closeVersionHistory"
    >
      <DocumentHistory
        :document-id="document.id"
        :current-version="document.version"
        :can-restore="permissions.canEdit"
        :original-title="document.title"
        @close="closeVersionHistory"
        @restore="handleVersionRestore"
      />
    </el-drawer>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, nextTick } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  ArrowLeft,
  User,
  Clock,
  Refresh,
  Folder,
  View,
  ChatLineRound,
  SuitHeart,
  Star,
  Edit,
  MoreFilled,
  Share,
  Download,
  Timer,
  Check,
  Close,
  Upload,
  FolderOpened,
  Delete,
  Lock,
  Unlock
} from '@element-plus/icons-vue'
import LikeButton from '@/components/LikeButton.vue'
import CommentSection from '@/components/CommentSection.vue'
import DocumentHistory from '@/components/DocumentHistory.vue'
import { DocumentStatus, UserRole, type Document } from '@/types/document'
import { checkDocumentPermissions, canLikeDocument, canCommentDocument, getDocumentStatusInfo } from '@/utils/permissions'

const route = useRoute()
const router = useRouter()

// 当前用户信息
const currentUser = ref({
  id: '1',
  name: '张三',
  role: UserRole.EDITOR // 这里应该从用户状态管理中获取
})

// 文档数据
const document = ref({
  id: '',
  title: '',
  content: '',
  author: {
    id: '',
    name: '',
    avatar: ''
  },
  status: DocumentStatus.DRAFT,
  createdAt: '',
  updatedAt: '',
  publishedAt: '',
  views: 0,
  likes: 0,
  comments: 0,
  liked: false,
  favorited: false,
  tags: [] as string[],
  spaceId: '',
  spaceName: '',
  permissions: [],
  version: 1,
  isLocked: false
})

// 评论数据
const comments = ref([])

// 版本历史相关
const showHistory = ref(false)

// 相关文档
const relatedDocs = ref([
  {
    id: '2',
    title: 'Vue3 响应式原理深度解析',
    description: '深入探讨Vue3响应式系统的实现原理和使用技巧',
    author: '李四',
    updatedAt: '2024-01-10',
    views: 234,
    likes: 18
  },
  {
    id: '3',
    title: 'TypeScript 在Vue项目中的最佳实践',
    description: 'TypeScript与Vue结合使用的完整指南和实践经验',
    author: '王五',
    updatedAt: '2024-01-08',
    views: 189,
    likes: 15
  }
])

// 计算属性：权限检查
const permissions = computed(() => {
  const isAuthor = document.value.author.id === currentUser.value.id
  return checkDocumentPermissions(
    currentUser.value.role,
    document.value.status,
    isAuthor,
    document.value.isLocked,
    document.value.lockedBy?.id === currentUser.value.id
  )
})

// 计算属性：状态信息
const statusInfo = computed(() => {
  return getDocumentStatusInfo(document.value.status)
})

// 计算属性：是否可以点赞
const canLike = computed(() => {
  return canLikeDocument(currentUser.value.role, document.value.status)
})

// 计算属性：是否可以评论
const canComment = computed(() => {
  return canCommentDocument(currentUser.value.role, document.value.status)
})

// 渲染的Markdown内容
const renderedContent = computed(() => {
  if (!document.value.content) return ''

  // 改进的markdown解析器
  let html = document.value.content
    // 处理标题并添加ID
    .replace(/^### (.*$)/gim, (match, title) => {
      const id = title.toLowerCase()
        .replace(/[^\w\s-]/g, '')
        .replace(/\s+/g, '-')
        .replace(/-+/g, '-')
        .replace(/^-|-$/g, '')
      return `<h3 id="${id}">${title}</h3>`
    })
    .replace(/^## (.*$)/gim, (match, title) => {
      const id = title.toLowerCase()
        .replace(/[^\w\s-]/g, '')
        .replace(/\s+/g, '-')
        .replace(/-+/g, '-')
        .replace(/^-|-$/g, '')
      return `<h2 id="${id}">${title}</h2>`
    })
    .replace(/^# (.*$)/gim, (match, title) => {
      const id = title.toLowerCase()
        .replace(/[^\w\s-]/g, '')
        .replace(/\s+/g, '-')
        .replace(/-+/g, '-')
        .replace(/^-|-$/g, '')
      return `<h1 id="${id}">${title}</h1>`
    })
    // 处理代码块
    .replace(/```(\w+)?\n([\s\S]*?)```/g, '<pre><code class="language-$1">$2</code></pre>')
    .replace(/`([^`]+)`/g, '<code>$1</code>')
    // 处理表格
    .replace(/\|(.+)\|\n\|[-:\s]+\|\n((?:\|.+\|\n?)*)/g, (match, header, rows) => {
      const headerCells = header.split('|').map(cell => `<th>${cell.trim()}</th>`).join('')
      const bodyRows = rows.trim().split('\n').map(row => {
        const cells = row.split('|').map(cell => `<td>${cell.trim()}</td>`).join('')
        return `<tr>${cells}</tr>`
      }).join('')
      return `<table><thead><tr>${headerCells}</tr></thead><tbody>${bodyRows}</tbody></table>`
    })
    // 处理引用块
    .replace(/^> (.+)$/gm, '<blockquote>$1</blockquote>')
    // 处理链接
    .replace(/\[([^\]]+)\]\(([^)]+)\)/g, '<a href="$2" target="_blank" rel="noopener">$1</a>')
    // 处理粗体
    .replace(/\*\*([^*]+)\*\*/g, '<strong>$1</strong>')
    // 处理斜体
    .replace(/\*([^*]+)\*/g, '<em>$1</em>')
    // 处理删除线
    .replace(/~~([^~]+)~~/g, '<del>$1</del>')
    // 处理高亮
    .replace(/==([^=]+)==/g, '<mark>$1</mark>')

  // 处理列表和段落
  const lines = html.split('\n')
  const result = []
  let inList = false
  let listType = ''

  for (let i = 0; i < lines.length; i++) {
    const line = lines[i].trim()

    // 处理无序列表
    if (line.match(/^- (.+)$/)) {
      if (!inList || listType !== 'ul') {
        if (inList) result.push(`</${listType}>`)
        result.push('<ul>')
        inList = true
        listType = 'ul'
      }
      result.push(`<li>${line.replace(/^- /, '')}</li>`)
    }
    // 处理有序列表
    else if (line.match(/^(\d+)\. (.+)$/)) {
      if (!inList || listType !== 'ol') {
        if (inList) result.push(`</${listType}>`)
        result.push('<ol>')
        inList = true
        listType = 'ol'
      }
      result.push(`<li>${line.replace(/^\d+\. /, '')}</li>`)
    }
    // 其他内容
    else {
      if (inList) {
        result.push(`</${listType}>`)
        inList = false
        listType = ''
      }

      if (line === '') {
        result.push('')
      } else if (!line.includes('<h') && !line.includes('<pre>') &&
                !line.includes('<table>') && !line.includes('<blockquote>')) {
        result.push(`<p>${line}</p>`)
      } else {
        result.push(line)
      }
    }
  }

  // 关闭未闭合的列表
  if (inList) {
    result.push(`</${listType}>`)
  }

  return result.join('\n')
    // 清理多余的换行和空段落
    .replace(/\n\s*\n/g, '\n')
    .replace(/<p><\/p>/g, '')
    // 处理引用块的换行
    .replace(/<\/blockquote>\n<blockquote>/g, '<br>')
})

// 目录项
const tocItems = computed(() => {
  if (!document.value.content) return []

  const headings = []
  const lines = document.value.content.split('\n')

  lines.forEach((line, index) => {
    const match = line.match(/^(#{1,3})\s+(.+)$/)
    if (match) {
      const level = match[1].length
      const text = match[2].replace(/📖|🏗️|🔧|📝|🎨|⚡|🧪|📚|🔍|📖|💡/g, '').trim()
      const id = text.toLowerCase()
        .replace(/[^\w\s-]/g, '')
        .replace(/\s+/g, '-')
        .replace(/-+/g, '-')
        .replace(/^-|-$/g, '')

      headings.push({ id, text, level })
    }
  })

  return headings
})

// 点赞处理
const handleLike = () => {
  document.value.liked = true
  document.value.likes++
  ElMessage.success('点赞成功')
}

const handleUnlike = () => {
  document.value.liked = false
  document.value.likes--
}

// 收藏切换
const toggleFavorite = () => {
  document.value.favorited = !document.value.favorited
  ElMessage.success(document.value.favorited ? '收藏成功' : '取消收藏')
}

// 编辑文档
const editDocument = () => {
  router.push(`/editor/${document.value.id}`)
}

// 下拉菜单操作
const handleAction = (command: string) => {
  switch (command) {
    case 'share':
      ElMessage.info('分享功能开发中...')
      break
    case 'export':
      ElMessage.info('导出功能开发中...')
      break
    case 'history':
      showVersionHistory()
      break
    case 'submitReview':
      handleSubmitReview()
      break
    case 'approveReview':
      handleApproveReview()
      break
    case 'rejectReview':
      handleRejectReview()
      break
    case 'publish':
      handlePublishDocument()
      break
    case 'archive':
      handleArchiveDocument()
      break
    case 'lock':
      handleLockDocument()
      break
    case 'unlock':
      handleUnlockDocument()
      break
    case 'delete':
      handleDeleteDocument()
      break
  }
}

// 提交审核
const handleSubmitReview = () => {
  ElMessageBox.prompt(
    '请选择审核人员并填写审核说明',
    '提交审核',
    {
      confirmButtonText: '提交',
      cancelButtonText: '取消',
      inputPlaceholder: '请填写审核说明...',
      inputType: 'textarea'
    }
  ).then(({ value }) => {
    document.value.status = DocumentStatus.REVIEW
    document.value.reviewers = [
      { id: '2', name: '审核员' }
    ]
    document.value.reviewDeadline = new Date(Date.now() + 7 * 24 * 60 * 60 * 1000).toISOString()
    ElMessage.success('已提交审核，请等待审核人员处理')
  }).catch(() => {
    ElMessage.info('已取消提交审核')
  })
}

// 审核通过
const handleApproveReview = () => {
  ElMessageBox.prompt(
    '审核通过，请填写审核意见',
    '审核通过',
    {
      confirmButtonText: '通过',
      cancelButtonText: '取消',
      inputPlaceholder: '请填写审核意见...',
      inputType: 'textarea'
    }
  ).then(({ value }) => {
    document.value.status = DocumentStatus.PUBLISHED
    document.value.publishedAt = new Date().toISOString()
    document.value.reviewResult = {
      approved: true,
      comment: value || '审核通过',
      reviewer: currentUser.value.name,
      reviewedAt: new Date().toISOString()
    }
    ElMessage.success('审核通过，文档已发布')
  }).catch(() => {
    ElMessage.info('已取消审核')
  })
}

// 审核拒绝
const handleRejectReview = () => {
  ElMessageBox.prompt(
    '审核拒绝，请填写拒绝原因',
    '审核拒绝',
    {
      confirmButtonText: '拒绝',
      cancelButtonText: '取消',
      inputPlaceholder: '请填写拒绝原因...',
      inputType: 'textarea',
      inputValidator: (value) => {
        if (!value || value.trim() === '') {
          return '请填写拒绝原因'
        }
        return true
      }
    }
  ).then(({ value }) => {
    document.value.status = DocumentStatus.DRAFT
    document.value.reviewResult = {
      approved: false,
      comment: value,
      reviewer: currentUser.value.name,
      reviewedAt: new Date().toISOString()
    }
    ElMessage.success('审核已拒绝，文档退回草稿状态')
  }).catch(() => {
    ElMessage.info('已取消审核')
  })
}

// 发布文档
const handlePublishDocument = () => {
  ElMessageBox.confirm(
    '确定要直接发布这个文档吗？发布后其他用户将可以查看该文档。',
    '直接发布文档',
    {
      confirmButtonText: '发布',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    document.value.status = DocumentStatus.PUBLISHED
    document.value.publishedAt = new Date().toISOString()
    ElMessage.success('文档发布成功')
  }).catch(() => {
    ElMessage.info('已取消发布')
  })
}

// 归档文档
const handleArchiveDocument = () => {
  ElMessageBox.confirm(
    '确定要归档这个文档吗？归档后文档将变为只读状态。',
    '归档文档',
    {
      confirmButtonText: '归档',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    document.value.status = DocumentStatus.ARCHIVED
    ElMessage.success('文档归档成功')
  }).catch(() => {
    ElMessage.info('已取消归档')
  })
}

// 锁定文档
const handleLockDocument = () => {
  document.value.isLocked = true
  document.value.lockedBy = {
    id: currentUser.value.id,
    name: currentUser.value.name || '当前用户'
  }
  document.value.lockedAt = new Date().toISOString()
  ElMessage.success('文档已锁定')
}

// 解锁文档
const handleUnlockDocument = () => {
  document.value.isLocked = false
  document.value.lockedBy = undefined
  document.value.lockedAt = undefined
  ElMessage.success('文档已解锁')
}

// 删除文档
const handleDeleteDocument = () => {
  ElMessageBox.confirm(
    '确定要删除这个文档吗？此操作不可撤销。',
    '删除文档',
    {
      confirmButtonText: '删除',
      cancelButtonText: '取消',
      type: 'error'
    }
  ).then(() => {
    ElMessage.success('文档删除成功')
    router.push('/dashboard')
  }).catch(() => {
    ElMessage.info('已取消删除')
  })
}

// 标签搜索
const searchByTag = (tag: string) => {
  router.push(`/search?tag=${encodeURIComponent(tag)}`)
}

// 滚动到标题
const scrollToHeading = (id: string) => {
  const element = document.getElementById(id)
  if (element) {
    element.scrollIntoView({ behavior: 'smooth' })
  }
}

// 导航到文档
const navigateToDocument = (id: string) => {
  router.push(`/document/${id}`)
}

// 版本历史相关方法
const showVersionHistory = () => {
  showHistory.value = true
}

const closeVersionHistory = () => {
  showHistory.value = false
}

const handleVersionRestore = (version: any) => {
  // 恢复版本逻辑
  document.value.title = version.title
  document.value.content = version.content
  document.value.version = document.value.version + 1
  ElMessage.success(`已恢复到版本 v${version.version}，当前版本为 v${document.value.version}`)
  closeVersionHistory()
}

// 评论相关处理
const handleCommentAdded = (comment: any) => {
  comments.value.push(comment)
  document.value.comments++
  ElMessage.success('评论发布成功')
}

const handleCommentUpdated = (comment: any) => {
  const index = comments.value.findIndex((c: any) => c.id === comment.id)
  if (index !== -1) {
    comments.value[index] = comment
  }
}

const handleCommentDeleted = (commentId: string) => {
  const index = comments.value.findIndex((c: any) => c.id === commentId)
  if (index !== -1) {
    comments.value.splice(index, 1)
    document.value.comments--
  }
}

// 初始化数据
onMounted(async () => {
  const docId = route.params.id as string

  // 模拟加载文档数据
  document.value = {
    id: docId,
    title: 'Vue3 组件开发规范完整指南',
    content: `# Vue3 组件开发规范完整指南

## 📖 概述

本文档详细介绍了基于Vue3和TypeScript的组件开发规范，包含了从项目结构到编码规范的完整指导。遵循这些规范可以提高代码质量、团队协作效率和项目可维护性。

## 🏗️ 项目结构

### 目录结构
\`\`\`
src/
├── components/          # 公共组件
│   ├── ui/             # 基础UI组件
│   └── business/       # 业务组件
├── views/              # 页面组件
├── composables/        # 组合式函数
├── types/              # TypeScript类型定义
├── utils/              # 工具函数
└── assets/             # 静态资源
\`\`\`

### 文件命名规范
- **组件文件**: 使用PascalCase，如 \`UserCard.vue\`
- **工具文件**: 使用camelCase，如 \`formatDate.ts\`
- **类型文件**: 使用camelCase，如 \`userTypes.ts\`

## 🔧 组件开发规范

### 1. 单文件组件结构

\`\`\`vue
<template>
  <!-- 模板内容 -->
</template>

<script setup lang="ts">
// 脚本内容
</script>

<style scoped>
/* 样式内容 */
</style>
\`\`\`

### 2. Props定义

使用TypeScript接口定义Props：

\`\`\`typescript
interface Props {
  title: string
  count?: number
  disabled?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  count: 0,
  disabled: false
})
\`\`\`

### 3. 事件定义

\`\`\`typescript
const emit = defineEmits<{
  click: [id: string]
  update: [data: UserData]
}>()
\`\`\`

## 📝 编码最佳实践

### 1. 响应式数据

- 使用 \`ref()\` 定义基本数据类型
- 使用 \`reactive()\` 定义复杂对象
- 避免直接修改props

### 2. 计算属性

\`\`\`typescript
const computedValue = computed(() => {
  return props.title.toUpperCase()
})
\`\`\`

### 3. 生命周期

\`\`\`typescript
onMounted(() => {
  // 组件挂载后的逻辑
})

onUnmounted(() => {
  // 组件卸载前的清理
})
\`\`\`

## 🎨 样式规范

### 1. CSS变量

使用CSS变量来管理主题色彩：

\`\`\`css
:root {
  --primary-color: #409EFF;
  --success-color: #67C23A;
  --warning-color: #E6A23C;
  --danger-color: #F56C6C;
}
\`\`\`

### 2. 类命名

使用BEM命名规范：

\`\`\`css
.user-card {}
.user-card__title {}
.user-card__content {}
.user-card--active {}
\`\`\`

## ⚡ 性能优化

### 1. 懒加载

\`\`\`typescript
const LazyComponent = defineAsyncComponent(() => import('./Heavy.vue'))
\`\`\`

### 2. memo化

\`\`\`typescript
const expensiveComputation = computed(() => {
  return heavyCalculation(props.data)
})
\`\`\`

## 🧪 测试规范

### 1. 单元测试

每个组件都应该有对应的单元测试：

\`\`\`typescript
import { mount } from '@vue/test-utils'
import UserCard from './UserCard.vue'

describe('UserCard', () => {
  test('renders user name', () => {
    const wrapper = mount(UserCard, {
      props: { name: 'John Doe' }
    })
    expect(wrapper.text()).toContain('John Doe')
  })
})
\`\`\`

## 📚 组件库推荐

- **Element Plus**: 企业级UI组件库
- **Vant**: 移动端组件库
- **Ant Design Vue**: 企业级设计语言

## 🔍 代码检查工具

- **ESLint**: 代码质量检查
- **Prettier**: 代码格式化
- **Vue Tsc**: TypeScript类型检查

## 📖 学习资源

1. [Vue3官方文档](https://v3.vuejs.org/)
2. [TypeScript文档](https://www.typescriptlang.org/)
3. [Vue3设计模式](https://patterns.dev/vue)

## 💡 常见问题解答

### Q: 什么时候使用ref和reactive？
A: 基本数据类型使用ref，复杂对象使用reactive。

### Q: 如何优化大列表性能？
A: 使用虚拟滚动或分页加载。

### Q: 组件如何实现国际化？
A: 使用Vue I18n插件。

---

**最后更新**: 2024年1月15日
**版本**: v2.1.0
**维护者**: 前端技术团队`,
    author: {
      id: '1',
      name: '张三',
      avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
    },
    status: DocumentStatus.PUBLISHED,
    createdAt: '2024-01-15',
    updatedAt: '2024-01-15 10:30',
    publishedAt: '2024-01-15 10:30',
    views: 1856,
    likes: 89,
    comments: 7,
    liked: false,
    favorited: false,
    tags: ['Vue3', '开发规范', '组件', 'TypeScript', '最佳实践'],
    spaceId: '1',
    spaceName: '技术文档',
    permissions: [],
    version: 3,
    isLocked: false
  }

  // 模拟加载评论数据
  comments.value = [
    {
      id: '1',
      content: '这个文档非常详细和实用！特别是TypeScript的部分写得很清楚，对我们团队的Vue3项目很有帮助。我们按照这个规范重构了几个组件，代码质量确实提升了不少。',
      author: '李四',
      avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
      createdAt: '2024-01-15 11:00',
      likes: 25,
      liked: false,
      replies: [
        {
          id: '1-1',
          content: '同意！我们也在用这套规范，特别是组合式API的部分，对新手很友好。',
          author: '王五',
          avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
          createdAt: '2024-01-15 14:30',
          likes: 8,
          liked: true
        },
        {
          id: '1-2',
          content: '能否分享一下具体的重构经验？我们团队也在考虑迁移到Vue3。',
          author: '陈十一',
          avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
          createdAt: '2024-01-15 15:45',
          likes: 6,
          liked: false
        }
      ]
    },
    {
      id: '2',
      content: '组件库推荐部分很有价值，不过建议再加一些Vue3特有的状态管理方案，比如Pinia的使用规范。另外CSS变量的部分也可以展开讲讲主题切换的实现。',
      author: '赵六',
      avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
      createdAt: '2024-01-15 12:30',
      likes: 18,
      liked: true,
      replies: [
        {
          id: '2-1',
          content: '同求Pinia的最佳实践！我们项目正在考虑从Vuex迁移过来。',
          author: '刘十二',
          avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
          createdAt: '2024-01-15 16:20',
          likes: 12,
          liked: true
        }
      ]
    },
    {
      id: '3',
      content: '测试规范这块写得不错，我们正好在推广单元测试，这个例子很实用👍 不过能否补充一些集成测试和E2E测试的例子？',
      author: '孙七',
      avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
      createdAt: '2024-01-15 16:45',
      likes: 22,
      liked: false,
      replies: [
        {
          id: '3-1',
          content: '确实，我们团队按照这个开始写测试用例了，覆盖率从30%提升到80%。',
          author: '钱八',
          avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
          createdAt: '2024-01-15 17:20',
          likes: 15,
          liked: false
        },
        {
          id: '3-2',
          content: '我们用的Cypress做E2E测试，配合这套组件规范效果很好。',
          author: '胡十三',
          avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
          createdAt: '2024-01-15 18:00',
          likes: 7,
          liked: true
        }
      ]
    },
    {
      id: '4',
      content: '性能优化部分能不能再详细一些？特别是大数据量列表的处理，我们项目有个1万+数据的表格，现在性能很差。',
      author: '周九',
      avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
      createdAt: '2024-01-15 18:10',
      likes: 31,
      liked: false,
      replies: [
        {
          id: '4-1',
          content: '推荐用虚拟滚动，我们用的vue-virtual-scroller，1万条数据秒开。',
          author: '郭十四',
          avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
          createdAt: '2024-01-15 19:30',
          likes: 28,
          liked: true
        },
        {
          id: '4-2',
          content: '还可以考虑分页加载或者懒加载，我们的解决方案是每次只渲染可视区域的数据。',
          author: '林十五',
          avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
          createdAt: '2024-01-15 20:15',
          likes: 16,
          liked: false
        }
      ]
    },
    {
      id: '5',
      content: '文档结构很清晰，已经分享给团队其他成员了。希望能定期更新维护🙏 另外建议加个版本号方便跟踪更新。',
      author: '吴十',
      avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
      createdAt: '2024-01-15 19:30',
      likes: 14,
      liked: false,
      replies: [
        {
          id: '5-1',
          content: '同感！我们团队已经开始按照这个规范来了，期待后续更新。',
          author: '何十六',
          avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
          createdAt: '2024-01-15 21:00',
          likes: 9,
          liked: true
        }
      ]
    },
    {
      id: '6',
      content: '关于组件命名的部分很受用！我们之前命名很混乱，现在按照这个规范统一了风格。不过能否再详细说说业务组件和通用组件的划分标准？',
      author: '徐十七',
      avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
      createdAt: '2024-01-16 09:15',
      likes: 19,
      liked: true,
      replies: []
    },
    {
      id: '7',
      content: '代码检查工具那块很实用！我们配置了ESLint + Prettier，开发体验确实好了很多。建议再加上Husky配置git hooks的内容。',
      author: '朱十八',
      avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
      createdAt: '2024-01-16 11:30',
      likes: 13,
      liked: false,
      replies: [
        {
          id: '7-1',
          content: '对！我们还配置了commitlint，强制规范commit message。',
          author: '高十九',
          avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
          createdAt: '2024-01-16 14:20',
          likes: 8,
          liked: true
        }
      ]
    }
  ]

  // 增加浏览量
  document.value.views++
})
</script>

<style scoped>
.document-view {
  max-width: 1200px;
  margin: 0 auto;
  padding: var(--spacing-6);
  background: var(--bg-primary);
}

/* 文档头部 */
.document-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: var(--spacing-6);
  padding-bottom: var(--spacing-6);
  border-bottom: 1px solid var(--border-light);
}

.header-left {
  display: flex;
  align-items: flex-start;
  gap: var(--spacing-4);
  flex: 1;
}

.back-btn {
  margin-top: var(--spacing-2);
  color: var(--text-secondary);
}

.back-btn:hover {
  color: var(--primary-500);
}

.document-meta {
  flex: 1;
}

.document-title {
  font-size: var(--text-3xl);
  font-weight: var(--font-bold);
  color: var(--text-primary);
  margin: 0 0 var(--spacing-3) 0;
  line-height: var(--leading-tight);
}

.document-info {
  display: flex;
  gap: var(--spacing-6);
  color: var(--text-secondary);
  font-size: var(--text-sm);
}

.document-info span {
  display: flex;
  align-items: center;
  gap: var(--spacing-2);
}

.header-right {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: var(--spacing-3);
}

.document-stats {
  display: flex;
  align-items: center;
  gap: var(--spacing-4);
  color: var(--text-secondary);
  font-size: var(--text-sm);
}

.stat-item {
  display: flex;
  align-items: center;
  gap: var(--spacing-1);
}

.action-buttons {
  display: flex;
  gap: var(--spacing-2);
}

.is-favorite {
  color: var(--warning-500);
}

/* 文档标签 */
.document-tags {
  display: flex;
  gap: var(--spacing-2);
  margin-bottom: var(--spacing-6);
}

.tag-item {
  cursor: pointer;
  transition: all var(--duration-fast);
}

.tag-item:hover {
  opacity: 0.8;
  transform: translateY(-1px);
}

/* 文档内容 */
.document-content {
  display: grid;
  grid-template-columns: 1fr 250px;
  gap: var(--spacing-8);
  margin-bottom: var(--spacing-8);
  align-items: start;
}

.content-wrapper {
  background: var(--bg-primary);
  border-radius: var(--radius-lg);
  overflow: hidden;
}

.markdown-content {
  padding: var(--spacing-6);
  line-height: var(--leading-relaxed);
}

.markdown-content h1,
.markdown-content h2,
.markdown-content h3,
.markdown-content h4,
.markdown-content h5,
.markdown-content h6 {
  color: var(--text-primary);
  margin-top: var(--spacing-6);
  margin-bottom: var(--spacing-3);
  font-weight: var(--font-bold);
}

.markdown-content h1 {
  font-size: var(--text-2xl);
  border-bottom: 2px solid var(--border-light);
  padding-bottom: var(--spacing-2);
}

.markdown-content h2 {
  font-size: var(--text-xl);
}

.markdown-content h3 {
  font-size: var(--text-lg);
}

.markdown-content p {
  margin-bottom: var(--spacing-4);
  color: var(--text-primary);
}

.markdown-content ul,
.markdown-content ol {
  margin-bottom: var(--spacing-4);
  padding-left: var(--spacing-6);
}

.markdown-content li {
  margin-bottom: var(--spacing-2);
  color: var(--text-primary);
}

.markdown-content strong {
  font-weight: var(--font-semibold);
  color: var(--text-primary);
}

.markdown-content em {
  font-style: italic;
  color: var(--text-secondary);
}

.markdown-content code {
  background: var(--bg-tertiary);
  color: var(--danger-600);
  padding: 2px 6px;
  border-radius: var(--radius-sm);
  font-family: var(--font-mono);
  font-size: 0.9em;
}

.markdown-content pre {
  background: var(--bg-tertiary);
  border: 1px solid var(--border-medium);
  border-radius: var(--radius-md);
  padding: var(--spacing-4);
  margin: var(--spacing-4) 0;
  overflow-x: auto;
}

.markdown-content pre code {
  background: none;
  color: var(--text-primary);
  padding: 0;
  border-radius: 0;
  font-size: var(--text-sm);
}

.markdown-content a {
  color: var(--primary-600);
  text-decoration: none;
  border-bottom: 1px solid transparent;
  transition: all var(--duration-fast);
}

.markdown-content a:hover {
  color: var(--primary-700);
  border-bottom-color: var(--primary-400);
}

.markdown-content blockquote {
  border-left: 4px solid var(--primary-400);
  background: var(--primary-50);
  padding: var(--spacing-3) var(--spacing-4);
  margin: var(--spacing-4) 0;
  font-style: italic;
}

.markdown-content table {
  width: 100%;
  border-collapse: collapse;
  margin: var(--spacing-4) 0;
}

.markdown-content th,
.markdown-content td {
  border: 1px solid var(--border-medium);
  padding: var(--spacing-2) var(--spacing-3);
  text-align: left;
}

.markdown-content th {
  background: var(--bg-secondary);
  font-weight: var(--font-semibold);
}

/* 目录导航 */
.content-toc {
  position: sticky;
  top: var(--spacing-6);
  background: var(--bg-secondary);
  border: 1px solid var(--border-light);
  border-radius: var(--radius-lg);
  padding: var(--spacing-4);
  max-height: calc(100vh - 12rem);
  overflow-y: auto;
}

.toc-header h3 {
  font-size: var(--text-base);
  font-weight: var(--font-semibold);
  color: var(--text-primary);
  margin: 0 0 var(--spacing-3) 0;
}

.toc-items {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-1);
}

.toc-item {
  display: block;
  padding: var(--spacing-2) var(--spacing-3);
  color: var(--text-secondary);
  text-decoration: none;
  border-radius: var(--radius-md);
  font-size: var(--text-sm);
  line-height: var(--leading-snug);
  transition: all var(--duration-fast);
}

.toc-item:hover {
  background: var(--bg-tertiary);
  color: var(--primary-500);
}

.toc-level-3 {
  padding-left: var(--spacing-6);
  font-size: var(--text-xs);
}

.toc-level-4 {
  padding-left: var(--spacing-8);
  font-size: var(--text-xs);
}

/* 相关文档 */
.related-documents {
  margin-bottom: var(--spacing-8);
}

.section-title {
  font-size: var(--text-xl);
  font-weight: var(--font-bold);
  color: var(--text-primary);
  margin-bottom: var(--spacing-4);
}

.related-list {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: var(--spacing-4);
}

.related-item {
  background: var(--bg-secondary);
  border: 1px solid var(--border-light);
  border-radius: var(--radius-lg);
  padding: var(--spacing-4);
  cursor: pointer;
  transition: all var(--duration-fast);
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
}

.related-item:hover {
  border-color: var(--primary-300);
  box-shadow: var(--shadow-md);
  transform: translateY(-2px);
}

.related-info {
  flex: 1;
}

.related-title {
  font-size: var(--text-base);
  font-weight: var(--font-semibold);
  color: var(--text-primary);
  margin: 0 0 var(--spacing-2) 0;
  line-height: var(--leading-snug);
}

.related-description {
  font-size: var(--text-sm);
  color: var(--text-secondary);
  margin: 0 0 var(--spacing-2) 0;
  line-height: var(--leading-relaxed);
}

.related-meta {
  display: flex;
  gap: var(--spacing-4);
  font-size: var(--text-xs);
  color: var(--text-tertiary);
}

.related-stats {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-1);
  font-size: var(--text-xs);
  color: var(--text-tertiary);
  align-items: flex-end;
}

.related-stats span {
  display: flex;
  align-items: center;
  gap: var(--spacing-1);
}

/* 响应式设计 */
@media (max-width: 1024px) {
  .document-content {
    grid-template-columns: 1fr;
  }

  .content-toc {
    position: static;
    max-height: none;
  }
}

@media (max-width: 768px) {
  .document-view {
    padding: var(--spacing-4);
  }

  .document-header {
    flex-direction: column;
    gap: var(--spacing-4);
  }

  .header-right {
    align-items: flex-start;
    width: 100%;
  }

  .document-stats {
    flex-wrap: wrap;
  }

  .action-buttons {
    flex-wrap: wrap;
  }

  .document-info {
    flex-direction: column;
    gap: var(--spacing-2);
  }

  .related-list {
    grid-template-columns: 1fr;
  }
}

/* 下拉菜单危险操作样式 */
:deep(.danger-item) {
  color: var(--danger-600) !important;
}

:deep(.danger-item:hover) {
  background-color: var(--danger-50) !important;
  color: var(--danger-700) !important;
}

/* 审核信息样式 */
.review-info,
.review-result {
  margin-bottom: var(--spacing-6);
}

.review-details p,
.review-result-details p {
  margin: var(--spacing-2) 0;
  font-size: var(--text-sm);
  line-height: var(--leading-relaxed);
}

.review-details p:first-child,
.review-result-details p:first-child {
  margin-top: 0;
}

.review-details p:last-child,
.review-result-details p:last-child {
  margin-bottom: 0;
}
</style>