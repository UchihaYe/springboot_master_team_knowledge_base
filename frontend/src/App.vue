<script setup lang="ts"></script>

<template>
  <router-view />
</template>

<style>
#app {
  height: 100vh;
  overflow: hidden;
}

.page-container {
  padding: var(--component-padding-lg);
  background: var(--bg-secondary);
  min-height: calc(100vh - var(--navbar-height));
}

.content-wrapper {
  max-width: 1200px;
  margin: 0 auto;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity var(--duration-normal) var(--ease-out);
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

.slide-enter-active,
.slide-leave-active {
  transition: transform var(--duration-normal) var(--ease-out);
}

.slide-enter-from {
  transform: translateX(-100%);
}

.slide-leave-to {
  transform: translateX(100%);
}

.el-header {
  border-bottom: 1px solid var(--border-light);
  background: var(--bg-primary);
  box-shadow: var(--shadow-sm);
}

.el-aside {
  background: var(--bg-primary);
  border-right: 1px solid var(--border-light);
  transition: width var(--duration-normal) var(--ease-out);
}

.el-main {
  background: var(--bg-secondary);
  padding: 0;
}

.el-menu {
  border-right: none;
}

.el-menu-item {
  height: 48px;
  line-height: 48px;
  margin: 0 var(--spacing-2);
  border-radius: var(--radius-md);
  transition: all var(--duration-fast) var(--ease-out);
}

.el-menu-item:hover,
.el-menu-item.is-active {
  background: var(--primary-50) !important;
  color: var(--primary-500) !important;
}

.el-menu-item.is-active {
  border-right: 3px solid var(--primary-500);
}

.el-button {
  border-radius: var(--radius-md);
  font-weight: var(--font-medium);
  transition: all var(--duration-fast) var(--ease-out);
}

.el-button--primary {
  background: var(--primary-gradient);
  border: none;
  box-shadow: var(--shadow-sm);
}

.el-button--primary:hover {
  transform: translateY(-1px);
  box-shadow: var(--shadow-md);
}

.el-card {
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-sm);
  border: 1px solid var(--border-light);
  transition: all var(--duration-fast) var(--ease-out);
}

.el-card:hover {
  transform: translateY(-2px);
  box-shadow: var(--shadow-md);
}

.el-input__wrapper {
  border-radius: var(--radius-md);
  transition: all var(--duration-fast) var(--ease-out);
}

.el-input__wrapper:hover {
  box-shadow: var(--shadow-outline);
}

.el-input__wrapper.is-focus {
  box-shadow: var(--shadow-focus);
}

.el-form-item__label {
  font-weight: var(--font-medium);
  color: var(--text-primary);
}

.el-tag {
  border-radius: var(--radius-md);
  font-weight: var(--font-medium);
}

.el-dialog {
  border-radius: var(--radius-xl);
  box-shadow: var(--shadow-xl);
}

.loading-spinner {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 200px;
}

.empty-state {
  text-align: center;
  padding: var(--spacing-16);
  color: var(--text-tertiary);
}

.empty-state .el-icon {
  font-size: 64px;
  margin-bottom: var(--spacing-4);
  color: var(--text-tertiary);
}

.status-badge {
  display: inline-flex;
  align-items: center;
  padding: var(--spacing-1) var(--spacing-3);
  border-radius: var(--radius-full);
  font-size: var(--text-xs);
  font-weight: var(--font-medium);
}

.status-badge.success {
  background: var(--success-50);
  color: var(--success-500);
}

.status-badge.warning {
  background: var(--warning-50);
  color: var(--warning-500);
}

.status-badge.error {
  background: var(--error-50);
  color: var(--error-500);
}

.status-badge.info {
  background: var(--info-50);
  color: var(--info-500);
}
</style>
