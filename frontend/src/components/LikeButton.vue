<template>
  <div class="like-button-container">
    <el-button
      :class="['like-button', { 'is-liked': liked }]"
      :type="liked ? 'primary' : ''"
      @click="toggleLike"
      :loading="loading"
      :disabled="disabled"
    >
      <el-icon class="like-icon">
        <component :is="liked ? 'SuitHeartFill' : 'SuitHeart'" />
      </el-icon>
      <span class="like-count">{{ displayCount }}</span>
    </el-button>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'

interface Props {
  liked?: boolean
  count?: number
  disabled?: boolean
  size?: 'small' | 'default' | 'large'
  showCount?: boolean
  type?: 'document' | 'comment'
  targetId?: string | number
}

const props = withDefaults(defineProps<Props>(), {
  liked: false,
  count: 0,
  disabled: false,
  size: 'default',
  showCount: true,
  type: 'document',
  targetId: ''
})

const emit = defineEmits<{
  like: [targetId: string | number, type: string]
  unlike: [targetId: string | number, type: string]
}>()

const loading = ref(false)
const currentLiked = ref(props.liked)
const currentCount = ref(props.count)

// 显示的点赞数量
const displayCount = computed(() => {
  if (!props.showCount) return ''

  if (currentCount.value >= 1000) {
    return `${(currentCount.value / 1000).toFixed(1)}k`
  }

  return currentCount.value.toString()
})

// 切换点赞状态
const toggleLike = async () => {
  if (props.disabled || loading.value) return

  loading.value = true

  try {
    // 模拟API调用延迟
    await new Promise(resolve => setTimeout(resolve, 300))

    if (currentLiked.value) {
      // 取消点赞
      currentLiked.value = false
      currentCount.value = Math.max(0, currentCount.value - 1)
      emit('unlike', props.targetId, props.type)

      if (props.type === 'document') {
        ElMessage.success('已取消点赞')
      }
    } else {
      // 点赞
      currentLiked.value = true
      currentCount.value += 1
      emit('like', props.targetId, props.type)

      if (props.type === 'document') {
        ElMessage.success('点赞成功')
      }
    }
  } catch (error) {
    ElMessage.error('操作失败，请稍后重试')
    console.error('Like operation failed:', error)
  } finally {
    loading.value = false
  }
}

// 监听props变化
import { watch } from 'vue'

watch(() => props.liked, (newVal) => {
  currentLiked.value = newVal
})

watch(() => props.count, (newVal) => {
  currentCount.value = newVal
})
</script>

<style scoped>
.like-button-container {
  display: inline-flex;
  align-items: center;
}

.like-button {
  display: flex;
  align-items: center;
  gap: var(--spacing-2);
  border-radius: var(--radius-md);
  transition: all var(--duration-normal) var(--ease-out);
  position: relative;
  overflow: hidden;
}

.like-button:not(.is-liked) {
  color: var(--text-secondary);
  border-color: var(--border-medium);
  background: var(--bg-primary);
}

.like-button:not(.is-liked):hover {
  color: var(--primary-500);
  border-color: var(--primary-300);
  background: var(--primary-50);
  transform: translateY(-1px);
}

.like-button.is-liked {
  color: white;
  background: linear-gradient(135deg, var(--primary-500) 0%, var(--primary-600) 100%);
  border-color: var(--primary-500);
  box-shadow: 0 2px 8px rgba(var(--primary-500-rgb), 0.3);
}

.like-button.is-liked:hover {
  background: linear-gradient(135deg, var(--primary-600) 0%, var(--primary-700) 100%);
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(var(--primary-500-rgb), 0.4);
}

.like-icon {
  font-size: 16px;
  transition: all var(--duration-fast) var(--ease-out);
}

.like-button.is-liked .like-icon {
  animation: likeAnimation 0.3s ease-out;
}

.like-count {
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
  line-height: 1;
  min-width: 20px;
  text-align: center;
}

/* 点赞动画 */
@keyframes likeAnimation {
  0% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.3);
  }
  100% {
    transform: scale(1);
  }
}

/* 涟漪效果 */
.like-button::before {
  content: '';
  position: absolute;
  top: 50%;
  left: 50%;
  width: 0;
  height: 0;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.5);
  transform: translate(-50%, -50%);
  transition: width 0.3s, height 0.3s;
}

.like-button:active::before {
  width: 100px;
  height: 100px;
}

/* 大小变体 */
.like-button.size-small {
  padding: var(--spacing-1) var(--spacing-3);
}

.like-button.size-small .like-icon {
  font-size: 14px;
}

.like-button.size-small .like-count {
  font-size: var(--text-xs);
}

.like-button.size-large {
  padding: var(--spacing-3) var(--spacing-5);
}

.like-button.size-large .like-icon {
  font-size: 18px;
}

.like-button.size-large .like-count {
  font-size: var(--text-base);
}

/* 禁用状态 */
.like-button:disabled {
  opacity: 0.6;
  cursor: not-allowed;
  transform: none !important;
}

.like-button:disabled:hover {
  transform: none !important;
  box-shadow: none !important;
}

/* 深色主题适配 */
@media (prefers-color-scheme: dark) {
  .like-button:not(.is-liked) {
    background: var(--bg-secondary);
  }

  .like-button:not(.is-liked):hover {
    background: var(--bg-tertiary);
  }
}

/* 无障碍支持 */
@media (prefers-reduced-motion: reduce) {
  .like-button,
  .like-icon {
    transition: none;
  }

  .like-icon {
    animation: none;
  }

  .like-button::before {
    transition: none;
  }
}

/* 移动端适配 */
@media (max-width: 768px) {
  .like-button {
    padding: var(--spacing-2) var(--spacing-3);
  }

  .like-icon {
    font-size: 14px;
  }

  .like-count {
    font-size: var(--text-xs);
  }
}
</style>