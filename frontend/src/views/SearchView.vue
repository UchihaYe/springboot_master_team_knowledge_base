<template>
  <div class="search-container">
    <el-card class="search-card">
      <div class="search-header">
        <h2>搜索结果</h2>
        <p v-if="searchQuery">关键词: "{{ searchQuery }}" - 找到 {{ searchResults.length }} 个结果</p>
      </div>

      <!-- 高级搜索 -->
      <el-collapse v-model="showAdvanced">
        <el-collapse-item title="高级搜索" name="advanced">
          <el-form :model="advancedSearch" label-width="80px" class="advanced-form">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="作者">
                  <el-select v-model="advancedSearch.author" placeholder="选择作者" clearable>
                    <el-option label="张三" value="张三" />
                    <el-option label="李四" value="李四" />
                    <el-option label="王五" value="王五" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="知识空间">
                  <el-select v-model="advancedSearch.space" placeholder="选择空间" clearable>
                    <el-option label="技术文档" value="技术文档" />
                    <el-option label="项目管理" value="项目管理" />
                    <el-option label="产品设计" value="产品设计" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="标签">
                  <el-select v-model="advancedSearch.tags" placeholder="选择标签" multiple clearable>
                    <el-option label="Vue3" value="Vue3" />
                    <el-option label="TypeScript" value="TypeScript" />
                    <el-option label="API" value="API" />
                    <el-option label="设计规范" value="设计规范" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="更新时间">
                  <el-date-picker
                    v-model="advancedSearch.dateRange"
                    type="daterange"
                    range-separator="至"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item>
              <el-button type="primary" @click="performAdvancedSearch">搜索</el-button>
              <el-button @click="resetAdvancedSearch">重置</el-button>
            </el-form-item>
          </el-form>
        </el-collapse-item>
      </el-collapse>

      <!-- 搜索结果 -->
      <div class="search-results">
        <el-empty v-if="searchResults.length === 0" description="暂无搜索结果" />
        <div v-else>
          <div
            v-for="result in searchResults"
            :key="result.id"
            class="result-item"
            @click="openDocument(result)"
          >
            <div class="result-header">
              <h3 class="result-title" v-html="highlightKeyword(result.title)"></h3>
              <div class="result-meta">
                <el-tag size="small">{{ result.space }}</el-tag>
                <span class="author">{{ result.author }}</span>
                <span class="date">{{ result.updatedAt }}</span>
              </div>
            </div>
            <div class="result-content" v-html="highlightKeyword(result.excerpt)"></div>
            <div class="result-tags">
              <el-tag
                v-for="tag in result.tags"
                :key="tag"
                size="small"
                type="info"
                class="tag"
              >
                {{ tag }}
              </el-tag>
            </div>
          </div>
        </div>
      </div>

      <!-- 分页 -->
      <div class="pagination" v-if="searchResults.length > 0">
        <el-pagination
          v-model:current-page="currentPage"
          :page-size="pageSize"
          :total="totalResults"
          layout="total, prev, pager, next, jumper"
          @current-change="handlePageChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, reactive } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

const searchQuery = ref('')
const showAdvanced = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const totalResults = ref(0)

const advancedSearch = reactive({
  author: '',
  space: '',
  tags: [] as string[],
  dateRange: [] as Date[]
})

// 模拟搜索结果
const searchResults = ref([
  {
    id: 1,
    title: 'Vue3 组件开发规范',
    excerpt: 'Vue3 提供了更好的 TypeScript 支持和组合式 API，本文档详细介绍了组件开发的最佳实践...',
    space: '技术文档',
    author: '张三',
    updatedAt: '2024-01-15',
    tags: ['Vue3', '开发规范', 'TypeScript']
  },
  {
    id: 2,
    title: 'API 接口设计指南',
    excerpt: '良好的 API 设计是前后端协作的基础，本文档涵盖了 RESTful API 的设计原则和最佳实践...',
    space: '技术文档',
    author: '李四',
    updatedAt: '2024-01-14',
    tags: ['API', '设计规范', 'RESTful']
  },
  {
    id: 3,
    title: 'TypeScript 最佳实践',
    excerpt: 'TypeScript 为 JavaScript 带来了静态类型检查，提高了代码的可维护性和开发效率...',
    space: '技术文档',
    author: '王五',
    updatedAt: '2024-01-13',
    tags: ['TypeScript', '最佳实践', '类型安全']
  }
])

const highlightKeyword = (text: string) => {
  if (!searchQuery.value) return text
  const regex = new RegExp(`(${searchQuery.value})`, 'gi')
  return text.replace(regex, '<mark>$1</mark>')
}

const performAdvancedSearch = () => {
  // 执行高级搜索逻辑
  console.log('Advanced search:', advancedSearch)
}

const resetAdvancedSearch = () => {
  Object.assign(advancedSearch, {
    author: '',
    space: '',
    tags: [],
    dateRange: []
  })
}

const handlePageChange = (page: number) => {
  currentPage.value = page
  // 重新搜索
}

const openDocument = (doc: any) => {
  router.push(`/document/${doc.id}`)
}

onMounted(() => {
  searchQuery.value = (route.query.q as string) || ''
  totalResults.value = searchResults.value.length
})
</script>

<style scoped>
.search-container {
  padding: 20px;
  max-width: 1000px;
  margin: 0 auto;
}

.search-header {
  margin-bottom: 20px;
}

.search-header h2 {
  margin: 0 0 8px 0;
  color: #2c3e50;
}

.search-header p {
  margin: 0;
  color: #7f8c8d;
}

.advanced-form {
  background: #f8f9fa;
  padding: 20px;
  border-radius: 4px;
  margin-bottom: 20px;
}

.search-results {
  margin: 20px 0;
}

.result-item {
  padding: 20px 0;
  border-bottom: 1px solid #e8eaed;
  cursor: pointer;
  transition: background-color 0.3s;
}

.result-item:hover {
  background-color: #f8f9fa;
  margin: 0 -20px;
  padding: 20px;
  border-radius: 4px;
}

.result-item:last-child {
  border-bottom: none;
}

.result-header {
  margin-bottom: 8px;
}

.result-title {
  margin: 0 0 8px 0;
  color: #1a73e8;
  font-size: 18px;
  font-weight: 500;
}

.result-meta {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 14px;
  color: #5f6368;
}

.result-content {
  margin: 12px 0;
  color: #3c4043;
  line-height: 1.5;
}

.result-tags {
  display: flex;
  gap: 8px;
  margin-top: 12px;
}

.tag {
  cursor: pointer;
}

.pagination {
  margin-top: 20px;
  text-align: center;
}

:deep(mark) {
  background-color: #fff3cd;
  padding: 0 2px;
  color: #856404;
}
</style>