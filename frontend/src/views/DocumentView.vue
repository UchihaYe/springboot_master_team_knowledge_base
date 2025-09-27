<template>
  <div class="document-view">
    <!-- 返回按钮 -->
    <div class="header-section">
      <el-button @click="$router.back()" text class="back-btn">
        <el-icon><ArrowLeft /></el-icon>
        返回
      </el-button>
    </div>

    <!-- 文档标题和基本信息 -->
    <div class="document-header">
      <h1 class="document-title">{{ document.title }}</h1>
      <div class="document-meta">
        <el-tag :type="statusInfo.color" size="small">
          {{ statusInfo.label }}
        </el-tag>
        <span class="author">作者: {{ document.author.name }}</span>
        <span class="date">更新时间: {{ document.updatedAt }}</span>
      </div>
    </div>

    <!-- 文档统计 -->
    <div class="document-stats">
      <span class="stat-item">
        <el-icon><View /></el-icon>
        {{ document.views }} 浏览
      </span>
      <span class="stat-item">
        <el-icon><SuitHeart /></el-icon>
        {{ document.likes }} 点赞
      </span>
      <span class="stat-item">
        <el-icon><ChatLineRound /></el-icon>
        {{ document.comments }} 评论
      </span>
    </div>

    <!-- 操作按钮 -->
    <div class="action-buttons">
      <el-button @click="toggleFavorite">
        <el-icon><Star /></el-icon>
        {{ document.favorited ? '已收藏' : '收藏' }}
      </el-button>
      <el-button v-if="permissions.canEdit" @click="editDocument">
        <el-icon><Edit /></el-icon>
        编辑
      </el-button>
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
      <el-card>
        <div v-html="renderedContent" class="markdown-content"></div>
      </el-card>
    </div>

    <!-- 评论区 -->
    <div class="comments-section" v-if="canComment">
      <h3>评论 ({{ comments.length }})</h3>
      <div class="comment-list">
        <div v-for="comment in comments" :key="comment.id" class="comment-item">
          <div class="comment-header">
            <strong>{{ comment.author }}</strong>
            <span class="comment-time">{{ comment.createdAt }}</span>
          </div>
          <div class="comment-content">{{ comment.content }}</div>
          <div class="comment-actions">
            <el-button text size="small">
              <el-icon><SuitHeart /></el-icon>
              {{ comment.likes }}
            </el-button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import {
  ArrowLeft,
  View,
  SuitHeart,
  ChatLineRound,
  Star,
  Edit
} from '@element-plus/icons-vue'
import { DocumentStatus, UserRole } from '@/types/document'
import { checkDocumentPermissions, canCommentDocument, getDocumentStatusInfo } from '@/utils/permissions'

const route = useRoute()
const router = useRouter()

// 当前用户信息
const currentUser = ref({
  id: '1',
  name: '张三',
  role: UserRole.EDITOR
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
  status: DocumentStatus.PUBLISHED,
  createdAt: '',
  updatedAt: '',
  views: 0,
  likes: 0,
  comments: 0,
  liked: false,
  favorited: false,
  tags: [] as string[],
  spaceId: '',
  spaceName: '',
  version: 1,
  isLocked: false
})

// 评论数据
const comments = ref([
  {
    id: '1',
    content: '这个文档非常详细和实用！特别是TypeScript的部分写得很清楚。',
    author: '李四',
    createdAt: '2024-01-15 11:00',
    likes: 25
  },
  {
    id: '2',
    content: '组件库推荐部分很有价值，建议再加一些Vue3特有的状态管理方案。',
    author: '赵六',
    createdAt: '2024-01-15 12:30',
    likes: 18
  },
  {
    id: '3',
    content: '测试规范这块写得不错，我们正好在推广单元测试。',
    author: '孙七',
    createdAt: '2024-01-15 16:45',
    likes: 22
  }
])

// 权限检查
const permissions = computed(() => {
  const isAuthor = document.value.author.id === currentUser.value.id
  return checkDocumentPermissions(
    currentUser.value.role,
    document.value.status,
    isAuthor,
    document.value.isLocked,
    false
  )
})

// 状态信息
const statusInfo = computed(() => {
  return getDocumentStatusInfo(document.value.status)
})

// 是否可以评论
const canComment = computed(() => {
  return canCommentDocument(currentUser.value.role, document.value.status)
})

// Markdown内容渲染
const renderedContent = computed(() => {
  if (!document.value.content) return ''

  return document.value.content
    .replace(/^### (.*$)/gim, '<h3>$1</h3>')
    .replace(/^## (.*$)/gim, '<h2>$1</h2>')
    .replace(/^# (.*$)/gim, '<h1>$1</h1>')
    .replace(/```(\w+)?\n([\s\S]*?)```/g, '<pre><code>$2</code></pre>')
    .replace(/`([^`]+)`/g, '<code>$1</code>')
    .replace(/\*\*([^*]+)\*\*/g, '<strong>$1</strong>')
    .replace(/\*([^*]+)\*/g, '<em>$1</em>')
    .replace(/^- (.+)$/gm, '<li>$1</li>')
    .replace(/(<li>.*<\/li>)/gs, '<ul>$1</ul>')
    .replace(/<\/ul>\s*<ul>/g, '')
    .split('\n\n')
    .map(p => p.includes('<h') || p.includes('<ul>') || p.includes('<pre>') ? p : `<p>${p}</p>`)
    .join('')
})

// 收藏切换
const toggleFavorite = () => {
  document.value.favorited = !document.value.favorited
  ElMessage.success(document.value.favorited ? '收藏成功' : '取消收藏')
}

// 编辑文档
const editDocument = () => {
  router.push(`/editor/${document.value.id}`)
}

// 标签搜索
const searchByTag = (tag: string) => {
  router.push(`/search?tag=${encodeURIComponent(tag)}`)
}

// 初始化数据
onMounted(() => {
  const docId = route.params.id as string

  // 模拟加载文档数据
  document.value = {
    id: docId,
    title: 'Vue3 组件开发规范完整指南',
    content: `# Vue3 组件开发规范完整指南

## 概述

本文档详细介绍了基于Vue3和TypeScript的组件开发规范，包含了从项目结构到编码规范的完整指导。

## 项目结构

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

## 组件开发规范

### 1. 单文件组件结构

推荐的组件结构：

- **template**: 模板部分
- **script**: 逻辑部分
- **style**: 样式部分

### 2. Props定义

使用TypeScript接口定义Props：

\`\`\`typescript
interface Props {
  title: string
  count?: number
  disabled?: boolean
}
\`\`\`

### 3. 事件定义

明确定义组件事件：

\`\`\`typescript
const emit = defineEmits<{
  click: [id: string]
  update: [data: UserData]
}>()
\`\`\`

## 编码最佳实践

### 响应式数据

- 使用 **ref()** 定义基本数据类型
- 使用 **reactive()** 定义复杂对象
- 避免直接修改props

### 计算属性

善用computed来处理派生状态。

### 生命周期

合理使用onMounted、onUnmounted等生命周期钩子。

## 样式规范

### CSS变量

使用CSS变量管理主题色彩。

### 类命名

推荐使用BEM命名规范。

## 性能优化

- 使用懒加载
- 合理使用memo化
- 避免不必要的重新渲染

## 测试规范

每个组件都应该有对应的单元测试。

## 总结

遵循这些规范可以提高代码质量、团队协作效率和项目可维护性。`,
    author: {
      id: '1',
      name: '张三',
      avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
    },
    status: DocumentStatus.PUBLISHED,
    createdAt: '2024-01-15',
    updatedAt: '2024-01-15 10:30',
    views: 1856,
    likes: 89,
    comments: 3,
    liked: false,
    favorited: false,
    tags: ['Vue3', '开发规范', '组件', 'TypeScript'],
    spaceId: '1',
    spaceName: '技术文档',
    version: 3,
    isLocked: false
  }

  // 增加浏览量
  document.value.views++
})
</script>

<style scoped>
.document-view {
  max-width: 1000px;
  margin: 0 auto;
  padding: 20px;
}

.header-section {
  margin-bottom: 20px;
}

.back-btn {
  color: #606266;
}

.back-btn:hover {
  color: #409eff;
}

.document-header {
  margin-bottom: 20px;
  padding-bottom: 20px;
  border-bottom: 1px solid #ebeef5;
}

.document-title {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
  margin: 0 0 15px 0;
}

.document-meta {
  display: flex;
  align-items: center;
  gap: 15px;
  color: #909399;
  font-size: 14px;
}

.document-stats {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
  padding: 15px;
  background: #f5f7fa;
  border-radius: 8px;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 5px;
  color: #606266;
  font-size: 14px;
}

.action-buttons {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}

.document-tags {
  display: flex;
  gap: 8px;
  margin-bottom: 20px;
}

.tag-item {
  cursor: pointer;
}

.tag-item:hover {
  opacity: 0.8;
}

.document-content {
  margin-bottom: 30px;
}

.markdown-content {
  line-height: 1.6;
}

.markdown-content h1,
.markdown-content h2,
.markdown-content h3 {
  color: #303133;
  margin-top: 20px;
  margin-bottom: 10px;
}

.markdown-content h1 {
  font-size: 24px;
  border-bottom: 2px solid #ebeef5;
  padding-bottom: 10px;
}

.markdown-content h2 {
  font-size: 20px;
}

.markdown-content h3 {
  font-size: 18px;
}

.markdown-content p {
  margin-bottom: 15px;
  color: #606266;
}

.markdown-content ul {
  margin-bottom: 15px;
  padding-left: 20px;
}

.markdown-content li {
  margin-bottom: 5px;
  color: #606266;
}

.markdown-content code {
  background: #f5f7fa;
  color: #e6a23c;
  padding: 2px 6px;
  border-radius: 4px;
  font-family: 'Courier New', monospace;
}

.markdown-content pre {
  background: #f5f7fa;
  border: 1px solid #ebeef5;
  border-radius: 8px;
  padding: 15px;
  margin: 15px 0;
  overflow-x: auto;
}

.markdown-content pre code {
  background: none;
  color: #303133;
  padding: 0;
}

.comments-section {
  margin-top: 30px;
}

.comments-section h3 {
  color: #303133;
  margin-bottom: 20px;
  font-size: 18px;
}

.comment-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.comment-item {
  background: #f5f7fa;
  border-radius: 8px;
  padding: 15px;
}

.comment-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.comment-header strong {
  color: #303133;
}

.comment-time {
  color: #909399;
  font-size: 12px;
}

.comment-content {
  color: #606266;
  line-height: 1.5;
  margin-bottom: 10px;
}

.comment-actions {
  display: flex;
  gap: 10px;
}

@media (max-width: 768px) {
  .document-view {
    padding: 15px;
  }

  .document-meta {
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
  }

  .document-stats {
    flex-direction: column;
    gap: 10px;
  }

  .action-buttons {
    flex-direction: column;
  }
}
</style>