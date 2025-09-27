<template>
  <div class="login-container">
    <div class="login-wrapper">
      <!-- 左侧品牌区域 -->
      <div class="brand-section">
        <div class="brand-content">
          <div class="brand-logo">
            <el-icon size="64" color="white">
              <Reading />
            </el-icon>
          </div>
          <h1 class="brand-title">团队知识库</h1>
          <p class="brand-subtitle">让知识流动，让协作更高效</p>
          <div class="feature-list">
            <div class="feature-item">
              <el-icon color="rgba(255,255,255,0.8)">
                <Document />
              </el-icon>
              <span>文档管理</span>
            </div>
            <div class="feature-item">
              <el-icon color="rgba(255,255,255,0.8)">
                <Share />
              </el-icon>
              <span>团队协作</span>
            </div>
            <div class="feature-item">
              <el-icon color="rgba(255,255,255,0.8)">
                <Search />
              </el-icon>
              <span>智能搜索</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 右侧登录区域 -->
      <div class="login-section">
        <el-card class="login-card">
          <div class="card-header">
            <h2>
              <el-icon size="24" color="var(--primary-500)">
                <UserFilled />
              </el-icon>
              欢迎回来
            </h2>
            <p>请登录您的账户</p>
          </div>

          <el-form
            :model="loginForm"
            :rules="rules"
            ref="loginFormRef"
            class="login-form"
            @submit.prevent="handleLogin"
          >
            <el-form-item prop="email">
              <el-input
                v-model="loginForm.email"
                placeholder="请输入邮箱"
                size="large"
                class="form-input"
              >
                <template #prefix>
                  <el-icon color="var(--text-tertiary)">
                    <Message />
                  </el-icon>
                </template>
              </el-input>
            </el-form-item>

            <el-form-item prop="password">
              <el-input
                v-model="loginForm.password"
                type="password"
                placeholder="请输入密码"
                size="large"
                show-password
                class="form-input"
              >
                <template #prefix>
                  <el-icon color="var(--text-tertiary)">
                    <Lock />
                  </el-icon>
                </template>
              </el-input>
            </el-form-item>

            <el-form-item class="form-options">
              <el-checkbox v-model="loginForm.remember" class="remember-checkbox">
                记住我
              </el-checkbox>
              <el-link type="primary" class="forgot-link">
                忘记密码？
              </el-link>
            </el-form-item>

            <el-form-item>
              <el-button
                type="primary"
                size="large"
                class="login-btn"
                :loading="loading"
                @click="handleLogin"
              >
                <el-icon v-if="!loading">
                  <Right />
                </el-icon>
                {{ loading ? '登录中...' : '登录' }}
              </el-button>
            </el-form-item>

            <el-divider class="divider">
              <span class="divider-text">或</span>
            </el-divider>

            <el-form-item>
              <div class="social-login">
                <el-button class="social-btn" @click="handleSocialLogin('github')">
                  <el-icon>
                    <Platform />
                  </el-icon>
                  GitHub
                </el-button>
                <el-button class="social-btn" @click="handleSocialLogin('wechat')">
                  <el-icon>
                    <ChatDotRound />
                  </el-icon>
                  微信
                </el-button>
              </div>
            </el-form-item>

            <el-form-item>
              <div class="register-link">
                还没有账户？
                <el-link type="primary" @click="$router.push('/register')" class="register-btn">
                  <el-icon>
                    <Plus />
                  </el-icon>
                  立即注册
                </el-link>
              </div>
            </el-form-item>
          </el-form>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'

const router = useRouter()
const loginFormRef = ref<FormInstance>()
const loading = ref(false)

const loginForm = reactive({
  email: '',
  password: '',
  remember: false
})

const rules: FormRules = {
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 8, message: '密码至少8位', trigger: 'blur' }
  ]
}

const handleLogin = async () => {
  if (!loginFormRef.value) return

  await loginFormRef.value.validate((valid) => {
    if (valid) {
      loading.value = true

      // 模拟登录请求
      setTimeout(() => {
        loading.value = false
        ElMessage.success('登录成功')
        router.push('/dashboard')
      }, 1000)
    }
  })
}

const handleSocialLogin = (platform: string) => {
  ElMessage.info(`${platform} 登录功能开发中...`)
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--bg-secondary);
  padding: var(--spacing-4);
}

.login-wrapper {
  display: flex;
  width: 100%;
  max-width: 1200px;
  min-height: 600px;
  border-radius: var(--radius-2xl);
  overflow: hidden;
  box-shadow: var(--shadow-xl);
}

/* 左侧品牌区域 */
.brand-section {
  flex: 1;
  background: var(--primary-gradient);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: var(--spacing-12);
  position: relative;
}

.brand-section::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: url('data:image/svg+xml,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100"><defs><pattern id="grid" width="10" height="10" patternUnits="userSpaceOnUse"><path d="M 10 0 L 0 0 0 10" fill="none" stroke="rgba(255,255,255,0.1)" stroke-width="0.5"/></pattern></defs><rect width="100" height="100" fill="url(%23grid)"/></svg>');
  opacity: 0.3;
}

.brand-content {
  position: relative;
  z-index: 1;
  text-align: center;
  color: white;
}

.brand-logo {
  margin-bottom: var(--spacing-6);
  display: flex;
  justify-content: center;
}

.brand-title {
  font-size: var(--text-4xl);
  font-weight: var(--font-bold);
  margin: 0 0 var(--spacing-4) 0;
  text-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.brand-subtitle {
  font-size: var(--text-xl);
  margin: 0 0 var(--spacing-8) 0;
  opacity: 0.9;
  line-height: var(--leading-relaxed);
}

.feature-list {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-4);
  align-items: flex-start;
  max-width: 300px;
  margin: 0 auto;
}

.feature-item {
  display: flex;
  align-items: center;
  gap: var(--spacing-3);
  font-size: var(--text-lg);
  opacity: 0.9;
}

/* 右侧登录区域 */
.login-section {
  flex: 1;
  background: var(--bg-primary);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: var(--spacing-8);
}

.login-card {
  width: 100%;
  max-width: 400px;
  border: none;
  box-shadow: none;
}

.card-header {
  text-align: center;
  margin-bottom: var(--spacing-8);
}

.card-header h2 {
  font-size: var(--text-2xl);
  font-weight: var(--font-semibold);
  color: var(--text-primary);
  margin: 0 0 var(--spacing-2) 0;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: var(--spacing-2);
}

.card-header p {
  font-size: var(--text-base);
  color: var(--text-secondary);
  margin: 0;
}

.login-form {
  padding: 0;
}

.form-input {
  margin-bottom: var(--spacing-2);
}

.form-input :deep(.el-input__wrapper) {
  padding: var(--spacing-4);
  border-radius: var(--radius-lg);
  border: 2px solid var(--border-light);
  transition: all var(--duration-normal) var(--ease-out);
}

.form-input :deep(.el-input__wrapper:hover) {
  border-color: var(--primary-300);
}

.form-input :deep(.el-input__wrapper.is-focus) {
  border-color: var(--primary-500);
  box-shadow: var(--shadow-focus);
}

.form-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: var(--spacing-4) 0;
}

.remember-checkbox {
  color: var(--text-secondary);
}

.forgot-link {
  font-size: var(--text-sm);
}

.login-btn {
  width: 100%;
  height: 48px;
  border-radius: var(--radius-lg);
  font-size: var(--text-lg);
  font-weight: var(--font-medium);
  background: var(--primary-gradient);
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: var(--spacing-2);
  transition: all var(--duration-normal) var(--ease-out);
}

.login-btn:hover {
  transform: translateY(-2px);
  box-shadow: var(--shadow-lg);
}

.divider {
  margin: var(--spacing-6) 0;
}

.divider-text {
  color: var(--text-tertiary);
  font-size: var(--text-sm);
  padding: 0 var(--spacing-4);
  background: var(--bg-primary);
}

.social-login {
  display: flex;
  gap: var(--spacing-3);
}

.social-btn {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: var(--spacing-2);
  height: 44px;
  border-radius: var(--radius-lg);
  border: 2px solid var(--border-light);
  background: var(--bg-primary);
  color: var(--text-secondary);
  transition: all var(--duration-normal) var(--ease-out);
}

.social-btn:hover {
  border-color: var(--primary-300);
  color: var(--primary-500);
  background: var(--primary-50);
}

.register-link {
  text-align: center;
  color: var(--text-secondary);
  font-size: var(--text-sm);
  margin-top: var(--spacing-4);
}

.register-btn {
  display: inline-flex;
  align-items: center;
  gap: var(--spacing-1);
  font-weight: var(--font-medium);
}

/* 响应式设计 */
@media (max-width: 1024px) {
  .login-wrapper {
    flex-direction: column;
    max-width: 500px;
  }

  .brand-section {
    min-height: 300px;
    padding: var(--spacing-8);
  }

  .brand-title {
    font-size: var(--text-3xl);
  }

  .brand-subtitle {
    font-size: var(--text-lg);
  }

  .feature-list {
    flex-direction: row;
    justify-content: center;
    max-width: none;
  }

  .feature-item {
    flex-direction: column;
    text-align: center;
    gap: var(--spacing-2);
    font-size: var(--text-base);
  }
}

@media (max-width: 768px) {
  .login-container {
    padding: var(--spacing-2);
  }

  .login-wrapper {
    min-height: auto;
  }

  .brand-section {
    min-height: 200px;
    padding: var(--spacing-6);
  }

  .brand-title {
    font-size: var(--text-2xl);
  }

  .brand-subtitle {
    font-size: var(--text-base);
  }

  .feature-list {
    gap: var(--spacing-2);
  }

  .login-section {
    padding: var(--spacing-6);
  }

  .social-login {
    flex-direction: column;
  }
}
</style>