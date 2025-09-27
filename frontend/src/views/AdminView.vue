<template>
  <div class="admin-container">
    <el-card class="admin-header">
      <h2>系统管理</h2>
      <p>管理用户、空间和系统设置</p>
    </el-card>

    <el-row :gutter="20">
      <el-col :span="6">
        <!-- 侧边导航 -->
        <el-card>
          <el-menu
            v-model="activeTab"
            class="admin-menu"
            @select="handleMenuSelect"
          >
            <el-menu-item index="dashboard">
              <el-icon><DataBoard /></el-icon>
              <span>仪表盘</span>
            </el-menu-item>
            <el-menu-item index="users">
              <el-icon><User /></el-icon>
              <span>用户管理</span>
            </el-menu-item>
            <el-menu-item index="spaces">
              <el-icon><Folder /></el-icon>
              <span>空间管理</span>
            </el-menu-item>
            <el-menu-item index="settings">
              <el-icon><Setting /></el-icon>
              <span>系统设置</span>
            </el-menu-item>
            <el-menu-item index="analytics">
              <el-icon><TrendCharts /></el-icon>
              <span>数据分析</span>
            </el-menu-item>
            <el-menu-item index="logs">
              <el-icon><Document /></el-icon>
              <span>审计日志</span>
            </el-menu-item>
          </el-menu>
        </el-card>
      </el-col>

      <el-col :span="18">
        <!-- 仪表盘 -->
        <div v-if="activeTab === 'dashboard'">
          <el-row :gutter="20" class="stats-row">
            <el-col :span="6">
              <el-card class="stat-card">
                <div class="stat-content">
                  <div class="stat-number">{{ stats.totalUsers }}</div>
                  <div class="stat-label">总用户数</div>
                </div>
                <el-icon class="stat-icon" color="#409eff"><User /></el-icon>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card class="stat-card">
                <div class="stat-content">
                  <div class="stat-number">{{ stats.totalSpaces }}</div>
                  <div class="stat-label">知识空间</div>
                </div>
                <el-icon class="stat-icon" color="#67c23a"><Folder /></el-icon>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card class="stat-card">
                <div class="stat-content">
                  <div class="stat-number">{{ stats.totalDocuments }}</div>
                  <div class="stat-label">文档总数</div>
                </div>
                <el-icon class="stat-icon" color="#e6a23c"><Document /></el-icon>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card class="stat-card">
                <div class="stat-content">
                  <div class="stat-number">{{ stats.activeUsers }}</div>
                  <div class="stat-label">活跃用户</div>
                </div>
                <el-icon class="stat-icon" color="#f56c6c"><TrendCharts /></el-icon>
              </el-card>
            </el-col>
          </el-row>

          <el-row :gutter="20" class="charts-row">
            <el-col :span="12">
              <el-card>
                <template #header>
                  <span>用户活跃度趋势</span>
                </template>
                <div class="chart-placeholder">
                  <p>这里可以显示用户活跃度趋势图</p>
                </div>
              </el-card>
            </el-col>
            <el-col :span="12">
              <el-card>
                <template #header>
                  <span>文档创建统计</span>
                </template>
                <div class="chart-placeholder">
                  <p>这里可以显示文档创建统计图</p>
                </div>
              </el-card>
            </el-col>
          </el-row>
        </div>

        <!-- 用户管理 -->
        <el-card v-if="activeTab === 'users'">
          <template #header>
            <div class="card-header">
              <span>用户管理</span>
              <el-button type="primary" @click="showCreateUserDialog = true">
                添加用户
              </el-button>
            </div>
          </template>

          <div class="table-toolbar">
            <el-input
              v-model="userSearch"
              placeholder="搜索用户"
              style="width: 200px;"
            />
            <el-select v-model="userStatusFilter" placeholder="状态筛选" style="width: 120px;">
              <el-option label="全部" value="" />
              <el-option label="活跃" value="active" />
              <el-option label="禁用" value="disabled" />
            </el-select>
          </div>

          <el-table :data="filteredUsers" style="width: 100%">
            <el-table-column label="用户">
              <template #default="scope">
                <div class="user-info">
                  <el-avatar :src="scope.row.avatar" size="small" />
                  <div>
                    <div>{{ scope.row.name }}</div>
                    <div class="user-email">{{ scope.row.email }}</div>
                  </div>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="role" label="角色">
              <template #default="scope">
                <el-tag :type="scope.row.role === 'admin' ? 'danger' : 'primary'">
                  {{ scope.row.role === 'admin' ? '管理员' : '普通用户' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="status" label="状态">
              <template #default="scope">
                <el-tag :type="scope.row.status === 'active' ? 'success' : 'info'">
                  {{ scope.row.status === 'active' ? '活跃' : '禁用' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="lastLogin" label="最后登录" />
            <el-table-column label="操作" width="200">
              <template #default="scope">
                <el-button size="small" @click="editUser(scope.row)">编辑</el-button>
                <el-button
                  size="small"
                  :type="scope.row.status === 'active' ? 'warning' : 'success'"
                  @click="toggleUserStatus(scope.row)"
                >
                  {{ scope.row.status === 'active' ? '禁用' : '启用' }}
                </el-button>
                <el-button size="small" type="danger" @click="deleteUser(scope.row)">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>

        <!-- 空间管理 -->
        <el-card v-if="activeTab === 'spaces'">
          <template #header>
            <span>空间管理</span>
          </template>

          <el-table :data="adminSpaces" style="width: 100%">
            <el-table-column prop="name" label="空间名称" />
            <el-table-column prop="description" label="描述" />
            <el-table-column prop="owner" label="所有者" />
            <el-table-column prop="memberCount" label="成员数" />
            <el-table-column prop="documentCount" label="文档数" />
            <el-table-column prop="createdAt" label="创建时间" />
            <el-table-column label="操作" width="150">
              <template #default="scope">
                <el-button size="small" @click="manageSpace(scope.row)">管理</el-button>
                <el-button size="small" type="danger" @click="archiveSpace(scope.row)">
                  归档
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>

        <!-- 系统设置 -->
        <el-card v-if="activeTab === 'settings'">
          <template #header>
            <span>系统设置</span>
          </template>

          <el-tabs>
            <el-tab-pane label="基本设置" name="basic">
              <el-form :model="systemSettings" label-width="120px">
                <el-form-item label="系统名称">
                  <el-input v-model="systemSettings.siteName" style="width: 300px;" />
                </el-form-item>
                <el-form-item label="系统Logo">
                  <el-upload
                    class="logo-uploader"
                    action="/api/upload"
                    :show-file-list="false"
                  >
                    <img v-if="systemSettings.logo" :src="systemSettings.logo" class="logo" />
                    <el-icon v-else class="logo-uploader-icon"><Plus /></el-icon>
                  </el-upload>
                </el-form-item>
                <el-form-item label="注册权限">
                  <el-radio-group v-model="systemSettings.registrationMode">
                    <el-radio value="open">开放注册</el-radio>
                    <el-radio value="invite">仅邀请注册</el-radio>
                    <el-radio value="closed">关闭注册</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-form>
            </el-tab-pane>

            <el-tab-pane label="邮件配置" name="email">
              <el-form :model="emailSettings" label-width="120px">
                <el-form-item label="SMTP服务器">
                  <el-input v-model="emailSettings.smtpHost" style="width: 300px;" />
                </el-form-item>
                <el-form-item label="端口">
                  <el-input v-model="emailSettings.smtpPort" style="width: 300px;" />
                </el-form-item>
                <el-form-item label="用户名">
                  <el-input v-model="emailSettings.smtpUser" style="width: 300px;" />
                </el-form-item>
                <el-form-item label="密码">
                  <el-input
                    v-model="emailSettings.smtpPassword"
                    type="password"
                    style="width: 300px;"
                  />
                </el-form-item>
                <el-form-item label="发件人邮箱">
                  <el-input v-model="emailSettings.fromEmail" style="width: 300px;" />
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="testEmailConfig">测试连接</el-button>
                </el-form-item>
              </el-form>
            </el-tab-pane>

            <el-tab-pane label="安全设置" name="security">
              <el-form label-width="120px">
                <el-form-item label="密码策略">
                  <el-checkbox-group v-model="securitySettings.passwordPolicy">
                    <el-checkbox value="minLength">最少8位字符</el-checkbox>
                    <el-checkbox value="requireNumbers">包含数字</el-checkbox>
                    <el-checkbox value="requireSymbols">包含特殊字符</el-checkbox>
                    <el-checkbox value="requireUpperCase">包含大写字母</el-checkbox>
                  </el-checkbox-group>
                </el-form-item>
                <el-form-item label="登录限制">
                  <el-input-number
                    v-model="securitySettings.maxLoginAttempts"
                    :min="3"
                    :max="10"
                  />
                  <span style="margin-left: 8px;">次失败后锁定账户</span>
                </el-form-item>
                <el-form-item label="会话超时">
                  <el-input-number
                    v-model="securitySettings.sessionTimeout"
                    :min="30"
                    :max="1440"
                  />
                  <span style="margin-left: 8px;">分钟</span>
                </el-form-item>
              </el-form>
            </el-tab-pane>
          </el-tabs>

          <div style="margin-top: 20px;">
            <el-button type="primary" @click="saveSystemSettings">保存设置</el-button>
          </div>
        </el-card>

        <!-- 数据分析 -->
        <el-card v-if="activeTab === 'analytics'">
          <template #header>
            <span>数据分析</span>
          </template>
          <div class="analytics-content">
            <p>这里可以显示详细的数据分析报告，包括用户行为、内容统计等</p>
          </div>
        </el-card>

        <!-- 审计日志 -->
        <el-card v-if="activeTab === 'logs'">
          <template #header>
            <span>审计日志</span>
          </template>

          <div class="log-filters">
            <el-date-picker
              v-model="logDateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              style="margin-right: 16px;"
            />
            <el-select v-model="logTypeFilter" placeholder="操作类型" style="width: 150px;">
              <el-option label="全部" value="" />
              <el-option label="登录" value="login" />
              <el-option label="创建" value="create" />
              <el-option label="编辑" value="edit" />
              <el-option label="删除" value="delete" />
            </el-select>
          </div>

          <el-table :data="auditLogs" style="width: 100%; margin-top: 16px;">
            <el-table-column prop="time" label="时间" width="180" />
            <el-table-column prop="user" label="用户" width="120" />
            <el-table-column prop="action" label="操作" width="100" />
            <el-table-column prop="resource" label="资源" />
            <el-table-column prop="ip" label="IP地址" width="150" />
            <el-table-column prop="userAgent" label="客户端" />
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <!-- 创建用户对话框 -->
    <el-dialog v-model="showCreateUserDialog" title="添加用户" width="400px">
      <el-form :model="newUser" label-width="80px">
        <el-form-item label="姓名">
          <el-input v-model="newUser.name" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="newUser.email" />
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="newUser.role">
            <el-option label="普通用户" value="user" />
            <el-option label="管理员" value="admin" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showCreateUserDialog = false">取消</el-button>
        <el-button type="primary" @click="createUser">创建</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const activeTab = ref('dashboard')
const showCreateUserDialog = ref(false)
const userSearch = ref('')
const userStatusFilter = ref('')
const logDateRange = ref([])
const logTypeFilter = ref('')

// 系统统计数据
const stats = reactive({
  totalUsers: 1234,
  totalSpaces: 56,
  totalDocuments: 7890,
  activeUsers: 456
})

// 新用户表单
const newUser = reactive({
  name: '',
  email: '',
  role: 'user'
})

// 用户列表
const users = ref([
  {
    id: 1,
    name: '张三',
    email: 'zhangsan@company.com',
    role: 'admin',
    status: 'active',
    lastLogin: '2024-01-15 10:30',
    avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
  },
  {
    id: 2,
    name: '李四',
    email: 'lisi@company.com',
    role: 'user',
    status: 'active',
    lastLogin: '2024-01-14 16:45',
    avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
  },
  {
    id: 3,
    name: '王五',
    email: 'wangwu@company.com',
    role: 'user',
    status: 'disabled',
    lastLogin: '2024-01-10 09:15',
    avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
  }
])

// 空间列表
const adminSpaces = ref([
  {
    id: 1,
    name: '技术文档',
    description: '技术相关文档',
    owner: '张三',
    memberCount: 8,
    documentCount: 25,
    createdAt: '2024-01-01'
  },
  {
    id: 2,
    name: '项目管理',
    description: '项目管理文档',
    owner: '李四',
    memberCount: 12,
    documentCount: 15,
    createdAt: '2024-01-05'
  }
])

// 系统设置
const systemSettings = reactive({
  siteName: '团队知识库',
  logo: '',
  registrationMode: 'open'
})

// 邮件设置
const emailSettings = reactive({
  smtpHost: '',
  smtpPort: 587,
  smtpUser: '',
  smtpPassword: '',
  fromEmail: ''
})

// 安全设置
const securitySettings = reactive({
  passwordPolicy: ['minLength', 'requireNumbers'],
  maxLoginAttempts: 5,
  sessionTimeout: 120
})

// 审计日志
const auditLogs = ref([
  {
    time: '2024-01-15 10:30:25',
    user: '张三',
    action: '登录',
    resource: '系统',
    ip: '192.168.1.100',
    userAgent: 'Chrome/120.0'
  },
  {
    time: '2024-01-15 10:32:15',
    user: '张三',
    action: '创建',
    resource: '文档/Vue3开发规范',
    ip: '192.168.1.100',
    userAgent: 'Chrome/120.0'
  },
  {
    time: '2024-01-15 11:15:30',
    user: '李四',
    action: '编辑',
    resource: '文档/API设计指南',
    ip: '192.168.1.101',
    userAgent: 'Firefox/121.0'
  }
])

// 过滤后的用户列表
const filteredUsers = computed(() => {
  return users.value.filter(user => {
    const matchesSearch = user.name.includes(userSearch.value) ||
                         user.email.includes(userSearch.value)
    const matchesStatus = !userStatusFilter.value || user.status === userStatusFilter.value
    return matchesSearch && matchesStatus
  })
})

const handleMenuSelect = (index: string) => {
  activeTab.value = index
}

const createUser = () => {
  if (!newUser.name || !newUser.email) {
    ElMessage.error('请填写完整信息')
    return
  }

  // 模拟创建用户
  const user = {
    id: Date.now(),
    ...newUser,
    status: 'active',
    lastLogin: '从未登录',
    avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
  }

  users.value.push(user)
  showCreateUserDialog.value = false
  ElMessage.success('用户创建成功')

  // 重置表单
  Object.assign(newUser, {
    name: '',
    email: '',
    role: 'user'
  })
}

const editUser = (user: any) => {
  ElMessage.info(`编辑用户: ${user.name}`)
}

const toggleUserStatus = (user: any) => {
  user.status = user.status === 'active' ? 'disabled' : 'active'
  ElMessage.success(`用户已${user.status === 'active' ? '启用' : '禁用'}`)
}

const deleteUser = async (user: any) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除用户"${user.name}"吗？`,
      '删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    const index = users.value.findIndex(u => u.id === user.id)
    if (index > -1) {
      users.value.splice(index, 1)
      ElMessage.success('用户已删除')
    }
  } catch {
    // 用户取消删除
  }
}

const manageSpace = (space: any) => {
  ElMessage.info(`管理空间: ${space.name}`)
}

const archiveSpace = (space: any) => {
  ElMessage.info(`归档空间: ${space.name}`)
}

const saveSystemSettings = () => {
  ElMessage.success('系统设置已保存')
}

const testEmailConfig = () => {
  ElMessage.success('邮件配置测试成功')
}
</script>

<style scoped>
.admin-container {
  padding: 20px;
  background-color: #f5f5f5;
  min-height: calc(100vh - 60px);
}

.admin-header {
  margin-bottom: 20px;
}

.admin-header h2 {
  margin: 0 0 8px 0;
  color: #2c3e50;
}

.admin-header p {
  margin: 0;
  color: #7f8c8d;
}

.admin-menu {
  border: none;
}

.stats-row {
  margin-bottom: 20px;
}

.stat-card {
  position: relative;
  overflow: hidden;
}

.stat-content {
  position: relative;
  z-index: 2;
}

.stat-number {
  font-size: 32px;
  font-weight: 600;
  color: #2c3e50;
  margin-bottom: 4px;
}

.stat-label {
  color: #7f8c8d;
  font-size: 14px;
}

.stat-icon {
  position: absolute;
  right: 16px;
  top: 50%;
  transform: translateY(-50%);
  font-size: 32px;
  opacity: 0.3;
}

.charts-row {
  margin-bottom: 20px;
}

.chart-placeholder {
  height: 200px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f8f9fa;
  border-radius: 4px;
  color: #999;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.table-toolbar {
  display: flex;
  gap: 16px;
  margin-bottom: 16px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.user-email {
  font-size: 12px;
  color: #999;
}

.logo-uploader {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  width: 100px;
  height: 100px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.logo {
  width: 100px;
  height: 100px;
  object-fit: cover;
}

.logo-uploader-icon {
  font-size: 28px;
  color: #8c939d;
}

.analytics-content {
  height: 300px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f8f9fa;
  border-radius: 4px;
  color: #999;
}

.log-filters {
  display: flex;
  align-items: center;
}
</style>