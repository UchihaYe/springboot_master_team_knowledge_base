<template>
  <div class="comment-section">
    <div class="section-header">
      <h3 class="section-title">
        <el-icon><ChatLineRound /></el-icon>
        评论 ({{ comments.length }})
      </h3>
    </div>

    <!-- 评论输入框 -->
    <div class="comment-input-section">
      <div class="comment-form">
        <div class="user-avatar">
          <el-avatar
            :src="currentUser.avatar"
            :alt="currentUser.name"
            size="default"
          />
        </div>
        <div class="input-area">
          <el-input
            v-model="newComment"
            type="textarea"
            :rows="3"
            placeholder="写下你的评论..."
            class="comment-textarea"
            :maxlength="1000"
            show-word-limit
            @keydown.ctrl.enter="submitComment"
            @keydown.meta.enter="submitComment"
          />
          <div class="input-actions">
            <div class="input-tips">
              <span class="tip-text">支持 Markdown 语法</span>
              <span class="shortcut-tip">Ctrl+Enter 发布</span>
            </div>
            <div class="action-buttons">
              <el-button @click="newComment = ''" :disabled="!newComment.trim()">
                清空
              </el-button>
              <el-button
                type="primary"
                @click="submitComment"
                :loading="submitting"
                :disabled="!newComment.trim()"
              >
                发布评论
              </el-button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 评论列表 -->
    <div class="comments-list">
      <div v-if="comments.length === 0" class="empty-comments">
        <el-empty description="暂无评论，快来发表第一条评论吧！" />
      </div>

      <div v-else class="comment-items">
        <CommentItem
          v-for="comment in sortedComments"
          :key="comment.id"
          :comment="comment"
          :current-user="currentUser"
          @reply="handleReply"
          @edit="handleEdit"
          @delete="handleDelete"
          @like="handleLike"
          @unlike="handleUnlike"
        />
      </div>
    </div>

    <!-- 加载更多 -->
    <div v-if="hasMore" class="load-more">
      <el-button @click="loadMore" :loading="loading" text>
        加载更多评论
      </el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import CommentItem from './CommentItem.vue'

interface Comment {
  id: string
  content: string
  author: {
    id: string
    name: string
    avatar: string
  }
  createdAt: string
  updatedAt?: string
  likes: number
  liked: boolean
  replies?: Comment[]
  parentId?: string
  mentionedUsers?: Array<{
    id: string
    name: string
  }>
}

interface Props {
  documentId: string
  comments?: Comment[]
  sortBy?: 'newest' | 'oldest' | 'likes'
}

const props = withDefaults(defineProps<Props>(), {
  comments: () => [],
  sortBy: 'newest'
})

const emit = defineEmits<{
  commentAdded: [comment: Comment]
  commentUpdated: [comment: Comment]
  commentDeleted: [commentId: string]
}>()

// 当前用户信息
const currentUser = ref({
  id: '1',
  name: '张三',
  avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
})

// 表单状态
const newComment = ref('')
const submitting = ref(false)
const loading = ref(false)
const hasMore = ref(false)

// 内部评论列表
const internalComments = ref<Comment[]>([...props.comments])

// 排序后的评论
const sortedComments = computed(() => {
  const comments = [...internalComments.value]

  switch (props.sortBy) {
    case 'oldest':
      return comments.sort((a, b) => new Date(a.createdAt).getTime() - new Date(b.createdAt).getTime())
    case 'likes':
      return comments.sort((a, b) => b.likes - a.likes)
    case 'newest':
    default:
      return comments.sort((a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime())
  }
})

// 提交评论
const submitComment = async () => {
  if (!newComment.value.trim() || submitting.value) return

  submitting.value = true

  try {
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 500))

    const comment: Comment = {
      id: Date.now().toString(),
      content: newComment.value.trim(),
      author: currentUser.value,
      createdAt: new Date().toISOString(),
      likes: 0,
      liked: false,
      replies: []
    }

    internalComments.value.unshift(comment)
    emit('commentAdded', comment)

    newComment.value = ''
    ElMessage.success('评论发布成功')
  } catch (error) {
    ElMessage.error('评论发布失败，请稍后重试')
    console.error('Submit comment failed:', error)
  } finally {
    submitting.value = false
  }
}

// 回复评论
const handleReply = (parentComment: Comment, replyContent: string) => {
  const reply: Comment = {
    id: Date.now().toString(),
    content: replyContent,
    author: currentUser.value,
    createdAt: new Date().toISOString(),
    likes: 0,
    liked: false,
    parentId: parentComment.id,
    replies: []
  }

  // 找到父评论并添加回复
  const parentIndex = internalComments.value.findIndex(c => c.id === parentComment.id)
  if (parentIndex !== -1) {
    if (!internalComments.value[parentIndex].replies) {
      internalComments.value[parentIndex].replies = []
    }
    internalComments.value[parentIndex].replies!.push(reply)
    emit('commentAdded', reply)
  }
}

// 编辑评论
const handleEdit = (comment: Comment, newContent: string) => {
  const findAndUpdate = (comments: Comment[]): boolean => {
    for (const c of comments) {
      if (c.id === comment.id) {
        c.content = newContent
        c.updatedAt = new Date().toISOString()
        emit('commentUpdated', c)
        return true
      }
      if (c.replies && findAndUpdate(c.replies)) {
        return true
      }
    }
    return false
  }

  findAndUpdate(internalComments.value)
  ElMessage.success('评论更新成功')
}

// 删除评论
const handleDelete = (commentId: string) => {
  const findAndDelete = (comments: Comment[]): boolean => {
    const index = comments.findIndex(c => c.id === commentId)
    if (index !== -1) {
      comments.splice(index, 1)
      emit('commentDeleted', commentId)
      return true
    }

    for (const comment of comments) {
      if (comment.replies && findAndDelete(comment.replies)) {
        return true
      }
    }
    return false
  }

  findAndDelete(internalComments.value)
  ElMessage.success('评论删除成功')
}

// 点赞评论
const handleLike = (commentId: string) => {
  const findAndLike = (comments: Comment[]): boolean => {
    for (const comment of comments) {
      if (comment.id === commentId) {
        comment.liked = true
        comment.likes++
        return true
      }
      if (comment.replies && findAndLike(comment.replies)) {
        return true
      }
    }
    return false
  }

  findAndLike(internalComments.value)
}

// 取消点赞评论
const handleUnlike = (commentId: string) => {
  const findAndUnlike = (comments: Comment[]): boolean => {
    for (const comment of comments) {
      if (comment.id === commentId) {
        comment.liked = false
        comment.likes = Math.max(0, comment.likes - 1)
        return true
      }
      if (comment.replies && findAndUnlike(comment.replies)) {
        return true
      }
    }
    return false
  }

  findAndUnlike(internalComments.value)
}

// 加载更多评论
const loadMore = async () => {
  loading.value = true

  try {
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 800))

    // 模拟加载更多数据
    const moreComments: Comment[] = [
      {
        id: (Date.now() + Math.random()).toString(),
        content: '这是加载的更多评论内容...',
        author: {
          id: '3',
          name: '王五',
          avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
        },
        createdAt: new Date(Date.now() - 86400000).toISOString(),
        likes: 1,
        liked: false,
        replies: []
      }
    ]

    internalComments.value.push(...moreComments)
    hasMore.value = false // 假设没有更多了
  } catch (error) {
    ElMessage.error('加载失败，请稍后重试')
  } finally {
    loading.value = false
  }
}

// 监听props变化
import { watch } from 'vue'

watch(() => props.comments, (newComments) => {
  internalComments.value = [...newComments]
}, { deep: true })

onMounted(() => {
  // 模拟检查是否有更多评论
  hasMore.value = internalComments.value.length >= 10
})
</script>

<style scoped>
.comment-section {
  background: var(--bg-primary);
  border-radius: var(--radius-lg);
  padding: var(--spacing-6);
  margin-top: var(--spacing-8);
}

.section-header {
  margin-bottom: var(--spacing-6);
  padding-bottom: var(--spacing-4);
  border-bottom: 1px solid var(--border-light);
}

.section-title {
  font-size: var(--text-xl);
  font-weight: var(--font-bold);
  color: var(--text-primary);
  margin: 0;
  display: flex;
  align-items: center;
  gap: var(--spacing-3);
}

/* 评论输入区域 */
.comment-input-section {
  margin-bottom: var(--spacing-8);
}

.comment-form {
  display: flex;
  gap: var(--spacing-4);
  align-items: flex-start;
}

.user-avatar {
  flex-shrink: 0;
}

.input-area {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: var(--spacing-3);
}

.comment-textarea {
  border-radius: var(--radius-md);
}

.comment-textarea :deep(.el-textarea__inner) {
  border: 1px solid var(--border-medium);
  border-radius: var(--radius-md);
  padding: var(--spacing-3);
  line-height: var(--leading-relaxed);
  resize: vertical;
  min-height: 80px;
}

.comment-textarea :deep(.el-textarea__inner):focus {
  border-color: var(--primary-500);
  box-shadow: 0 0 0 2px rgba(var(--primary-500-rgb), 0.1);
}

.input-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.input-tips {
  display: flex;
  gap: var(--spacing-4);
  color: var(--text-tertiary);
  font-size: var(--text-xs);
}

.tip-text,
.shortcut-tip {
  display: flex;
  align-items: center;
}

.action-buttons {
  display: flex;
  gap: var(--spacing-2);
}

/* 评论列表 */
.comments-list {
  margin-bottom: var(--spacing-6);
}

.empty-comments {
  padding: var(--spacing-8) 0;
  text-align: center;
}

.comment-items {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-6);
}

/* 加载更多 */
.load-more {
  text-align: center;
  padding-top: var(--spacing-4);
  border-top: 1px solid var(--border-light);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .comment-section {
    padding: var(--spacing-4);
  }

  .comment-form {
    flex-direction: column;
    gap: var(--spacing-3);
  }

  .input-actions {
    flex-direction: column;
    align-items: flex-start;
    gap: var(--spacing-3);
  }

  .action-buttons {
    width: 100%;
    justify-content: flex-end;
  }

  .input-tips {
    flex-direction: column;
    gap: var(--spacing-1);
  }
}
</style>