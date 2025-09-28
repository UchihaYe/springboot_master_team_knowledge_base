<template>
  <el-container class="main-layout">
    <!-- 顶部导航栏 -->
    <el-header class="navbar">
      <div class="navbar-left">
        <!-- 侧边栏切换按钮 -->
        <el-button
          text
          class="sidebar-toggle"
          @click="toggleSidebar"
        >
          <el-icon size="20"><Menu /></el-icon>
        </el-button>

        <!-- Logo 和品牌 -->
        <div class="brand" @click="$router.push('/dashboard')">
          <el-icon size="28" color="var(--primary-500)"><Reading /></el-icon>
          <span class="brand-text">团队知识库</span>
        </div>
      </div>

      <div class="navbar-center">
        <!-- 全局搜索 -->
        <div class="global-search">
          <el-input
            v-model="searchQuery"
            placeholder="搜索文档、标签或内容..."
            class="search-input"
            @keyup.enter="handleSearch"
          >
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
          </el-input>
        </div>
      </div>

      <div class="navbar-right">
        <!-- 通知中心 -->
        <NotificationCenter ref="notificationCenter" />

        <!-- 用户菜单 -->
        <el-dropdown @command="handleUserCommand" class="user-dropdown">
          <div class="user-info">
            <el-avatar :src="userInfo.avatar" :alt="userInfo.name" size="medium" />
            <span class="username">{{ userInfo.name }}</span>
            <el-icon class="dropdown-icon"><ArrowDown /></el-icon>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="profile">
                <el-icon><User /></el-icon>
                个人设置
              </el-dropdown-item>
              <el-dropdown-item command="spaces">
                <el-icon><FolderOpened /></el-icon>
                我的空间
              </el-dropdown-item>
              <el-dropdown-item divided command="logout">
                <el-icon><SwitchButton /></el-icon>
                退出登录
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </el-header>

    <el-container class="main-container">
      <!-- 侧边栏 -->
      <el-aside class="sidebar" :class="{ collapsed: sidebarCollapsed }" :width="sidebarWidth">
        <div class="sidebar-content">
          <!-- 导航菜单 -->
          <el-menu
            :default-active="activeMenu"
            class="sidebar-menu"
            @select="handleMenuSelect"
          >
            <el-menu-item index="/dashboard" @click="$router.push('/dashboard')">
              <el-icon><House /></el-icon>
              <span v-show="!sidebarCollapsed">仪表盘</span>
            </el-menu-item>

            <el-menu-item index="/spaces" @click="$router.push('/spaces')">
              <el-icon><FolderOpened /></el-icon>
              <span v-show="!sidebarCollapsed">我的空间</span>
            </el-menu-item>

            <el-menu-item index="/profile" @click="$router.push('/profile')">
              <el-icon><User /></el-icon>
              <span v-show="!sidebarCollapsed">个人中心</span>
            </el-menu-item>

            <el-menu-item index="/admin" @click="$router.push('/admin')" v-if="userInfo.isAdmin">
              <el-icon><Setting /></el-icon>
              <span v-show="!sidebarCollapsed">系统管理</span>
            </el-menu-item>
          </el-menu>

          <!-- 知识空间列表 -->
          <div class="space-section" v-show="!sidebarCollapsed">
            <div class="section-header">
              <span class="section-title">知识空间</span>
              <el-button text size="small" @click="$router.push('/spaces')">
                <el-icon><Plus /></el-icon>
              </el-button>
            </div>

            <div class="space-list">
              <div
                v-for="space in userSpaces"
                :key="space.id"
                class="space-item"
                @click="openSpace(space)"
              >
                <el-icon :color="space.color" size="16">
                  <component :is="space.icon" />
                </el-icon>
                <span class="space-name">{{ space.name }}</span>
                <el-badge :value="space.unreadCount" :hidden="space.unreadCount === 0" />
              </div>
            </div>
          </div>
        </div>
      </el-aside>

      <!-- 主要内容区域 -->
      <el-main class="main-content">
        <router-view />
      </el-main>
    </el-container>

    <!-- 通知抽屉 -->
    <el-drawer
      v-model="showNotifications"
      title="通知中心"
      direction="rtl"
      size="400px"
      class="notification-drawer"
    >
      <div class="notifications">
        <div v-if="notifications.length === 0" class="no-notifications">
          <el-empty description="暂无通知" />
        </div>
        <div v-else class="notification-list">
          <div
            v-for="notification in notifications"
            :key="notification.id"
            class="notification-item"
            :class="{ unread: !notification.read }"
            @click="markAsRead(notification)"
          >
            <div class="notification-avatar">
              <el-avatar :src="notification.avatar" size="small" />
            </div>
            <div class="notification-content">
              <div class="notification-text">{{ notification.message }}</div>
              <div class="notification-time">{{ notification.time }}</div>
            </div>
            <div class="notification-dot" v-if="!notification.read"></div>
          </div>
        </div>

        <div class="notification-actions">
          <el-button @click="markAllAsRead" size="small">全部已读</el-button>
          <el-button @click="clearAll" size="small" type="danger">清空通知</el-button>
        </div>
      </div>
    </el-drawer>
  </el-container>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import NotificationCenter from '@/components/NotificationCenter.vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const route = useRoute()
const authStore = useAuthStore()

const searchQuery = ref('')
const showNotifications = ref(false)
const sidebarCollapsed = ref(false)

// 侧边栏宽度计算
const sidebarWidth = computed(() =>
  sidebarCollapsed.value ? 'var(--sidebar-collapsed-width)' : 'var(--sidebar-width)'
)

// 当前激活的菜单项
const activeMenu = computed(() => route.path)

// 未读通知数量
const unreadCount = computed(() =>
  notifications.value.filter(n => !n.read).length
)

// 用户信息
const userInfo = computed(() => {
  return {
    name: authStore.user?.displayName || '用户',
    avatar: authStore.user?.avatarUrl || 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
    isAdmin: authStore.isAdmin
  }
})

// 用户的知识空间
const userSpaces = ref([
  {
    id: 1,
    name: '技术文档',
    icon: 'Document',
    color: '#67c23a',
    unreadCount: 3
  },
  {
    id: 2,
    name: '项目管理',
    icon: 'DataBoard',
    color: '#409eff',
    unreadCount: 0
  },
  {
    id: 3,
    name: '产品设计',
    icon: 'Collection',
    color: '#e6a23c',
    unreadCount: 1
  }
])

// 通知列表
const notifications = ref([
  {
    id: 1,
    message: '李四 评论了你的文档 "Vue3 组件开发规范"',
    time: '2 小时前',
    read: false,
    avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
  },
  {
    id: 2,
    message: '王五 在 "技术文档" 空间中提到了你',
    time: '1 天前',
    read: false,
    avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
  },
  {
    id: 3,
    message: '你的文档 "API设计规范" 需要审查',
    time: '2 天前',
    read: true,
    avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
  }
])

// 切换侧边栏
const toggleSidebar = () => {
  sidebarCollapsed.value = !sidebarCollapsed.value
}

// 处理菜单选择
const handleMenuSelect = (index: string) => {
  console.log('Selected menu:', index)
}

// 打开知识空间
const openSpace = (space: any) => {
  router.push(`/space/${space.id}`)
}

// 搜索功能
const handleSearch = () => {
  if (searchQuery.value.trim()) {
    router.push({
      path: '/search',
      query: { q: searchQuery.value }
    })
  }
}

// 用户菜单处理
const handleUserCommand = (command: string) => {
  switch (command) {
    case 'profile':
      router.push('/profile')
      break
    case 'spaces':
      router.push('/spaces')
      break
    case 'admin':
      router.push('/admin')
      break
    case 'logout':
      handleLogout()
      break
  }
}

// 退出登录
const handleLogout = async () => {
  try {
    await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    await authStore.logout()
    ElMessage.success('已退出登录')
    router.push('/login')
  } catch (error: any) {
    if (error !== 'cancel') {
      console.error('登出失败:', error)
      ElMessage.error('登出失败')
    }
  }
}

// 通知相关方法
const markAsRead = (notification: any) => {
  notification.read = true
}

const markAllAsRead = () => {
  notifications.value.forEach(n => n.read = true)
  ElMessage.success('已全部标记为已读')
}

const clearAll = () => {
  notifications.value.length = 0
  ElMessage.success('已清空所有通知')
}

onMounted(() => {
  // 初始化
})
</script>

<style scoped>
.main-layout {
  height: 100vh;
  display: flex;
  flex-direction: column;
}

/* 顶部导航栏 */
.navbar {
  height: var(--navbar-height);
  background: var(--bg-primary);
  border-bottom: 1px solid var(--border-light);
  box-shadow: var(--shadow-sm);
  display: flex;
  align-items: center;
  padding: 0 var(--spacing-4);
  position: relative;
  z-index: 100;
}

.navbar-left {
  display: flex;
  align-items: center;
  gap: var(--spacing-4);
}

.sidebar-toggle {
  width: 40px;
  height: 40px;
  padding: 0;
  color: var(--text-secondary);
  transition: var(--duration-normal);
}

.sidebar-toggle:hover {
  color: var(--primary-500);
  background-color: var(--primary-50);
}

.brand {
  display: flex;
  align-items: center;
  gap: var(--spacing-2);
  cursor: pointer;
  padding: var(--spacing-2);
  border-radius: var(--radius-md);
  transition: var(--duration-normal);
}

.brand:hover {
  background-color: var(--bg-secondary);
}

.brand-text {
  font-size: var(--text-xl);
  font-weight: var(--font-semibold);
  color: var(--text-primary);
}

.navbar-center {
  flex: 1;
  display: flex;
  justify-content: center;
  max-width: 600px;
  margin: 0 auto;
}

.global-search {
  width: 100%;
  max-width: 500px;
}

.search-input {
  border-radius: var(--radius-lg);
}

.navbar-right {
  display: flex;
  align-items: center;
  gap: var(--spacing-3);
}

.nav-button {
  width: 40px;
  height: 40px;
  padding: 0;
  color: var(--text-secondary);
  transition: var(--duration-normal);
}

.nav-button:hover {
  color: var(--primary-500);
  background-color: var(--primary-50);
}

.notification-badge {
  position: relative;
}

.user-dropdown {
  cursor: pointer;
}

.user-info {
  display: flex;
  align-items: center;
  gap: var(--spacing-2);
  padding: var(--spacing-2);
  border-radius: var(--radius-md);
  transition: var(--duration-normal);
}

.user-info:hover {
  background-color: var(--bg-secondary);
}

.username {
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
  color: var(--text-primary);
}

.dropdown-icon {
  color: var(--text-tertiary);
  transition: var(--duration-normal);
}

/* 主容器 */
.main-container {
  flex: 1;
  overflow: hidden;
}

/* 侧边栏 */
.sidebar {
  background: var(--bg-primary);
  border-right: 1px solid var(--border-light);
  transition: width var(--duration-normal) var(--ease-out);
  overflow: hidden;
}

.sidebar.collapsed {
  width: var(--sidebar-collapsed-width) !important;
}

.sidebar-content {
  height: 100%;
  display: flex;
  flex-direction: column;
  padding: var(--spacing-4) 0;
}

.sidebar-menu {
  border: none;
  background: transparent;
}

.sidebar-menu :deep(.el-menu-item) {
  margin: 0 var(--spacing-3) var(--spacing-1) var(--spacing-3);
  border-radius: var(--radius-md);
  height: 44px;
  line-height: 44px;
  color: var(--text-secondary);
  transition: var(--duration-normal);
}

.sidebar-menu :deep(.el-menu-item:hover) {
  background-color: var(--primary-50);
  color: var(--primary-500);
}

.sidebar-menu :deep(.el-menu-item.is-active) {
  background-color: var(--primary-100);
  color: var(--primary-600);
  font-weight: var(--font-medium);
}

.sidebar-menu :deep(.el-menu-item .el-icon) {
  margin-right: var(--spacing-3);
  font-size: 18px;
}

/* 知识空间部分 */
.space-section {
  margin-top: var(--spacing-6);
  padding: 0 var(--spacing-3);
}

.section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: var(--spacing-2) var(--spacing-3);
  margin-bottom: var(--spacing-2);
}

.section-title {
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
  color: var(--text-tertiary);
  text-transform: uppercase;
  letter-spacing: 0.05em;
}

.space-list {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-1);
}

.space-item {
  display: flex;
  align-items: center;
  gap: var(--spacing-3);
  padding: var(--spacing-2) var(--spacing-3);
  border-radius: var(--radius-md);
  cursor: pointer;
  transition: var(--duration-normal);
}

.space-item:hover {
  background-color: var(--bg-secondary);
}

.space-name {
  flex: 1;
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
  color: var(--text-primary);
}

/* 主内容区域 */
.main-content {
  background-color: var(--bg-secondary);
  padding: 0;
  overflow-y: auto;
}

/* 通知抽屉 */
.notification-drawer {
  z-index: 2000;
}

.notifications {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.no-notifications {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
}

.notification-list {
  flex: 1;
  overflow-y: auto;
}

.notification-item {
  display: flex;
  align-items: flex-start;
  gap: var(--spacing-3);
  padding: var(--spacing-4);
  border-bottom: 1px solid var(--border-light);
  cursor: pointer;
  transition: var(--duration-normal);
  position: relative;
}

.notification-item:hover {
  background-color: var(--bg-secondary);
}

.notification-item.unread {
  background-color: var(--primary-50);
}

.notification-item.unread::before {
  content: '';
  position: absolute;
  left: 0;
  top: 0;
  bottom: 0;
  width: 3px;
  background-color: var(--primary-500);
}

.notification-content {
  flex: 1;
}

.notification-text {
  color: var(--text-primary);
  font-size: var(--text-sm);
  line-height: var(--leading-normal);
  margin-bottom: var(--spacing-1);
}

.notification-time {
  color: var(--text-tertiary);
  font-size: var(--text-xs);
}

.notification-dot {
  width: 8px;
  height: 8px;
  background-color: var(--primary-500);
  border-radius: var(--radius-full);
  position: absolute;
  right: var(--spacing-4);
  top: var(--spacing-4);
}

.notification-actions {
  padding: var(--spacing-4);
  border-top: 1px solid var(--border-light);
  background-color: var(--bg-secondary);
  display: flex;
  gap: var(--spacing-2);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .navbar-center {
    display: none;
  }

  .brand-text {
    display: none;
  }

  .username {
    display: none;
  }

  .sidebar {
    position: fixed;
    left: 0;
    top: var(--navbar-height);
    height: calc(100vh - var(--navbar-height));
    z-index: 1000;
    transform: translateX(-100%);
    transition: transform var(--duration-normal) var(--ease-out);
  }

  .sidebar:not(.collapsed) {
    transform: translateX(0);
  }
}
</style>