<template>
  <div class="space-detail">
    <!-- 空间头部 -->
    <div class="space-header">
      <div class="header-left">
        <el-button @click="$router.back()" text class="back-btn">
          <el-icon><ArrowLeft /></el-icon>
          返回
        </el-button>
        <div class="space-info">
          <div class="space-icon">
            <el-icon size="32" :color="spaceData.color">
              <component :is="spaceData.icon" />
            </el-icon>
          </div>
          <div class="space-meta">
            <h1 class="space-title">{{ spaceData.name }}</h1>
            <p class="space-description">{{ spaceData.description }}</p>
            <div class="space-stats">
              <span class="stat-item">
                <el-icon><Document /></el-icon>
                {{ spaceData.documentCount }} 文档
              </span>
              <span class="stat-item">
                <el-icon><User /></el-icon>
                {{ spaceData.memberCount }} 成员
              </span>
              <span class="stat-item">
                <el-icon><View /></el-icon>
                {{ spaceData.totalViews }} 浏览
              </span>
            </div>
          </div>
        </div>
      </div>
      <div class="header-right">
        <el-button @click="createDocument" type="primary">
          <el-icon><EditPen /></el-icon>
          新建文档
        </el-button>
        <el-dropdown @command="handleSpaceAction">
          <el-button>
            <el-icon><MoreFilled /></el-icon>
          </el-button>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="share">
                <el-icon><Share /></el-icon>
                分享空间
              </el-dropdown-item>
              <el-dropdown-item command="members">
                <el-icon><UserFilled /></el-icon>
                管理成员
              </el-dropdown-item>
              <el-dropdown-item command="settings">
                <el-icon><Setting /></el-icon>
                空间设置
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>

    <!-- 筛选和搜索栏 -->
    <div class="filter-bar">
      <div class="filter-left">
        <el-input
          v-model="searchQuery"
          placeholder="搜索文档..."
          class="search-input"
          clearable
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
        <el-select v-model="selectedTag" placeholder="按标签筛选" clearable>
          <el-option
            v-for="tag in availableTags"
            :key="tag"
            :label="tag"
            :value="tag"
          />
        </el-select>
        <el-select v-model="sortBy" placeholder="排序方式">
          <el-option label="最近修改" value="updated" />
          <el-option label="创建时间" value="created" />
          <el-option label="标题" value="title" />
          <el-option label="浏览量" value="views" />
          <el-option label="点赞数" value="likes" />
        </el-select>
      </div>
      <div class="filter-right">
        <el-button
          :type="batchMode ? 'primary' : ''"
          @click="toggleBatchMode"
        >
          <el-icon><Operation /></el-icon>
          {{ batchMode ? '退出批量' : '批量操作' }}
        </el-button>
        <el-button-group class="view-toggle">
          <el-button
            :type="viewMode === 'list' ? 'primary' : ''"
            @click="viewMode = 'list'"
          >
            <el-icon><List /></el-icon>
          </el-button>
          <el-button
            :type="viewMode === 'grid' ? 'primary' : ''"
            @click="viewMode = 'grid'"
          >
            <el-icon><Grid /></el-icon>
          </el-button>
        </el-button-group>
      </div>
    </div>

    <!-- 批量操作工具栏 -->
    <div v-if="batchMode" class="batch-toolbar">
      <div class="batch-selection">
        <el-checkbox
          :indeterminate="isIndeterminate"
          :model-value="isAllSelected"
          @change="toggleSelectAll"
        >
          全选
        </el-checkbox>
        <span class="selection-count">
          已选择 {{ selectedDocuments.size }} 个文档
        </span>
      </div>
      <div class="batch-actions">
        <el-button
          type="primary"
          :disabled="selectedDocuments.size === 0"
          @click="batchPublish"
        >
          <el-icon><Upload /></el-icon>
          批量发布
        </el-button>
        <el-button
          type="warning"
          :disabled="selectedDocuments.size === 0"
          @click="batchArchive"
        >
          <el-icon><FolderOpened /></el-icon>
          批量归档
        </el-button>
        <el-button
          type="danger"
          :disabled="selectedDocuments.size === 0"
          @click="batchDelete"
        >
          <el-icon><Delete /></el-icon>
          批量删除
        </el-button>
      </div>
    </div>

    <!-- 文档列表 -->
    <div class="documents-container">
      <div v-if="viewMode === 'list'" class="documents-list">
        <div
          v-for="doc in filteredDocuments"
          :key="doc.id"
          class="document-item"
          :class="{ 'selected': selectedDocuments.has(doc.id) }"
          @click="batchMode ? toggleDocumentSelection(doc.id) : openDocument(doc)"
        >
          <div v-if="batchMode" class="doc-checkbox" @click.stop>
            <el-checkbox
              :model-value="selectedDocuments.has(doc.id)"
              @change="toggleDocumentSelection(doc.id)"
            />
          </div>
          <div class="doc-icon">
            <el-icon size="24" color="var(--primary-500)">
              <Document />
            </el-icon>
          </div>
          <div class="doc-content">
            <div class="doc-header">
              <h3 class="doc-title">{{ doc.title }}</h3>
              <div class="doc-actions" @click.stop>
                <LikeButton
                  v-if="canLikeDoc(doc)"
                  :liked="doc.isLiked"
                  :count="doc.likes"
                  size="small"
                  type="document"
                  :target-id="doc.id"
                  @like="() => handleDocumentLike(doc)"
                  @unlike="() => handleDocumentUnlike(doc)"
                />
                <span v-else class="like-disabled">
                  <el-icon><SuitHeart /></el-icon>
                  {{ doc.likes }}
                </span>
                <el-button
                  :type="doc.isBookmarked ? 'warning' : ''"
                  size="small"
                  @click="toggleBookmark(doc)"
                  class="action-btn"
                >
                  <el-icon><Collection /></el-icon>
                </el-button>
                <el-dropdown @command="handleDocAction" trigger="click">
                  <el-button size="small" text>
                    <el-icon><MoreFilled /></el-icon>
                  </el-button>
                  <template #dropdown>
                    <el-dropdown-menu>
                      <el-dropdown-item :command="`edit-${doc.id}`">
                        <el-icon><Edit /></el-icon>
                        编辑
                      </el-dropdown-item>
                      <el-dropdown-item :command="`share-${doc.id}`">
                        <el-icon><Share /></el-icon>
                        分享
                      </el-dropdown-item>
                      <el-dropdown-item :command="`history-${doc.id}`">
                        <el-icon><Clock /></el-icon>
                        版本历史
                      </el-dropdown-item>
                      <el-dropdown-item divided :command="`delete-${doc.id}`">
                        <el-icon><Delete /></el-icon>
                        删除
                      </el-dropdown-item>
                    </el-dropdown-menu>
                  </template>
                </el-dropdown>
              </div>
            </div>
            <p class="doc-summary">{{ doc.summary }}</p>
            <div class="doc-tags">
              <el-tag
                :type="getDocumentStatusInfo(doc.status).color"
                size="small"
                class="doc-status-tag"
              >
                {{ getDocumentStatusInfo(doc.status).label }}
              </el-tag>
              <el-tag
                v-for="tag in doc.tags"
                :key="tag"
                size="small"
                class="doc-tag"
              >
                {{ tag }}
              </el-tag>
            </div>
            <div class="doc-meta">
              <div class="author-info">
                <el-avatar :src="doc.author.avatar" size="small" />
                <span class="author-name">{{ doc.author.name }}</span>
              </div>
              <div class="doc-stats">
                <span class="stat">
                  <el-icon><View /></el-icon>
                  {{ doc.views }}
                </span>
                <span class="stat">
                  <el-icon><ChatDotRound /></el-icon>
                  {{ doc.comments }}
                </span>
                <span class="update-time">{{ doc.updatedAt }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div v-else class="documents-grid">
        <div
          v-for="doc in filteredDocuments"
          :key="doc.id"
          class="document-card"
          @click="openDocument(doc)"
        >
          <div class="card-header">
            <div class="doc-icon">
              <el-icon size="20" color="var(--primary-500)">
                <Document />
              </el-icon>
            </div>
            <el-dropdown @command="handleDocAction" trigger="click" @click.stop>
              <el-button size="small" text>
                <el-icon><MoreFilled /></el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item :command="`edit-${doc.id}`">编辑</el-dropdown-item>
                  <el-dropdown-item :command="`share-${doc.id}`">分享</el-dropdown-item>
                  <el-dropdown-item :command="`delete-${doc.id}`">删除</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
          <div class="card-content">
            <h3 class="doc-title">{{ doc.title }}</h3>
            <p class="doc-summary">{{ doc.summary }}</p>
            <div class="doc-tags">
              <el-tag
                v-for="tag in doc.tags.slice(0, 2)"
                :key="tag"
                size="small"
              >
                {{ tag }}
              </el-tag>
              <span v-if="doc.tags.length > 2" class="more-tags">
                +{{ doc.tags.length - 2 }}
              </span>
            </div>
          </div>
          <div class="card-footer">
            <div class="author-info">
              <el-avatar :src="doc.author.avatar" size="small" />
              <span>{{ doc.author.name }}</span>
            </div>
            <div class="card-actions" @click.stop>
              <el-button
                :type="doc.isLiked ? 'primary' : ''"
                size="small"
                @click="toggleLike(doc)"
                text
              >
                <el-icon><Star /></el-icon>
                {{ doc.likes }}
              </el-button>
              <el-button
                :type="doc.isBookmarked ? 'warning' : ''"
                size="small"
                @click="toggleBookmark(doc)"
                text
              >
                <el-icon><Collection /></el-icon>
              </el-button>
            </div>
          </div>
        </div>
      </div>

      <!-- 空状态 -->
      <div v-if="filteredDocuments.length === 0" class="empty-state">
        <el-empty description="暂无文档">
          <el-button type="primary" @click="createDocument">
            <el-icon><EditPen /></el-icon>
            创建第一个文档
          </el-button>
        </el-empty>
      </div>
    </div>

    <!-- 评论抽屉 -->
    <el-drawer
      v-model="showComments"
      title="文档评论"
      direction="rtl"
      size="400px"
    >
      <div class="comments-container">
        <div class="comment-input">
          <el-input
            v-model="newComment"
            type="textarea"
            :rows="3"
            placeholder="写下你的评论..."
          />
          <el-button
            type="primary"
            @click="addComment"
            :disabled="!newComment.trim()"
            style="margin-top: 8px;"
          >
            发表评论
          </el-button>
        </div>

        <div class="comments-list">
          <div
            v-for="comment in documentComments"
            :key="comment.id"
            class="comment-item"
          >
            <el-avatar :src="comment.author.avatar" size="small" />
            <div class="comment-content">
              <div class="comment-header">
                <span class="author-name">{{ comment.author.name }}</span>
                <span class="comment-time">{{ comment.createdAt }}</span>
              </div>
              <p class="comment-text">{{ comment.content }}</p>
              <div class="comment-actions">
                <el-button size="small" text @click="replyComment(comment)">
                  回复
                </el-button>
                <el-button
                  v-if="comment.author.id === currentUser.id"
                  size="small"
                  text
                  type="danger"
                  @click="deleteComment(comment)"
                >
                  删除
                </el-button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import LikeButton from '@/components/LikeButton.vue'
import { DocumentStatus, UserRole } from '@/types/document'
import { canLikeDocument, getDocumentStatusInfo } from '@/utils/permissions'

const router = useRouter()
const route = useRoute()

// 当前用户
const currentUser = ref({
  id: '1',
  role: UserRole.EDITOR
})

// 响应式数据
const searchQuery = ref('')
const selectedTag = ref('')
const sortBy = ref('updated')
const viewMode = ref('list')
const showComments = ref(false)
const newComment = ref('')
const currentDocumentId = ref(null)

// 批量操作相关
const batchMode = ref(false)
const selectedDocuments = ref(new Set())


// 空间数据
const spaceData = ref({
  id: 1,
  name: '技术文档',
  description: '存放技术相关的文档和规范',
  icon: 'Document',
  color: '#67c23a',
  documentCount: 25,
  memberCount: 8,
  totalViews: 1268
})

// 文档列表
const documents = ref([
  {
    id: 1,
    title: 'Vue3 组件开发规范',
    summary: '详细介绍Vue3项目的开发标准和最佳实践，包含组件结构、命名规范、类型安全等内容。',
    content: '# Vue3 组件开发规范\n\n这是一个详细的开发规范文档...',
    tags: ['Vue3', '开发规范', '前端'],
    author: {
      id: 1,
      name: '张三',
      avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
    },
    views: 156,
    likes: 12,
    comments: 5,
    isLiked: false,
    isBookmarked: true,
    status: DocumentStatus.PUBLISHED,
    createdAt: '2024-01-15',
    updatedAt: '2 小时前'
  },
  {
    id: 2,
    title: 'API接口设计文档',
    summary: 'RESTful API设计规范和接口文档模板，包含请求响应格式、错误处理、版本控制等。',
    content: '# API接口设计文档\n\n本文档规范了API设计标准...',
    tags: ['API', '后端', '接口设计'],
    author: {
      id: 2,
      name: '李四',
      avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
    },
    views: 89,
    likes: 8,
    comments: 3,
    isLiked: true,
    isBookmarked: false,
    status: DocumentStatus.PUBLISHED,
    createdAt: '2024-01-14',
    updatedAt: '1 天前'
  },
  {
    id: 3,
    title: 'TypeScript配置指南',
    summary: 'TypeScript项目配置详解，包含编译选项、类型声明、工具链配置等实用内容。',
    content: '# TypeScript配置指南\n\n完整的TS配置说明...',
    tags: ['TypeScript', '配置', '工具链'],
    author: {
      id: 3,
      name: '王五',
      avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
    },
    views: 234,
    likes: 15,
    comments: 8,
    isLiked: false,
    isBookmarked: false,
    status: DocumentStatus.DRAFT,
    createdAt: '2024-01-13',
    updatedAt: '3 天前'
  }
])

// 评论数据
const documentComments = ref([
  {
    id: 1,
    content: '这个规范很详细，对新人很有帮助！',
    author: {
      id: 2,
      name: '李四',
      avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
    },
    createdAt: '2 小时前'
  },
  {
    id: 2,
    content: '建议增加一些实际的代码示例',
    author: {
      id: 3,
      name: '王五',
      avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
    },
    createdAt: '1 天前'
  }
])

// 可用标签
const availableTags = computed(() => {
  const tags = new Set()
  documents.value.forEach(doc => {
    doc.tags.forEach(tag => tags.add(tag))
  })
  return Array.from(tags)
})

// 过滤后的文档
const filteredDocuments = computed(() => {
  let filtered = documents.value

  // 搜索过滤
  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase()
    filtered = filtered.filter(doc =>
      doc.title.toLowerCase().includes(query) ||
      doc.summary.toLowerCase().includes(query) ||
      doc.tags.some(tag => tag.toLowerCase().includes(query))
    )
  }

  // 标签过滤
  if (selectedTag.value) {
    filtered = filtered.filter(doc => doc.tags.includes(selectedTag.value))
  }

  // 排序
  filtered.sort((a, b) => {
    switch (sortBy.value) {
      case 'title':
        return a.title.localeCompare(b.title)
      case 'views':
        return b.views - a.views
      case 'likes':
        return b.likes - a.likes
      case 'created':
        return new Date(b.createdAt) - new Date(a.createdAt)
      default: // updated
        return b.updatedAt.localeCompare(a.updatedAt)
    }
  })

  return filtered
})

// 检查文档是否可以点赞
const canLikeDoc = computed(() => {
  return (doc: any) => canLikeDocument(currentUser.value.role, doc.status)
})

// 批量选择相关的计算属性
const isAllSelected = computed(() => {
  return filteredDocuments.value.length > 0 &&
         selectedDocuments.value.size === filteredDocuments.value.length
})

const isIndeterminate = computed(() => {
  return selectedDocuments.value.size > 0 &&
         selectedDocuments.value.size < filteredDocuments.value.length
})

// 方法
const createDocument = () => {
  router.push('/editor/new')
}

const openDocument = (doc) => {
  router.push(`/document/${doc.id}`)
}

const toggleLike = (doc) => {
  doc.isLiked = !doc.isLiked
  doc.likes += doc.isLiked ? 1 : -1
  ElMessage.success(doc.isLiked ? '已点赞' : '已取消点赞')
}

const toggleBookmark = (doc) => {
  doc.isBookmarked = !doc.isBookmarked
  ElMessage.success(doc.isBookmarked ? '已收藏' : '已取消收藏')
}

// LikeButton组件的点赞处理函数
const handleDocumentLike = (doc) => {
  doc.isLiked = true
  doc.likes = (doc.likes || 0) + 1
}

const handleDocumentUnlike = (doc) => {
  doc.isLiked = false
  doc.likes = Math.max(0, (doc.likes || 0) - 1)
}

// 批量操作相关方法
const toggleBatchMode = () => {
  batchMode.value = !batchMode.value
  if (!batchMode.value) {
    selectedDocuments.value.clear()
  }
}

const toggleDocumentSelection = (docId) => {
  if (selectedDocuments.value.has(docId)) {
    selectedDocuments.value.delete(docId)
  } else {
    selectedDocuments.value.add(docId)
  }
}

const toggleSelectAll = () => {
  if (isAllSelected.value) {
    selectedDocuments.value.clear()
  } else {
    filteredDocuments.value.forEach(doc => {
      selectedDocuments.value.add(doc.id)
    })
  }
}

const batchPublish = () => {
  ElMessageBox.confirm(
    `确定要批量发布 ${selectedDocuments.value.size} 个文档吗？`,
    '批量发布',
    {
      confirmButtonText: '发布',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    selectedDocuments.value.forEach(docId => {
      const doc = documents.value.find(d => d.id === docId)
      if (doc && doc.status === DocumentStatus.DRAFT) {
        doc.status = DocumentStatus.PUBLISHED
        doc.publishedAt = new Date().toISOString()
      }
    })
    ElMessage.success(`成功发布 ${selectedDocuments.value.size} 个文档`)
    selectedDocuments.value.clear()
  }).catch(() => {
    ElMessage.info('已取消批量发布')
  })
}

const batchArchive = () => {
  ElMessageBox.confirm(
    `确定要批量归档 ${selectedDocuments.value.size} 个文档吗？`,
    '批量归档',
    {
      confirmButtonText: '归档',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    selectedDocuments.value.forEach(docId => {
      const doc = documents.value.find(d => d.id === docId)
      if (doc && doc.status === DocumentStatus.PUBLISHED) {
        doc.status = DocumentStatus.ARCHIVED
      }
    })
    ElMessage.success(`成功归档 ${selectedDocuments.value.size} 个文档`)
    selectedDocuments.value.clear()
  }).catch(() => {
    ElMessage.info('已取消批量归档')
  })
}

const batchDelete = () => {
  ElMessageBox.confirm(
    `确定要批量删除 ${selectedDocuments.value.size} 个文档吗？此操作不可撤销。`,
    '批量删除',
    {
      confirmButtonText: '删除',
      cancelButtonText: '取消',
      type: 'error'
    }
  ).then(() => {
    const deleteIds = Array.from(selectedDocuments.value)
    documents.value = documents.value.filter(doc => !deleteIds.includes(doc.id))
    ElMessage.success(`成功删除 ${deleteIds.length} 个文档`)
    selectedDocuments.value.clear()
  }).catch(() => {
    ElMessage.info('已取消批量删除')
  })
}

const handleDocAction = (command) => {
  const [action, docId] = command.split('-')
  const doc = documents.value.find(d => d.id === parseInt(docId))

  switch (action) {
    case 'edit':
      router.push(`/editor/${docId}`)
      break
    case 'share':
      ElMessage.info('分享功能开发中...')
      break
    case 'history':
      ElMessage.info('版本历史功能开发中...')
      break
    case 'delete':
      deleteDocument(doc)
      break
  }
}

const deleteDocument = async (doc) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除文档"${doc.title}"吗？`,
      '删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    const index = documents.value.findIndex(d => d.id === doc.id)
    if (index > -1) {
      documents.value.splice(index, 1)
      ElMessage.success('文档已删除')
    }
  } catch {
    // 用户取消
  }
}

const handleSpaceAction = (command) => {
  switch (command) {
    case 'share':
      ElMessage.info('分享功能开发中...')
      break
    case 'members':
      ElMessage.info('成员管理功能开发中...')
      break
    case 'settings':
      ElMessage.info('设置功能开发中...')
      break
  }
}

const addComment = () => {
  if (!newComment.value.trim()) return

  const comment = {
    id: Date.now(),
    content: newComment.value,
    author: currentUser.value,
    createdAt: '刚刚'
  }

  documentComments.value.unshift(comment)
  newComment.value = ''
  ElMessage.success('评论发表成功')
}

const replyComment = (comment) => {
  newComment.value = `@${comment.author.name} `
}

const deleteComment = async (comment) => {
  try {
    await ElMessageBox.confirm('确定要删除这条评论吗？', '删除确认')
    const index = documentComments.value.findIndex(c => c.id === comment.id)
    if (index > -1) {
      documentComments.value.splice(index, 1)
      ElMessage.success('评论已删除')
    }
  } catch {
    // 用户取消
  }
}

onMounted(() => {
  const spaceId = route.params.id
  // 根据spaceId加载空间数据
  console.log('加载空间:', spaceId)
})
</script>

<style scoped>
.space-detail {
  background: var(--bg-secondary);
  min-height: 100vh;
}

/* 空间头部 */
.space-header {
  background: var(--bg-primary);
  padding: var(--spacing-6);
  border-bottom: 1px solid var(--border-light);
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
}

.header-left {
  display: flex;
  align-items: flex-start;
  gap: var(--spacing-4);
}

.back-btn {
  color: var(--text-secondary);
  margin-top: var(--spacing-1);
}

.space-info {
  display: flex;
  gap: var(--spacing-4);
}

.space-icon {
  padding: var(--spacing-3);
  background: var(--bg-secondary);
  border-radius: var(--radius-lg);
}

.space-title {
  font-size: var(--text-3xl);
  font-weight: var(--font-bold);
  color: var(--text-primary);
  margin: 0 0 var(--spacing-2) 0;
}

.space-description {
  font-size: var(--text-lg);
  color: var(--text-secondary);
  margin: 0 0 var(--spacing-4) 0;
}

.space-stats {
  display: flex;
  gap: var(--spacing-6);
}

.stat-item {
  display: flex;
  align-items: center;
  gap: var(--spacing-1);
  color: var(--text-tertiary);
  font-size: var(--text-sm);
}

.header-right {
  display: flex;
  gap: var(--spacing-2);
}

/* 筛选栏 */
.filter-bar {
  background: var(--bg-primary);
  padding: var(--spacing-4) var(--spacing-6);
  border-bottom: 1px solid var(--border-light);
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.filter-left {
  display: flex;
  gap: var(--spacing-3);
  align-items: center;
}

.search-input {
  width: 300px;
}

.view-toggle {
  border-radius: var(--radius-md);
}

/* 文档容器 */
.documents-container {
  padding: var(--spacing-6);
}

/* 列表视图 */
.documents-list {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-4);
}

.document-item {
  background: var(--bg-primary);
  border: 1px solid var(--border-light);
  border-radius: var(--radius-lg);
  padding: var(--spacing-6);
  cursor: pointer;
  transition: all var(--duration-normal) var(--ease-out);
  display: flex;
  gap: var(--spacing-4);
}

.document-item:hover {
  border-color: var(--primary-300);
  box-shadow: var(--shadow-md);
  transform: translateY(-1px);
}

.doc-icon {
  flex-shrink: 0;
  padding: var(--spacing-3);
  background: var(--primary-50);
  border-radius: var(--radius-md);
  height: fit-content;
}

.doc-content {
  flex: 1;
  min-width: 0;
}

.doc-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: var(--spacing-3);
}

.doc-title {
  font-size: var(--text-xl);
  font-weight: var(--font-semibold);
  color: var(--text-primary);
  margin: 0;
  line-height: var(--leading-snug);
}

.doc-actions {
  display: flex;
  gap: var(--spacing-1);
  flex-shrink: 0;
}

.action-btn {
  padding: var(--spacing-1) var(--spacing-2);
}

.doc-summary {
  font-size: var(--text-base);
  color: var(--text-secondary);
  line-height: var(--leading-relaxed);
  margin: 0 0 var(--spacing-3) 0;
}

.doc-tags {
  display: flex;
  gap: var(--spacing-2);
  margin-bottom: var(--spacing-4);
  flex-wrap: wrap;
}

.doc-tag {
  border-radius: var(--radius-md);
}

.doc-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.author-info {
  display: flex;
  align-items: center;
  gap: var(--spacing-2);
}

.author-name {
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
  color: var(--text-primary);
}

.doc-stats {
  display: flex;
  align-items: center;
  gap: var(--spacing-4);
  font-size: var(--text-sm);
  color: var(--text-tertiary);
}

.stat {
  display: flex;
  align-items: center;
  gap: var(--spacing-1);
}

.update-time {
  color: var(--text-tertiary);
}

/* 网格视图 */
.documents-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: var(--spacing-4);
}

.document-card {
  background: var(--bg-primary);
  border: 1px solid var(--border-light);
  border-radius: var(--radius-lg);
  padding: var(--spacing-5);
  cursor: pointer;
  transition: all var(--duration-normal) var(--ease-out);
  display: flex;
  flex-direction: column;
  height: 240px;
}

.document-card:hover {
  border-color: var(--primary-300);
  box-shadow: var(--shadow-md);
  transform: translateY(-2px);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--spacing-3);
}

.card-content {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.card-content .doc-title {
  font-size: var(--text-lg);
  margin-bottom: var(--spacing-2);
}

.card-content .doc-summary {
  font-size: var(--text-sm);
  margin-bottom: var(--spacing-3);
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.more-tags {
  font-size: var(--text-xs);
  color: var(--text-tertiary);
}

.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: auto;
  padding-top: var(--spacing-3);
  border-top: 1px solid var(--border-light);
}

.card-actions {
  display: flex;
  gap: var(--spacing-1);
}

/* 评论 */
.comments-container {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.comment-input {
  padding: var(--spacing-4);
  border-bottom: 1px solid var(--border-light);
}

.comments-list {
  flex: 1;
  overflow-y: auto;
  padding: var(--spacing-4);
}

.comment-item {
  display: flex;
  gap: var(--spacing-3);
  margin-bottom: var(--spacing-4);
  align-items: flex-start;
}

.comment-content {
  flex: 1;
}

.comment-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--spacing-1);
}

.comment-header .author-name {
  font-weight: var(--font-semibold);
}

.comment-time {
  font-size: var(--text-xs);
  color: var(--text-tertiary);
}

.comment-text {
  margin: 0 0 var(--spacing-2) 0;
  line-height: var(--leading-relaxed);
}

.comment-actions {
  display: flex;
  gap: var(--spacing-2);
}

/* 空状态 */
.empty-state {
  text-align: center;
  padding: var(--spacing-16);
}

/* 批量操作工具栏 */
.batch-toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: var(--spacing-4) var(--spacing-6);
  background: var(--bg-secondary);
  border: 1px solid var(--border-light);
  border-radius: var(--radius-lg);
  margin-bottom: var(--spacing-4);
}

.batch-selection {
  display: flex;
  align-items: center;
  gap: var(--spacing-4);
}

.selection-count {
  font-size: var(--text-sm);
  color: var(--text-secondary);
}

.batch-actions {
  display: flex;
  gap: var(--spacing-2);
}

/* 文档复选框 */
.doc-checkbox {
  display: flex;
  align-items: center;
  margin-right: var(--spacing-3);
}

/* 选中状态的文档项 */
.document-item.selected {
  background-color: var(--primary-50);
  border-color: var(--primary-200);
}

/* 响应式 */
@media (max-width: 768px) {
  .space-header {
    flex-direction: column;
    gap: var(--spacing-4);
  }

  .filter-bar {
    flex-direction: column;
    gap: var(--spacing-3);
    align-items: stretch;
  }

  .filter-left {
    flex-direction: column;
  }

  .search-input {
    width: 100%;
  }

  .documents-grid {
    grid-template-columns: 1fr;
  }

  .document-item {
    flex-direction: column;
  }

  .doc-header {
    flex-direction: column;
    gap: var(--spacing-2);
  }

  .doc-meta {
    flex-direction: column;
    align-items: flex-start;
    gap: var(--spacing-2);
  }
}
</style>