<template>
  <div class="dashboard">
    <!-- 欢迎横幅 -->
    <div class="welcome-banner">
      <div class="welcome-content">
        <div class="welcome-text">
          <h1 class="welcome-title">欢迎回来，{{ userInfo.name }}</h1>
          <p class="welcome-subtitle">继续您的知识管理之旅</p>
        </div>
        <div class="welcome-stats">
          <div class="stat-item">
            <div class="stat-number">{{ userStats.spaces }}</div>
            <div class="stat-label">知识空间</div>
          </div>
          <div class="stat-item">
            <div class="stat-number">{{ userStats.documents }}</div>
            <div class="stat-label">文档</div>
          </div>
          <div class="stat-item">
            <div class="stat-number">{{ userStats.views }}</div>
            <div class="stat-label">浏览量</div>
          </div>
        </div>
      </div>
    </div>

    <!-- 快速操作区域 -->
    <div class="quick-actions-section">
      <h2 class="section-title">快速操作</h2>
      <div class="quick-actions-grid">
        <div class="quick-action-card" @click="createDocument">
          <div class="action-icon">
            <el-icon size="32" color="var(--primary-500)"><EditPen /></el-icon>
          </div>
          <div class="action-content">
            <h3>新建文档</h3>
            <p>创建一个新的文档</p>
          </div>
        </div>

        <div class="quick-action-card" @click="createSpace">
          <div class="action-icon">
            <el-icon size="32" color="var(--success-500)"><FolderAdd /></el-icon>
          </div>
          <div class="action-content">
            <h3>新建空间</h3>
            <p>创建知识空间</p>
          </div>
        </div>

        <div class="quick-action-card" @click="$router.push('/profile')">
          <div class="action-icon">
            <el-icon size="32" color="var(--warning-500)"><User /></el-icon>
          </div>
          <div class="action-content">
            <h3>个人中心</h3>
            <p>管理个人信息和设置</p>
          </div>
        </div>

        <div class="quick-action-card" @click="handleTemplate">
          <div class="action-icon">
            <el-icon size="32" color="var(--info-500)"><Memo /></el-icon>
          </div>
          <div class="action-content">
            <h3>模板中心</h3>
            <p>使用文档模板快速创建</p>
          </div>
        </div>
      </div>
    </div>

    <!-- 主要内容区域 -->
    <div class="main-content-grid">
      <div class="content-left">
        <!-- 最近编辑 -->
        <div class="content-card">
          <div class="card-header">
            <h3 class="card-title">
              <el-icon><Clock /></el-icon>
              最近编辑
            </h3>
            <el-link type="primary" @click="$router.push('/profile?tab=documents')">
              查看全部
            </el-link>
          </div>
          <div class="card-content">
            <el-empty v-if="recentDocs.length === 0" description="暂无最近编辑的文档" />
            <div v-else class="doc-list">
              <div
                v-for="doc in recentDocs"
                :key="doc.id"
                class="doc-item"
                @click="openDocument(doc)"
              >
                <div class="doc-icon">
                  <el-icon color="var(--primary-500)"><Document /></el-icon>
                </div>
                <div class="doc-info">
                  <h4 class="doc-title">{{ doc.title }}</h4>
                  <p class="doc-meta">{{ doc.updatedAt }}</p>
                </div>
                <div class="doc-stats">
                  <span class="view-count">
                    <el-icon><View /></el-icon>
                    {{ doc.views || 0 }}
                  </span>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 团队动态 -->
        <div class="content-card">
          <div class="card-header">
            <h3 class="card-title">
              <el-icon><Bell /></el-icon>
              团队动态
            </h3>
          </div>
          <div class="card-content">
            <div class="activity-list">
              <div
                v-for="activity in activities"
                :key="activity.id"
                class="activity-item"
              >
                <el-avatar :src="activity.avatar" :alt="activity.user" size="small" />
                <div class="activity-content">
                  <span class="activity-text">
                    <strong>{{ activity.user }}</strong>
                    {{ activity.action }}
                    <el-link type="primary" @click="openDocument(activity.doc)">
                      {{ activity.doc.title }}
                    </el-link>
                  </span>
                  <span class="activity-time">{{ activity.time }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="content-right">
        <!-- 我的收藏 -->
        <div class="content-card">
          <div class="card-header">
            <h3 class="card-title">
              <el-icon><Star /></el-icon>
              我的收藏
            </h3>
            <el-link type="primary" @click="$router.push('/profile?tab=favorites')">
              查看全部
            </el-link>
          </div>
          <div class="card-content">
            <el-empty v-if="favorites.length === 0" description="暂无收藏的文档" />
            <div v-else class="doc-list">
              <div
                v-for="doc in favorites"
                :key="doc.id"
                class="doc-item"
                @click="openDocument(doc)"
              >
                <div class="doc-icon">
                  <el-icon color="var(--warning-500)"><Star /></el-icon>
                </div>
                <div class="doc-info">
                  <h4 class="doc-title">{{ doc.title }}</h4>
                  <p class="doc-meta">by {{ doc.author }}</p>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 知识空间 -->
        <div class="content-card">
          <div class="card-header">
            <h3 class="card-title">
              <el-icon><Folder /></el-icon>
              我的空间
            </h3>
            <el-link type="primary" @click="$router.push('/spaces')">
              管理空间
            </el-link>
          </div>
          <div class="card-content">
            <div class="space-list">
              <div
                v-for="space in userSpaces"
                :key="space.id"
                class="space-item"
                @click="openSpace(space)"
              >
                <div class="space-icon">
                  <el-icon :color="space.color">
                    <component :is="space.icon" />
                  </el-icon>
                </div>
                <div class="space-info">
                  <h4 class="space-name">{{ space.name }}</h4>
                  <p class="space-meta">{{ space.documentCount }} 文档</p>
                </div>
                <el-badge
                  :value="space.unreadCount"
                  :hidden="space.unreadCount === 0"
                  class="space-badge"
                />
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()

// 用户信息
const userInfo = ref({
  name: '张三',
  avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
})

// 用户统计数据
const userStats = ref({
  spaces: 5,
  documents: 42,
  views: 1268
})

// 用户的知识空间
const userSpaces = ref([
  {
    id: 1,
    name: '技术文档',
    icon: 'Document',
    color: 'var(--success-500)',
    documentCount: 25,
    unreadCount: 3
  },
  {
    id: 2,
    name: '项目管理',
    icon: 'DataBoard',
    color: 'var(--primary-500)',
    documentCount: 15,
    unreadCount: 0
  },
  {
    id: 3,
    name: '产品设计',
    icon: 'Collection',
    color: 'var(--warning-500)',
    documentCount: 8,
    unreadCount: 1
  }
])

// 最近编辑的文档
const recentDocs = ref([
  {
    id: 1,
    title: 'Vue3 组件开发规范',
    updatedAt: '2 小时前',
    views: 156
  },
  {
    id: 2,
    title: 'API接口设计文档',
    updatedAt: '1 天前',
    views: 89
  },
  {
    id: 3,
    title: 'TypeScript配置指南',
    updatedAt: '3 天前',
    views: 234
  }
])

// 收藏的文档
const favorites = ref([
  {
    id: 4,
    title: 'React Hooks 最佳实践',
    author: '李四'
  },
  {
    id: 5,
    title: 'Node.js 性能优化',
    author: '王五'
  },
  {
    id: 6,
    title: 'CSS Grid 完整指南',
    author: '赵六'
  }
])

// 团队动态
const activities = ref([
  {
    id: 1,
    user: '李四',
    action: '更新了',
    doc: { id: 1, title: 'Vue3 组件开发规范' },
    time: '2 小时前',
    avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
  },
  {
    id: 2,
    user: '王五',
    action: '创建了',
    doc: { id: 2, title: 'API接口设计文档' },
    time: '1 天前',
    avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
  },
  {
    id: 3,
    user: '赵六',
    action: '评论了',
    doc: { id: 3, title: 'TypeScript配置指南' },
    time: '2 天前',
    avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
  },
  {
    id: 4,
    user: '孙七',
    action: '收藏了',
    doc: { id: 4, title: 'React Hooks 最佳实践' },
    time: '3 天前',
    avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
  }
])

const handleTemplate = () => {
  ElMessage.info('模板中心功能开发中...')
}

const createSpace = () => {
  router.push('/spaces?action=create')
}

const createDocument = () => {
  router.push('/editor/new')
}

const openDocument = (doc: any) => {
  router.push(`/editor/${doc.id}`)
}

const openSpace = (space: any) => {
  router.push(`/spaces/${space.id}`)
}

onMounted(() => {
  // 初始化数据
})
</script>

<style scoped>
.dashboard {
  padding: var(--spacing-6);
  background-color: var(--bg-secondary);
  min-height: 100vh;
}

/* 欢迎横幅 */
.welcome-banner {
  background: linear-gradient(135deg, var(--primary-500) 0%, var(--primary-600) 100%);
  border-radius: var(--radius-xl);
  padding: var(--spacing-8);
  margin-bottom: var(--spacing-8);
  color: white;
  box-shadow: var(--shadow-lg);
}

.welcome-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: var(--spacing-6);
}

.welcome-title {
  font-size: var(--text-4xl);
  font-weight: var(--font-extrabold);
  margin: 0 0 var(--spacing-2) 0;
  line-height: var(--leading-tight);
}

.welcome-subtitle {
  font-size: var(--text-xl);
  font-weight: var(--font-medium);
  margin: 0;
  opacity: 0.95;
  line-height: var(--leading-relaxed);
}

.welcome-stats {
  display: flex;
  gap: var(--spacing-8);
}

.stat-item {
  text-align: center;
  min-width: 80px;
}

.stat-number {
  font-size: var(--text-3xl);
  font-weight: var(--font-extrabold);
  margin-bottom: var(--spacing-1);
  line-height: var(--leading-none);
}

.stat-label {
  font-size: var(--text-base);
  font-weight: var(--font-medium);
  opacity: 0.9;
}

/* 区块标题 */
.section-title {
  font-size: var(--text-2xl);
  font-weight: var(--font-bold);
  color: var(--text-primary);
  margin: 0 0 var(--spacing-6) 0;
  line-height: var(--leading-tight);
}

/* 快速操作区域 */
.quick-actions-section {
  margin-bottom: var(--spacing-8);
}

.quick-actions-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: var(--spacing-4);
}

.quick-action-card {
  background: var(--bg-primary);
  border: 1px solid var(--border-light);
  border-radius: var(--radius-lg);
  padding: var(--spacing-6);
  display: flex;
  align-items: center;
  gap: var(--spacing-4);
  cursor: pointer;
  transition: var(--duration-normal);
  box-shadow: var(--shadow-sm);
}

.quick-action-card:hover {
  border-color: var(--primary-300);
  box-shadow: var(--shadow-md);
  transform: translateY(-2px);
}

.action-icon {
  flex-shrink: 0;
  width: 56px;
  height: 56px;
  background: var(--bg-secondary);
  border-radius: var(--radius-xl);
  display: flex;
  align-items: center;
  justify-content: center;
}

.action-content h3 {
  font-size: var(--text-xl);
  font-weight: var(--font-bold);
  color: var(--text-primary);
  margin: 0 0 var(--spacing-2) 0;
  line-height: var(--leading-snug);
}

.action-content p {
  font-size: var(--text-base);
  font-weight: var(--font-normal);
  color: var(--text-secondary);
  margin: 0;
  line-height: var(--leading-relaxed);
}

/* 主要内容网格 */
.main-content-grid {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: var(--spacing-6);
}

/* 内容卡片 */
.content-card {
  background: var(--bg-primary);
  border: 1px solid var(--border-light);
  border-radius: var(--radius-lg);
  margin-bottom: var(--spacing-6);
  box-shadow: var(--shadow-sm);
  overflow: hidden;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: var(--spacing-5) var(--spacing-6);
  border-bottom: 1px solid var(--border-light);
  background: var(--bg-secondary);
}

.card-title {
  font-size: var(--text-xl);
  font-weight: var(--font-bold);
  color: var(--text-primary);
  margin: 0;
  display: flex;
  align-items: center;
  gap: var(--spacing-3);
  line-height: var(--leading-snug);
}

.card-content {
  padding: var(--spacing-6);
}

/* 文档列表 */
.doc-list {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-3);
}

.doc-item {
  display: flex;
  align-items: center;
  gap: var(--spacing-3);
  padding: var(--spacing-3);
  border-radius: var(--radius-md);
  cursor: pointer;
  transition: var(--duration-normal);
  border: 1px solid transparent;
}

.doc-item:hover {
  background-color: var(--bg-secondary);
  border-color: var(--border-medium);
}

.doc-icon {
  flex-shrink: 0;
  width: 40px;
  height: 40px;
  background: var(--bg-tertiary);
  border-radius: var(--radius-md);
  display: flex;
  align-items: center;
  justify-content: center;
}

.doc-info {
  flex: 1;
  min-width: 0;
}

.doc-title {
  font-size: var(--text-base);
  font-weight: var(--font-medium);
  color: var(--text-primary);
  margin: 0 0 var(--spacing-1) 0;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.doc-meta {
  font-size: var(--text-sm);
  color: var(--text-secondary);
  margin: 0;
}

.doc-stats {
  display: flex;
  align-items: center;
  gap: var(--spacing-2);
}

.view-count {
  font-size: var(--text-xs);
  color: var(--text-tertiary);
  display: flex;
  align-items: center;
  gap: var(--spacing-1);
}

/* 活动列表 */
.activity-list {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-4);
}

.activity-item {
  display: flex;
  gap: var(--spacing-3);
  align-items: flex-start;
}

.activity-content {
  flex: 1;
  min-width: 0;
}

.activity-text {
  font-size: var(--text-sm);
  color: var(--text-primary);
  line-height: var(--leading-relaxed);
  display: block;
  margin-bottom: var(--spacing-1);
}

.activity-time {
  font-size: var(--text-xs);
  color: var(--text-tertiary);
}

/* 空间列表 */
.space-list {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-3);
}

.space-item {
  display: flex;
  align-items: center;
  gap: var(--spacing-3);
  padding: var(--spacing-3);
  border-radius: var(--radius-md);
  cursor: pointer;
  transition: var(--duration-normal);
  border: 1px solid transparent;
}

.space-item:hover {
  background-color: var(--bg-secondary);
  border-color: var(--border-medium);
}

.space-icon {
  flex-shrink: 0;
  width: 40px;
  height: 40px;
  background: var(--bg-tertiary);
  border-radius: var(--radius-md);
  display: flex;
  align-items: center;
  justify-content: center;
}

.space-info {
  flex: 1;
  min-width: 0;
}

.space-name {
  font-size: var(--text-base);
  font-weight: var(--font-medium);
  color: var(--text-primary);
  margin: 0 0 var(--spacing-1) 0;
}

.space-meta {
  font-size: var(--text-sm);
  color: var(--text-secondary);
  margin: 0;
}

.space-badge {
  flex-shrink: 0;
}

/* 响应式设计 */
@media (max-width: 1024px) {
  .main-content-grid {
    grid-template-columns: 1fr;
  }

  .welcome-content {
    flex-direction: column;
    text-align: center;
  }

  .welcome-stats {
    justify-content: center;
  }
}

@media (max-width: 768px) {
  .dashboard {
    padding: var(--spacing-4);
  }

  .welcome-banner {
    padding: var(--spacing-6);
  }

  .welcome-title {
    font-size: var(--text-2xl);
  }

  .welcome-stats {
    gap: var(--spacing-6);
  }

  .quick-actions-grid {
    grid-template-columns: 1fr;
  }

  .quick-action-card {
    padding: var(--spacing-4);
  }

  .card-header {
    padding: var(--spacing-4);
  }

  .card-content {
    padding: var(--spacing-4);
  }
}
</style>