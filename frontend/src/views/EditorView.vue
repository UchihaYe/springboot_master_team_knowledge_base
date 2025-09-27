<template>
  <div class="editor-container">
    <!-- 顶部工具栏 -->
    <div class="toolbar">
      <div class="toolbar-left">
        <el-button @click="goBack" class="toolbar-btn" text>
          <el-icon size="18"><ArrowLeft /></el-icon>
          返回
        </el-button>
        <div class="toolbar-divider"></div>
        <div class="document-title-section">
          <el-icon size="20" color="var(--primary-500)">
            <Document />
          </el-icon>
          <el-input
            v-model="document.title"
            placeholder="无标题文档"
            class="title-input"
            :border="false"
          />
        </div>
      </div>

      <div class="toolbar-center">
        <div class="status-indicators">
          <span class="save-status" :class="{ saving: saving, saved: !saving }">
            <el-icon v-if="saving"><Loading /></el-icon>
            <el-icon v-else><CircleCheck /></el-icon>
            {{ saving ? '保存中...' : '已保存' }}
          </span>
        </div>
      </div>

      <div class="toolbar-right">
        <el-button-group class="view-toggle">
          <el-button
            :type="!previewMode ? 'primary' : ''"
            @click="previewMode = false"
            size="small"
          >
            <el-icon><Edit /></el-icon>
            编辑
          </el-button>
          <el-button
            :type="previewMode ? 'primary' : ''"
            @click="previewMode = true"
            size="small"
          >
            <el-icon><View /></el-icon>
            预览
          </el-button>
        </el-button-group>

        <el-button @click="saveDocument" type="primary" class="save-btn" :loading="saving">
          <el-icon v-if="!saving"><Check /></el-icon>
          {{ saving ? '保存中' : '保存' }}
        </el-button>

        <el-dropdown>
          <el-button class="more-btn" text>
            <el-icon size="18"><MoreFilled /></el-icon>
          </el-button>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="showSettings = true">
                <el-icon><Setting /></el-icon>
                文档设置
              </el-dropdown-item>
              <el-dropdown-item @click="showVersions = true">
                <el-icon><Clock /></el-icon>
                版本历史
              </el-dropdown-item>
              <el-dropdown-item @click="exportDocument">
                <el-icon><Download /></el-icon>
                导出文档
              </el-dropdown-item>
              <el-dropdown-item divided @click="shareDocument">
                <el-icon><Share /></el-icon>
                分享文档
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>

    <!-- 编辑器主体 -->
    <div class="editor-main" v-if="!previewMode">
      <div class="editor-layout">
        <!-- 左侧编辑区 -->
        <div class="editor-pane">
          <div class="editor-header">
            <div class="header-left">
              <h3 class="pane-title">
                <el-icon><EditPen /></el-icon>
                编辑器
              </h3>
              <span class="char-count">{{ document.content.length }} 字符</span>
            </div>
            <div class="editor-tools">
              <el-button-group size="small" class="format-tools">
                <el-button @click="insertFormat('**', '**')" title="粗体">
                  <el-icon><BoldIcon /></el-icon>
                </el-button>
                <el-button @click="insertFormat('*', '*')" title="斜体">
                  <el-icon><ItalicIcon /></el-icon>
                </el-button>
                <el-button @click="insertFormat('`', '`')" title="代码">
                  <el-icon><DocumentCopy /></el-icon>
                </el-button>
              </el-button-group>
              <el-button size="small" @click="insertLink" title="插入链接">
                <el-icon><Link /></el-icon>
              </el-button>
              <el-button size="small" @click="insertImage" title="插入图片">
                <el-icon><Picture /></el-icon>
              </el-button>
              <el-button size="small" @click="insertTable" title="插入表格">
                <el-icon><Grid /></el-icon>
              </el-button>
            </div>
          </div>
          <div class="editor-wrapper">
            <el-input
              v-model="document.content"
              type="textarea"
              class="markdown-editor"
              placeholder="# 开始编写你的文档...

使用 Markdown 语法来格式化你的内容：
- **粗体文本**
- *斜体文本*
- `代码`
- [链接](http://example.com)
- ![图片](http://example.com/image.jpg)

## 二级标题
### 三级标题

1. 有序列表
2. 项目二

- 无序列表
- 项目二"
              @input="onContentChange"
              ref="editorRef"
            />
          </div>
        </div>

        <!-- 右侧预览区 -->
        <div class="preview-pane">
          <div class="preview-header">
            <div class="header-left">
              <h3 class="pane-title">
                <el-icon><View /></el-icon>
                预览
              </h3>
              <el-tag size="small" type="success">实时预览</el-tag>
            </div>
            <div class="preview-tools">
              <el-button size="small" @click="copyContent" title="复制内容">
                <el-icon><DocumentCopy /></el-icon>
              </el-button>
              <el-button size="small" @click="printPreview" title="打印预览">
                <el-icon><Printer /></el-icon>
              </el-button>
            </div>
          </div>
          <div class="preview-wrapper">
            <div class="markdown-preview" v-html="renderedContent"></div>
          </div>
        </div>
      </div>
    </div>

    <!-- 纯预览模式 -->
    <div class="preview-only" v-else>
      <div class="document-header">
        <h1>{{ document.title || '无标题文档' }}</h1>
        <div class="document-meta">
          <span>最后编辑: {{ document.updatedAt }}</span>
          <span>作者: {{ document.author }}</span>
        </div>
      </div>
      <div class="markdown-preview" v-html="renderedContent"></div>
    </div>

    <!-- 文档设置对话框 -->
    <el-dialog v-model="showSettings" title="文档设置" width="500px">
      <el-form :model="document" label-width="80px">
        <el-form-item label="标题">
          <el-input v-model="document.title" />
        </el-form-item>
        <el-form-item label="标签">
          <el-tag
            v-for="tag in document.tags"
            :key="tag"
            closable
            @close="removeTag(tag)"
            class="mr-8"
          >
            {{ tag }}
          </el-tag>
          <el-input
            v-if="tagInputVisible"
            ref="tagInput"
            v-model="tagInputValue"
            size="small"
            @keyup.enter="handleTagInput"
            @blur="handleTagInput"
            style="width: 100px;"
          />
          <el-button v-else size="small" @click="showTagInput">+ 添加标签</el-button>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="document.description" type="textarea" rows="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showSettings = false">取消</el-button>
        <el-button type="primary" @click="saveSettings">保存</el-button>
      </template>
    </el-dialog>

    <!-- 版本历史对话框 -->
    <el-dialog v-model="showVersions" title="版本历史" width="800px">
      <el-table :data="versions" style="width: 100%">
        <el-table-column prop="version" label="版本" width="80" />
        <el-table-column prop="author" label="作者" width="120" />
        <el-table-column prop="time" label="时间" width="180" />
        <el-table-column prop="comment" label="备注" />
        <el-table-column label="操作" width="120">
          <template #default="scope">
            <el-button size="small" @click="viewVersion(scope.row)">查看</el-button>
            <el-button size="small" type="primary" @click="restoreVersion(scope.row)">恢复</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, nextTick } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()
const route = useRoute()

const editorRef = ref()
const tagInput = ref()
const saving = ref(false)
const previewMode = ref(false)
const showSettings = ref(false)
const showVersions = ref(false)
const tagInputVisible = ref(false)
const tagInputValue = ref('')

// 文档数据
const document = ref({
  id: null as string | null,
  title: '',
  content: '',
  description: '',
  tags: [] as string[],
  author: '当前用户',
  updatedAt: new Date().toLocaleString()
})

// 版本历史
const versions = ref([
  {
    version: 'v1.0',
    author: '张三',
    time: '2024-01-15 10:30',
    comment: '初始版本'
  },
  {
    version: 'v1.1',
    author: '李四',
    time: '2024-01-16 14:20',
    comment: '添加了API说明'
  }
])

// 简单的 Markdown 渲染（实际项目中应使用专业的 Markdown 解析器）
const renderedContent = computed(() => {
  let html = document.value.content

  // 标题
  html = html.replace(/^### (.*$)/gim, '<h3>$1</h3>')
  html = html.replace(/^## (.*$)/gim, '<h2>$1</h2>')
  html = html.replace(/^# (.*$)/gim, '<h1>$1</h1>')

  // 粗体和斜体
  html = html.replace(/\*\*(.*)\*\*/gim, '<strong>$1</strong>')
  html = html.replace(/\*(.*)\*/gim, '<em>$1</em>')

  // 代码
  html = html.replace(/`(.*?)`/gim, '<code>$1</code>')

  // 链接
  html = html.replace(/\[([^\]]+)\]\(([^)]+)\)/gim, '<a href="$2" target="_blank">$1</a>')

  // 换行
  html = html.replace(/\n/gim, '<br>')

  return html
})

const goBack = () => {
  router.back()
}

const saveDocument = async () => {
  saving.value = true

  // 模拟保存请求
  setTimeout(() => {
    saving.value = false
    document.value.updatedAt = new Date().toLocaleString()
    ElMessage.success('保存成功')
  }, 1000)
}

const onContentChange = () => {
  // 自动保存草稿
}

const insertFormat = (prefix: string, suffix: string) => {
  const textarea = editorRef.value?.textarea
  if (!textarea) return

  const start = textarea.selectionStart
  const end = textarea.selectionEnd
  const selectedText = document.value.content.substring(start, end)

  const newText = prefix + selectedText + suffix
  document.value.content = document.value.content.substring(0, start) + newText + document.value.content.substring(end)

  nextTick(() => {
    textarea.focus()
    textarea.setSelectionRange(start + prefix.length, start + prefix.length + selectedText.length)
  })
}

const insertLink = () => {
  const url = prompt('请输入链接地址:')
  const text = prompt('请输入链接文字:') || url
  if (url) {
    insertFormat(`[${text}](`, ')')
    document.value.content = document.value.content.replace(']()', `](${url})`)
  }
}

const insertImage = () => {
  const url = prompt('请输入图片地址:')
  const alt = prompt('请输入图片描述:') || '图片'
  if (url) {
    const markdown = `![${alt}](${url})`
    const textarea = editorRef.value?.textarea
    if (textarea) {
      const start = textarea.selectionStart
      document.value.content = document.value.content.substring(0, start) + markdown + document.value.content.substring(start)
    }
  }
}

const insertTable = () => {
  const table = `
| 列1 | 列2 | 列3 |
|-----|-----|-----|
| 内容1 | 内容2 | 内容3 |
| 内容4 | 内容5 | 内容6 |
`
  const textarea = editorRef.value?.textarea
  if (textarea) {
    const start = textarea.selectionStart
    document.value.content = document.value.content.substring(0, start) + table + document.value.content.substring(start)
  }
}

const showTagInput = () => {
  tagInputVisible.value = true
  nextTick(() => {
    tagInput.value.focus()
  })
}

const handleTagInput = () => {
  if (tagInputValue.value && !document.value.tags.includes(tagInputValue.value)) {
    document.value.tags.push(tagInputValue.value)
  }
  tagInputVisible.value = false
  tagInputValue.value = ''
}

const removeTag = (tag: string) => {
  const index = document.value.tags.indexOf(tag)
  if (index > -1) {
    document.value.tags.splice(index, 1)
  }
}

const saveSettings = () => {
  showSettings.value = false
  ElMessage.success('设置已保存')
}

const viewVersion = (version: any) => {
  ElMessage.info(`查看版本 ${version.version}`)
}

const restoreVersion = (version: any) => {
  ElMessage.info(`恢复到版本 ${version.version}`)
}

const exportDocument = () => {
  ElMessage.info('导出功能开发中...')
}

const shareDocument = () => {
  ElMessage.info('分享功能开发中...')
}

const copyContent = () => {
  navigator.clipboard.writeText(document.value.content)
  ElMessage.success('内容已复制到剪贴板')
}

const printPreview = () => {
  window.print()
}

onMounted(() => {
  const docId = route.params.id as string
  if (docId && docId !== 'new') {
    // 加载文档数据
    document.value = {
      id: docId,
      title: 'Vue3 组件开发规范',
      content: `# Vue3 组件开发规范

## 概述

这是一个关于Vue3组件开发的规范文档，详细介绍了Vue3项目的开发标准和最佳实践。

## 组件结构

### 基本结构
组件应该按照标准的Vue3单文件组件格式编写，包含template、script和style三个部分。

## 命名规范

- 组件名称使用PascalCase
- 属性名称使用camelCase
- 事件名称使用kebab-case

## 最佳实践

1. **使用组合式API**: 优先使用组合式API语法
2. **类型安全**: 使用TypeScript提供类型提示
3. **响应式数据**: 合理使用ref和reactive
4. **性能优化**: 适当使用computed和watch

## 代码示例

请参考项目中的示例代码和官方文档获取更多详细信息。

更多内容请参考官方文档...`,
      description: '这是一个Vue3组件开发规范文档',
      tags: ['Vue3', '开发规范', '组件'],
      author: '张三',
      updatedAt: '2024-01-15 10:30'
    }
  }
})
</script>

<style scoped>
.editor-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background: var(--bg-primary);
}

/* 工具栏样式 */
.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: var(--spacing-4) var(--spacing-6);
  border-bottom: 1px solid var(--border-light);
  background: var(--bg-primary);
  box-shadow: var(--shadow-sm);
  position: relative;
  z-index: 10;
}

.toolbar-left {
  display: flex;
  align-items: center;
  gap: var(--spacing-4);
  flex: 1;
}

.toolbar-center {
  display: flex;
  align-items: center;
  justify-content: center;
  flex: 1;
}

.toolbar-right {
  display: flex;
  align-items: center;
  gap: var(--spacing-3);
  flex: 1;
  justify-content: flex-end;
}

.toolbar-btn {
  color: var(--text-secondary);
  font-weight: var(--font-medium);
  transition: all var(--duration-fast) var(--ease-out);
}

.toolbar-btn:hover {
  color: var(--primary-500);
  background: var(--primary-50);
}

.toolbar-divider {
  width: 1px;
  height: 24px;
  background: var(--border-medium);
  margin: 0 var(--spacing-2);
}

.document-title-section {
  display: flex;
  align-items: center;
  gap: var(--spacing-3);
  flex: 1;
  max-width: 400px;
}

.title-input {
  flex: 1;
  font-size: var(--text-xl);
  font-weight: var(--font-semibold);
}

.title-input :deep(.el-input__wrapper) {
  border: none;
  box-shadow: none;
  padding: var(--spacing-2) 0;
  background: transparent;
}

.title-input :deep(.el-input__inner) {
  font-size: var(--text-xl);
  font-weight: var(--font-semibold);
  color: var(--text-primary);
}

.status-indicators {
  display: flex;
  align-items: center;
  gap: var(--spacing-4);
}

.save-status {
  display: flex;
  align-items: center;
  gap: var(--spacing-2);
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
  padding: var(--spacing-1) var(--spacing-3);
  border-radius: var(--radius-md);
  transition: all var(--duration-fast) var(--ease-out);
}

.save-status.saving {
  color: var(--warning-600);
  background: var(--warning-50);
}

.save-status.saved {
  color: var(--success-600);
  background: var(--success-50);
}

.view-toggle {
  border-radius: var(--radius-md);
  overflow: hidden;
}

.save-btn {
  font-weight: var(--font-medium);
  padding: var(--spacing-2) var(--spacing-4);
}

.more-btn {
  color: var(--text-secondary);
  transition: all var(--duration-fast) var(--ease-out);
}

.more-btn:hover {
  color: var(--primary-500);
  background: var(--primary-50);
}

/* 编辑器主体布局 */
.editor-main {
  flex: 1;
  overflow: hidden;
  background: var(--bg-secondary);
}

.editor-layout {
  height: 100%;
  display: flex;
}

.editor-pane,
.preview-pane {
  flex: 1;
  display: flex;
  flex-direction: column;
  background: var(--bg-primary);
}

.editor-pane {
  border-right: 1px solid var(--border-light);
}

/* 编辑器和预览区头部 */
.editor-header,
.preview-header {
  padding: var(--spacing-4) var(--spacing-6);
  border-bottom: 1px solid var(--border-light);
  background: var(--bg-tertiary);
  display: flex;
  justify-content: space-between;
  align-items: center;
  min-height: 60px;
}

.header-left {
  display: flex;
  align-items: center;
  gap: var(--spacing-4);
}

.pane-title {
  font-size: var(--text-lg);
  font-weight: var(--font-semibold);
  color: var(--text-primary);
  margin: 0;
  display: flex;
  align-items: center;
  gap: var(--spacing-2);
}

.char-count {
  font-size: var(--text-sm);
  color: var(--text-tertiary);
  background: var(--bg-secondary);
  padding: var(--spacing-1) var(--spacing-2);
  border-radius: var(--radius-sm);
}

.editor-tools,
.preview-tools {
  display: flex;
  align-items: center;
  gap: var(--spacing-2);
}

.format-tools {
  margin-right: var(--spacing-2);
}

/* 编辑器包装器 */
.editor-wrapper,
.preview-wrapper {
  flex: 1;
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

.markdown-editor {
  flex: 1;
  border: none;
  background: var(--bg-primary);
}

.markdown-editor :deep(.el-textarea__wrapper) {
  border: none;
  box-shadow: none;
  border-radius: 0;
  background: var(--bg-primary);
  height: 100%;
}

.markdown-editor :deep(.el-textarea__inner) {
  border: none;
  border-radius: 0;
  resize: none;
  font-family: var(--font-mono);
  font-size: var(--text-base);
  line-height: var(--leading-relaxed);
  height: 100% !important;
  padding: var(--spacing-6);
  color: var(--text-primary);
  background: var(--bg-primary);
}

.markdown-editor :deep(.el-textarea__inner::placeholder) {
  color: var(--text-tertiary);
  line-height: var(--leading-relaxed);
}

/* 预览区域样式 */
.markdown-preview {
  flex: 1;
  padding: var(--spacing-8);
  overflow-y: auto;
  font-size: var(--text-base);
  line-height: var(--leading-relaxed);
  color: var(--text-primary);
  background: var(--bg-primary);
}

.preview-only {
  flex: 1;
  overflow-y: auto;
  padding: var(--spacing-12);
  max-width: 900px;
  margin: 0 auto;
  background: var(--bg-primary);
}

/* 文档头部样式 */
.document-header {
  margin-bottom: var(--spacing-10);
  padding-bottom: var(--spacing-6);
  border-bottom: 2px solid var(--border-light);
}

.document-header h1 {
  font-size: var(--text-4xl);
  font-weight: var(--font-bold);
  margin: 0 0 var(--spacing-3) 0;
  color: var(--text-primary);
  line-height: var(--leading-tight);
}

.document-meta {
  color: var(--text-secondary);
  font-size: var(--text-base);
  display: flex;
  gap: var(--spacing-6);
}

.document-meta span {
  display: flex;
  align-items: center;
  gap: var(--spacing-1);
}

.mr-8 {
  margin-right: var(--spacing-2);
}

/* 增强的 Markdown 预览样式 */
.markdown-preview :deep(h1) {
  font-size: var(--text-3xl);
  font-weight: var(--font-bold);
  color: var(--text-primary);
  margin: var(--spacing-10) 0 var(--spacing-6) 0;
  padding-bottom: var(--spacing-4);
  border-bottom: 3px solid var(--primary-200);
  line-height: var(--leading-tight);
}

.markdown-preview :deep(h2) {
  font-size: var(--text-2xl);
  font-weight: var(--font-semibold);
  color: var(--text-primary);
  margin: var(--spacing-8) 0 var(--spacing-4) 0;
  line-height: var(--leading-snug);
}

.markdown-preview :deep(h3) {
  font-size: var(--text-xl);
  font-weight: var(--font-semibold);
  color: var(--text-primary);
  margin: var(--spacing-6) 0 var(--spacing-3) 0;
  line-height: var(--leading-snug);
}

.markdown-preview :deep(h4) {
  font-size: var(--text-lg);
  font-weight: var(--font-medium);
  color: var(--text-primary);
  margin: var(--spacing-5) 0 var(--spacing-2) 0;
}

.markdown-preview :deep(p) {
  margin: var(--spacing-4) 0;
  line-height: var(--leading-relaxed);
  color: var(--text-primary);
}

.markdown-preview :deep(ul),
.markdown-preview :deep(ol) {
  margin: var(--spacing-4) 0;
  padding-left: var(--spacing-6);
}

.markdown-preview :deep(li) {
  margin: var(--spacing-1) 0;
  line-height: var(--leading-relaxed);
}

.markdown-preview :deep(code) {
  background: var(--bg-tertiary);
  color: var(--primary-600);
  padding: var(--spacing-1) var(--spacing-2);
  border-radius: var(--radius-sm);
  font-family: var(--font-mono);
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
}

.markdown-preview :deep(pre) {
  background: var(--bg-dark);
  color: var(--text-inverse);
  padding: var(--spacing-5);
  border-radius: var(--radius-lg);
  overflow-x: auto;
  margin: var(--spacing-4) 0;
  border-left: 4px solid var(--primary-500);
}

.markdown-preview :deep(pre code) {
  background: none;
  color: inherit;
  padding: 0;
  font-size: var(--text-sm);
}

.markdown-preview :deep(blockquote) {
  border-left: 4px solid var(--primary-300);
  background: var(--primary-50);
  padding: var(--spacing-4) var(--spacing-5);
  margin: var(--spacing-4) 0;
  color: var(--text-secondary);
  font-style: italic;
}

.markdown-preview :deep(strong) {
  font-weight: var(--font-bold);
  color: var(--text-primary);
}

.markdown-preview :deep(em) {
  font-style: italic;
  color: var(--text-secondary);
}

.markdown-preview :deep(a) {
  color: var(--primary-500);
  text-decoration: none;
  font-weight: var(--font-medium);
  transition: all var(--duration-fast) var(--ease-out);
}

.markdown-preview :deep(a:hover) {
  color: var(--primary-600);
  text-decoration: underline;
}

.markdown-preview :deep(table) {
  border-collapse: collapse;
  width: 100%;
  margin: var(--spacing-4) 0;
  border: 1px solid var(--border-light);
  border-radius: var(--radius-md);
  overflow: hidden;
}

.markdown-preview :deep(th),
.markdown-preview :deep(td) {
  border: 1px solid var(--border-light);
  padding: var(--spacing-3) var(--spacing-4);
  text-align: left;
}

.markdown-preview :deep(th) {
  background: var(--bg-tertiary);
  font-weight: var(--font-semibold);
  color: var(--text-primary);
}

.markdown-preview :deep(hr) {
  border: none;
  height: 2px;
  background: var(--border-medium);
  margin: var(--spacing-8) 0;
  border-radius: var(--radius-full);
}
</style>