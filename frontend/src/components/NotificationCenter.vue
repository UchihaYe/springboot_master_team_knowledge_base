<template>
  <div class="notification-center">
    <el-popover
      :visible="visible"
      placement="bottom-end"
      :width="400"
      trigger="click"
      popper-class="notification-popover"
      @hide="handleClose"
    >
      <template #reference>
        <el-button
          @click="toggleNotifications"
          text
          class="notification-trigger"
          :class="{ 'has-unread': unreadCount > 0 }"
        >
          <el-badge
            :value="unreadCount"
            :max="99"
            :hidden="unreadCount === 0"
            class="notification-badge"
          >
            <el-icon size="20">
              <Bell />
            </el-icon>
          </el-badge>
        </el-button>
      </template>

      <div class="notification-panel">
        <!-- 头部 -->
        <div class="notification-header">
          <div class="header-left">
            <h3 class="header-title">通知中心</h3>
            <span class="unread-count" v-if="unreadCount > 0">
              {{ unreadCount }} 条未读
            </span>
          </div>
          <div class="header-actions">
            <el-button
              text
              size="small"
              @click="markAllAsRead"
              :disabled="unreadCount === 0"
            >
              全部已读
            </el-button>
            <el-button
              text
              size="small"
              @click="clearAll"
              :disabled="notifications.length === 0"
            >
              清空
            </el-button>
          </div>
        </div>

        <!-- 筛选标签 -->
        <div class="notification-filters">
          <el-radio-group v-model="activeFilter" size="small" @change="handleFilterChange">
            <el-radio-button label="all">全部</el-radio-button>
            <el-radio-button label="unread">未读</el-radio-button>
            <el-radio-button label="mentions">@我的</el-radio-button>
            <el-radio-button label="likes">点赞</el-radio-button>
          </el-radio-group>
        </div>

        <!-- 通知列表 -->
        <div class="notification-list" v-loading="loading">
          <div v-if="filteredNotifications.length === 0" class="empty-notifications">
            <el-empty
              :description="getEmptyDescription()"
              :image-size="60"
            />
          </div>

          <div v-else class="notification-items">
            <div
              v-for="notification in filteredNotifications"
              :key="notification.id"
              class="notification-item"
              :class="{
                'is-unread': !notification.read,
                'is-mention': notification.type === 'mention',
                'is-like': notification.type === 'like',
                'is-comment': notification.type === 'comment'
              }"
              @click="handleNotificationClick(notification)"
            >
              <div class="notification-avatar">
                <el-avatar
                  :src="notification.avatar"
                  :alt="notification.sender"
                  size="small"
                />
              </div>

              <div class="notification-content">
                <div class="notification-text">
                  <span class="sender-name">{{ notification.sender }}</span>
                  <span class="action-text">{{ notification.action }}</span>
                  <span class="target-text">{{ notification.target }}</span>
                </div>
                <div class="notification-meta">
                  <span class="notification-time">{{ formatTime(notification.createdAt) }}</span>
                  <span class="notification-type">{{ getTypeLabel(notification.type) }}</span>
                </div>
              </div>

              <div class="notification-actions">
                <el-icon
                  v-if="!notification.read"
                  class="unread-indicator"
                  color="var(--primary-500)"
                >
                  <CircleFilled />
                </el-icon>
                <el-dropdown @command="(cmd) => handleAction(cmd, notification)">
                  <el-button text size="small" class="action-btn">
                    <el-icon><MoreFilled /></el-icon>
                  </el-button>
                  <template #dropdown>
                    <el-dropdown-menu>
                      <el-dropdown-item
                        :command="notification.read ? 'unread' : 'read'"
                      >
                        {{ notification.read ? '标为未读' : '标为已读' }}
                      </el-dropdown-item>
                      <el-dropdown-item command="delete" divided>
                        删除通知
                      </el-dropdown-item>
                    </el-dropdown-menu>
                  </template>
                </el-dropdown>
              </div>
            </div>
          </div>
        </div>

        <!-- 底部 -->
        <div class="notification-footer" v-if="hasMore">
          <el-button
            text
            @click="loadMore"
            :loading="loadingMore"
            class="load-more-btn"
          >
            加载更多
          </el-button>
        </div>
      </div>
    </el-popover>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'

interface Notification {
  id: string
  type: 'comment' | 'like' | 'mention' | 'system' | 'review'
  sender: string
  avatar: string
  action: string
  target: string
  targetId?: string
  targetType?: 'document' | 'comment' | 'space'
  createdAt: string
  read: boolean
  data?: any
}

const router = useRouter()

// 状态管理
const visible = ref(false)
const loading = ref(false)
const loadingMore = ref(false)
const activeFilter = ref('all')
const hasMore = ref(true)

// 通知数据
const notifications = ref<Notification[]>([
  {
    id: '1',
    type: 'comment',
    sender: '李四',
    avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
    action: '评论了你的文档',
    target: 'Vue3 组件开发规范',
    targetId: '1',
    targetType: 'document',
    createdAt: '2024-01-15T10:30:00Z',
    read: false
  },
  {
    id: '2',
    type: 'like',
    sender: '王五',
    avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
    action: '点赞了你的文档',
    target: 'TypeScript 进阶教程',
    targetId: '2',
    targetType: 'document',
    createdAt: '2024-01-15T09:15:00Z',
    read: false
  },
  {
    id: '3',
    type: 'mention',
    sender: '赵六',
    avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
    action: '在评论中提到了你',
    target: 'API设计规范指南',
    targetId: '3',
    targetType: 'comment',
    createdAt: '2024-01-15T08:45:00Z',
    read: false
  },
  {
    id: '4',
    type: 'system',
    sender: '系统通知',
    avatar: '',
    action: '你的文档',
    target: '"React Hooks 指南" 即将过期，请及时更新',
    createdAt: '2024-01-14T16:00:00Z',
    read: true
  },
  {
    id: '5',
    type: 'review',
    sender: '孙七',
    avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
    action: '请求审查文档',
    target: '项目架构设计方案',
    targetId: '4',
    targetType: 'document',
    createdAt: '2024-01-14T14:20:00Z',
    read: true
  }
])

// 计算属性
const unreadCount = computed(() => {
  return notifications.value.filter(n => !n.read).length
})

const filteredNotifications = computed(() => {
  let filtered = notifications.value

  switch (activeFilter.value) {
    case 'unread':
      filtered = notifications.value.filter(n => !n.read)
      break
    case 'mentions':
      filtered = notifications.value.filter(n => n.type === 'mention')
      break
    case 'likes':
      filtered = notifications.value.filter(n => n.type === 'like')
      break
    case 'all':
    default:
      filtered = notifications.value
      break
  }

  return filtered.sort((a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime())
})

// 方法
const toggleNotifications = () => {
  visible.value = !visible.value
  if (visible.value && unreadCount.value > 0) {
    // 延迟标记为已读，给用户时间查看
    setTimeout(() => {
      // 这里可以实现部分已读逻辑
    }, 2000)
  }
}

const handleClose = () => {
  visible.value = false
}

const handleFilterChange = (filter: string) => {
  activeFilter.value = filter
}

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

const getTypeLabel = (type: string) => {
  const labels = {
    comment: '评论',
    like: '点赞',
    mention: '提及',
    system: '系统',
    review: '审查'
  }
  return labels[type] || type
}

const getEmptyDescription = () => {
  switch (activeFilter.value) {
    case 'unread':
      return '没有未读通知'
    case 'mentions':
      return '没有@提及通知'
    case 'likes':
      return '没有点赞通知'
    default:
      return '暂无通知'
  }
}

const handleNotificationClick = (notification: Notification) => {
  // 标记为已读
  if (!notification.read) {
    notification.read = true
  }

  // 根据通知类型跳转
  if (notification.targetId && notification.targetType) {
    switch (notification.targetType) {
      case 'document':
        router.push(`/document/${notification.targetId}`)
        break
      case 'comment':
        router.push(`/document/${notification.targetId}#comment`)
        break
      case 'space':
        router.push(`/space/${notification.targetId}`)
        break
    }
  }

  // 关闭通知面板
  visible.value = false
}

const handleAction = async (command: string, notification: Notification) => {
  switch (command) {
    case 'read':
      notification.read = true
      ElMessage.success('已标记为已读')
      break
    case 'unread':
      notification.read = false
      ElMessage.success('已标记为未读')
      break
    case 'delete':
      try {
        await ElMessageBox.confirm(
          '确定要删除这条通知吗？',
          '确认删除',
          {
            confirmButtonText: '删除',
            cancelButtonText: '取消',
            type: 'warning'
          }
        )
        const index = notifications.value.findIndex(n => n.id === notification.id)
        if (index !== -1) {
          notifications.value.splice(index, 1)
          ElMessage.success('通知已删除')
        }
      } catch {
        // 用户取消删除
      }
      break
  }
}

const markAllAsRead = () => {
  notifications.value.forEach(n => {
    n.read = true
  })
  ElMessage.success('所有通知已标记为已读')
}

const clearAll = async () => {
  try {
    await ElMessageBox.confirm(
      '确定要清空所有通知吗？清空后无法恢复。',
      '确认清空',
      {
        confirmButtonText: '清空',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    notifications.value = []
    ElMessage.success('通知已清空')
  } catch {
    // 用户取消清空
  }
}

const loadMore = async () => {
  loadingMore.value = true

  try {
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 800))

    // 模拟加载更多通知
    const moreNotifications: Notification[] = [
      {
        id: Date.now().toString(),
        type: 'comment',
        sender: '新用户',
        avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
        action: '评论了你的文档',
        target: '更多历史文档',
        createdAt: new Date(Date.now() - 86400000 * 2).toISOString(),
        read: true
      }
    ]

    notifications.value.push(...moreNotifications)
    hasMore.value = false // 假设没有更多了
  } catch (error) {
    ElMessage.error('加载失败，请稍后重试')
  } finally {
    loadingMore.value = false
  }
}

// 生命周期
onMounted(() => {
  // 模拟加载通知数据
  loading.value = false
})

// 暴露给父组件的方法
const addNotification = (notification: Notification) => {
  notifications.value.unshift(notification)
}

defineExpose({
  addNotification
})
</script>

<style scoped>
.notification-center {
  position: relative;
}

.notification-trigger {
  padding: var(--spacing-2);
  border-radius: var(--radius-md);
  transition: all var(--duration-fast);
  color: var(--text-secondary);
}

.notification-trigger:hover {
  background: var(--bg-secondary);
  color: var(--text-primary);
}

.notification-trigger.has-unread {
  color: var(--primary-500);
}

.notification-badge {
  display: inline-block;
}

/* 通知面板 */
.notification-panel {
  background: var(--bg-primary);
  border-radius: var(--radius-lg);
  overflow: hidden;
  box-shadow: var(--shadow-lg);
  max-height: 500px;
  display: flex;
  flex-direction: column;
}

/* 头部 */
.notification-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: var(--spacing-4);
  border-bottom: 1px solid var(--border-light);
  background: var(--bg-secondary);
}

.header-left {
  display: flex;
  align-items: center;
  gap: var(--spacing-3);
}

.header-title {
  font-size: var(--text-base);
  font-weight: var(--font-semibold);
  color: var(--text-primary);
  margin: 0;
}

.unread-count {
  font-size: var(--text-xs);
  color: var(--primary-500);
  background: var(--primary-50);
  padding: 2px 8px;
  border-radius: var(--radius-sm);
}

.header-actions {
  display: flex;
  gap: var(--spacing-2);
}

/* 筛选器 */
.notification-filters {
  padding: var(--spacing-3) var(--spacing-4);
  border-bottom: 1px solid var(--border-light);
}

/* 通知列表 */
.notification-list {
  flex: 1;
  overflow-y: auto;
  min-height: 200px;
  max-height: 300px;
}

.empty-notifications {
  padding: var(--spacing-6);
  text-align: center;
}

.notification-items {
  display: flex;
  flex-direction: column;
}

.notification-item {
  display: flex;
  align-items: flex-start;
  gap: var(--spacing-3);
  padding: var(--spacing-3) var(--spacing-4);
  cursor: pointer;
  transition: all var(--duration-fast);
  border-left: 3px solid transparent;
  position: relative;
}

.notification-item:hover {
  background: var(--bg-secondary);
}

.notification-item.is-unread {
  background: var(--primary-50);
  border-left-color: var(--primary-500);
}

.notification-item.is-mention {
  border-left-color: var(--warning-500);
}

.notification-item.is-like {
  border-left-color: var(--success-500);
}

.notification-item.is-comment {
  border-left-color: var(--info-500);
}

.notification-avatar {
  flex-shrink: 0;
  margin-top: 2px;
}

.notification-content {
  flex: 1;
  min-width: 0;
}

.notification-text {
  font-size: var(--text-sm);
  line-height: var(--leading-relaxed);
  margin-bottom: var(--spacing-1);
}

.sender-name {
  font-weight: var(--font-medium);
  color: var(--text-primary);
}

.action-text {
  color: var(--text-secondary);
  margin: 0 var(--spacing-1);
}

.target-text {
  color: var(--primary-500);
  font-weight: var(--font-medium);
}

.notification-meta {
  display: flex;
  gap: var(--spacing-3);
  font-size: var(--text-xs);
  color: var(--text-tertiary);
}

.notification-actions {
  display: flex;
  align-items: center;
  gap: var(--spacing-2);
  opacity: 0;
  transition: opacity var(--duration-fast);
}

.notification-item:hover .notification-actions {
  opacity: 1;
}

.notification-item.is-unread .notification-actions {
  opacity: 1;
}

.unread-indicator {
  font-size: 8px;
}

.action-btn {
  padding: var(--spacing-1);
}

/* 底部 */
.notification-footer {
  padding: var(--spacing-3);
  text-align: center;
  border-top: 1px solid var(--border-light);
}

.load-more-btn {
  font-size: var(--text-sm);
}

/* 动画效果 */
.notification-item {
  animation: slideIn 0.3s ease-out;
}

@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateX(-20px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

/* 自定义滚动条 */
.notification-list::-webkit-scrollbar {
  width: 4px;
}

.notification-list::-webkit-scrollbar-track {
  background: var(--bg-tertiary);
}

.notification-list::-webkit-scrollbar-thumb {
  background: var(--border-medium);
  border-radius: 2px;
}

.notification-list::-webkit-scrollbar-thumb:hover {
  background: var(--border-dark);
}
</style>

<style>
/* 全局样式，用于Popover */
.notification-popover {
  padding: 0 !important;
  border: 1px solid var(--border-light) !important;
  box-shadow: var(--shadow-lg) !important;
}

.notification-popover .el-popover__arrow::before {
  border-bottom-color: var(--border-light) !important;
}
</style>