# 团队在线知识库UI设计说明书 - v1.0

## 1. 引言

### 1.1 目的
本文档旨在为"团队在线知识库"项目提供完整的用户界面设计规范和指导方案，确保产品在视觉效果、交互体验和用户友好性方面达到现代化、专业化的标准。

### 1.2 设计原则
- **简洁优雅**：界面简洁明了，信息层次清晰，避免视觉噪音
- **直观易用**：符合用户认知习惯，操作流程自然流畅
- **一致性**：保持全局设计语言统一，提供可预测的用户体验
- **响应式**：适配不同屏幕尺寸，确保多设备体验一致
- **可访问性**：遵循WCAG 2.1 AA标准，支持无障碍访问

### 1.3 目标用户
- 知识工作者（内容创作者、编辑者）
- 团队协作人员（项目经理、团队成员）
- 决策制定者（管理层、领导者）
- IT管理员（系统管理员）

## 2. 设计系统 (Design System)

### 2.1 色彩系统 (Color Palette)

#### 2.1.1 主色调 (Primary Colors)
```css
/* 主品牌色 */
--primary-500: #2563eb;     /* 蓝色主色调 - 专业、信任 */
--primary-400: #3b82f6;     /* 蓝色浅色 */
--primary-600: #1d4ed8;     /* 蓝色深色 */
--primary-700: #1e40af;     /* 蓝色更深 */

/* 主色渐变 */
--primary-gradient: linear-gradient(135deg, #2563eb 0%, #1e40af 100%);
```

#### 2.1.2 辅助色调 (Secondary Colors)
```css
/* 成功色 */
--success-500: #10b981;     /* 绿色 - 成功、完成 */
--success-100: #d1fae5;     /* 绿色背景 */

/* 警告色 */
--warning-500: #f59e0b;     /* 橙色 - 警告、注意 */
--warning-100: #fef3c7;     /* 橙色背景 */

/* 错误色 */
--error-500: #ef4444;       /* 红色 - 错误、危险 */
--error-100: #fee2e2;       /* 红色背景 */

/* 信息色 */
--info-500: #06b6d4;        /* 青色 - 信息提示 */
--info-100: #cffafe;        /* 青色背景 */
```

#### 2.1.3 中性色调 (Neutral Colors)
```css
/* 文字颜色 */
--text-primary: #111827;     /* 主要文字 */
--text-secondary: #6b7280;   /* 次要文字 */
--text-tertiary: #9ca3af;    /* 辅助文字 */
--text-inverse: #ffffff;     /* 反色文字 */

/* 背景颜色 */
--bg-primary: #ffffff;       /* 主要背景 */
--bg-secondary: #f9fafb;     /* 次要背景 */
--bg-tertiary: #f3f4f6;      /* 第三级背景 */
--bg-dark: #1f2937;          /* 深色背景 */

/* 边框颜色 */
--border-light: #e5e7eb;     /* 浅色边框 */
--border-medium: #d1d5db;    /* 中等边框 */
--border-dark: #9ca3af;      /* 深色边框 */
```

### 2.2 字体系统 (Typography)

#### 2.2.1 字体族 (Font Family)
```css
/* 主字体 - 无衬线字体，现代简洁 */
--font-primary: 'Inter', 'SF Pro Display', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;

/* 代码字体 - 等宽字体，适合代码显示 */
--font-mono: 'JetBrains Mono', 'Fira Code', 'SF Mono', Consolas, 'Liberation Mono', Menlo, monospace;

/* 中文字体优化 */
--font-zh: 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', '微软雅黑', sans-serif;
```

#### 2.2.2 字体大小和行高 (Font Size & Line Height)
```css
/* 标题字体 */
--text-4xl: 2.25rem;    /* 36px - 页面主标题 */
--text-3xl: 1.875rem;   /* 30px - 区块标题 */
--text-2xl: 1.5rem;     /* 24px - 子标题 */
--text-xl: 1.25rem;     /* 20px - 重要文字 */
--text-lg: 1.125rem;    /* 18px - 强调文字 */

/* 正文字体 */
--text-base: 1rem;      /* 16px - 基础文字 */
--text-sm: 0.875rem;    /* 14px - 小号文字 */
--text-xs: 0.75rem;     /* 12px - 最小文字 */

/* 行高 */
--leading-tight: 1.25;   /* 紧凑行高 */
--leading-normal: 1.5;   /* 标准行高 */
--leading-relaxed: 1.625; /* 宽松行高 */
```

#### 2.2.3 字重 (Font Weight)
```css
--font-light: 300;      /* 细体 */
--font-normal: 400;     /* 正常 */
--font-medium: 500;     /* 中等 */
--font-semibold: 600;   /* 半粗体 */
--font-bold: 700;       /* 粗体 */
```

### 2.3 间距系统 (Spacing System)

#### 2.3.1 基础间距单位
```css
--spacing-1: 0.25rem;   /* 4px */
--spacing-2: 0.5rem;    /* 8px */
--spacing-3: 0.75rem;   /* 12px */
--spacing-4: 1rem;      /* 16px */
--spacing-5: 1.25rem;   /* 20px */
--spacing-6: 1.5rem;    /* 24px */
--spacing-8: 2rem;      /* 32px */
--spacing-10: 2.5rem;   /* 40px */
--spacing-12: 3rem;     /* 48px */
--spacing-16: 4rem;     /* 64px */
--spacing-20: 5rem;     /* 80px */
```

#### 2.3.2 布局间距规范
```css
/* 组件内间距 */
--component-padding-sm: var(--spacing-3);    /* 12px - 小组件内边距 */
--component-padding-md: var(--spacing-4);    /* 16px - 中等组件内边距 */
--component-padding-lg: var(--spacing-6);    /* 24px - 大组件内边距 */

/* 组件间间距 */
--component-margin-sm: var(--spacing-4);     /* 16px - 小组件间距 */
--component-margin-md: var(--spacing-6);     /* 24px - 中等组件间距 */
--component-margin-lg: var(--spacing-8);     /* 32px - 大组件间距 */

/* 布局区域间距 */
--layout-gap-sm: var(--spacing-6);           /* 24px - 小布局间距 */
--layout-gap-md: var(--spacing-8);           /* 32px - 中等布局间距 */
--layout-gap-lg: var(--spacing-12);          /* 48px - 大布局间距 */
```

### 2.4 圆角系统 (Border Radius)

```css
--radius-none: 0;           /* 无圆角 */
--radius-sm: 0.25rem;       /* 4px - 小圆角 */
--radius-md: 0.375rem;      /* 6px - 中等圆角 */
--radius-lg: 0.5rem;        /* 8px - 大圆角 */
--radius-xl: 0.75rem;       /* 12px - 超大圆角 */
--radius-2xl: 1rem;         /* 16px - 最大圆角 */
--radius-full: 9999px;      /* 完全圆形 */
```

### 2.5 阴影系统 (Shadow System)

```css
/* 卡片阴影 */
--shadow-sm: 0 1px 2px 0 rgba(0, 0, 0, 0.05);
--shadow-md: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
--shadow-lg: 0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05);
--shadow-xl: 0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04);

/* 特殊阴影 */
--shadow-inner: inset 0 2px 4px 0 rgba(0, 0, 0, 0.06);
--shadow-outline: 0 0 0 3px rgba(37, 99, 235, 0.1);
--shadow-focus: 0 0 0 3px rgba(37, 99, 235, 0.2);
```

### 2.6 动画系统 (Animation System)

#### 2.6.1 动画时长
```css
--duration-fast: 150ms;     /* 快速动画 */
--duration-normal: 250ms;   /* 标准动画 */
--duration-slow: 350ms;     /* 慢速动画 */
```

#### 2.6.2 缓动函数
```css
--ease-in: cubic-bezier(0.4, 0, 1, 1);
--ease-out: cubic-bezier(0, 0, 0.2, 1);
--ease-in-out: cubic-bezier(0.4, 0, 0.2, 1);
--ease-bounce: cubic-bezier(0.68, -0.55, 0.265, 1.55);
```

## 3. 组件设计规范

### 3.1 编辑器专用组件

#### 3.1.1 Markdown工具栏设计
```css
.editor-toolbar {
  background: var(--bg-primary);
  border-bottom: 1px solid var(--border-light);
  padding: var(--spacing-3) var(--spacing-4);
  display: flex;
  align-items: center;
  gap: var(--spacing-2);
  flex-wrap: wrap;
}

.toolbar-group {
  display: flex;
  align-items: center;
  gap: var(--spacing-1);
  padding-right: var(--spacing-3);
  border-right: 1px solid var(--border-light);
}

.toolbar-group:last-child {
  border-right: none;
}

.toolbar-button {
  width: 32px;
  height: 32px;
  background: none;
  border: none;
  border-radius: var(--radius-md);
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  color: var(--text-secondary);
  transition: all var(--duration-normal) var(--ease-out);
}

.toolbar-button:hover {
  background: var(--bg-secondary);
  color: var(--text-primary);
}

.toolbar-button.active {
  background: var(--primary-100);
  color: var(--primary-500);
}

.toolbar-separator {
  width: 1px;
  height: 20px;
  background: var(--border-light);
  margin: 0 var(--spacing-2);
}
```

#### 3.1.2 实时预览面板
```css
.preview-panel {
  background: var(--bg-secondary);
  border-left: 1px solid var(--border-light);
  overflow-y: auto;
  position: relative;
}

.preview-header {
  background: var(--bg-primary);
  padding: var(--spacing-3) var(--spacing-4);
  border-bottom: 1px solid var(--border-light);
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.preview-title {
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
  color: var(--text-secondary);
}

.preview-content {
  padding: var(--spacing-6);
  max-width: none;
}

.preview-loading {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  color: var(--text-tertiary);
}
```

#### 3.1.3 图片上传进度组件
```css
.upload-progress {
  position: fixed;
  bottom: var(--spacing-6);
  right: var(--spacing-6);
  background: var(--bg-primary);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-lg);
  padding: var(--spacing-4);
  min-width: 300px;
  z-index: 1000;
}

.upload-item {
  display: flex;
  align-items: center;
  gap: var(--spacing-3);
  margin-bottom: var(--spacing-3);
}

.upload-item:last-child {
  margin-bottom: 0;
}

.upload-icon {
  width: 40px;
  height: 40px;
  border-radius: var(--radius-md);
  background: var(--bg-tertiary);
  display: flex;
  align-items: center;
  justify-content: center;
}

.upload-info {
  flex: 1;
}

.upload-filename {
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
  color: var(--text-primary);
  margin-bottom: var(--spacing-1);
}

.upload-progress-bar {
  width: 100%;
  height: 4px;
  background: var(--bg-tertiary);
  border-radius: var(--radius-full);
  overflow: hidden;
}

.upload-progress-fill {
  height: 100%;
  background: var(--primary-500);
  transition: width var(--duration-normal) var(--ease-out);
}

.upload-status {
  font-size: var(--text-xs);
  color: var(--text-secondary);
}
```

#### 3.1.4 表格编辑器界面
```css
.table-editor-modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.table-editor-panel {
  background: var(--bg-primary);
  border-radius: var(--radius-xl);
  padding: var(--spacing-6);
  max-width: 800px;
  max-height: 600px;
  overflow: auto;
  box-shadow: var(--shadow-xl);
}

.table-editor-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: var(--spacing-6);
}

.table-editor-title {
  font-size: var(--text-lg);
  font-weight: var(--font-semibold);
  color: var(--text-primary);
}

.table-editor-grid {
  border: 1px solid var(--border-light);
  border-radius: var(--radius-md);
  overflow: hidden;
}

.table-editor-cell {
  border: 1px solid var(--border-light);
  padding: var(--spacing-2);
  min-width: 100px;
  background: var(--bg-primary);
  outline: none;
  font-size: var(--text-sm);
}

.table-editor-cell:focus {
  background: var(--primary-50);
  border-color: var(--primary-500);
}

.table-editor-controls {
  display: flex;
  gap: var(--spacing-3);
  margin-top: var(--spacing-4);
}
```

### 3.2 版本控制UI组件

#### 3.2.1 版本历史时间线
```css
.version-history {
  background: var(--bg-primary);
  border: 1px solid var(--border-light);
  border-radius: var(--radius-lg);
  overflow: hidden;
}

.version-timeline {
  position: relative;
  padding: var(--spacing-4);
}

.version-timeline::before {
  content: '';
  position: absolute;
  left: 20px;
  top: 0;
  bottom: 0;
  width: 2px;
  background: var(--border-light);
}

.version-item {
  position: relative;
  padding-left: var(--spacing-12);
  padding-bottom: var(--spacing-6);
}

.version-item:last-child {
  padding-bottom: 0;
}

.version-dot {
  position: absolute;
  left: 12px;
  top: 4px;
  width: 16px;
  height: 16px;
  border-radius: var(--radius-full);
  background: var(--primary-500);
  border: 3px solid var(--bg-primary);
  box-shadow: 0 0 0 1px var(--border-light);
}

.version-dot.current {
  background: var(--success-500);
}

.version-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: var(--spacing-2);
}

.version-info {
  display: flex;
  align-items: center;
  gap: var(--spacing-3);
}

.version-number {
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
  color: var(--text-primary);
}

.version-author {
  font-size: var(--text-sm);
  color: var(--text-secondary);
}

.version-date {
  font-size: var(--text-xs);
  color: var(--text-tertiary);
}

.version-actions {
  display: flex;
  gap: var(--spacing-2);
}
```

#### 3.2.2 差异对比视图
```css
.diff-viewer {
  background: var(--bg-primary);
  border: 1px solid var(--border-light);
  border-radius: var(--radius-lg);
  overflow: hidden;
}

.diff-header {
  background: var(--bg-secondary);
  padding: var(--spacing-4);
  border-bottom: 1px solid var(--border-light);
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.diff-versions {
  display: flex;
  align-items: center;
  gap: var(--spacing-4);
}

.diff-version-label {
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
  color: var(--text-primary);
}

.diff-content {
  display: grid;
  grid-template-columns: 1fr 1fr;
  min-height: 400px;
}

.diff-panel {
  overflow: auto;
  border-right: 1px solid var(--border-light);
}

.diff-panel:last-child {
  border-right: none;
}

.diff-line {
  padding: var(--spacing-1) var(--spacing-3);
  font-family: var(--font-mono);
  font-size: var(--text-sm);
  line-height: var(--leading-relaxed);
  white-space: pre-wrap;
  position: relative;
}

.diff-line-number {
  display: inline-block;
  width: 40px;
  color: var(--text-tertiary);
  user-select: none;
}

.diff-line.added {
  background: rgba(34, 197, 94, 0.1);
  border-left: 3px solid var(--success-500);
}

.diff-line.removed {
  background: rgba(239, 68, 68, 0.1);
  border-left: 3px solid var(--error-500);
}

.diff-line.unchanged {
  background: var(--bg-primary);
}
```

### 3.3 权限管理界面组件

#### 3.3.1 用户组管理面板
```css
.user-groups-panel {
  background: var(--bg-primary);
  border: 1px solid var(--border-light);
  border-radius: var(--radius-lg);
  overflow: hidden;
}

.groups-header {
  background: var(--bg-secondary);
  padding: var(--spacing-4);
  border-bottom: 1px solid var(--border-light);
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.groups-title {
  font-size: var(--text-lg);
  font-weight: var(--font-semibold);
  color: var(--text-primary);
}

.group-item {
  padding: var(--spacing-4);
  border-bottom: 1px solid var(--border-light);
  transition: background var(--duration-normal) var(--ease-out);
}

.group-item:hover {
  background: var(--bg-secondary);
}

.group-item:last-child {
  border-bottom: none;
}

.group-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: var(--spacing-3);
}

.group-info {
  display: flex;
  align-items: center;
  gap: var(--spacing-3);
}

.group-icon {
  width: 40px;
  height: 40px;
  border-radius: var(--radius-full);
  background: var(--primary-100);
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--primary-500);
}

.group-details h3 {
  font-size: var(--text-base);
  font-weight: var(--font-medium);
  color: var(--text-primary);
  margin-bottom: var(--spacing-1);
}

.group-meta {
  font-size: var(--text-sm);
  color: var(--text-secondary);
}

.group-members {
  display: flex;
  align-items: center;
  gap: var(--spacing-2);
}

.group-members-avatars {
  display: flex;
  margin-right: var(--spacing-2);
}

.group-member-avatar {
  width: 24px;
  height: 24px;
  border-radius: var(--radius-full);
  border: 2px solid var(--bg-primary);
  margin-left: -6px;
  background: var(--bg-tertiary);
}

.group-members-count {
  font-size: var(--text-sm);
  color: var(--text-secondary);
}
```

#### 3.3.2 权限设置矩阵界面
```css
.permissions-matrix {
  background: var(--bg-primary);
  border: 1px solid var(--border-light);
  border-radius: var(--radius-lg);
  overflow: hidden;
}

.matrix-header {
  background: var(--bg-secondary);
  padding: var(--spacing-4);
  border-bottom: 1px solid var(--border-light);
}

.matrix-title {
  font-size: var(--text-lg);
  font-weight: var(--font-semibold);
  color: var(--text-primary);
  margin-bottom: var(--spacing-2);
}

.matrix-subtitle {
  font-size: var(--text-sm);
  color: var(--text-secondary);
}

.matrix-table {
  width: 100%;
  border-collapse: collapse;
}

.matrix-table th {
  background: var(--bg-tertiary);
  padding: var(--spacing-3);
  text-align: left;
  font-size: var(--text-sm);
  font-weight: var(--font-semibold);
  color: var(--text-primary);
  border-bottom: 1px solid var(--border-light);
}

.matrix-table td {
  padding: var(--spacing-3);
  border-bottom: 1px solid var(--border-light);
  text-align: center;
}

.permission-cell {
  display: flex;
  align-items: center;
  justify-content: center;
}

.permission-checkbox {
  width: 18px;
  height: 18px;
  border: 2px solid var(--border-medium);
  border-radius: var(--radius-sm);
  cursor: pointer;
  transition: all var(--duration-normal) var(--ease-out);
}

.permission-checkbox.checked {
  background: var(--primary-500);
  border-color: var(--primary-500);
  position: relative;
}

.permission-checkbox.checked::after {
  content: '✓';
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  color: white;
  font-size: 12px;
  font-weight: bold;
}

.permission-role {
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
  color: var(--text-primary);
}

.permission-action {
  font-size: var(--text-sm);
  color: var(--text-secondary);
  text-align: left;
}
```

### 3.4 搜索组件设计

#### 3.4.1 高级搜索筛选器
```css
.search-filters {
  background: var(--bg-primary);
  border: 1px solid var(--border-light);
  border-radius: var(--radius-lg);
  padding: var(--spacing-6);
}

.filter-section {
  margin-bottom: var(--spacing-6);
}

.filter-section:last-child {
  margin-bottom: 0;
}

.filter-label {
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
  color: var(--text-primary);
  margin-bottom: var(--spacing-3);
  display: block;
}

.filter-tags {
  display: flex;
  flex-wrap: wrap;
  gap: var(--spacing-2);
}

.filter-tag {
  background: var(--bg-secondary);
  border: 1px solid var(--border-light);
  border-radius: var(--radius-full);
  padding: var(--spacing-1) var(--spacing-3);
  font-size: var(--text-sm);
  color: var(--text-secondary);
  cursor: pointer;
  transition: all var(--duration-normal) var(--ease-out);
}

.filter-tag:hover {
  border-color: var(--primary-500);
  color: var(--primary-500);
}

.filter-tag.active {
  background: var(--primary-500);
  border-color: var(--primary-500);
  color: white;
}

.date-range-inputs {
  display: grid;
  grid-template-columns: 1fr auto 1fr;
  gap: var(--spacing-3);
  align-items: center;
}

.date-separator {
  color: var(--text-tertiary);
  font-size: var(--text-sm);
}
```

#### 3.4.2 搜索结果高亮
```css
.search-results {
  margin-top: var(--spacing-6);
}

.search-result-item {
  background: var(--bg-primary);
  border: 1px solid var(--border-light);
  border-radius: var(--radius-lg);
  padding: var(--spacing-6);
  margin-bottom: var(--spacing-4);
  transition: all var(--duration-normal) var(--ease-out);
  cursor: pointer;
}

.search-result-item:hover {
  box-shadow: var(--shadow-md);
  border-color: var(--primary-200);
}

.result-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: var(--spacing-3);
}

.result-title {
  font-size: var(--text-lg);
  font-weight: var(--font-semibold);
  color: var(--text-primary);
  margin-bottom: var(--spacing-1);
}

.result-path {
  font-size: var(--text-sm);
  color: var(--text-tertiary);
  display: flex;
  align-items: center;
  gap: var(--spacing-1);
}

.result-snippet {
  font-size: var(--text-base);
  color: var(--text-secondary);
  line-height: var(--leading-relaxed);
  margin-bottom: var(--spacing-3);
}

.search-highlight {
  background: var(--warning-100);
  color: var(--warning-700);
  padding: 2px 4px;
  border-radius: var(--radius-sm);
  font-weight: var(--font-medium);
}

.result-meta {
  display: flex;
  align-items: center;
  gap: var(--spacing-4);
  font-size: var(--text-sm);
  color: var(--text-tertiary);
}

.result-tags {
  display: flex;
  gap: var(--spacing-2);
}

.result-tag {
  background: var(--bg-tertiary);
  color: var(--text-secondary);
  padding: var(--spacing-1) var(--spacing-2);
  border-radius: var(--radius-sm);
  font-size: var(--text-xs);
}
```

### 3.5 按钮组件 (Button Component)

#### 3.1.1 主要按钮 (Primary Button)
```css
.btn-primary {
  background: var(--primary-500);
  color: var(--text-inverse);
  border: none;
  border-radius: var(--radius-md);
  padding: var(--spacing-3) var(--spacing-6);
  font-size: var(--text-base);
  font-weight: var(--font-medium);
  cursor: pointer;
  transition: all var(--duration-normal) var(--ease-out);
  box-shadow: var(--shadow-sm);
}

.btn-primary:hover {
  background: var(--primary-600);
  box-shadow: var(--shadow-md);
  transform: translateY(-1px);
}

.btn-primary:active {
  background: var(--primary-700);
  transform: translateY(0);
  box-shadow: var(--shadow-sm);
}

.btn-primary:focus {
  outline: none;
  box-shadow: var(--shadow-focus);
}
```

#### 3.1.2 次要按钮 (Secondary Button)
```css
.btn-secondary {
  background: transparent;
  color: var(--primary-500);
  border: 1px solid var(--border-medium);
  border-radius: var(--radius-md);
  padding: var(--spacing-3) var(--spacing-6);
  font-size: var(--text-base);
  font-weight: var(--font-medium);
  cursor: pointer;
  transition: all var(--duration-normal) var(--ease-out);
}

.btn-secondary:hover {
  background: var(--bg-secondary);
  border-color: var(--primary-500);
}
```

#### 3.1.3 按钮尺寸变体
```css
/* 小按钮 */
.btn-sm {
  padding: var(--spacing-2) var(--spacing-4);
  font-size: var(--text-sm);
}

/* 大按钮 */
.btn-lg {
  padding: var(--spacing-4) var(--spacing-8);
  font-size: var(--text-lg);
}

/* 全宽按钮 */
.btn-full {
  width: 100%;
}
```

### 3.2 表单组件 (Form Components)

#### 3.2.1 输入框 (Input Field)
```css
.input-field {
  width: 100%;
  padding: var(--spacing-3) var(--spacing-4);
  border: 1px solid var(--border-light);
  border-radius: var(--radius-md);
  font-size: var(--text-base);
  font-family: var(--font-primary);
  background: var(--bg-primary);
  transition: all var(--duration-normal) var(--ease-out);
}

.input-field:focus {
  outline: none;
  border-color: var(--primary-500);
  box-shadow: var(--shadow-focus);
}

.input-field::placeholder {
  color: var(--text-tertiary);
}

/* 错误状态 */
.input-field.error {
  border-color: var(--error-500);
}

.input-field.error:focus {
  box-shadow: 0 0 0 3px rgba(239, 68, 68, 0.1);
}
```

#### 3.2.2 标签 (Label)
```css
.form-label {
  display: block;
  margin-bottom: var(--spacing-2);
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
  color: var(--text-primary);
}

.form-label.required::after {
  content: ' *';
  color: var(--error-500);
}
```

#### 3.2.3 错误提示 (Error Message)
```css
.form-error {
  margin-top: var(--spacing-1);
  font-size: var(--text-xs);
  color: var(--error-500);
  display: flex;
  align-items: center;
  gap: var(--spacing-1);
}
```

### 3.3 卡片组件 (Card Component)

```css
.card {
  background: var(--bg-primary);
  border: 1px solid var(--border-light);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-sm);
  transition: all var(--duration-normal) var(--ease-out);
  overflow: hidden;
}

.card:hover {
  box-shadow: var(--shadow-md);
  transform: translateY(-2px);
}

.card-header {
  padding: var(--spacing-6);
  border-bottom: 1px solid var(--border-light);
  background: var(--bg-secondary);
}

.card-body {
  padding: var(--spacing-6);
}

.card-footer {
  padding: var(--spacing-4) var(--spacing-6);
  border-top: 1px solid var(--border-light);
  background: var(--bg-secondary);
  display: flex;
  justify-content: flex-end;
  gap: var(--spacing-3);
}
```

### 3.4 导航组件 (Navigation Components)

#### 3.4.1 顶部导航栏
```css
.navbar {
  background: var(--bg-primary);
  border-bottom: 1px solid var(--border-light);
  height: 64px;
  display: flex;
  align-items: center;
  padding: 0 var(--spacing-6);
  position: sticky;
  top: 0;
  z-index: 100;
  backdrop-filter: blur(8px);
  background: rgba(255, 255, 255, 0.95);
}

.navbar-brand {
  font-size: var(--text-xl);
  font-weight: var(--font-bold);
  color: var(--primary-500);
  text-decoration: none;
  display: flex;
  align-items: center;
  gap: var(--spacing-2);
}

.navbar-nav {
  display: flex;
  align-items: center;
  gap: var(--spacing-6);
  margin-left: auto;
}

.navbar-link {
  color: var(--text-secondary);
  text-decoration: none;
  font-weight: var(--font-medium);
  transition: color var(--duration-normal) var(--ease-out);
}

.navbar-link:hover,
.navbar-link.active {
  color: var(--primary-500);
}
```

#### 3.4.2 侧边导航栏
```css
.sidebar {
  width: 280px;
  background: var(--bg-primary);
  border-right: 1px solid var(--border-light);
  height: 100vh;
  position: fixed;
  left: 0;
  top: 64px;
  overflow-y: auto;
  z-index: 50;
}

.sidebar-section {
  padding: var(--spacing-4);
  border-bottom: 1px solid var(--border-light);
}

.sidebar-title {
  font-size: var(--text-sm);
  font-weight: var(--font-semibold);
  color: var(--text-tertiary);
  text-transform: uppercase;
  letter-spacing: 0.5px;
  margin-bottom: var(--spacing-3);
}

.sidebar-link {
  display: flex;
  align-items: center;
  gap: var(--spacing-3);
  padding: var(--spacing-3) var(--spacing-4);
  color: var(--text-secondary);
  text-decoration: none;
  border-radius: var(--radius-md);
  margin-bottom: var(--spacing-1);
  transition: all var(--duration-normal) var(--ease-out);
}

.sidebar-link:hover {
  background: var(--bg-secondary);
  color: var(--text-primary);
}

.sidebar-link.active {
  background: var(--primary-500);
  color: var(--text-inverse);
}
```

### 3.5 模态框组件 (Modal Component)

```css
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  backdrop-filter: blur(4px);
}

.modal {
  background: var(--bg-primary);
  border-radius: var(--radius-xl);
  box-shadow: var(--shadow-xl);
  max-width: 500px;
  width: 90%;
  max-height: 90vh;
  overflow: hidden;
  transform: scale(0.95);
  animation: modalAppear var(--duration-normal) var(--ease-out) forwards;
}

@keyframes modalAppear {
  to {
    transform: scale(1);
  }
}

.modal-header {
  padding: var(--spacing-6);
  border-bottom: 1px solid var(--border-light);
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.modal-title {
  font-size: var(--text-xl);
  font-weight: var(--font-semibold);
  color: var(--text-primary);
}

.modal-close {
  background: none;
  border: none;
  font-size: var(--text-xl);
  color: var(--text-tertiary);
  cursor: pointer;
  padding: var(--spacing-2);
  border-radius: var(--radius-md);
  transition: all var(--duration-normal) var(--ease-out);
}

.modal-close:hover {
  background: var(--bg-secondary);
  color: var(--text-primary);
}

.modal-body {
  padding: var(--spacing-6);
}

.modal-footer {
  padding: var(--spacing-4) var(--spacing-6);
  border-top: 1px solid var(--border-light);
  display: flex;
  justify-content: flex-end;
  gap: var(--spacing-3);
}
```

### 3.6 通知系统UI组件

#### 3.6.1 @提及用户下拉选择器
```css
.mention-dropdown {
  position: absolute;
  background: var(--bg-primary);
  border: 1px solid var(--border-light);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-lg);
  max-height: 200px;
  overflow-y: auto;
  z-index: 1000;
  min-width: 250px;
}

.mention-item {
  padding: var(--spacing-3);
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: var(--spacing-3);
  transition: background var(--duration-normal) var(--ease-out);
}

.mention-item:hover,
.mention-item.highlighted {
  background: var(--bg-secondary);
}

.mention-avatar {
  width: 32px;
  height: 32px;
  border-radius: var(--radius-full);
  background: var(--bg-tertiary);
}

.mention-info {
  flex: 1;
}

.mention-name {
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
  color: var(--text-primary);
  margin-bottom: var(--spacing-1);
}

.mention-role {
  font-size: var(--text-xs);
  color: var(--text-tertiary);
}
```

#### 3.6.2 通知消息样式设计
```css
.notification-center {
  position: fixed;
  top: 64px;
  right: var(--spacing-4);
  width: 360px;
  max-height: 600px;
  background: var(--bg-primary);
  border: 1px solid var(--border-light);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-xl);
  z-index: 1000;
  overflow: hidden;
}

.notification-header {
  padding: var(--spacing-4);
  border-bottom: 1px solid var(--border-light);
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.notification-title {
  font-size: var(--text-lg);
  font-weight: var(--font-semibold);
  color: var(--text-primary);
}

.notification-list {
  max-height: 480px;
  overflow-y: auto;
}

.notification-item {
  padding: var(--spacing-4);
  border-bottom: 1px solid var(--border-light);
  cursor: pointer;
  transition: background var(--duration-normal) var(--ease-out);
  position: relative;
}

.notification-item:hover {
  background: var(--bg-secondary);
}

.notification-item.unread {
  background: var(--primary-50);
}

.notification-item.unread::before {
  content: '';
  position: absolute;
  left: var(--spacing-2);
  top: 50%;
  transform: translateY(-50%);
  width: 8px;
  height: 8px;
  background: var(--primary-500);
  border-radius: var(--radius-full);
}

.notification-content {
  margin-left: var(--spacing-4);
}

.notification-message {
  font-size: var(--text-sm);
  color: var(--text-primary);
  margin-bottom: var(--spacing-2);
  line-height: var(--leading-relaxed);
}

.notification-meta {
  display: flex;
  align-items: center;
  gap: var(--spacing-3);
  font-size: var(--text-xs);
  color: var(--text-tertiary);
}

.notification-time {
  font-size: var(--text-xs);
  color: var(--text-tertiary);
}

.notification-type-badge {
  padding: var(--spacing-1) var(--spacing-2);
  border-radius: var(--radius-sm);
  font-size: var(--text-xs);
  font-weight: var(--font-medium);
}

.notification-type-badge.mention {
  background: var(--warning-100);
  color: var(--warning-700);
}

.notification-type-badge.comment {
  background: var(--info-100);
  color: var(--info-700);
}

.notification-type-badge.review {
  background: var(--error-100);
  color: var(--error-700);
}
```

### 3.7 数据导入导出界面组件

#### 3.7.1 文件上传拖拽区域
```css
.upload-dropzone {
  border: 2px dashed var(--border-medium);
  border-radius: var(--radius-lg);
  padding: var(--spacing-12);
  text-align: center;
  background: var(--bg-secondary);
  cursor: pointer;
  transition: all var(--duration-normal) var(--ease-out);
}

.upload-dropzone:hover,
.upload-dropzone.dragover {
  border-color: var(--primary-500);
  background: var(--primary-50);
}

.upload-icon {
  width: 80px;
  height: 80px;
  margin: 0 auto var(--spacing-4);
  background: var(--bg-tertiary);
  border-radius: var(--radius-full);
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--text-tertiary);
  font-size: var(--text-2xl);
}

.upload-dropzone:hover .upload-icon,
.upload-dropzone.dragover .upload-icon {
  background: var(--primary-100);
  color: var(--primary-500);
}

.upload-text {
  font-size: var(--text-lg);
  font-weight: var(--font-medium);
  color: var(--text-primary);
  margin-bottom: var(--spacing-2);
}

.upload-hint {
  font-size: var(--text-sm);
  color: var(--text-secondary);
  margin-bottom: var(--spacing-4);
}

.upload-format-list {
  display: flex;
  justify-content: center;
  gap: var(--spacing-2);
  flex-wrap: wrap;
}

.format-tag {
  background: var(--bg-tertiary);
  color: var(--text-secondary);
  padding: var(--spacing-1) var(--spacing-2);
  border-radius: var(--radius-sm);
  font-size: var(--text-xs);
  font-weight: var(--font-medium);
}
```

#### 3.7.2 导入进度指示器
```css
.import-progress-modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.import-progress-panel {
  background: var(--bg-primary);
  border-radius: var(--radius-xl);
  padding: var(--spacing-8);
  min-width: 400px;
  box-shadow: var(--shadow-xl);
}

.import-progress-header {
  text-align: center;
  margin-bottom: var(--spacing-6);
}

.import-progress-title {
  font-size: var(--text-xl);
  font-weight: var(--font-semibold);
  color: var(--text-primary);
  margin-bottom: var(--spacing-2);
}

.import-progress-subtitle {
  font-size: var(--text-sm);
  color: var(--text-secondary);
}

.import-progress-bar {
  width: 100%;
  height: 8px;
  background: var(--bg-tertiary);
  border-radius: var(--radius-full);
  overflow: hidden;
  margin-bottom: var(--spacing-4);
}

.import-progress-fill {
  height: 100%;
  background: var(--primary-gradient);
  transition: width var(--duration-normal) var(--ease-out);
  position: relative;
}

.import-progress-fill::after {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(90deg, transparent, rgba(255,255,255,0.3), transparent);
  animation: shimmer 2s infinite;
}

@keyframes shimmer {
  0% { transform: translateX(-100%); }
  100% { transform: translateX(100%); }
}

.import-progress-stats {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  gap: var(--spacing-4);
  text-align: center;
}

.import-stat {
  padding: var(--spacing-3);
  background: var(--bg-secondary);
  border-radius: var(--radius-md);
}

.import-stat-number {
  font-size: var(--text-lg);
  font-weight: var(--font-bold);
  color: var(--primary-500);
  margin-bottom: var(--spacing-1);
}

.import-stat-label {
  font-size: var(--text-xs);
  color: var(--text-secondary);
}
```

#### 3.7.3 导出格式选择器
```css
.export-format-selector {
  background: var(--bg-primary);
  border: 1px solid var(--border-light);
  border-radius: var(--radius-lg);
  padding: var(--spacing-6);
}

.export-format-title {
  font-size: var(--text-lg);
  font-weight: var(--font-semibold);
  color: var(--text-primary);
  margin-bottom: var(--spacing-4);
}

.export-format-options {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: var(--spacing-4);
}

.export-format-option {
  border: 2px solid var(--border-light);
  border-radius: var(--radius-lg);
  padding: var(--spacing-4);
  cursor: pointer;
  transition: all var(--duration-normal) var(--ease-out);
  text-align: center;
}

.export-format-option:hover {
  border-color: var(--primary-300);
  background: var(--primary-50);
}

.export-format-option.selected {
  border-color: var(--primary-500);
  background: var(--primary-50);
}

.export-format-icon {
  width: 48px;
  height: 48px;
  margin: 0 auto var(--spacing-3);
  background: var(--bg-tertiary);
  border-radius: var(--radius-lg);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: var(--text-xl);
  color: var(--text-tertiary);
}

.export-format-option.selected .export-format-icon {
  background: var(--primary-100);
  color: var(--primary-500);
}

.export-format-name {
  font-size: var(--text-base);
  font-weight: var(--font-medium);
  color: var(--text-primary);
  margin-bottom: var(--spacing-1);
}

.export-format-description {
  font-size: var(--text-sm);
  color: var(--text-secondary);
  line-height: var(--leading-relaxed);
}
```

### 3.8 内容生命周期管理UI

#### 3.8.1 审查周期设置界面
```css
.review-settings-panel {
  background: var(--bg-primary);
  border: 1px solid var(--border-light);
  border-radius: var(--radius-lg);
  padding: var(--spacing-6);
}

.review-cycle-selector {
  margin-bottom: var(--spacing-6);
}

.cycle-options {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(120px, 1fr));
  gap: var(--spacing-3);
  margin-bottom: var(--spacing-4);
}

.cycle-option {
  padding: var(--spacing-3);
  border: 2px solid var(--border-light);
  border-radius: var(--radius-md);
  text-align: center;
  cursor: pointer;
  transition: all var(--duration-normal) var(--ease-out);
}

.cycle-option:hover {
  border-color: var(--primary-300);
}

.cycle-option.selected {
  border-color: var(--primary-500);
  background: var(--primary-50);
}

.cycle-period {
  font-size: var(--text-lg);
  font-weight: var(--font-bold);
  color: var(--text-primary);
  margin-bottom: var(--spacing-1);
}

.cycle-label {
  font-size: var(--text-sm);
  color: var(--text-secondary);
}

.reviewers-section {
  margin-bottom: var(--spacing-6);
}

.reviewers-title {
  font-size: var(--text-base);
  font-weight: var(--font-medium);
  color: var(--text-primary);
  margin-bottom: var(--spacing-3);
}

.reviewers-list {
  display: flex;
  flex-wrap: wrap;
  gap: var(--spacing-2);
  margin-bottom: var(--spacing-3);
}

.reviewer-tag {
  display: flex;
  align-items: center;
  gap: var(--spacing-2);
  background: var(--bg-secondary);
  border: 1px solid var(--border-light);
  border-radius: var(--radius-full);
  padding: var(--spacing-1) var(--spacing-3);
}

.reviewer-avatar {
  width: 20px;
  height: 20px;
  border-radius: var(--radius-full);
  background: var(--bg-tertiary);
}

.reviewer-name {
  font-size: var(--text-sm);
  color: var(--text-primary);
}

.remove-reviewer {
  background: none;
  border: none;
  color: var(--text-tertiary);
  cursor: pointer;
  padding: 0;
  margin-left: var(--spacing-1);
}

.remove-reviewer:hover {
  color: var(--error-500);
}
```

#### 3.8.2 内容归档状态标识
```css
.archive-status-badge {
  display: inline-flex;
  align-items: center;
  gap: var(--spacing-2);
  padding: var(--spacing-1) var(--spacing-3);
  border-radius: var(--radius-full);
  font-size: var(--text-xs);
  font-weight: var(--font-medium);
}

.archive-status-badge.active {
  background: var(--success-100);
  color: var(--success-700);
}

.archive-status-badge.archived {
  background: var(--warning-100);
  color: var(--warning-700);
}

.archive-status-badge.review-needed {
  background: var(--error-100);
  color: var(--error-700);
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.7; }
}

.status-icon {
  width: 12px;
  height: 12px;
  border-radius: var(--radius-full);
}

.archive-timeline {
  position: relative;
  padding-left: var(--spacing-8);
}

.archive-timeline::before {
  content: '';
  position: absolute;
  left: 12px;
  top: 0;
  bottom: 0;
  width: 2px;
  background: var(--border-light);
}

.timeline-item {
  position: relative;
  margin-bottom: var(--spacing-4);
}

.timeline-dot {
  position: absolute;
  left: -20px;
  top: 4px;
  width: 12px;
  height: 12px;
  border-radius: var(--radius-full);
  border: 3px solid var(--bg-primary);
  background: var(--primary-500);
}

.timeline-dot.completed {
  background: var(--success-500);
}

.timeline-dot.pending {
  background: var(--warning-500);
}

.timeline-content {
  background: var(--bg-secondary);
  padding: var(--spacing-3);
  border-radius: var(--radius-md);
}

.timeline-title {
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
  color: var(--text-primary);
  margin-bottom: var(--spacing-1);
}

.timeline-description {
  font-size: var(--text-xs);
  color: var(--text-secondary);
}
```

### 3.9 企业功能UI组件

#### 3.9.1 LDAP/AD集成配置界面
```css
.ldap-config-panel {
  background: var(--bg-primary);
  border: 1px solid var(--border-light);
  border-radius: var(--radius-lg);
  padding: var(--spacing-6);
}

.config-section {
  margin-bottom: var(--spacing-8);
  padding-bottom: var(--spacing-6);
  border-bottom: 1px solid var(--border-light);
}

.config-section:last-child {
  border-bottom: none;
  margin-bottom: 0;
}

.config-section-title {
  font-size: var(--text-lg);
  font-weight: var(--font-semibold);
  color: var(--text-primary);
  margin-bottom: var(--spacing-4);
  display: flex;
  align-items: center;
  gap: var(--spacing-2);
}

.config-section-icon {
  width: 24px;
  height: 24px;
  color: var(--primary-500);
}

.config-fields {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: var(--spacing-4);
}

.config-field {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-2);
}

.config-label {
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
  color: var(--text-primary);
}

.config-input {
  padding: var(--spacing-3);
  border: 1px solid var(--border-light);
  border-radius: var(--radius-md);
  font-size: var(--text-sm);
  transition: border-color var(--duration-normal) var(--ease-out);
}

.config-input:focus {
  outline: none;
  border-color: var(--primary-500);
}

.config-help {
  font-size: var(--text-xs);
  color: var(--text-tertiary);
  line-height: var(--leading-relaxed);
}

.test-connection-button {
  display: flex;
  align-items: center;
  gap: var(--spacing-2);
  padding: var(--spacing-3) var(--spacing-4);
  background: var(--info-500);
  color: white;
  border: none;
  border-radius: var(--radius-md);
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
  cursor: pointer;
  transition: background var(--duration-normal) var(--ease-out);
}

.test-connection-button:hover {
  background: var(--info-600);
}

.test-connection-button.testing {
  pointer-events: none;
  opacity: 0.7;
}

.test-result {
  margin-top: var(--spacing-3);
  padding: var(--spacing-3);
  border-radius: var(--radius-md);
  font-size: var(--text-sm);
}

.test-result.success {
  background: var(--success-100);
  color: var(--success-700);
  border: 1px solid var(--success-200);
}

.test-result.error {
  background: var(--error-100);
  color: var(--error-700);
  border: 1px solid var(--error-200);
}
```

#### 3.9.2 品牌化定制面板
```css
.branding-customizer {
  background: var(--bg-primary);
  border: 1px solid var(--border-light);
  border-radius: var(--radius-lg);
  overflow: hidden;
}

.branding-preview {
  background: var(--bg-secondary);
  padding: var(--spacing-6);
  border-bottom: 1px solid var(--border-light);
}

.preview-navbar {
  background: var(--bg-primary);
  padding: var(--spacing-3) var(--spacing-4);
  border-radius: var(--radius-md);
  display: flex;
  align-items: center;
  gap: var(--spacing-3);
  box-shadow: var(--shadow-sm);
}

.preview-logo {
  width: 32px;
  height: 32px;
  background: var(--primary-gradient);
  border-radius: var(--radius-md);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-weight: var(--font-bold);
}

.preview-brand-name {
  font-size: var(--text-lg);
  font-weight: var(--font-semibold);
  color: var(--text-primary);
}

.branding-controls {
  padding: var(--spacing-6);
}

.color-picker-section {
  margin-bottom: var(--spacing-6);
}

.color-picker-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: var(--spacing-4);
}

.color-picker-item {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-2);
}

.color-picker-label {
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
  color: var(--text-primary);
}

.color-picker-input {
  display: flex;
  align-items: center;
  gap: var(--spacing-2);
  padding: var(--spacing-2);
  border: 1px solid var(--border-light);
  border-radius: var(--radius-md);
}

.color-swatch {
  width: 32px;
  height: 32px;
  border-radius: var(--radius-sm);
  border: 1px solid var(--border-light);
  cursor: pointer;
}

.color-hex-input {
  flex: 1;
  border: none;
  outline: none;
  font-family: var(--font-mono);
  font-size: var(--text-sm);
}

.logo-upload-section {
  margin-bottom: var(--spacing-6);
}

.logo-upload-area {
  border: 2px dashed var(--border-medium);
  border-radius: var(--radius-lg);
  padding: var(--spacing-6);
  text-align: center;
  cursor: pointer;
  transition: all var(--duration-normal) var(--ease-out);
}

.logo-upload-area:hover {
  border-color: var(--primary-500);
  background: var(--primary-50);
}

.logo-preview {
  width: 64px;
  height: 64px;
  margin: 0 auto var(--spacing-3);
  border-radius: var(--radius-md);
  background: var(--bg-tertiary);
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}

.logo-preview img {
  width: 100%;
  height: 100%;
  object-fit: contain;
}
```

#### 3.9.3 系统分析图表组件
```css
.analytics-dashboard {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: var(--spacing-6);
}

.analytics-card {
  background: var(--bg-primary);
  border: 1px solid var(--border-light);
  border-radius: var(--radius-lg);
  padding: var(--spacing-6);
  box-shadow: var(--shadow-sm);
}

.analytics-card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: var(--spacing-4);
}

.analytics-card-title {
  font-size: var(--text-lg);
  font-weight: var(--font-semibold);
  color: var(--text-primary);
}

.analytics-card-period {
  font-size: var(--text-sm);
  color: var(--text-tertiary);
  background: var(--bg-secondary);
  padding: var(--spacing-1) var(--spacing-2);
  border-radius: var(--radius-sm);
}

.analytics-metric {
  display: flex;
  align-items: center;
  gap: var(--spacing-3);
  margin-bottom: var(--spacing-4);
}

.analytics-metric-icon {
  width: 48px;
  height: 48px;
  border-radius: var(--radius-lg);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: var(--text-xl);
}

.analytics-metric-icon.users {
  background: var(--primary-100);
  color: var(--primary-500);
}

.analytics-metric-icon.documents {
  background: var(--success-100);
  color: var(--success-500);
}

.analytics-metric-icon.activity {
  background: var(--warning-100);
  color: var(--warning-500);
}

.analytics-metric-value {
  flex: 1;
}

.metric-number {
  font-size: var(--text-2xl);
  font-weight: var(--font-bold);
  color: var(--text-primary);
  margin-bottom: var(--spacing-1);
}

.metric-label {
  font-size: var(--text-sm);
  color: var(--text-secondary);
}

.metric-change {
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
  display: flex;
  align-items: center;
  gap: var(--spacing-1);
}

.metric-change.positive {
  color: var(--success-500);
}

.metric-change.negative {
  color: var(--error-500);
}

.chart-container {
  height: 200px;
  position: relative;
  background: var(--bg-secondary);
  border-radius: var(--radius-md);
  overflow: hidden;
}

.chart-placeholder {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  text-align: center;
  color: var(--text-tertiary);
}

.chart-placeholder-icon {
  font-size: var(--text-3xl);
  margin-bottom: var(--spacing-2);
}

.chart-placeholder-text {
  font-size: var(--text-sm);
}
```

## 4. 页面清单与布局设计

### 4.1 页面架构总览

#### 4.1.1 页面分类
根据用户角色和功能需求，系统包含以下页面类别：

**认证相关页面**
- 登录页面
- 注册页面
- 忘记密码页面
- 重置密码页面

**工作台页面**
- 个人仪表盘
- 空间工作台
- 搜索结果页面

**空间管理页面**
- 空间列表页面
- 空间详情页面
- 空间设置页面
- 空间成员管理页面

**文档管理页面**
- 文档列表页面
- 文档查看页面
- 文档编辑页面
- 文档历史版本页面
- 文档分享页面

**系统管理页面**
- 系统设置页面
- 用户管理页面
- 系统监控页面
- 审计日志页面

**个人中心页面**
- 个人资料页面
- 个人设置页面
- 个人收藏页面
- 通知中心页面

#### 4.1.2 页面层级关系
```
知识库系统
├── 认证层
│   ├── 登录页面
│   ├── 注册页面
│   └── 密码重置页面
├── 应用主界面
│   ├── 个人仪表盘
│   ├── 空间管理
│   │   ├── 空间列表
│   │   ├── 空间详情
│   │   │   ├── 文档列表
│   │   │   ├── 文档查看
│   │   │   ├── 文档编辑
│   │   │   └── 文档历史
│   │   └── 空间设置
│   ├── 搜索结果
│   ├── 个人中心
│   └── 系统管理（管理员）
└── 辅助页面
    ├── 404页面
    ├── 500页面
    └── 维护页面
```

### 4.2 整体布局结构

#### 4.2.1 主布局架构
```
┌─────────────────────────────────────────────────────────┐
│                    顶部导航栏 (64px)                      │
├─────────┬───────────────────────────────────────────────┤
│         │                                               │
│         │                                               │
│ 侧边栏  │                 主内容区域                     │
│ 导航    │              (自适应高度)                      │
│(280px)  │                                               │
│         │                                               │
│         │                                               │
└─────────┴───────────────────────────────────────────────┘
```

#### 4.2.2 响应式断点
```css
/* 移动设备 */
@media (max-width: 768px) {
  .sidebar {
    transform: translateX(-100%);
    transition: transform var(--duration-normal) var(--ease-out);
  }

  .sidebar.mobile-open {
    transform: translateX(0);
  }

  .main-content {
    margin-left: 0;
  }
}

/* 平板设备 */
@media (min-width: 769px) and (max-width: 1024px) {
  .sidebar {
    width: 240px;
  }
}

/* 桌面设备 */
@media (min-width: 1025px) {
  .main-content {
    margin-left: 280px;
  }
}
```

### 4.3 认证页面布局设计

#### 4.3.1 登录页面布局

**页面结构**
```
┌─────────────────────────────────────────────────────────┐
│                                                         │
│                   [全屏渐变背景]                         │
│                                                         │
│         ┌─────────────────────────────────┐             │
│         │           登录卡片               │             │
│         │                                 │             │
│         │  [Logo]                         │             │
│         │  知识库系统                      │             │
│         │  欢迎回来                        │             │
│         │                                 │             │
│         │  [邮箱输入框]                    │             │
│         │  [密码输入框]                    │             │
│         │  [记住我] [忘记密码?]             │             │
│         │  [登录按钮]                      │             │
│         │                                 │             │
│         │  ─────── 或 ───────              │             │
│         │  [SSO登录按钮]                   │             │
│         │                                 │             │
│         │  还没有账号? [立即注册]           │             │
│         └─────────────────────────────────┘             │
│                                                         │
└─────────────────────────────────────────────────────────┘
```

**具体实现**
```css
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: var(--spacing-6);
}

.login-container {
  background: var(--bg-primary);
  border-radius: var(--radius-2xl);
  box-shadow: var(--shadow-xl);
  padding: var(--spacing-12);
  width: 100%;
  max-width: 400px;
}

.login-header {
  text-align: center;
  margin-bottom: var(--spacing-8);
}

.login-logo {
  width: 64px;
  height: 64px;
  margin: 0 auto var(--spacing-4);
  border-radius: var(--radius-xl);
  background: var(--primary-gradient);
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--text-inverse);
  font-size: var(--text-2xl);
  font-weight: var(--font-bold);
}

.login-title {
  font-size: var(--text-2xl);
  font-weight: var(--font-bold);
  color: var(--text-primary);
  margin-bottom: var(--spacing-2);
}

.login-subtitle {
  color: var(--text-secondary);
  font-size: var(--text-base);
}
```

#### 4.3.2 注册页面布局

**页面结构**
```
┌─────────────────────────────────────────────────────────┐
│                   [全屏渐变背景]                         │
│         ┌─────────────────────────────────┐             │
│         │           注册卡片               │             │
│         │                                 │             │
│         │  [Logo] 创建新账户               │             │
│         │                                 │             │
│         │  [姓名输入框]                    │             │
│         │  [邮箱输入框]                    │             │
│         │  [密码输入框]                    │             │
│         │  [确认密码输入框]                │             │
│         │  [同意条款复选框]                │             │
│         │  [注册按钮]                      │             │
│         │                                 │             │
│         │  已有账号? [立即登录]            │             │
│         └─────────────────────────────────┘             │
└─────────────────────────────────────────────────────────┘
```

### 4.4 工作台页面布局设计

#### 4.4.1 个人仪表盘布局

**页面结构**
```
┌─────────────────────────────────────────────────────────┐
│                    顶部导航栏                            │
├─────────┬───────────────────────────────────────────────┤
│         │  欢迎回来，张三                               │
│         │  ┌─────────────┐ ┌─────────────┐ ┌──────────┐  │
│         │  │  我的空间   │ │  最近文档   │ │  待办事项│  │
│侧边导航 │  │     12     │ │     25     │ │    3     │  │
│         │  └─────────────┘ └─────────────┘ └──────────┘  │
│         │                                               │
│         │  ┌─────────────────────────────────────────┐   │
│         │  │            最近访问的文档                │   │
│         │  │  ┌─────┐ API设计文档    2小时前          │   │
│         │  │  │[图标]│ 产品需求文档   昨天             │   │
│         │  │  └─────┘ 技术方案     3天前             │   │
│         │  └─────────────────────────────────────────┘   │
│         │                                               │
│         │  ┌─────────────────────────────────────────┐   │
│         │  │            我的收藏                      │   │
│         │  │  ┌─────┐ 代码规范     [⭐]               │   │
│         │  │  │[图标]│ 部署指南     [⭐]               │   │
│         │  │  └─────┘ 团队手册     [⭐]               │   │
│         │  └─────────────────────────────────────────┘   │
└─────────┴───────────────────────────────────────────────┘
```

**具体实现**
```css
.dashboard-page {
  padding: var(--spacing-8);
}

.dashboard-header {
  margin-bottom: var(--spacing-8);
}

.welcome-message {
  font-size: var(--text-3xl);
  font-weight: var(--font-bold);
  color: var(--text-primary);
  margin-bottom: var(--spacing-6);
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: var(--spacing-6);
  margin-bottom: var(--spacing-8);
}

.stat-card {
  background: var(--bg-primary);
  border: 1px solid var(--border-light);
  border-radius: var(--radius-xl);
  padding: var(--spacing-6);
  text-align: center;
  transition: all var(--duration-normal) var(--ease-out);
}

.stat-card:hover {
  transform: translateY(-2px);
  box-shadow: var(--shadow-md);
}

.stat-number {
  font-size: var(--text-4xl);
  font-weight: var(--font-bold);
  color: var(--primary-500);
  display: block;
}

.stat-label {
  font-size: var(--text-base);
  color: var(--text-secondary);
  margin-top: var(--spacing-2);
}
```

#### 4.4.2 搜索结果页面布局

**页面结构**
```
┌─────────────────────────────────────────────────────────┐
│                    顶部导航栏                            │
├─────────┬───────────────────────────────────────────────┤
│         │  ┌─────────────────────────────────────────┐   │
│         │  │     [搜索框] "API文档"     [搜索按钮]    │   │
│         │  └─────────────────────────────────────────┘   │
│         │                                               │
│筛选侧栏 │  找到 127 个结果，用时 0.08 秒                │
│         │                                               │
│空间筛选 │  ┌─────────────────────────────────────────┐   │
│类型筛选 │  │  📄 API设计规范文档                      │   │
│时间筛选 │  │     ...接口设计的核心原则和最佳实践...   │   │
│标签筛选 │  │     技术文档 > API设计   2天前 张三      │   │
│         │  └─────────────────────────────────────────┘   │
│         │                                               │
│         │  ┌─────────────────────────────────────────┐   │
│         │  │  📄 RESTful API开发指南                  │   │
│         │  │     ...详细介绍了RESTful API的设计...    │   │
│         │  │     开发指南 > 后端开发   1周前 李四      │   │
│         │  └─────────────────────────────────────────┘   │
│         │                                               │
│         │  [分页组件]                                   │
└─────────┴───────────────────────────────────────────────┘
```

### 4.5 空间管理页面布局设计

#### 4.5.1 空间列表页面布局

**页面结构**
```
┌─────────────────────────────────────────────────────────┐
│                    顶部导航栏                            │
├─────────┬───────────────────────────────────────────────┤
│         │  知识空间                    [+ 创建空间]      │
│         │                                               │
│         │  ┌─────────┐ ┌─────────┐ ┌─────────┐         │
│         │  │  [图标] │ │  [图标] │ │  [图标] │         │
│侧边导航 │  │  技术   │ │  产品   │ │  运营   │         │
│         │  │  文档   │ │  设计   │ │  手册   │         │
│         │  │  23文档 │ │  15文档 │ │  8文档  │         │
│         │  │  5成员  │ │  3成员  │ │  12成员 │         │
│         │  │ 2天前   │ │ 1周前   │ │ 3天前   │         │
│         │  └─────────┘ └─────────┘ └─────────┘         │
│         │                                               │
│         │  ┌─────────┐ ┌─────────┐ ┌─────────┐         │
│         │  │  [图标] │ │  [图标] │ │  [图标] │         │
│         │  │  客服   │ │  法务   │ │  财务   │         │
│         │  │  支持   │ │  条款   │ │  制度   │         │
│         │  │  12文档 │ │  6文档  │ │  4文档  │         │
│         │  │  8成员  │ │  2成员  │ │  3成员  │         │
│         │  │ 1天前   │ │ 2周前   │ │ 5天前   │         │
│         │  └─────────┘ └─────────┘ └─────────┘         │
└─────────┴───────────────────────────────────────────────┘
```

**具体实现**
```css
.spaces-page {
  padding: var(--spacing-8);
}

.spaces-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: var(--spacing-8);
}

.spaces-title {
  font-size: var(--text-3xl);
  font-weight: var(--font-bold);
  color: var(--text-primary);
}

.spaces-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: var(--spacing-6);
}

.space-card {
  background: var(--bg-primary);
  border: 1px solid var(--border-light);
  border-radius: var(--radius-xl);
  padding: var(--spacing-6);
  cursor: pointer;
  transition: all var(--duration-normal) var(--ease-out);
  position: relative;
  overflow: hidden;
}

.space-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
  background: var(--primary-gradient);
}

.space-card:hover {
  transform: translateY(-4px);
  box-shadow: var(--shadow-lg);
}

.space-card-header {
  display: flex;
  align-items: center;
  margin-bottom: var(--spacing-4);
}

.space-icon {
  width: 48px;
  height: 48px;
  border-radius: var(--radius-lg);
  background: var(--primary-gradient);
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--text-inverse);
  font-size: var(--text-xl);
  font-weight: var(--font-bold);
  margin-right: var(--spacing-4);
}

.space-info h3 {
  font-size: var(--text-lg);
  font-weight: var(--font-semibold);
  color: var(--text-primary);
  margin-bottom: var(--spacing-1);
}

.space-meta {
  font-size: var(--text-sm);
  color: var(--text-tertiary);
}

.space-description {
  color: var(--text-secondary);
  font-size: var(--text-base);
  line-height: var(--leading-relaxed);
  margin-bottom: var(--spacing-4);
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.space-stats {
  display: flex;
  gap: var(--spacing-4);
  font-size: var(--text-sm);
  color: var(--text-tertiary);
  margin-bottom: var(--spacing-4);
}

.space-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.space-members {
  display: flex;
  margin-left: -var(--spacing-1);
}

.member-avatar {
  width: 32px;
  height: 32px;
  border-radius: var(--radius-full);
  border: 2px solid var(--bg-primary);
  margin-left: -var(--spacing-1);
  background: var(--bg-tertiary);
}

.space-updated {
  font-size: var(--text-xs);
  color: var(--text-tertiary);
}
```

#### 4.5.2 空间详情页面布局

**页面结构**
```
┌─────────────────────────────────────────────────────────┐
│                    顶部导航栏                            │
├─────────┬───────────────────────────────────────────────┤
│         │  📚 技术文档空间              [设置] [成员]     │
│         │  详细的技术文档和开发指南                     │
│         │  ─────────────────────────────────────────    │
│         │                                               │
│文档树   │  ┌─────────────────────────────────────────┐   │
│导航     │  │            📝 快速操作                   │   │
│         │  │  [+ 新建文档] [📁 新建文件夹]             │   │
│📁 前端  │  │  [📤 导入文档] [🔍 搜索文档]             │   │
│  └📄API │  └─────────────────────────────────────────┘   │
│📁 后端  │                                               │
│  └📄架构│  ┌─────────────────────────────────────────┐   │
│📁 数据库│  │            📋 最近文档                   │   │
│  └📄设计│  │  ┌─────┐ API接口文档   2小时前           │   │
│📁 部署  │  │  │ 📄  │ 数据库设计    昨天              │   │
│  └📄配置│  │  └─────┘ 部署指南      3天前             │   │
│         │  └─────────────────────────────────────────┘   │
│         │                                               │
│         │  ┌─────────────────────────────────────────┐   │
│         │  │            👥 团队成员                   │   │
│         │  │  👤 张三 (管理员)  👤 李四 (编辑者)       │   │
│         │  │  👤 王五 (查看者)  👤 赵六 (编辑者)       │   │
│         │  └─────────────────────────────────────────┘   │
└─────────┴───────────────────────────────────────────────┘
```

### 4.6 文档管理页面布局设计

#### 4.6.1 文档编辑页面布局

**页面结构**
```
┌─────────────────────────────────────────────────────────┐
│                    顶部导航栏                            │
├─────────────────────────────────────────────────────────┤
│  [标题输入框]           [编辑|预览|分屏]    [保存] [更多] │
├─────────────────────────┬───────────────────┬───────────┤
│                         │                   │           │
│                         │                   │  📝 大纲  │
│                         │    📖 预览区      │  ──────── │
│         ⌨️ 编辑区       │                   │  # 介绍   │
│                         │   渲染后的        │  ## 功能  │
│   # API文档             │   Markdown        │  ## 使用  │
│   ## 接口列表           │   内容            │           │
│   ### 用户相关          │                   │  🏷️ 标签  │
│   - GET /users          │                   │  ──────── │
│   - POST /users         │                   │  [API]    │
│   ...                   │                   │  [文档]   │
│                         │                   │  [后端]   │
│                         │                   │           │
│                         │                   │  📎 附件  │
│                         │                   │  ──────── │
│                         │                   │  image.png│
│                         │                   │  spec.pdf │
└─────────────────────────┴───────────────────┴───────────┘
```

**具体实现**
```css
.editor-page {
  height: calc(100vh - 64px);
  display: flex;
  flex-direction: column;
}

.editor-toolbar {
  background: var(--bg-primary);
  border-bottom: 1px solid var(--border-light);
  padding: var(--spacing-3) var(--spacing-6);
  display: flex;
  align-items: center;
  justify-content: space-between;
  flex-shrink: 0;
}

.editor-title-input {
  flex: 1;
  background: none;
  border: none;
  font-size: var(--text-xl);
  font-weight: var(--font-semibold);
  color: var(--text-primary);
  margin-right: var(--spacing-4);
}

.editor-title-input::placeholder {
  color: var(--text-tertiary);
}

.editor-actions {
  display: flex;
  align-items: center;
  gap: var(--spacing-3);
}

.editor-mode-toggle {
  display: flex;
  background: var(--bg-secondary);
  border-radius: var(--radius-md);
  padding: var(--spacing-1);
}

.mode-button {
  padding: var(--spacing-2) var(--spacing-4);
  background: none;
  border: none;
  border-radius: var(--radius-sm);
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
  color: var(--text-secondary);
  cursor: pointer;
  transition: all var(--duration-normal) var(--ease-out);
}

.mode-button.active {
  background: var(--bg-primary);
  color: var(--primary-500);
  box-shadow: var(--shadow-sm);
}

.editor-content {
  flex: 1;
  display: flex;
  min-height: 0;
}

.editor-panel {
  flex: 1;
  position: relative;
}

.markdown-editor {
  width: 100%;
  height: 100%;
  border: none;
  outline: none;
  resize: none;
  padding: var(--spacing-6);
  font-family: var(--font-mono);
  font-size: var(--text-base);
  line-height: var(--leading-relaxed);
  background: var(--bg-primary);
  color: var(--text-primary);
}

.preview-panel {
  flex: 1;
  border-left: 1px solid var(--border-light);
  overflow-y: auto;
  padding: var(--spacing-6);
  background: var(--bg-secondary);
}

.editor-sidebar {
  width: 280px;
  border-left: 1px solid var(--border-light);
  background: var(--bg-primary);
  flex-shrink: 0;
}

.sidebar-section {
  padding: var(--spacing-4);
  border-bottom: 1px solid var(--border-light);
}

.sidebar-section-title {
  font-size: var(--text-sm);
  font-weight: var(--font-semibold);
  color: var(--text-primary);
  margin-bottom: var(--spacing-3);
}
```

#### 4.6.2 文档查看页面布局

**页面结构**
```
┌─────────────────────────────────────────────────────────┐
│                    顶部导航栏                            │
├─────────┬───────────────────────────────────────────────┤
│         │  首页 > 技术文档 > 后端开发 > API设计          │
│         │                                               │
│         │  # API接口设计文档                           │
│         │  👤 张三  📅 2024-01-15  👁️ 128次浏览         │
│         │  🏷️ [API] [后端] [文档]          [编辑] [分享] │
│文档树   │  ─────────────────────────────────────────    │
│导航     │                                               │
│         │  ## 1. 接口概述                               │
│📁 前端  │  本文档详细描述了系统的API接口设计...          │
│📁 后端  │                                               │
│📁 数据库│  ## 2. 认证接口                               │
│📁 部署  │  ### 2.1 用户登录                             │
│         │  **POST** `/api/v1/auth/login`                │
│         │                                               │
│         │  ```json                                      │
│         │  {                                            │
│         │    "email": "user@example.com",               │
│         │    "password": "password123"                  │
│         │  }                                            │
│         │  ```                                          │
│         │                                               │
│         │  ### 2.2 用户注册                             │
│         │  **POST** `/api/v1/auth/register`             │
│         │  ...                                          │
└─────────┴───────────────────────────────────────────────┘
```

### 4.7 系统管理页面布局设计

#### 4.7.1 用户管理页面布局

**页面结构**
```
┌─────────────────────────────────────────────────────────┐
│                    顶部导航栏                            │
├─────────┬───────────────────────────────────────────────┤
│         │  用户管理                      [+ 添加用户]   │
│         │                                               │
│管理     │  ┌─────────────────────────────────────────┐   │
│菜单     │  │ [搜索框]        [状态筛选] [角色筛选]    │   │
│         │  └─────────────────────────────────────────┘   │
│📊 仪表盘│                                               │
│👥 用户  │  ┌─────────────────────────────────────────┐   │
│🏢 空间  │  │ 头像 | 姓名    | 邮箱        | 角色 |操作│   │
│📋 日志  │  │ ──── | ────── | ────────── | ──── |────│   │
│⚙️ 设置  │  │ [👤] | 张三    | zhang@xx   | 管理 |[编辑]   │
│         │  │ [👤] | 李四    | li@xx      | 编辑 |[编辑]   │
│         │  │ [👤] | 王五    | wang@xx    | 查看 |[编辑]   │
│         │  │ [👤] | 赵六    | zhao@xx    | 编辑 |[编辑]   │
│         │  └─────────────────────────────────────────┘   │
│         │                                               │
│         │              [分页组件]                       │
└─────────┴───────────────────────────────────────────────┘
```

#### 4.7.2 系统设置页面布局

**页面结构**
```
┌─────────────────────────────────────────────────────────┐
│                    顶部导航栏                            │
├─────────┬───────────────────────────────────────────────┤
│         │  系统设置                                     │
│         │                                               │
│管理     │  ┌─────────────────────────────────────────┐   │
│菜单     │  │            🏢 基础设置                   │   │
│         │  │  系统名称: [知识库系统]                  │   │
│📊 仪表盘│  │  Logo上传: [选择文件]                    │   │
│👥 用户  │  │  主题色彩: [#2563eb] [颜色选择器]        │   │
│🏢 空间  │  └─────────────────────────────────────────┘   │
│📋 日志  │                                               │
│⚙️ 设置  │  ┌─────────────────────────────────────────┐   │
│  ├基础  │  │            📧 邮件设置                   │   │
│  ├邮件  │  │  SMTP服务器: [smtp.gmail.com]            │   │
│  ├存储  │  │  端口: [587]                             │   │
│  └安全  │  │  用户名: [admin@example.com]             │   │
│         │  │  密码: [••••••••]                        │   │
│         │  │  [测试连接]                              │   │
│         │  └─────────────────────────────────────────┘   │
│         │                                               │
│         │  ┌─────────────────────────────────────────┐   │
│         │  │            💾 存储设置                   │   │
│         │  │  文件存储路径: [/data/uploads]           │   │
│         │  │  最大文件大小: [100MB]                   │   │
│         │  │  允许的文件类型: [图片,文档,压缩包]      │   │
│         │  └─────────────────────────────────────────┘   │
└─────────┴───────────────────────────────────────────────┘
```

### 4.8 个人中心页面布局设计

#### 4.8.1 个人资料页面布局

**页面结构**
```
┌─────────────────────────────────────────────────────────┐
│                    顶部导航栏                            │
├─────────┬───────────────────────────────────────────────┤
│         │  个人中心                                     │
│         │                                               │
│个人     │  ┌─────────────────────────────────────────┐   │
│菜单     │  │            👤 基本信息                   │   │
│         │  │  ┌─────┐                                │   │
│📝 资料  │  │  │头像 │  姓名: [张三]                   │   │
│⚙️ 设置  │  │  │照片 │  邮箱: [zhang@example.com]      │   │
│⭐ 收藏  │  │  └─────┘  电话: [138****1234]           │   │
│🔔 通知  │  │           部门: [技术部]                 │   │
│📊 统计  │  │           职位: [高级工程师]             │   │
│         │  │           [上传头像] [保存修改]          │   │
│         │  └─────────────────────────────────────────┘   │
│         │                                               │
│         │  ┌─────────────────────────────────────────┐   │
│         │  │            🔐 安全设置                   │   │
│         │  │  修改密码: [当前密码] [新密码] [确认]    │   │
│         │  │  两步验证: [启用] [已关闭]               │   │
│         │  │  登录历史: [查看详情]                    │   │
│         │  └─────────────────────────────────────────┘   │
│         │                                               │
│         │  ┌─────────────────────────────────────────┐   │
│         │  │            🎨 偏好设置                   │   │
│         │  │  主题模式: (○) 浅色 (○) 深色 (●) 自动  │   │
│         │  │  语言设置: [中文 ▼]                      │   │
│         │  │  时区设置: [UTC+8 ▼]                     │   │
│         │  └─────────────────────────────────────────┘   │
└─────────┴───────────────────────────────────────────────┘
```

### 4.9 移动端布局适配

#### 4.9.1 移动端主布局
```
┌─────────────────────┐
│    顶部导航栏       │
│ [☰] 知识库 [🔍][👤]│
├─────────────────────┤
│                     │
│                     │
│    主内容区域       │
│   (全屏显示)        │
│                     │
│                     │
│                     │
├─────────────────────┤
│ [🏠][📚][🔍][⭐][👤]│
│    底部导航栏       │
└─────────────────────┘
```

#### 4.9.2 移动端侧边栏（抽屉式）
```
┌─────────────────────┐
│ [×] 导航菜单        │
├─────────────────────┤
│ 👤 张三             │
│    技术部·高级工程师 │
├─────────────────────┤
│ 🏠 个人仪表盘       │
│ 📚 我的空间         │
│ ⭐ 我的收藏         │
│ 🔍 搜索文档         │
│ 🔔 通知中心         │
│ ⚙️ 个人设置         │
├─────────────────────┤
│ 📱 下载App         │
│ 🌙 深色模式         │
│ 🚪 退出登录         │
└─────────────────────┘
```

### 4.10 页面状态设计

#### 4.10.1 空状态页面
```css
.empty-state {
  text-align: center;
  padding: var(--spacing-16) var(--spacing-8);
}

.empty-icon {
  width: 120px;
  height: 120px;
  margin: 0 auto var(--spacing-6);
  opacity: 0.5;
}

.empty-title {
  font-size: var(--text-xl);
  font-weight: var(--font-semibold);
  color: var(--text-primary);
  margin-bottom: var(--spacing-3);
}

.empty-description {
  color: var(--text-secondary);
  font-size: var(--text-base);
  margin-bottom: var(--spacing-6);
  max-width: 400px;
  margin-left: auto;
  margin-right: auto;
}
```

#### 4.10.2 错误页面 (404/500)
```css
.error-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  text-align: center;
  padding: var(--spacing-8);
}

.error-code {
  font-size: 6rem;
  font-weight: var(--font-bold);
  color: var(--primary-500);
  line-height: 1;
  margin-bottom: var(--spacing-4);
}

.error-title {
  font-size: var(--text-2xl);
  font-weight: var(--font-semibold);
  color: var(--text-primary);
  margin-bottom: var(--spacing-3);
}

.error-description {
  color: var(--text-secondary);
  font-size: var(--text-base);
  margin-bottom: var(--spacing-6);
  max-width: 500px;
}
```

### 4.11 页面清单总结

#### 4.11.1 完整页面清单

**认证相关页面 (4个)**
1. **登录页面** (`/login`)
   - 用途：用户身份验证
   - 布局：居中卡片设计，渐变背景
   - 功能：邮箱登录、SSO登录、记住密码

2. **注册页面** (`/register`)
   - 用途：新用户注册
   - 布局：居中表单，简洁设计
   - 功能：基本信息填写、条款同意

3. **忘记密码页面** (`/forgot-password`)
   - 用途：密码重置请求
   - 布局：简单表单设计
   - 功能：邮箱验证、发送重置链接

4. **重置密码页面** (`/reset-password`)
   - 用途：设置新密码
   - 布局：安全表单设计
   - 功能：新密码设置、强度检测

**主应用页面 (15个)**
5. **个人仪表盘** (`/dashboard`)
   - 用途：用户个人工作台
   - 布局：卡片式统计 + 最近访问列表
   - 功能：数据概览、快速访问、收藏管理

6. **空间列表页面** (`/spaces`)
   - 用途：展示所有知识空间
   - 布局：响应式网格布局
   - 功能：空间浏览、创建、搜索

7. **空间详情页面** (`/spaces/:id`)
   - 用途：空间内容管理
   - 布局：侧边文档树 + 主内容区
   - 功能：文档管理、成员协作、快速操作

8. **文档查看页面** (`/documents/:id`)
   - 用途：文档阅读
   - 布局：侧边导航 + 主阅读区
   - 功能：Markdown渲染、目录导航、评论交互

9. **文档编辑页面** (`/documents/:id/edit`)
   - 用途：文档创建/编辑
   - 布局：工具栏 + 编辑区 + 预览区 + 侧边栏
   - 功能：Markdown编辑、实时预览、大纲生成

10. **文档历史页面** (`/documents/:id/history`)
    - 用途：版本管理
    - 布局：时间线 + 对比视图
    - 功能：版本浏览、差异对比、恢复操作

11. **搜索结果页面** (`/search`)
    - 用途：全局搜索
    - 布局：筛选侧栏 + 结果列表
    - 功能：多维度筛选、高亮显示、排序

12. **个人资料页面** (`/profile`)
    - 用途：个人信息管理
    - 布局：侧边菜单 + 表单区域
    - 功能：信息编辑、头像上传、安全设置

13. **个人设置页面** (`/settings`)
    - 用途：个人偏好配置
    - 布局：分类设置面板
    - 功能：主题切换、通知配置、隐私设置

14. **个人收藏页面** (`/favorites`)
    - 用途：收藏内容管理
    - 布局：分类标签 + 卡片列表
    - 功能：收藏浏览、分类管理、快速访问

15. **通知中心页面** (`/notifications`)
    - 用途：消息通知管理
    - 布局：消息列表 + 筛选器
    - 功能：消息阅读、标记、删除

16. **空间设置页面** (`/spaces/:id/settings`)
    - 用途：空间配置管理
    - 布局：分组设置面板
    - 功能：基本信息、权限配置、成员管理

17. **空间成员页面** (`/spaces/:id/members`)
    - 用途：成员权限管理
    - 布局：成员列表 + 操作面板
    - 功能：成员添加、角色分配、权限管理

18. **文档分享页面** (`/shared/:shareCode`)
    - 用途：公开文档访问
    - 布局：简化导航 + 文档内容
    - 功能：无需登录访问、访问统计

19. **文档模板页面** (`/templates`)
    - 用途：模板管理
    - 布局：模板库 + 预览区
    - 功能：模板选择、预览、应用

**管理员页面 (5个)**
20. **系统仪表盘** (`/admin/dashboard`)
    - 用途：系统状态监控
    - 布局：统计卡片 + 图表面板
    - 功能：数据统计、系统监控、快速操作

21. **用户管理页面** (`/admin/users`)
    - 用途：用户账户管理
    - 布局：数据表格 + 操作面板
    - 功能：用户CRUD、角色分配、状态管理

22. **空间管理页面** (`/admin/spaces`)
    - 用途：全局空间管理
    - 布局：表格视图 + 详情面板
    - 功能：空间监控、权限审核、数据备份

23. **系统设置页面** (`/admin/settings`)
    - 用途：系统配置管理
    - 布局：分类配置面板
    - 功能：基础设置、邮件配置、存储配置

24. **审计日志页面** (`/admin/logs`)
    - 用途：系统日志查看
    - 布局：时间线 + 筛选器
    - 功能：日志查询、导出、分析

**辅助页面 (3个)**
25. **404错误页面** (`/404`)
    - 用途：页面未找到提示
    - 布局：居中错误信息
    - 功能：错误提示、导航引导

26. **500错误页面** (`/500`)
    - 用途：服务器错误提示
    - 布局：居中错误信息
    - 功能：错误报告、联系支持

27. **系统维护页面** (`/maintenance`)
    - 用途：维护期间提示
    - 布局：居中维护信息
    - 功能：维护通知、预计恢复时间

#### 4.11.2 页面布局特点总结

**布局模式**
- **全屏布局**：登录、注册、错误页面
- **侧边栏布局**：主应用页面（280px固定宽度）
- **双栏布局**：文档编辑、搜索结果
- **三栏布局**：文档编辑（编辑+预览+侧栏）
- **卡片布局**：仪表盘、空间列表
- **表格布局**：用户管理、日志查看

**响应式断点**
- **桌面端** (≥1025px): 完整布局，所有功能可见
- **平板端** (769-1024px): 侧栏收窄，保持主要功能
- **移动端** (≤768px): 折叠侧栏，单栏布局，底部导航

**导航结构**
- **顶部导航**：品牌标识、搜索、用户菜单
- **侧边导航**：主要功能模块、空间列表
- **面包屑**：当前位置导航
- **底部导航**（移动端）：核心功能快速访问

**交互模式**
- **点击交互**：主要操作、导航跳转
- **悬停效果**：卡片提升、按钮反馈
- **拖拽操作**：文件上传、内容重排
- **键盘快捷键**：编辑器、搜索快捷操作

这个完整的页面清单涵盖了知识管理系统的所有核心功能，每个页面都有明确的用途、精心设计的布局和完整的功能规划，确保用户能够高效地进行知识管理和团队协作。
│                    顶部导航栏 (64px)                      │
├─────────────────────────────────────────────────────────┤
│     │                                                   │
│     │                                                   │
│ 侧  │                 主内容区域                         │
│ 边  │              (自适应高度)                          │
│ 栏  │                                                   │
│(280)│                                                   │
│     │                                                   │
└─────┴───────────────────────────────────────────────────┘
```

#### 4.1.2 响应式断点
```css
/* 移动设备 */
@media (max-width: 768px) {
  .sidebar {
    transform: translateX(-100%);
    transition: transform var(--duration-normal) var(--ease-out);
  }

  .sidebar.mobile-open {
    transform: translateX(0);
  }

  .main-content {
    margin-left: 0;
  }
}

/* 平板设备 */
@media (min-width: 769px) and (max-width: 1024px) {
  .sidebar {
    width: 240px;
  }
}

/* 桌面设备 */
@media (min-width: 1025px) {
  .main-content {
    margin-left: 280px;
  }
}
```

### 4.2 关键页面布局

#### 4.2.1 登录页面布局

```css
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: var(--spacing-6);
}

.login-container {
  background: var(--bg-primary);
  border-radius: var(--radius-2xl);
  box-shadow: var(--shadow-xl);
  padding: var(--spacing-12);
  width: 100%;
  max-width: 400px;
}

.login-header {
  text-align: center;
  margin-bottom: var(--spacing-8);
}

.login-logo {
  width: 64px;
  height: 64px;
  margin: 0 auto var(--spacing-4);
  border-radius: var(--radius-xl);
  background: var(--primary-gradient);
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--text-inverse);
  font-size: var(--text-2xl);
  font-weight: var(--font-bold);
}

.login-title {
  font-size: var(--text-2xl);
  font-weight: var(--font-bold);
  color: var(--text-primary);
  margin-bottom: var(--spacing-2);
}

.login-subtitle {
  color: var(--text-secondary);
  font-size: var(--text-base);
}
```

#### 4.2.2 知识空间列表页面

```css
.spaces-page {
  padding: var(--spacing-8);
}

.spaces-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: var(--spacing-8);
}

.spaces-title {
  font-size: var(--text-3xl);
  font-weight: var(--font-bold);
  color: var(--text-primary);
}

.spaces-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: var(--spacing-6);
}

.space-card {
  background: var(--bg-primary);
  border: 1px solid var(--border-light);
  border-radius: var(--radius-xl);
  padding: var(--spacing-6);
  cursor: pointer;
  transition: all var(--duration-normal) var(--ease-out);
  position: relative;
  overflow: hidden;
}

.space-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
  background: var(--primary-gradient);
}

.space-card:hover {
  transform: translateY(-4px);
  box-shadow: var(--shadow-lg);
}

.space-card-header {
  display: flex;
  align-items: center;
  margin-bottom: var(--spacing-4);
}

.space-icon {
  width: 48px;
  height: 48px;
  border-radius: var(--radius-lg);
  background: var(--primary-gradient);
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--text-inverse);
  font-size: var(--text-xl);
  font-weight: var(--font-bold);
  margin-right: var(--spacing-4);
}

.space-info h3 {
  font-size: var(--text-lg);
  font-weight: var(--font-semibold);
  color: var(--text-primary);
  margin-bottom: var(--spacing-1);
}

.space-meta {
  font-size: var(--text-sm);
  color: var(--text-tertiary);
}

.space-description {
  color: var(--text-secondary);
  font-size: var(--text-base);
  line-height: var(--leading-relaxed);
  margin-bottom: var(--spacing-4);
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.space-stats {
  display: flex;
  gap: var(--spacing-4);
  font-size: var(--text-sm);
  color: var(--text-tertiary);
  margin-bottom: var(--spacing-4);
}

.space-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.space-members {
  display: flex;
  margin-left: -var(--spacing-1);
}

.member-avatar {
  width: 32px;
  height: 32px;
  border-radius: var(--radius-full);
  border: 2px solid var(--bg-primary);
  margin-left: -var(--spacing-1);
  background: var(--bg-tertiary);
}

.space-updated {
  font-size: var(--text-xs);
  color: var(--text-tertiary);
}
```

#### 4.2.3 文档编辑页面

```css
.editor-page {
  height: calc(100vh - 64px);
  display: flex;
  flex-direction: column;
}

.editor-toolbar {
  background: var(--bg-primary);
  border-bottom: 1px solid var(--border-light);
  padding: var(--spacing-3) var(--spacing-6);
  display: flex;
  align-items: center;
  justify-content: space-between;
  flex-shrink: 0;
}

.editor-title-input {
  flex: 1;
  background: none;
  border: none;
  font-size: var(--text-xl);
  font-weight: var(--font-semibold);
  color: var(--text-primary);
  margin-right: var(--spacing-4);
}

.editor-title-input::placeholder {
  color: var(--text-tertiary);
}

.editor-actions {
  display: flex;
  align-items: center;
  gap: var(--spacing-3);
}

.editor-mode-toggle {
  display: flex;
  background: var(--bg-secondary);
  border-radius: var(--radius-md);
  padding: var(--spacing-1);
}

.mode-button {
  padding: var(--spacing-2) var(--spacing-4);
  background: none;
  border: none;
  border-radius: var(--radius-sm);
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
  color: var(--text-secondary);
  cursor: pointer;
  transition: all var(--duration-normal) var(--ease-out);
}

.mode-button.active {
  background: var(--bg-primary);
  color: var(--primary-500);
  box-shadow: var(--shadow-sm);
}

.editor-content {
  flex: 1;
  display: flex;
  min-height: 0;
}

.editor-panel {
  flex: 1;
  position: relative;
}

.markdown-editor {
  width: 100%;
  height: 100%;
  border: none;
  outline: none;
  resize: none;
  padding: var(--spacing-6);
  font-family: var(--font-mono);
  font-size: var(--text-base);
  line-height: var(--leading-relaxed);
  background: var(--bg-primary);
  color: var(--text-primary);
}

.preview-panel {
  flex: 1;
  border-left: 1px solid var(--border-light);
  overflow-y: auto;
  padding: var(--spacing-6);
  background: var(--bg-secondary);
}

.editor-sidebar {
  width: 280px;
  border-left: 1px solid var(--border-light);
  background: var(--bg-primary);
  flex-shrink: 0;
}

.sidebar-section {
  padding: var(--spacing-4);
  border-bottom: 1px solid var(--border-light);
}

.sidebar-section-title {
  font-size: var(--text-sm);
  font-weight: var(--font-semibold);
  color: var(--text-primary);
  margin-bottom: var(--spacing-3);
}
```

#### 4.2.4 文档查看页面

```css
.document-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: var(--spacing-8);
}

.document-header {
  margin-bottom: var(--spacing-8);
  padding-bottom: var(--spacing-6);
  border-bottom: 1px solid var(--border-light);
}

.document-breadcrumb {
  display: flex;
  align-items: center;
  gap: var(--spacing-2);
  margin-bottom: var(--spacing-4);
  font-size: var(--text-sm);
  color: var(--text-secondary);
}

.breadcrumb-separator {
  color: var(--text-tertiary);
}

.document-title {
  font-size: var(--text-4xl);
  font-weight: var(--font-bold);
  color: var(--text-primary);
  margin-bottom: var(--spacing-4);
  line-height: var(--leading-tight);
}

.document-meta {
  display: flex;
  align-items: center;
  gap: var(--spacing-6);
  font-size: var(--text-sm);
  color: var(--text-secondary);
}

.document-author {
  display: flex;
  align-items: center;
  gap: var(--spacing-2);
}

.author-avatar {
  width: 24px;
  height: 24px;
  border-radius: var(--radius-full);
  background: var(--bg-tertiary);
}

.document-content {
  line-height: var(--leading-relaxed);
  color: var(--text-primary);
}

/* Markdown 样式 */
.document-content h1,
.document-content h2,
.document-content h3,
.document-content h4,
.document-content h5,
.document-content h6 {
  font-weight: var(--font-semibold);
  color: var(--text-primary);
  margin-top: var(--spacing-8);
  margin-bottom: var(--spacing-4);
  line-height: var(--leading-tight);
}

.document-content h1 {
  font-size: var(--text-3xl);
  border-bottom: 2px solid var(--border-light);
  padding-bottom: var(--spacing-3);
}

.document-content h2 {
  font-size: var(--text-2xl);
}

.document-content h3 {
  font-size: var(--text-xl);
}

.document-content p {
  margin-bottom: var(--spacing-4);
}

.document-content code {
  background: var(--bg-tertiary);
  color: var(--primary-500);
  padding: var(--spacing-1) var(--spacing-2);
  border-radius: var(--radius-sm);
  font-family: var(--font-mono);
  font-size: 0.875em;
}

.document-content pre {
  background: var(--bg-dark);
  color: var(--text-inverse);
  padding: var(--spacing-4);
  border-radius: var(--radius-lg);
  overflow-x: auto;
  margin: var(--spacing-4) 0;
}

.document-content pre code {
  background: none;
  color: inherit;
  padding: 0;
}

.document-content blockquote {
  border-left: 4px solid var(--primary-500);
  background: var(--bg-secondary);
  padding: var(--spacing-4);
  margin: var(--spacing-4) 0;
  border-radius: 0 var(--radius-md) var(--radius-md) 0;
}

.document-content ul,
.document-content ol {
  margin-bottom: var(--spacing-4);
  padding-left: var(--spacing-6);
}

.document-content li {
  margin-bottom: var(--spacing-2);
}

.document-content table {
  width: 100%;
  border-collapse: collapse;
  margin: var(--spacing-4) 0;
  border: 1px solid var(--border-light);
  border-radius: var(--radius-md);
  overflow: hidden;
}

.document-content th,
.document-content td {
  padding: var(--spacing-3) var(--spacing-4);
  text-align: left;
  border-bottom: 1px solid var(--border-light);
}

.document-content th {
  background: var(--bg-secondary);
  font-weight: var(--font-semibold);
  color: var(--text-primary);
}

.document-content img {
  max-width: 100%;
  height: auto;
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-md);
  margin: var(--spacing-4) 0;
}
```

## 5. 交互设计规范

### 5.1 交互状态

#### 5.1.1 悬停状态 (Hover)
- **触发**：鼠标悬停在可交互元素上
- **视觉反馈**：轻微提升阴影、颜色变化、位移效果
- **时长**：150ms 缓动动画

#### 5.1.2 聚焦状态 (Focus)
- **触发**：键盘导航或点击聚焦
- **视觉反馈**：明显的轮廓线，使用品牌色
- **可访问性**：确保焦点可见，支持键盘导航

#### 5.1.3 激活状态 (Active)
- **触发**：点击或按下元素
- **视觉反馈**：颜色加深、轻微缩放
- **时长**：瞬时反馈

#### 5.1.4 禁用状态 (Disabled)
- **视觉表现**：降低透明度至 50%，移除交互效果
- **行为**：阻止所有交互操作
- **提示**：提供禁用原因的工具提示

### 5.2 加载状态

#### 5.2.1 页面加载
```css
.loading-spinner {
  width: 40px;
  height: 40px;
  border: 3px solid var(--bg-tertiary);
  border-top: 3px solid var(--primary-500);
  border-radius: var(--radius-full);
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.loading-skeleton {
  background: linear-gradient(90deg,
    var(--bg-tertiary) 25%,
    var(--bg-secondary) 50%,
    var(--bg-tertiary) 75%);
  background-size: 200% 100%;
  animation: loading 1.5s infinite;
  border-radius: var(--radius-md);
}

@keyframes loading {
  0% { background-position: 200% 0; }
  100% { background-position: -200% 0; }
}
```

### 5.3 反馈机制

#### 5.3.1 成功提示
```css
.toast-success {
  background: var(--success-500);
  color: var(--text-inverse);
  padding: var(--spacing-4) var(--spacing-6);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-lg);
  display: flex;
  align-items: center;
  gap: var(--spacing-3);
  animation: slideIn var(--duration-normal) var(--ease-out);
}
```

#### 5.3.2 错误提示
```css
.toast-error {
  background: var(--error-500);
  color: var(--text-inverse);
  padding: var(--spacing-4) var(--spacing-6);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-lg);
  display: flex;
  align-items: center;
  gap: var(--spacing-3);
  animation: slideIn var(--duration-normal) var(--ease-out);
}
```

## 6. 响应式设计

### 6.1 移动端适配

#### 6.1.1 导航适配
- 侧边栏收起为汉堡菜单
- 顶部导航栏简化，只保留核心功能
- 使用底部导航栏提供快速访问

#### 6.1.2 内容适配
- 卡片布局改为单列显示
- 表格横向滚动
- 文字大小适当增大，提升可读性

#### 6.1.3 交互适配
- 增大点击区域，最小 44px
- 优化手势操作体验
- 减少悬停效果，增加点击反馈

### 6.2 平板端适配
- 保持桌面端功能完整性
- 调整间距和尺寸适配触摸操作
- 优化横竖屏切换体验

## 7. 可访问性设计

### 7.1 颜色对比度
- 确保所有文字与背景对比度达到 WCAG 2.1 AA 标准（4.5:1）
- 重要信息的对比度达到 AAA 标准（7:1）

### 7.2 键盘导航
- 所有交互元素支持键盘访问
- 提供清晰的焦点指示器
- 支持快捷键操作

### 7.3 屏幕阅读器支持
- 使用语义化 HTML 标签
- 提供适当的 ARIA 标签
- 为图片提供替代文字

### 7.4 运动敏感性
- 提供减少动画的选项
- 避免快速闪烁的内容
- 使用渐进增强的动画

## 8. 品牌定制

### 8.1 主题配置
```css
:root {
  /* 主题色彩可配置 */
  --theme-primary: var(--primary-500);
  --theme-secondary: var(--success-500);

  /* Logo 配置 */
  --brand-logo: url('/assets/logo.svg');
  --brand-name: 'Knowledge Base';

  /* 字体配置 */
  --brand-font: var(--font-primary);
}

/* 深色主题 */
[data-theme="dark"] {
  --bg-primary: #1f2937;
  --bg-secondary: #374151;
  --bg-tertiary: #4b5563;
  --text-primary: #f9fafb;
  --text-secondary: #d1d5db;
  --text-tertiary: #9ca3af;
  --border-light: #374151;
  --border-medium: #4b5563;
}
```

### 8.2 自定义配置
- 支持 Logo 和品牌名称自定义
- 主题色彩可配置
- 页面布局可调整
- 功能模块可启用/禁用

## 9. 性能优化

### 9.1 图片优化
- 使用 WebP 格式图片
- 实现懒加载机制
- 提供多种尺寸适配

### 9.2 CSS 优化
- 使用 CSS 变量提高维护性
- 避免深层嵌套选择器
- 利用 CSS Grid 和 Flexbox 布局

### 9.3 交互优化
- 使用 CSS transform 而非改变布局属性
- 合理使用 will-change 属性
- 避免强制同步布局

## 10. 设计交付

### 10.1 设计资产
- 完整的组件库文档
- 图标库和插图资源
- 设计规范文档

### 10.2 开发协作
- 提供 CSS 样式库
- 组件使用示例
- 响应式断点说明

### 10.3 质量保证
- 设计还原度检查清单
- 可访问性测试指南
- 性能优化建议

---

本UI设计说明书提供了完整的设计系统和实现指南，确保产品在视觉设计和用户体验方面达到现代化、专业化的标准。所有设计元素都经过精心考虑，既保证了美观性，又确保了实用性和可访问性。