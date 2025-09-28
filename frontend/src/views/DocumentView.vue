<template>
  <div class="document-view">
    <!-- 左侧大纲导航 -->
    <div class="left-sidebar" v-if="tocItems.length > 0">
      <div class="toc-section">
        <h3 class="toc-title">文章大纲</h3>
        <div class="toc-list">
          <div
            v-for="item in tocItems"
            :key="item.id"
            class="toc-item"
            :class="`toc-level-${item.level}`"
            @click="scrollToHeading(item.id)"
          >
            <span class="toc-text">{{ item.text }}</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 主要内容区域 -->
    <div class="main-layout">
      <!-- 顶部导航 -->
      <div class="top-header">
        <div class="header-left">
          <el-button @click="$router.back()" text class="back-btn">
            <el-icon><ArrowLeft /></el-icon>
            返回
          </el-button>
        </div>
        <div class="header-right">
          <span class="page-title">文章详情页</span>
        </div>
      </div>

      <!-- 内容容器 -->
      <div class="content-container">
        <!-- 文章头部信息 -->
        <div class="article-header">
          <h1 class="article-title">{{ document.title }}</h1>

          <!-- 作者信息和统计数据 -->
          <div class="author-stats-section">
            <!-- 作者信息 -->
            <div class="author-info">
              <el-avatar
                :size="40"
                :src="document.author.avatar"
                class="author-avatar"
              >
                {{ document.author.name.charAt(0) }}
              </el-avatar>
              <div class="author-details">
                <div class="author-name">{{ document.author.name }}</div>
              </div>
            </div>

            <!-- 统计数据 -->
            <div class="stats-list">
              <div class="stat-item">
                <el-icon class="stat-icon"><View /></el-icon>
                <span class="stat-label">点赞量</span>
                <span class="stat-value">{{ document.likes }}</span>
              </div>
              <div class="stat-item">
                <el-icon class="stat-icon"><View /></el-icon>
                <span class="stat-label">浏览量</span>
                <span class="stat-value">{{ document.views }}</span>
              </div>
              <div class="stat-item">
                <span class="stat-label">更新时间</span>
                <span class="stat-value">{{ document.updatedAt }}</span>
              </div>
              <div class="stat-item">
                <span class="stat-label">字数</span>
                <span class="stat-value">{{ Math.floor(document.content.length / 500) }}.{{ Math.floor((document.content.length % 500) / 50) }}k</span>
              </div>
            </div>
          </div>
        </div>

        <!-- 文档标签 -->
        <div class="document-tags" v-if="document.tags && document.tags.length > 0">
          <el-tag
            v-for="tag in document.tags"
            :key="tag"
            class="tag-item"
            type="primary"
            effect="light"
            @click="searchByTag(tag)"
          >
            {{ tag }}
          </el-tag>
        </div>

        <!-- 文档内容 -->
        <div class="document-content">
          <div v-html="renderedContent" class="markdown-content"></div>
        </div>

        <!-- 互动按钮区域 -->
        <div class="interaction-section">
          <div class="interaction-buttons">
            <el-button
              class="interaction-btn like-btn"
              :class="{ active: document.liked }"
              @click="toggleLike"
              size="large"
              round
            >
              <el-icon>❤️</el-icon>
            </el-button>

            <el-button
              class="interaction-btn comment-btn"
              size="large"
              round
            >
              <el-icon><ChatLineRound /></el-icon>
            </el-button>

            <el-button
              class="interaction-btn star-btn"
              :class="{ active: document.favorited }"
              @click="toggleFavorite"
              size="large"
              round
            >
              <el-icon><Star /></el-icon>
            </el-button>
          </div>
        </div>

        <!-- 评论区 -->
        <div class="comments-section" v-if="canComment">
          <div class="comments-header">
            <h3 class="comments-title">评论({{ comments.length }})</h3>
            <el-button type="primary" size="small">发表</el-button>
          </div>

          <div class="comment-list">
            <div v-for="comment in comments" :key="comment.id" class="comment-item">
              <el-avatar
                :size="40"
                class="comment-avatar"
              >
                {{ comment.author.charAt(0) }}
              </el-avatar>
              <div class="comment-content-wrapper">
                <div class="comment-header">
                  <strong class="comment-author">{{ comment.author }}</strong>
                  <span class="comment-time">{{ comment.createdAt }}</span>
                </div>
                <div class="comment-content">{{ comment.content }}</div>
              </div>
            </div>
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
  StarFilled,
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

// 大纲数据
const tocItems = ref([])

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

  const lines = document.value.content.split('\n')
  let processedContent = ''

  lines.forEach((line, index) => {
    // 处理标题并添加ID
    const headingMatch = line.match(/^(#{1,6})\s+(.+)$/)
    if (headingMatch) {
      const level = headingMatch[1].length
      const text = headingMatch[2].trim()
      const id = `heading-${index}-${text.replace(/[^\w\u4e00-\u9fa5]/g, '-').toLowerCase()}`
      processedContent += `<h${level} id="${id}">${text}</h${level}>\n`
    } else {
      processedContent += line + '\n'
    }
  })

  return processedContent
    .replace(/```(\w+)?\n([\s\S]*?)```/g, '<pre><code>$2</code></pre>')
    .replace(/`([^`]+)`/g, '<code>$1</code>')
    .replace(/\*\*([^*]+)\*\*/g, '<strong>$1</strong>')
    .replace(/\*([^*]+)\*/g, '<em>$1</em>')
    .replace(/^- (.+)$/gm, '<li>$1</li>')
    .replace(/(<li>.*<\/li>)/gs, '<ul>$1</ul>')
    .replace(/<\/ul>\s*<ul>/g, '')
    .split('\n\n')
    .map(p => {
      if (p.includes('<h') || p.includes('<ul>') || p.includes('<pre>')) {
        return p
      }
      return p.trim() ? `<p>${p}</p>` : ''
    })
    .filter(p => p)
    .join('')
})

// 点赞切换
const toggleLike = () => {
  document.value.liked = !document.value.liked
  document.value.likes += document.value.liked ? 1 : -1
  ElMessage.success(document.value.liked ? '点赞成功' : '取消点赞')
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

// 标签搜索
const searchByTag = (tag: string) => {
  router.push(`/search?tag=${encodeURIComponent(tag)}`)
}

// 生成大纲
const generateTOC = () => {
  const content = document.value.content
  const headings = []
  const lines = content.split('\n')

  lines.forEach((line, index) => {
    const headingMatch = line.match(/^(#{1,6})\s+(.+)$/)
    if (headingMatch) {
      const level = headingMatch[1].length
      const text = headingMatch[2].trim()
      const id = `heading-${index}-${text.replace(/[^\w\u4e00-\u9fa5]/g, '-').toLowerCase()}`

      headings.push({
        id,
        text,
        level
      })
    }
  })

  tocItems.value = headings
}

// 跳转到标题
const scrollToHeading = (headingId: string) => {
  const element = document.getElementById(headingId)
  if (element) {
    element.scrollIntoView({ behavior: 'smooth', block: 'start' })
  }
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

  // 生成大纲
  generateTOC()
})
</script>

<style scoped>
.document-view {
  display: flex;
  min-height: 100vh;
  background: #f8fafc;
}

/* 左侧大纲导航 */
.left-sidebar {
  width: 280px;
  background: white;
  border-right: 1px solid #e2e8f0;
  padding: 24px 20px;
  flex-shrink: 0;
  height: 100vh;
  overflow-y: auto;
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.05);
}

.toc-section {
  position: sticky;
  top: 0;
}

.toc-title {
  font-size: 16px;
  font-weight: 600;
  color: #1e293b;
  margin: 0 0 16px 0;
  padding-bottom: 8px;
  border-bottom: 2px solid #3b82f6;
}

.toc-list {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.toc-item {
  padding: 8px 12px;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.2s ease;
  border-left: 3px solid transparent;
}

.toc-item:hover {
  background: #f1f5f9;
  border-left-color: #3b82f6;
}

.toc-level-1 {
  font-weight: 600;
  color: #1e293b;
}

.toc-level-2 {
  margin-left: 16px;
  font-weight: 500;
  color: #475569;
}

.toc-level-3 {
  margin-left: 32px;
  color: #64748b;
}

.toc-level-4 {
  margin-left: 48px;
  color: #64748b;
  font-size: 14px;
}

.toc-level-5,
.toc-level-6 {
  margin-left: 64px;
  color: #94a3b8;
  font-size: 14px;
}

.toc-text {
  display: block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

/* 主要布局 */
.main-layout {
  flex: 1;
  display: flex;
  flex-direction: column;
  background: #f8fafc;
}

/* 顶部导航 */
.top-header {
  background: white;
  border-bottom: 1px solid #e2e8f0;
  padding: 16px 32px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.header-left {
  display: flex;
  align-items: center;
}

.back-btn {
  color: #64748b;
  font-size: 14px;
  display: flex;
  align-items: center;
  gap: 6px;
  border: none;
  background: transparent;
  padding: 8px 12px;
  border-radius: 6px;
  transition: all 0.2s ease;
}

.back-btn:hover {
  background: #f1f5f9;
  color: #475569;
}

.header-right {
  display: flex;
  align-items: center;
}

.page-title {
  font-size: 16px;
  font-weight: 600;
  color: #1e293b;
}

/* 内容容器 */
.content-container {
  flex: 1;
  max-width: 1000px;
  margin: 0 auto;
  padding: 32px;
  width: 100%;
}

/* 文章头部 */
.article-header {
  background: white;
  border-radius: 12px;
  padding: 32px;
  margin-bottom: 24px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
  border: 1px solid #e2e8f0;
}

.article-title {
  font-size: 36px;
  font-weight: 700;
  color: #1e293b;
  margin: 0 0 32px 0;
  line-height: 1.2;
}

/* 作者信息和统计数据区域 */
.author-stats-section {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 32px;
  margin-bottom: 24px;
  flex-wrap: wrap;
}

.author-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.author-avatar {
  flex-shrink: 0;
  border: 2px solid #3b82f6;
  box-shadow: 0 2px 8px rgba(59, 130, 246, 0.2);
}

.author-details {
  display: flex;
  flex-direction: column;
}

.author-name {
  font-size: 16px;
  font-weight: 600;
  color: #2563eb;
  cursor: pointer;
  transition: color 0.2s ease;
}

.author-name:hover {
  color: #1d4ed8;
}

/* 统计数据列表 */
.stats-list {
  display: flex;
  align-items: center;
  gap: 24px;
  flex-wrap: wrap;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 6px;
  color: #64748b;
  font-size: 14px;
}

.stat-icon {
  color: #3b82f6;
  font-size: 16px;
}

.stat-label {
  color: #64748b;
  white-space: nowrap;
}

.stat-value {
  color: #1e293b;
  font-weight: 600;
  white-space: nowrap;
}

/* 文档标签 */
.document-tags {
  margin-bottom: 24px;
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

.tag-item {
  cursor: pointer;
  border-radius: 20px;
  font-size: 14px;
  font-weight: 500;
  transition: all 0.2s ease;
}

.tag-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(37, 99, 235, 0.3);
}

/* 文档内容 */
.document-content {
  background: white;
  border-radius: 12px;
  padding: 40px;
  margin-bottom: 32px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
  border: 1px solid #e2e8f0;
}

.markdown-content {
  line-height: 1.8;
  color: #374151;
  font-size: 16px;
}

.markdown-content h1,
.markdown-content h2,
.markdown-content h3 {
  color: #1e293b;
  margin-top: 36px;
  margin-bottom: 20px;
  font-weight: 700;
}

.markdown-content h1 {
  font-size: 32px;
  border-bottom: 3px solid #3b82f6;
  padding-bottom: 16px;
}

.markdown-content h2 {
  font-size: 28px;
  color: #2563eb;
}

.markdown-content h3 {
  font-size: 24px;
  color: #3730a3;
}

.markdown-content p {
  margin-bottom: 20px;
  color: #4b5563;
}

.markdown-content ul {
  margin-bottom: 20px;
  padding-left: 28px;
}

.markdown-content li {
  margin-bottom: 10px;
  color: #4b5563;
}

.markdown-content code {
  background: #f3f4f6;
  color: #c026d3;
  padding: 4px 8px;
  border-radius: 6px;
  font-family: 'JetBrains Mono', 'Consolas', 'Monaco', monospace;
  font-size: 14px;
  font-weight: 500;
}

.markdown-content pre {
  background: #1e293b;
  color: #e2e8f0;
  border-radius: 12px;
  padding: 24px;
  margin: 24px 0;
  overflow-x: auto;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.markdown-content pre code {
  background: none;
  color: inherit;
  padding: 0;
  font-size: 14px;
}

/* 互动按钮区域 */
.interaction-section {
  background: white;
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 32px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
  border: 1px solid #e2e8f0;
}

.interaction-buttons {
  display: flex;
  justify-content: center;
  gap: 24px;
}

.interaction-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  min-width: 60px;
  height: 44px;
  border: 2px solid #e2e8f0;
  background: white;
  color: #64748b;
  cursor: pointer;
  transition: all 0.3s ease;
  font-weight: 500;
}

.interaction-btn:hover {
  border-color: #3b82f6;
  color: #3b82f6;
  background: #eff6ff;
  transform: translateY(-2px);
  box-shadow: 0 8px 16px rgba(59, 130, 246, 0.2);
}

.like-btn {
  border-color: #ef4444;
  color: #ef4444;
}

.like-btn:hover {
  border-color: #dc2626;
  color: #dc2626;
  background: #fef2f2;
}

.like-btn.active {
  border-color: #dc2626;
  color: #dc2626;
  background: #fee2e2;
}

.star-btn.active {
  border-color: #f59e0b;
  color: #f59e0b;
  background: #fffbeb;
}

.interaction-btn .el-icon {
  font-size: 18px;
}

/* 评论区域 */
.comments-section {
  background: white;
  border-radius: 12px;
  padding: 32px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
  border: 1px solid #e2e8f0;
}

.comments-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 28px;
  padding-bottom: 16px;
  border-bottom: 1px solid #e2e8f0;
}

.comments-title {
  color: #1e293b;
  margin: 0;
  font-size: 22px;
  font-weight: 700;
}

.comment-list {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.comment-item {
  display: flex;
  gap: 16px;
  padding: 20px;
  background: #f8fafc;
  border-radius: 12px;
  border: 1px solid #e2e8f0;
  transition: all 0.2s ease;
}

.comment-item:hover {
  background: #f1f5f9;
  border-color: #cbd5e1;
}

.comment-avatar {
  flex-shrink: 0;
  margin-top: 2px;
  border: 2px solid white;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.comment-content-wrapper {
  flex: 1;
}

.comment-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 12px;
}

.comment-author {
  color: #2563eb;
  font-size: 15px;
  font-weight: 600;
}

.comment-time {
  color: #64748b;
  font-size: 13px;
}

.comment-content {
  color: #4b5563;
  line-height: 1.7;
  font-size: 15px;
}

/* 响应式设计 */
@media (max-width: 1024px) {
  .left-sidebar {
    width: 250px;
  }

  .content-container {
    padding: 24px;
  }

  .article-title {
    font-size: 28px;
  }
}

@media (max-width: 768px) {
  .document-view {
    flex-direction: column;
  }

  .left-sidebar {
    width: 100%;
    padding: 20px;
  }

  .top-header {
    padding: 12px 16px;
  }

  .content-container {
    padding: 16px;
  }

  .article-header {
    padding: 24px;
  }

  .article-title {
    font-size: 24px;
  }

  .author-card {
    padding: 20px;
  }

  .document-content {
    padding: 24px;
  }

  .interaction-buttons {
    gap: 16px;
  }

  .interaction-btn {
    min-width: 50px;
    height: 40px;
  }

  .comments-section {
    padding: 24px;
  }

  .comment-item {
    padding: 16px;
  }

  .author-stats-section {
    flex-direction: column;
    align-items: flex-start;
    gap: 16px;
  }

  .stats-list {
    gap: 16px;
  }

  .stat-item {
    font-size: 13px;
  }
}
</style>