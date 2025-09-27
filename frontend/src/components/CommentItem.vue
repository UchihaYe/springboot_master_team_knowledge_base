<template>
  <div class="comment-item" :class="{ 'is-reply': isReply }">
    <div class="comment-content">
      <div class="comment-avatar">
        <el-avatar
          :src="comment.author.avatar"
          :alt="comment.author.name"
          :size="isReply ? 'small' : 'default'"
        />
      </div>

      <div class="comment-body">
        <div class="comment-header">
          <div class="author-info">
            <span class="author-name">{{ comment.author.name }}</span>
            <span class="comment-time">{{ formatTime(comment.createdAt) }}</span>
            <span v-if="comment.updatedAt" class="edited-mark">已编辑</span>
          </div>

          <div class="comment-actions" v-if="!isEditing">
            <LikeButton
              :liked="comment.liked"
              :count="comment.likes"
              :show-count="true"
              size="small"
              type="comment"
              :target-id="comment.id"
              @like="$emit('like', comment.id)"
              @unlike="$emit('unlike', comment.id)"
            />

            <el-button
              text
              size="small"
              @click="showReplyForm = !showReplyForm"
              v-if="!isReply"
            >
              <el-icon><ChatLineRound /></el-icon>
              回复
            </el-button>

            <el-dropdown
              @command="handleCommand"
              v-if="canEdit || canDelete"
            >
              <el-button text size="small">
                <el-icon><MoreFilled /></el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="edit" v-if="canEdit">
                    <el-icon><Edit /></el-icon>
                    编辑
                  </el-dropdown-item>
                  <el-dropdown-item command="delete" v-if="canDelete" divided>
                    <el-icon><Delete /></el-icon>
                    删除
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>

        <!-- 评论内容 -->
        <div v-if="!isEditing" class="comment-text">
          <div v-html="renderedContent" class="markdown-content"></div>
        </div>

        <!-- 编辑表单 -->
        <div v-else class="edit-form">
          <el-input
            v-model="editContent"
            type="textarea"
            :rows="3"
            class="edit-textarea"
            @keydown.ctrl.enter="saveEdit"
            @keydown.meta.enter="saveEdit"
            @keydown.esc="cancelEdit"
          />
          <div class="edit-actions">
            <div class="edit-tips">
              <span class="shortcut-tip">Ctrl+Enter 保存，Esc 取消</span>
            </div>
            <div class="edit-buttons">
              <el-button @click="cancelEdit" size="small">取消</el-button>
              <el-button
                type="primary"
                @click="saveEdit"
                size="small"
                :loading="saving"
                :disabled="!editContent.trim()"
              >
                保存
              </el-button>
            </div>
          </div>
        </div>

        <!-- 回复表单 -->
        <div v-if="showReplyForm" class="reply-form">
          <div class="reply-input">
            <el-input
              v-model="replyContent"
              type="textarea"
              :rows="2"
              :placeholder="`回复 @${comment.author.name}`"
              class="reply-textarea"
              @keydown.ctrl.enter="submitReply"
              @keydown.meta.enter="submitReply"
            />
            <div class="reply-actions">
              <div class="reply-tips">
                <span class="shortcut-tip">Ctrl+Enter 发布</span>
              </div>
              <div class="reply-buttons">
                <el-button @click="cancelReply" size="small">取消</el-button>
                <el-button
                  type="primary"
                  @click="submitReply"
                  size="small"
                  :loading="replying"
                  :disabled="!replyContent.trim()"
                >
                  回复
                </el-button>
              </div>
            </div>
          </div>
        </div>

        <!-- 回复列表 -->
        <div v-if="comment.replies && comment.replies.length > 0" class="replies-list">
          <CommentItem
            v-for="reply in comment.replies"
            :key="reply.id"
            :comment="reply"
            :current-user="currentUser"
            :is-reply="true"
            @edit="$emit('edit', $event, arguments[1])"
            @delete="$emit('delete', $event)"
            @like="$emit('like', $event)"
            @unlike="$emit('unlike', $event)"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import LikeButton from './LikeButton.vue'

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
}

interface Props {
  comment: Comment
  currentUser: {
    id: string
    name: string
    avatar: string
  }
  isReply?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  isReply: false
})

const emit = defineEmits<{
  reply: [comment: Comment, content: string]
  edit: [comment: Comment, content: string]
  delete: [commentId: string]
  like: [commentId: string]
  unlike: [commentId: string]
}>()

// 表单状态
const showReplyForm = ref(false)
const replyContent = ref('')
const replying = ref(false)

const isEditing = ref(false)
const editContent = ref('')
const saving = ref(false)

// 权限检查
const canEdit = computed(() => {
  return props.comment.author.id === props.currentUser.id
})

const canDelete = computed(() => {
  return props.comment.author.id === props.currentUser.id
})

// 渲染的内容
const renderedContent = computed(() => {
  // 这里应该使用markdown解析器，现在暂时简单处理
  let content = props.comment.content

  // 简单的换行处理
  content = content.replace(/\n/g, '<br>')

  // 简单的@用户处理
  content = content.replace(/@(\w+)/g, '<span class="mention">@$1</span>')

  // 简单的加粗处理
  content = content.replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')

  return content
})

// 时间格式化
const formatTime = (timeStr: string) => {
  const time = new Date(timeStr)
  const now = new Date()
  const diff = now.getTime() - time.getTime()

  const minutes = Math.floor(diff / 60000)
  const hours = Math.floor(diff / 3600000)
  const days = Math.floor(diff / 86400000)

  if (minutes < 1) return '刚刚'
  if (minutes < 60) return `${minutes}分钟前`
  if (hours < 24) return `${hours}小时前`
  if (days < 30) return `${days}天前`

  return time.toLocaleDateString('zh-CN')
}

// 下拉菜单操作
const handleCommand = (command: string) => {
  switch (command) {
    case 'edit':
      startEdit()
      break
    case 'delete':
      confirmDelete()
      break
  }
}

// 开始编辑
const startEdit = () => {
  isEditing.value = true
  editContent.value = props.comment.content
}

// 保存编辑
const saveEdit = async () => {
  if (!editContent.value.trim() || saving.value) return

  saving.value = true

  try {
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 300))

    emit('edit', props.comment, editContent.value.trim())
    isEditing.value = false
    ElMessage.success('评论更新成功')
  } catch (error) {
    ElMessage.error('保存失败，请稍后重试')
  } finally {
    saving.value = false
  }
}

// 取消编辑
const cancelEdit = () => {
  isEditing.value = false
  editContent.value = ''
}

// 确认删除
const confirmDelete = async () => {
  try {
    await ElMessageBox.confirm(
      '确定要删除这条评论吗？删除后无法恢复。',
      '确认删除',
      {
        confirmButtonText: '删除',
        cancelButtonText: '取消',
        type: 'warning',
        confirmButtonClass: 'el-button--danger'
      }
    )

    emit('delete', props.comment.id)
  } catch {
    // 用户取消删除
  }
}

// 提交回复
const submitReply = async () => {
  if (!replyContent.value.trim() || replying.value) return

  replying.value = true

  try {
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 300))

    emit('reply', props.comment, replyContent.value.trim())
    cancelReply()
    ElMessage.success('回复发布成功')
  } catch (error) {
    ElMessage.error('回复发布失败，请稍后重试')
  } finally {
    replying.value = false
  }
}

// 取消回复
const cancelReply = () => {
  showReplyForm.value = false
  replyContent.value = ''
}
</script>

<style scoped>
.comment-item {
  position: relative;
}

.comment-item.is-reply {
  margin-left: var(--spacing-8);
  padding-left: var(--spacing-4);
  border-left: 2px solid var(--border-light);
}

.comment-content {
  display: flex;
  gap: var(--spacing-3);
  align-items: flex-start;
}

.comment-avatar {
  flex-shrink: 0;
}

.comment-body {
  flex: 1;
  min-width: 0;
}

.comment-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--spacing-2);
}

.author-info {
  display: flex;
  align-items: center;
  gap: var(--spacing-3);
  flex: 1;
  min-width: 0;
}

.author-name {
  font-weight: var(--font-semibold);
  color: var(--text-primary);
  font-size: var(--text-sm);
}

.comment-time {
  color: var(--text-tertiary);
  font-size: var(--text-xs);
}

.edited-mark {
  color: var(--text-tertiary);
  font-size: var(--text-xs);
  font-style: italic;
}

.comment-actions {
  display: flex;
  align-items: center;
  gap: var(--spacing-2);
  opacity: 0;
  transition: opacity var(--duration-fast);
}

.comment-item:hover .comment-actions {
  opacity: 1;
}

.comment-text {
  margin-bottom: var(--spacing-3);
}

.markdown-content {
  color: var(--text-primary);
  font-size: var(--text-sm);
  line-height: var(--leading-relaxed);
  word-wrap: break-word;
}

.markdown-content :deep(.mention) {
  color: var(--primary-500);
  font-weight: var(--font-medium);
  cursor: pointer;
}

.markdown-content :deep(.mention):hover {
  text-decoration: underline;
}

/* 编辑表单 */
.edit-form {
  margin-bottom: var(--spacing-3);
}

.edit-textarea :deep(.el-textarea__inner) {
  border-radius: var(--radius-md);
  font-size: var(--text-sm);
}

.edit-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: var(--spacing-2);
}

.edit-tips {
  color: var(--text-tertiary);
  font-size: var(--text-xs);
}

.edit-buttons {
  display: flex;
  gap: var(--spacing-2);
}

/* 回复表单 */
.reply-form {
  margin-top: var(--spacing-3);
  padding: var(--spacing-3);
  background: var(--bg-secondary);
  border-radius: var(--radius-md);
}

.reply-input {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-2);
}

.reply-textarea :deep(.el-textarea__inner) {
  border-radius: var(--radius-md);
  font-size: var(--text-sm);
  background: var(--bg-primary);
}

.reply-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.reply-tips {
  color: var(--text-tertiary);
  font-size: var(--text-xs);
}

.reply-buttons {
  display: flex;
  gap: var(--spacing-2);
}

/* 回复列表 */
.replies-list {
  margin-top: var(--spacing-4);
  display: flex;
  flex-direction: column;
  gap: var(--spacing-4);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .comment-item.is-reply {
    margin-left: var(--spacing-4);
    padding-left: var(--spacing-3);
  }

  .comment-header {
    flex-direction: column;
    align-items: flex-start;
    gap: var(--spacing-2);
  }

  .comment-actions {
    opacity: 1;
    align-self: flex-end;
  }

  .author-info {
    flex-direction: column;
    align-items: flex-start;
    gap: var(--spacing-1);
  }

  .edit-actions,
  .reply-actions {
    flex-direction: column;
    align-items: flex-start;
    gap: var(--spacing-2);
  }

  .edit-buttons,
  .reply-buttons {
    align-self: flex-end;
  }
}

/* 动画效果 */
.reply-form {
  animation: slideDown 0.3s ease-out;
}

@keyframes slideDown {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.edit-form {
  animation: fadeIn 0.2s ease-out;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}
</style>