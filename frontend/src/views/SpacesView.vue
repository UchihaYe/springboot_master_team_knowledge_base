<template>
  <div class="spaces-container">
    <div class="spaces-header">
      <h2>我的知识空间</h2>
      <el-button type="primary" @click="showCreateDialog = true">
        <el-icon><Plus /></el-icon>
        创建空间
      </el-button>
    </div>

    <!-- 空间网格 -->
    <div class="spaces-grid">
      <div
        v-for="space in spaces"
        :key="space.id"
        class="space-card"
        @click="enterSpace(space)"
      >
        <div class="space-icon">
          <el-icon size="32" :color="space.color">
            <component :is="space.icon" />
          </el-icon>
        </div>
        <div class="space-info">
          <h3>{{ space.name }}</h3>
          <p>{{ space.description }}</p>
          <div class="space-stats">
            <span>{{ space.documentCount }} 文档</span>
            <span>{{ space.memberCount }} 成员</span>
          </div>
        </div>
        <div class="space-actions">
          <el-dropdown @command="handleSpaceAction" trigger="click">
            <el-button text>
              <el-icon><MoreFilled /></el-icon>
            </el-button>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item :command="`edit-${space.id}`">编辑</el-dropdown-item>
                <el-dropdown-item :command="`members-${space.id}`">成员管理</el-dropdown-item>
                <el-dropdown-item :command="`settings-${space.id}`">设置</el-dropdown-item>
                <el-dropdown-item divided :command="`delete-${space.id}`">删除</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </div>

    <!-- 创建空间对话框 -->
    <el-dialog v-model="showCreateDialog" title="创建知识空间" width="500px">
      <el-form :model="newSpace" label-width="80px">
        <el-form-item label="空间名称">
          <el-input v-model="newSpace.name" placeholder="请输入空间名称" />
        </el-form-item>
        <el-form-item label="空间描述">
          <el-input
            v-model="newSpace.description"
            type="textarea"
            rows="3"
            placeholder="请输入空间描述"
          />
        </el-form-item>
        <el-form-item label="空间图标">
          <div class="icon-selector">
            <div
              v-for="icon in availableIcons"
              :key="icon.name"
              class="icon-option"
              :class="{ selected: newSpace.icon === icon.name }"
              @click="newSpace.icon = icon.name; newSpace.color = icon.color"
            >
              <el-icon size="24" :color="icon.color">
                <component :is="icon.name" />
              </el-icon>
            </div>
          </div>
        </el-form-item>
        <el-form-item label="访问权限">
          <el-radio-group v-model="newSpace.visibility">
            <el-radio value="public">公开 - 所有成员可见</el-radio>
            <el-radio value="private">私有 - 仅邀请成员可见</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showCreateDialog = false">取消</el-button>
        <el-button type="primary" @click="createSpace">创建</el-button>
      </template>
    </el-dialog>

    <!-- 空间设置对话框 -->
    <el-dialog v-model="showSettingsDialog" title="空间设置" width="600px">
      <el-tabs v-model="settingsTab">
        <el-tab-pane label="基本信息" name="basic">
          <el-form :model="currentSpace" label-width="80px">
            <el-form-item label="空间名称">
              <el-input v-model="currentSpace.name" />
            </el-form-item>
            <el-form-item label="空间描述">
              <el-input v-model="currentSpace.description" type="textarea" rows="3" />
            </el-form-item>
            <el-form-item label="访问权限">
              <el-radio-group v-model="currentSpace.visibility">
                <el-radio value="public">公开</el-radio>
                <el-radio value="private">私有</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <el-tab-pane label="成员管理" name="members">
          <div class="member-management">
            <div class="member-header">
              <el-input
                v-model="memberSearch"
                placeholder="搜索成员"
                style="width: 200px;"
              />
              <el-button type="primary" @click="showInviteDialog = true">邀请成员</el-button>
            </div>

            <el-table :data="filteredMembers" style="width: 100%; margin-top: 16px;">
              <el-table-column label="成员">
                <template #default="scope">
                  <div class="member-info">
                    <el-avatar :src="scope.row.avatar" size="small" />
                    <span>{{ scope.row.name }}</span>
                  </div>
                </template>
              </el-table-column>
              <el-table-column prop="email" label="邮箱" />
              <el-table-column prop="role" label="角色">
                <template #default="scope">
                  <el-select v-model="scope.row.role" size="small">
                    <el-option label="管理员" value="admin" />
                    <el-option label="编辑者" value="editor" />
                    <el-option label="查看者" value="viewer" />
                  </el-select>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="120">
                <template #default="scope">
                  <el-button size="small" type="danger" @click="removeMember(scope.row)">
                    移除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-tab-pane>

        <el-tab-pane label="文档模板" name="templates">
          <div class="template-management">
            <div class="template-header">
              <span>文档模板</span>
              <el-button type="primary" size="small">添加模板</el-button>
            </div>

            <div class="template-list">
              <div
                v-for="template in spaceTemplates"
                :key="template.id"
                class="template-item"
              >
                <div class="template-info">
                  <h4>{{ template.name }}</h4>
                  <p>{{ template.description }}</p>
                </div>
                <div class="template-actions">
                  <el-button size="small">编辑</el-button>
                  <el-button size="small" type="danger">删除</el-button>
                </div>
              </div>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
      <template #footer>
        <el-button @click="showSettingsDialog = false">取消</el-button>
        <el-button type="primary" @click="saveSpaceSettings">保存</el-button>
      </template>
    </el-dialog>

    <!-- 邀请成员对话框 -->
    <el-dialog v-model="showInviteDialog" title="邀请成员" width="400px">
      <el-form label-width="80px">
        <el-form-item label="邮箱地址">
          <el-input v-model="inviteEmail" placeholder="请输入邮箱地址" />
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="inviteRole">
            <el-option label="编辑者" value="editor" />
            <el-option label="查看者" value="viewer" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showInviteDialog = false">取消</el-button>
        <el-button type="primary" @click="inviteMember">发送邀请</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()

const showCreateDialog = ref(false)
const showSettingsDialog = ref(false)
const showInviteDialog = ref(false)
const settingsTab = ref('basic')
const memberSearch = ref('')
const inviteEmail = ref('')
const inviteRole = ref('editor')

// 新建空间表单
const newSpace = reactive({
  name: '',
  description: '',
  icon: 'Folder',
  color: '#409eff',
  visibility: 'public'
})

// 当前编辑的空间
const currentSpace = reactive({
  id: null,
  name: '',
  description: '',
  visibility: 'public'
})

// 可用图标
const availableIcons = [
  { name: 'Folder', color: '#409eff' },
  { name: 'Document', color: '#67c23a' },
  { name: 'Collection', color: '#e6a23c' },
  { name: 'Box', color: '#f56c6c' },
  { name: 'DataBoard', color: '#909399' },
  { name: 'Reading', color: '#9c88ff' }
]

// 知识空间列表
const spaces = ref([
  {
    id: 1,
    name: '技术文档',
    description: '存放技术相关的文档和规范',
    icon: 'Document',
    color: '#67c23a',
    documentCount: 25,
    memberCount: 8,
    visibility: 'public'
  },
  {
    id: 2,
    name: '项目管理',
    description: '项目管理相关的文档和流程',
    icon: 'DataBoard',
    color: '#409eff',
    documentCount: 15,
    memberCount: 12,
    visibility: 'public'
  },
  {
    id: 3,
    name: '产品设计',
    description: '产品设计和用户体验相关文档',
    icon: 'Collection',
    color: '#e6a23c',
    documentCount: 18,
    memberCount: 6,
    visibility: 'private'
  }
])

// 空间成员
const spaceMembers = ref([
  {
    id: 1,
    name: '张三',
    email: 'zhangsan@company.com',
    role: 'admin',
    avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
  },
  {
    id: 2,
    name: '李四',
    email: 'lisi@company.com',
    role: 'editor',
    avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
  },
  {
    id: 3,
    name: '王五',
    email: 'wangwu@company.com',
    role: 'viewer',
    avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
  }
])

// 文档模板
const spaceTemplates = ref([
  {
    id: 1,
    name: '会议纪要模板',
    description: '标准的会议纪要格式模板'
  },
  {
    id: 2,
    name: 'API 文档模板',
    description: 'API 接口文档的标准模板'
  }
])

// 过滤后的成员列表
const filteredMembers = computed(() => {
  return spaceMembers.value.filter(member =>
    member.name.includes(memberSearch.value) ||
    member.email.includes(memberSearch.value)
  )
})

const createSpace = () => {
  if (!newSpace.name.trim()) {
    ElMessage.error('请输入空间名称')
    return
  }

  // 模拟创建空间
  const space = {
    id: Date.now(),
    ...newSpace,
    documentCount: 0,
    memberCount: 1
  }

  spaces.value.push(space)
  showCreateDialog.value = false
  ElMessage.success('知识空间创建成功')

  // 重置表单
  Object.assign(newSpace, {
    name: '',
    description: '',
    icon: 'Folder',
    color: '#409eff',
    visibility: 'public'
  })
}

const enterSpace = (space: any) => {
  router.push(`/space/${space.id}`)
}

const handleSpaceAction = (command: string) => {
  const [action, spaceId] = command.split('-')
  const space = spaces.value.find(s => s.id === parseInt(spaceId))

  switch (action) {
    case 'edit':
      Object.assign(currentSpace, space)
      showSettingsDialog.value = true
      settingsTab.value = 'basic'
      break
    case 'members':
      Object.assign(currentSpace, space)
      showSettingsDialog.value = true
      settingsTab.value = 'members'
      break
    case 'settings':
      Object.assign(currentSpace, space)
      showSettingsDialog.value = true
      settingsTab.value = 'basic'
      break
    case 'delete':
      deleteSpace(space)
      break
  }
}

const deleteSpace = async (space: any) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除空间"${space.name}"吗？此操作不可恢复。`,
      '删除确认',
      {
        confirmButtonText: '确定删除',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    const index = spaces.value.findIndex(s => s.id === space.id)
    if (index > -1) {
      spaces.value.splice(index, 1)
      ElMessage.success('空间已删除')
    }
  } catch {
    // 用户取消删除
  }
}

const saveSpaceSettings = () => {
  const space = spaces.value.find(s => s.id === currentSpace.id)
  if (space) {
    Object.assign(space, currentSpace)
  }
  showSettingsDialog.value = false
  ElMessage.success('设置已保存')
}

const inviteMember = () => {
  if (!inviteEmail.value.trim()) {
    ElMessage.error('请输入邮箱地址')
    return
  }

  // 模拟发送邀请
  showInviteDialog.value = false
  ElMessage.success('邀请已发送')
  inviteEmail.value = ''
  inviteRole.value = 'editor'
}

const removeMember = async (member: any) => {
  try {
    await ElMessageBox.confirm(
      `确定要移除成员"${member.name}"吗？`,
      '移除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    const index = spaceMembers.value.findIndex(m => m.id === member.id)
    if (index > -1) {
      spaceMembers.value.splice(index, 1)
      ElMessage.success('成员已移除')
    }
  } catch {
    // 用户取消移除
  }
}
</script>

<style scoped>
.spaces-container {
  padding: 20px;
  background-color: #f5f5f5;
  min-height: calc(100vh - 60px);
}

.spaces-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.spaces-header h2 {
  margin: 0;
  color: #2c3e50;
}

.spaces-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 20px;
}

.space-card {
  background: white;
  border-radius: 8px;
  padding: 24px;
  border: 1px solid #e4e7ed;
  cursor: pointer;
  transition: all 0.3s;
  position: relative;
}

.space-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transform: translateY(-2px);
}

.space-icon {
  margin-bottom: 16px;
}

.space-info h3 {
  margin: 0 0 8px 0;
  color: #2c3e50;
  font-size: 18px;
}

.space-info p {
  margin: 0 0 16px 0;
  color: #7f8c8d;
  font-size: 14px;
  line-height: 1.4;
}

.space-stats {
  display: flex;
  gap: 16px;
  color: #909399;
  font-size: 14px;
}

.space-actions {
  position: absolute;
  top: 16px;
  right: 16px;
}

.icon-selector {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

.icon-option {
  padding: 8px;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s;
}

.icon-option:hover {
  border-color: #409eff;
}

.icon-option.selected {
  border-color: #409eff;
  background-color: #ecf5ff;
}

.member-management {
  max-height: 400px;
  overflow-y: auto;
}

.member-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.member-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.template-management {
  max-height: 400px;
  overflow-y: auto;
}

.template-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.template-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.template-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
}

.template-info h4 {
  margin: 0 0 4px 0;
  color: #2c3e50;
}

.template-info p {
  margin: 0;
  color: #7f8c8d;
  font-size: 14px;
}

.template-actions {
  display: flex;
  gap: 8px;
}
</style>