<template>
  <div class="document-history">
    <div class="history-header">
      <h3 class="history-title">
        <el-icon><Timer /></el-icon>
        版本历史
      </h3>
      <div class="history-actions">
        <el-button @click="refreshHistory" size="small">
          <el-icon><Refresh /></el-icon>
          刷新
        </el-button>
        <el-button @click="$emit('close')" size="small">
          <el-icon><Close /></el-icon>
          关闭
        </el-button>
      </div>
    </div>

    <div class="history-content">
      <el-timeline>
        <el-timeline-item
          v-for="version in versions"
          :key="version.id"
          :timestamp="formatTime(version.createdAt)"
          placement="top"
          :type="getVersionType(version.changeType)"
          :icon="getVersionIcon(version.changeType)"
        >
          <el-card class="version-card">
            <template #header>
              <div class="version-header">
                <div class="version-info">
                  <span class="version-number">v{{ version.version }}</span>
                  <span class="version-type">{{ getChangeTypeLabel(version.changeType) }}</span>
                  <span class="version-size">{{ formatSize(version.size) }}</span>
                </div>
                <div class="version-actions">
                  <el-button-group size="small">
                    <el-button @click="previewVersion(version)">
                      <el-icon><View /></el-icon>
                      预览
                    </el-button>
                    <el-button
                      v-if="canRestore && version.version !== currentVersion"
                      @click="restoreVersion(version)"
                      type="warning"
                    >
                      <el-icon><RefreshRight /></el-icon>
                      恢复
                    </el-button>
                    <el-button @click="compareVersion(version)">
                      <el-icon><Operation /></el-icon>
                      对比
                    </el-button>
                  </el-button-group>
                </div>
              </div>
            </template>

            <div class="version-content">
              <div class="version-author">
                <el-avatar :src="version.author.avatar" size="small" />
                <span class="author-name">{{ version.author.name }}</span>
              </div>
              <p class="version-description">{{ version.changeDescription }}</p>
              <div v-if="version.title !== originalTitle" class="title-change">
                <el-tag size="small" type="info">标题变更</el-tag>
                <span class="title-diff">{{ version.title }}</span>
              </div>
            </div>
          </el-card>
        </el-timeline-item>
      </el-timeline>

      <div v-if="loading" class="loading-container">
        <el-loading :loading="loading" />
      </div>

      <div v-if="versions.length === 0 && !loading" class="empty-history">
        <el-empty description="暂无版本历史" />
      </div>
    </div>

    <!-- 版本预览对话框 -->
    <el-dialog
      v-model="previewVisible"
      title="版本预览"
      width="80%"
      :before-close="closePreview"
    >
      <div v-if="selectedVersion" class="version-preview">
        <div class="preview-header">
          <h3>{{ selectedVersion.title }}</h3>
          <div class="preview-meta">
            <el-tag>v{{ selectedVersion.version }}</el-tag>
            <span>{{ selectedVersion.author.name }}</span>
            <span>{{ formatTime(selectedVersion.createdAt) }}</span>
          </div>
        </div>
        <div class="preview-content markdown-content" v-html="renderMarkdown(selectedVersion.content)"></div>
      </div>
    </el-dialog>

    <!-- 版本对比对话框 -->
    <el-dialog
      v-model="compareVisible"
      title="版本对比"
      width="90%"
      :before-close="closeCompare"
    >
      <div v-if="compareData" class="version-compare">
        <div class="compare-header">
          <div class="compare-versions">
            <div class="version-selector">
              <label>对比版本:</label>
              <el-select v-model="compareFrom" @change="updateCompare">
                <el-option
                  v-for="version in versions"
                  :key="version.id"
                  :label="`v${version.version} - ${version.changeDescription}`"
                  :value="version.id"
                />
              </el-select>
            </div>
            <div class="version-selector">
              <label>当前版本:</label>
              <el-select v-model="compareTo" @change="updateCompare">
                <el-option
                  v-for="version in versions"
                  :key="version.id"
                  :label="`v${version.version} - ${version.changeDescription}`"
                  :value="version.id"
                />
              </el-select>
            </div>
          </div>
        </div>
        <div class="compare-content">
          <div class="diff-container" v-html="compareData.diff"></div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { DocumentVersion } from '@/types/document'

interface Props {
  documentId: string
  currentVersion: number
  canRestore?: boolean
  originalTitle: string
}

const props = withDefaults(defineProps<Props>(), {
  canRestore: false
})

const emit = defineEmits<{
  close: []
  restore: [version: DocumentVersion]
}>()

// 响应式数据
const loading = ref(false)
const versions = ref<DocumentVersion[]>([])
const previewVisible = ref(false)
const compareVisible = ref(false)
const selectedVersion = ref<DocumentVersion | null>(null)
const compareFrom = ref('')
const compareTo = ref('')
const compareData = ref<{ diff: string } | null>(null)

// 生命周期
onMounted(() => {
  loadVersionHistory()
})

// 加载版本历史
const loadVersionHistory = async () => {
  loading.value = true
  try {
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 1000))

    versions.value = [
      {
        id: '1',
        version: 3,
        title: 'Vue3 组件开发规范 - 最新版',
        content: '# Vue3 组件开发规范\n\n最新的组件开发规范...',
        author: {
          id: '1',
          name: '张三',
          avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
        },
        createdAt: new Date().toISOString(),
        changeType: 'update',
        changeDescription: '更新了响应式数据处理规范',
        size: 15420
      },
      {
        id: '2',
        version: 2,
        title: 'Vue3 组件开发规范',
        content: '# Vue3 组件开发规范\n\n组件开发规范...',
        author: {
          id: '1',
          name: '张三',
          avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
        },
        createdAt: new Date(Date.now() - 24 * 60 * 60 * 1000).toISOString(),
        changeType: 'publish',
        changeDescription: '发布文档',
        size: 12340
      },
      {
        id: '3',
        version: 1,
        title: 'Vue3 组件开发规范',
        content: '# Vue3 组件开发规范\n\n初版内容...',
        author: {
          id: '1',
          name: '张三',
          avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
        },
        createdAt: new Date(Date.now() - 7 * 24 * 60 * 60 * 1000).toISOString(),
        changeType: 'create',
        changeDescription: '创建文档',
        size: 8900
      }
    ]
  } catch (error) {
    ElMessage.error('加载版本历史失败')
  } finally {
    loading.value = false
  }
}

// 刷新历史
const refreshHistory = () => {
  loadVersionHistory()
}

// 格式化时间
const formatTime = (time: string) => {
  return new Date(time).toLocaleString()
}

// 格式化文件大小
const formatSize = (bytes: number) => {
  if (bytes === 0) return '0 B'
  const k = 1024
  const sizes = ['B', 'KB', 'MB', 'GB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
}

// 获取版本类型
const getVersionType = (changeType: string) => {
  const typeMap = {
    create: 'success',
    update: 'primary',
    publish: 'warning',
    archive: 'info',
    restore: 'success'
  }
  return typeMap[changeType] || 'primary'
}

// 获取版本图标
const getVersionIcon = (changeType: string) => {
  const iconMap = {
    create: 'Plus',
    update: 'Edit',
    publish: 'Upload',
    archive: 'FolderOpened',
    restore: 'RefreshRight'
  }
  return iconMap[changeType] || 'Edit'
}

// 获取变更类型标签
const getChangeTypeLabel = (changeType: string) => {
  const labelMap = {
    create: '创建',
    update: '更新',
    publish: '发布',
    archive: '归档',
    restore: '恢复'
  }
  return labelMap[changeType] || '未知'
}

// 预览版本
const previewVersion = (version: DocumentVersion) => {
  selectedVersion.value = version
  previewVisible.value = true
}

// 关闭预览
const closePreview = () => {
  previewVisible.value = false
  selectedVersion.value = null
}

// 恢复版本
const restoreVersion = (version: DocumentVersion) => {
  ElMessageBox.confirm(
    `确定要恢复到版本 v${version.version} 吗？当前版本将被保存为新的历史记录。`,
    '恢复版本',
    {
      confirmButtonText: '恢复',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    emit('restore', version)
    ElMessage.success(`已恢复到版本 v${version.version}`)
  }).catch(() => {
    ElMessage.info('已取消恢复')
  })
}

// 对比版本
const compareVersion = (version: DocumentVersion) => {
  compareFrom.value = version.id
  // 默认与当前版本对比
  const currentVersionData = versions.value.find(v => v.version === props.currentVersion)
  if (currentVersionData) {
    compareTo.value = currentVersionData.id
  }
  updateCompare()
  compareVisible.value = true
}

// 更新对比
const updateCompare = () => {
  if (!compareFrom.value || !compareTo.value) return

  // 模拟生成diff
  compareData.value = {
    diff: `
      <div class="diff-line">
        <span class="line-number">1</span>
        <span class="diff-content">
          <span class="diff-removed">- # Vue3 组件开发规范</span>
        </span>
      </div>
      <div class="diff-line">
        <span class="line-number">1</span>
        <span class="diff-content">
          <span class="diff-added">+ # Vue3 组件开发规范 - 最新版</span>
        </span>
      </div>
      <div class="diff-line">
        <span class="line-number">2</span>
        <span class="diff-content">
          <span class="diff-unchanged">## 概述</span>
        </span>
      </div>
    `
  }
}

// 关闭对比
const closeCompare = () => {
  compareVisible.value = false
  compareData.value = null
}

// 渲染Markdown
const renderMarkdown = (content: string) => {
  // 简单的markdown渲染，实际项目中应使用专门的markdown解析器
  return content
    .replace(/^# (.*$)/gim, '<h1>$1</h1>')
    .replace(/^## (.*$)/gim, '<h2>$1</h2>')
    .replace(/^### (.*$)/gim, '<h3>$1</h3>')
    .replace(/\n/g, '<br>')
}
</script>

<style scoped>
.document-history {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.history-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: var(--spacing-4) var(--spacing-6);
  border-bottom: 1px solid var(--border-light);
  background: var(--bg-secondary);
}

.history-title {
  margin: 0;
  display: flex;
  align-items: center;
  gap: var(--spacing-2);
  font-size: var(--text-lg);
  font-weight: var(--font-semibold);
  color: var(--text-primary);
}

.history-actions {
  display: flex;
  gap: var(--spacing-2);
}

.history-content {
  flex: 1;
  padding: var(--spacing-6);
  overflow-y: auto;
}

.version-card {
  margin-bottom: var(--spacing-4);
}

.version-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.version-info {
  display: flex;
  align-items: center;
  gap: var(--spacing-3);
}

.version-number {
  font-weight: var(--font-bold);
  color: var(--primary-600);
  font-size: var(--text-lg);
}

.version-type {
  font-size: var(--text-sm);
  color: var(--text-secondary);
}

.version-size {
  font-size: var(--text-xs);
  color: var(--text-tertiary);
}

.version-content {
  margin-top: var(--spacing-3);
}

.version-author {
  display: flex;
  align-items: center;
  gap: var(--spacing-2);
  margin-bottom: var(--spacing-2);
}

.author-name {
  font-size: var(--text-sm);
  color: var(--text-secondary);
}

.version-description {
  margin: var(--spacing-2) 0;
  color: var(--text-primary);
  line-height: var(--leading-relaxed);
}

.title-change {
  display: flex;
  align-items: center;
  gap: var(--spacing-2);
  margin-top: var(--spacing-2);
}

.title-diff {
  font-size: var(--text-sm);
  color: var(--text-secondary);
  font-style: italic;
}

.loading-container {
  text-align: center;
  padding: var(--spacing-8);
}

.empty-history {
  text-align: center;
  padding: var(--spacing-16);
}

/* 预览样式 */
.version-preview {
  max-height: 60vh;
  overflow-y: auto;
}

.preview-header {
  margin-bottom: var(--spacing-6);
  padding-bottom: var(--spacing-4);
  border-bottom: 1px solid var(--border-light);
}

.preview-header h3 {
  margin: 0 0 var(--spacing-3) 0;
  color: var(--text-primary);
}

.preview-meta {
  display: flex;
  align-items: center;
  gap: var(--spacing-3);
  font-size: var(--text-sm);
  color: var(--text-secondary);
}

.preview-content {
  line-height: var(--leading-relaxed);
}

/* 对比样式 */
.compare-header {
  margin-bottom: var(--spacing-6);
  padding-bottom: var(--spacing-4);
  border-bottom: 1px solid var(--border-light);
}

.compare-versions {
  display: flex;
  gap: var(--spacing-6);
}

.version-selector {
  display: flex;
  align-items: center;
  gap: var(--spacing-2);
}

.version-selector label {
  font-size: var(--text-sm);
  color: var(--text-secondary);
  white-space: nowrap;
}

.diff-container {
  background: var(--bg-secondary);
  border-radius: var(--radius-md);
  padding: var(--spacing-4);
  font-family: var(--font-mono);
  font-size: var(--text-sm);
  line-height: var(--leading-relaxed);
  max-height: 50vh;
  overflow-y: auto;
}

.diff-line {
  display: flex;
  margin: var(--spacing-1) 0;
}

.line-number {
  width: 40px;
  color: var(--text-tertiary);
  text-align: right;
  margin-right: var(--spacing-3);
  user-select: none;
}

.diff-content {
  flex: 1;
}

.diff-removed {
  background-color: var(--danger-50);
  color: var(--danger-700);
  padding: 2px 4px;
}

.diff-added {
  background-color: var(--success-50);
  color: var(--success-700);
  padding: 2px 4px;
}

.diff-unchanged {
  color: var(--text-primary);
}

/* 响应式 */
@media (max-width: 768px) {
  .history-header {
    flex-direction: column;
    gap: var(--spacing-3);
    align-items: stretch;
  }

  .version-header {
    flex-direction: column;
    gap: var(--spacing-3);
    align-items: stretch;
  }

  .compare-versions {
    flex-direction: column;
    gap: var(--spacing-3);
  }
}
</style>