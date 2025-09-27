<template>
  <div class="profile-container">
    <el-row :gutter="20">
      <el-col :span="8">
        <!-- 用户信息卡片 -->
        <el-card class="user-card">
          <div class="user-info">
            <el-avatar :size="80" :src="userProfile.avatar" />
            <h3>{{ userProfile.name }}</h3>
            <p>{{ userProfile.email }}</p>
            <div class="user-stats">
              <div class="stat">
                <span class="value">{{ userProfile.documentCount }}</span>
                <span class="label">文档</span>
              </div>
              <div class="stat">
                <span class="value">{{ userProfile.spaceCount }}</span>
                <span class="label">空间</span>
              </div>
              <div class="stat">
                <span class="value">{{ userProfile.followerCount }}</span>
                <span class="label">关注</span>
              </div>
            </div>
          </div>
        </el-card>

        <!-- 快捷操作 -->
        <el-card class="actions-card">
          <template #header>
            <span>快捷操作</span>
          </template>
          <el-button type="primary" @click="activeTab = 'documents'" block>
            <el-icon><Document /></el-icon>
            我的文档
          </el-button>
          <el-button @click="activeTab = 'favorites'" block>
            <el-icon><Star /></el-icon>
            我的收藏
          </el-button>
          <el-button @click="activeTab = 'history'" block>
            <el-icon><Clock /></el-icon>
            浏览历史
          </el-button>
        </el-card>
      </el-col>

      <el-col :span="16">
        <el-card>
          <el-tabs v-model="activeTab">
            <!-- 个人设置 -->
            <el-tab-pane label="个人设置" name="settings">
              <el-form :model="userProfile" label-width="100px" class="profile-form">
                <el-form-item label="头像">
                  <div class="avatar-upload">
                    <el-avatar :size="60" :src="userProfile.avatar" />
                    <el-button size="small" style="margin-left: 16px;">更换头像</el-button>
                  </div>
                </el-form-item>

                <el-form-item label="姓名">
                  <el-input v-model="userProfile.name" style="width: 300px;" />
                </el-form-item>

                <el-form-item label="邮箱">
                  <el-input v-model="userProfile.email" disabled style="width: 300px;" />
                </el-form-item>

                <el-form-item label="个人简介">
                  <el-input
                    v-model="userProfile.bio"
                    type="textarea"
                    rows="3"
                    style="width: 400px;"
                  />
                </el-form-item>

                <el-form-item label="主题设置">
                  <el-radio-group v-model="userProfile.theme">
                    <el-radio-button value="light">浅色主题</el-radio-button>
                    <el-radio-button value="dark">深色主题</el-radio-button>
                    <el-radio-button value="auto">跟随系统</el-radio-button>
                  </el-radio-group>
                </el-form-item>

                <el-form-item>
                  <el-button type="primary" @click="saveProfile">保存设置</el-button>
                </el-form-item>
              </el-form>
            </el-tab-pane>

            <!-- 我的文档 -->
            <el-tab-pane label="我的文档" name="documents">
              <div class="document-list">
                <div
                  v-for="doc in myDocuments"
                  :key="doc.id"
                  class="document-item"
                  @click="openDocument(doc)"
                >
                  <div class="doc-info">
                    <h4>{{ doc.title }}</h4>
                    <p>{{ doc.space }} · {{ doc.updatedAt }}</p>
                  </div>
                  <div class="doc-stats">
                    <span><el-icon><View /></el-icon> {{ doc.views }}</span>
                    <span><el-icon><ChatLineRound /></el-icon> {{ doc.comments }}</span>
                    <span><el-icon><Star /></el-icon> {{ doc.likes }}</span>
                  </div>
                </div>
              </div>
            </el-tab-pane>

            <!-- 我的收藏 -->
            <el-tab-pane label="我的收藏" name="favorites">
              <div class="document-list">
                <div
                  v-for="doc in favoriteDocuments"
                  :key="doc.id"
                  class="document-item"
                  @click="openDocument(doc)"
                >
                  <div class="doc-info">
                    <h4>{{ doc.title }}</h4>
                    <p>{{ doc.author }} · {{ doc.space }} · {{ doc.updatedAt }}</p>
                  </div>
                  <el-button @click.stop="removeFavorite(doc)" text type="danger">
                    取消收藏
                  </el-button>
                </div>
              </div>
            </el-tab-pane>

            <!-- 浏览历史 -->
            <el-tab-pane label="浏览历史" name="history">
              <div class="document-list">
                <div
                  v-for="doc in historyDocuments"
                  :key="doc.id"
                  class="document-item"
                  @click="openDocument(doc)"
                >
                  <div class="doc-info">
                    <h4>{{ doc.title }}</h4>
                    <p>{{ doc.author }} · {{ doc.space }} · 访问时间: {{ doc.visitedAt }}</p>
                  </div>
                </div>
              </div>
            </el-tab-pane>

            <!-- 通知设置 -->
            <el-tab-pane label="通知设置" name="notifications">
              <el-form label-width="120px">
                <el-form-item label="邮件通知">
                  <el-switch v-model="notificationSettings.email" />
                  <span class="setting-desc">接收重要通知的邮件提醒</span>
                </el-form-item>

                <el-form-item label="评论通知">
                  <el-switch v-model="notificationSettings.comments" />
                  <span class="setting-desc">有人评论我的文档时通知我</span>
                </el-form-item>

                <el-form-item label="提及通知">
                  <el-switch v-model="notificationSettings.mentions" />
                  <span class="setting-desc">有人@我时通知我</span>
                </el-form-item>

                <el-form-item label="文档更新">
                  <el-switch v-model="notificationSettings.updates" />
                  <span class="setting-desc">我关注的文档有更新时通知我</span>
                </el-form-item>

                <el-form-item>
                  <el-button type="primary" @click="saveNotificationSettings">保存设置</el-button>
                </el-form-item>
              </el-form>
            </el-tab-pane>

            <!-- 安全设置 -->
            <el-tab-pane label="安全设置" name="security">
              <el-form label-width="120px">
                <el-form-item label="修改密码">
                  <el-button @click="showPasswordDialog = true">修改密码</el-button>
                </el-form-item>

                <el-form-item label="登录记录">
                  <el-table :data="loginHistory" style="width: 100%">
                    <el-table-column prop="time" label="登录时间" width="180" />
                    <el-table-column prop="ip" label="IP地址" width="150" />
                    <el-table-column prop="location" label="登录地点" />
                    <el-table-column prop="device" label="设备" />
                  </el-table>
                </el-form-item>
              </el-form>
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-col>
    </el-row>

    <!-- 修改密码对话框 -->
    <el-dialog v-model="showPasswordDialog" title="修改密码" width="400px">
      <el-form :model="passwordForm" label-width="100px">
        <el-form-item label="当前密码">
          <el-input v-model="passwordForm.current" type="password" show-password />
        </el-form-item>
        <el-form-item label="新密码">
          <el-input v-model="passwordForm.new" type="password" show-password />
        </el-form-item>
        <el-form-item label="确认密码">
          <el-input v-model="passwordForm.confirm" type="password" show-password />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showPasswordDialog = false">取消</el-button>
        <el-button type="primary" @click="changePassword">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()
const activeTab = ref('settings')
const showPasswordDialog = ref(false)

// 用户资料
const userProfile = reactive({
  name: '张三',
  email: 'zhangsan@company.com',
  avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
  bio: '一名热爱技术的前端开发工程师',
  theme: 'light',
  documentCount: 25,
  spaceCount: 5,
  followerCount: 12
})

// 密码修改表单
const passwordForm = reactive({
  current: '',
  new: '',
  confirm: ''
})

// 通知设置
const notificationSettings = reactive({
  email: true,
  comments: true,
  mentions: true,
  updates: false
})

// 我的文档
const myDocuments = ref([
  {
    id: 1,
    title: 'Vue3 组件开发规范',
    space: '技术文档',
    updatedAt: '2024-01-15',
    views: 156,
    comments: 8,
    likes: 23
  },
  {
    id: 2,
    title: 'API 接口设计指南',
    space: '技术文档',
    updatedAt: '2024-01-14',
    views: 89,
    comments: 5,
    likes: 15
  }
])

// 收藏的文档
const favoriteDocuments = ref([
  {
    id: 3,
    title: 'TypeScript 最佳实践',
    author: '李四',
    space: '技术文档',
    updatedAt: '2024-01-13'
  }
])

// 浏览历史
const historyDocuments = ref([
  {
    id: 4,
    title: '项目管理流程',
    author: '王五',
    space: '项目管理',
    visitedAt: '2024-01-15 14:30'
  }
])

// 登录记录
const loginHistory = ref([
  {
    time: '2024-01-15 09:30',
    ip: '192.168.1.100',
    location: '北京',
    device: 'Chrome/Windows'
  },
  {
    time: '2024-01-14 18:45',
    ip: '192.168.1.100',
    location: '北京',
    device: 'Chrome/Windows'
  }
])

const saveProfile = () => {
  ElMessage.success('个人设置已保存')
}

const saveNotificationSettings = () => {
  ElMessage.success('通知设置已保存')
}

const changePassword = () => {
  if (!passwordForm.current || !passwordForm.new || !passwordForm.confirm) {
    ElMessage.error('请填写完整信息')
    return
  }

  if (passwordForm.new !== passwordForm.confirm) {
    ElMessage.error('两次输入的密码不一致')
    return
  }

  showPasswordDialog.value = false
  ElMessage.success('密码修改成功')

  // 重置表单
  Object.assign(passwordForm, {
    current: '',
    new: '',
    confirm: ''
  })
}

const openDocument = (doc: any) => {
  router.push(`/editor/${doc.id}`)
}

const removeFavorite = (doc: any) => {
  const index = favoriteDocuments.value.findIndex(d => d.id === doc.id)
  if (index > -1) {
    favoriteDocuments.value.splice(index, 1)
    ElMessage.success('已取消收藏')
  }
}
</script>

<style scoped>
.profile-container {
  padding: 20px;
  background-color: #f5f5f5;
  min-height: calc(100vh - 60px);
}

.user-card {
  margin-bottom: 20px;
}

.user-info {
  text-align: center;
}

.user-info h3 {
  margin: 16px 0 8px 0;
  color: #2c3e50;
}

.user-info p {
  margin: 0 0 20px 0;
  color: #7f8c8d;
}

.user-stats {
  display: flex;
  justify-content: space-around;
  padding-top: 20px;
  border-top: 1px solid #eee;
}

.stat {
  text-align: center;
}

.stat .value {
  display: block;
  font-size: 20px;
  font-weight: 600;
  color: #2563eb;
}

.stat .label {
  font-size: 14px;
  color: #7f8c8d;
}

.actions-card :deep(.el-button) {
  margin-bottom: 12px;
  justify-content: flex-start;
}

.actions-card :deep(.el-button:last-child) {
  margin-bottom: 0;
}

.profile-form {
  padding-right: 40px;
}

.avatar-upload {
  display: flex;
  align-items: center;
}

.document-list {
  max-height: 500px;
  overflow-y: auto;
}

.document-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 0;
  border-bottom: 1px solid #f0f0f0;
  cursor: pointer;
  transition: background-color 0.3s;
}

.document-item:hover {
  background-color: #f9f9f9;
  margin: 0 -16px;
  padding: 16px;
  border-radius: 4px;
}

.document-item:last-child {
  border-bottom: none;
}

.doc-info h4 {
  margin: 0 0 4px 0;
  color: #2c3e50;
  font-size: 16px;
}

.doc-info p {
  margin: 0;
  color: #7f8c8d;
  font-size: 14px;
}

.doc-stats {
  display: flex;
  gap: 16px;
  color: #7f8c8d;
  font-size: 14px;
}

.doc-stats span {
  display: flex;
  align-items: center;
  gap: 4px;
}

.setting-desc {
  margin-left: 12px;
  color: #7f8c8d;
  font-size: 14px;
}
</style>