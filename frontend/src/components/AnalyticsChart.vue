<template>
  <div class="analytics-chart">
    <div class="chart-header">
      <h3 class="chart-title">{{ title }}</h3>
      <div class="chart-actions" v-if="showActions">
        <el-select v-model="timeRange" @change="onTimeRangeChange" size="small">
          <el-option label="最近7天" value="7d" />
          <el-option label="最近30天" value="30d" />
          <el-option label="最近90天" value="90d" />
        </el-select>
        <el-button @click="exportChart" size="small" text>
          <el-icon><Download /></el-icon>
          导出
        </el-button>
      </div>
    </div>

    <div class="chart-container" ref="chartContainer">
      <canvas ref="chartCanvas" :width="canvasWidth" :height="canvasHeight"></canvas>
    </div>

    <div class="chart-legend" v-if="showLegend">
      <div
        v-for="(item, index) in legendItems"
        :key="index"
        class="legend-item"
        @click="toggleDataset(index)"
      >
        <span
          class="legend-color"
          :style="{ backgroundColor: item.color }"
        ></span>
        <span class="legend-label">{{ item.label }}</span>
        <span class="legend-value">{{ item.value }}</span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, watch, nextTick } from 'vue'
import { ElMessage } from 'element-plus'

interface ChartData {
  labels: string[]
  datasets: Array<{
    label: string
    data: number[]
    borderColor?: string
    backgroundColor?: string
    fill?: boolean
  }>
}

interface Props {
  title: string
  data: ChartData
  type?: 'line' | 'bar' | 'doughnut' | 'area'
  showActions?: boolean
  showLegend?: boolean
  height?: number
}

const props = withDefaults(defineProps<Props>(), {
  type: 'line',
  showActions: true,
  showLegend: true,
  height: 300
})

const emit = defineEmits<{
  timeRangeChange: [range: string]
  export: [type: string]
}>()

const chartContainer = ref<HTMLDivElement>()
const chartCanvas = ref<HTMLCanvasElement>()
const timeRange = ref('7d')

const canvasWidth = ref(800)
const canvasHeight = ref(props.height)

// 图例数据
const legendItems = ref([
  { color: '#409EFF', label: '文档浏览', value: '1,234' },
  { color: '#67C23A', label: '用户活跃', value: '567' },
  { color: '#E6A23C', label: '内容创建', value: '89' }
])

// 绘制图表
const drawChart = () => {
  if (!chartCanvas.value) return

  const canvas = chartCanvas.value
  const ctx = canvas.getContext('2d')
  if (!ctx) return

  // 清空画布
  ctx.clearRect(0, 0, canvas.width, canvas.height)

  // 设置样式
  ctx.strokeStyle = '#409EFF'
  ctx.fillStyle = '#409EFF'
  ctx.lineWidth = 2

  switch (props.type) {
    case 'line':
      drawLineChart(ctx)
      break
    case 'bar':
      drawBarChart(ctx)
      break
    case 'doughnut':
      drawDoughnutChart(ctx)
      break
    case 'area':
      drawAreaChart(ctx)
      break
  }
}

// 绘制折线图
const drawLineChart = (ctx: CanvasRenderingContext2D) => {
  const padding = 60
  const chartWidth = canvasWidth.value - 2 * padding
  const chartHeight = canvasHeight.value - 2 * padding

  // 模拟数据点
  const dataPoints = [
    { x: padding + chartWidth * 0.1, y: padding + chartHeight * 0.8 },
    { x: padding + chartWidth * 0.3, y: padding + chartHeight * 0.6 },
    { x: padding + chartWidth * 0.5, y: padding + chartHeight * 0.4 },
    { x: padding + chartWidth * 0.7, y: padding + chartHeight * 0.7 },
    { x: padding + chartWidth * 0.9, y: padding + chartHeight * 0.3 }
  ]

  // 绘制网格线
  ctx.strokeStyle = '#E4E7ED'
  ctx.lineWidth = 1
  for (let i = 0; i <= 5; i++) {
    const y = padding + (chartHeight / 5) * i
    ctx.beginPath()
    ctx.moveTo(padding, y)
    ctx.lineTo(padding + chartWidth, y)
    ctx.stroke()
  }

  // 绘制坐标轴
  ctx.strokeStyle = '#C0C4CC'
  ctx.lineWidth = 2
  ctx.beginPath()
  ctx.moveTo(padding, padding)
  ctx.lineTo(padding, padding + chartHeight)
  ctx.lineTo(padding + chartWidth, padding + chartHeight)
  ctx.stroke()

  // 绘制数据线
  ctx.strokeStyle = '#409EFF'
  ctx.lineWidth = 3
  ctx.beginPath()
  ctx.moveTo(dataPoints[0].x, dataPoints[0].y)
  for (let i = 1; i < dataPoints.length; i++) {
    ctx.lineTo(dataPoints[i].x, dataPoints[i].y)
  }
  ctx.stroke()

  // 绘制数据点
  ctx.fillStyle = '#409EFF'
  dataPoints.forEach(point => {
    ctx.beginPath()
    ctx.arc(point.x, point.y, 4, 0, 2 * Math.PI)
    ctx.fill()
  })
}

// 绘制柱状图
const drawBarChart = (ctx: CanvasRenderingContext2D) => {
  const padding = 60
  const chartWidth = canvasWidth.value - 2 * padding
  const chartHeight = canvasHeight.value - 2 * padding

  const barCount = 5
  const barWidth = chartWidth / barCount * 0.6
  const barSpacing = chartWidth / barCount * 0.4

  // 模拟数据
  const values = [80, 65, 90, 75, 95]
  const maxValue = Math.max(...values)

  // 绘制坐标轴
  ctx.strokeStyle = '#C0C4CC'
  ctx.lineWidth = 2
  ctx.beginPath()
  ctx.moveTo(padding, padding)
  ctx.lineTo(padding, padding + chartHeight)
  ctx.lineTo(padding + chartWidth, padding + chartHeight)
  ctx.stroke()

  // 绘制柱子
  values.forEach((value, index) => {
    const barHeight = (value / maxValue) * chartHeight
    const x = padding + index * (barWidth + barSpacing) + barSpacing / 2
    const y = padding + chartHeight - barHeight

    // 渐变填充
    const gradient = ctx.createLinearGradient(0, y, 0, y + barHeight)
    gradient.addColorStop(0, '#409EFF')
    gradient.addColorStop(1, '#66B2FF')

    ctx.fillStyle = gradient
    ctx.fillRect(x, y, barWidth, barHeight)

    // 添加数值标签
    ctx.fillStyle = '#303133'
    ctx.font = '12px Arial'
    ctx.textAlign = 'center'
    ctx.fillText(value.toString(), x + barWidth / 2, y - 10)
  })
}

// 绘制环形图
const drawDoughnutChart = (ctx: CanvasRenderingContext2D) => {
  const centerX = canvasWidth.value / 2
  const centerY = canvasHeight.value / 2
  const radius = Math.min(centerX, centerY) - 40
  const innerRadius = radius * 0.6

  const data = [30, 25, 20, 15, 10]
  const colors = ['#409EFF', '#67C23A', '#E6A23C', '#F56C6C', '#909399']
  const total = data.reduce((sum, value) => sum + value, 0)

  let currentAngle = -Math.PI / 2

  data.forEach((value, index) => {
    const sliceAngle = (value / total) * 2 * Math.PI

    // 绘制扇形
    ctx.beginPath()
    ctx.arc(centerX, centerY, radius, currentAngle, currentAngle + sliceAngle)
    ctx.arc(centerX, centerY, innerRadius, currentAngle + sliceAngle, currentAngle, true)
    ctx.closePath()
    ctx.fillStyle = colors[index]
    ctx.fill()

    currentAngle += sliceAngle
  })

  // 中心文字
  ctx.fillStyle = '#303133'
  ctx.font = 'bold 16px Arial'
  ctx.textAlign = 'center'
  ctx.fillText('总计', centerX, centerY - 10)
  ctx.font = 'bold 24px Arial'
  ctx.fillText(total.toString(), centerX, centerY + 15)
}

// 绘制面积图
const drawAreaChart = (ctx: CanvasRenderingContext2D) => {
  const padding = 60
  const chartWidth = canvasWidth.value - 2 * padding
  const chartHeight = canvasHeight.value - 2 * padding

  const dataPoints = [
    { x: padding + chartWidth * 0.1, y: padding + chartHeight * 0.8 },
    { x: padding + chartWidth * 0.3, y: padding + chartHeight * 0.6 },
    { x: padding + chartWidth * 0.5, y: padding + chartHeight * 0.4 },
    { x: padding + chartWidth * 0.7, y: padding + chartHeight * 0.7 },
    { x: padding + chartWidth * 0.9, y: padding + chartHeight * 0.3 }
  ]

  // 创建渐变
  const gradient = ctx.createLinearGradient(0, padding, 0, padding + chartHeight)
  gradient.addColorStop(0, 'rgba(64, 158, 255, 0.3)')
  gradient.addColorStop(1, 'rgba(64, 158, 255, 0.05)')

  // 绘制坐标轴
  ctx.strokeStyle = '#C0C4CC'
  ctx.lineWidth = 2
  ctx.beginPath()
  ctx.moveTo(padding, padding)
  ctx.lineTo(padding, padding + chartHeight)
  ctx.lineTo(padding + chartWidth, padding + chartHeight)
  ctx.stroke()

  // 绘制面积
  ctx.beginPath()
  ctx.moveTo(dataPoints[0].x, padding + chartHeight)
  ctx.lineTo(dataPoints[0].x, dataPoints[0].y)
  for (let i = 1; i < dataPoints.length; i++) {
    ctx.lineTo(dataPoints[i].x, dataPoints[i].y)
  }
  ctx.lineTo(dataPoints[dataPoints.length - 1].x, padding + chartHeight)
  ctx.closePath()
  ctx.fillStyle = gradient
  ctx.fill()

  // 绘制线条
  ctx.strokeStyle = '#409EFF'
  ctx.lineWidth = 2
  ctx.beginPath()
  ctx.moveTo(dataPoints[0].x, dataPoints[0].y)
  for (let i = 1; i < dataPoints.length; i++) {
    ctx.lineTo(dataPoints[i].x, dataPoints[i].y)
  }
  ctx.stroke()
}

// 处理时间范围变化
const onTimeRangeChange = (range: string) => {
  emit('timeRangeChange', range)
  // 重新绘制图表
  nextTick(() => {
    drawChart()
  })
}

// 切换数据集显示
const toggleDataset = (index: number) => {
  // 实现数据集的显示/隐藏
  console.log('Toggle dataset:', index)
}

// 导出图表
const exportChart = () => {
  if (!chartCanvas.value) return

  try {
    const link = document.createElement('a')
    link.download = `${props.title}-${timeRange.value}.png`
    link.href = chartCanvas.value.toDataURL('image/png')
    link.click()
    ElMessage.success('图表已导出')
  } catch (error) {
    ElMessage.error('导出失败')
  }
}

// 响应式处理
const handleResize = () => {
  if (!chartContainer.value) return

  const containerWidth = chartContainer.value.clientWidth
  canvasWidth.value = Math.max(containerWidth, 400)

  nextTick(() => {
    drawChart()
  })
}

onMounted(() => {
  nextTick(() => {
    handleResize()
    drawChart()
  })

  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
})

// 监听数据变化
watch(() => props.data, () => {
  nextTick(() => {
    drawChart()
  })
}, { deep: true })
</script>

<style scoped>
.analytics-chart {
  background: var(--bg-primary);
  border: 1px solid var(--border-light);
  border-radius: var(--radius-lg);
  padding: var(--spacing-5);
  height: 100%;
  display: flex;
  flex-direction: column;
}

.chart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--spacing-4);
  flex-shrink: 0;
}

.chart-title {
  font-size: var(--text-lg);
  font-weight: var(--font-semibold);
  color: var(--text-primary);
  margin: 0;
}

.chart-actions {
  display: flex;
  align-items: center;
  gap: var(--spacing-3);
}

.chart-container {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 200px;
  overflow: hidden;
}

.chart-legend {
  display: flex;
  flex-wrap: wrap;
  gap: var(--spacing-4);
  margin-top: var(--spacing-4);
  padding-top: var(--spacing-4);
  border-top: 1px solid var(--border-light);
  flex-shrink: 0;
}

.legend-item {
  display: flex;
  align-items: center;
  gap: var(--spacing-2);
  cursor: pointer;
  transition: opacity var(--duration-fast);
}

.legend-item:hover {
  opacity: 0.8;
}

.legend-color {
  width: 12px;
  height: 12px;
  border-radius: 2px;
  flex-shrink: 0;
}

.legend-label {
  font-size: var(--text-sm);
  color: var(--text-secondary);
}

.legend-value {
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
  color: var(--text-primary);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .chart-header {
    flex-direction: column;
    align-items: flex-start;
    gap: var(--spacing-3);
  }

  .chart-actions {
    width: 100%;
    justify-content: space-between;
  }

  .chart-legend {
    justify-content: center;
  }
}
</style>