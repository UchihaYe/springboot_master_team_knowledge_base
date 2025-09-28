// 认证相关的API调用

import axios from 'axios'
import type {
  LoginRequest,
  RegisterRequest,
  LoginResponse,
  ApiResponse,
  User
} from '@/types/auth'

// 创建axios实例
const api = axios.create({
  baseURL: 'http://localhost:8080/api/v1',
  headers: {
    'Content-Type': 'application/json'
  },
  timeout: 10000
})

// 请求拦截器 - 添加token
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('auth_token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 响应拦截器 - 处理错误
api.interceptors.response.use(
  (response) => {
    return response
  },
  (error) => {
    if (error.response?.status === 401) {
      // Token过期，清除本地存储并跳转到登录页
      localStorage.removeItem('auth_token')
      localStorage.removeItem('user_info')
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

export const authApi = {
  // 用户登录
  async login(loginRequest: LoginRequest): Promise<LoginResponse> {
    const response = await api.post<ApiResponse<LoginResponse>>('/auth/login', loginRequest)

    if (response.data.success) {
      const { token, user } = response.data.data

      // 保存token和用户信息到localStorage
      localStorage.setItem('auth_token', token)
      localStorage.setItem('user_info', JSON.stringify(user))

      return response.data.data
    } else {
      throw new Error(response.data.message || '登录失败')
    }
  },

  // 用户注册
  async register(registerRequest: RegisterRequest): Promise<User> {
    const response = await api.post<ApiResponse<User>>('/auth/register', registerRequest)

    if (response.data.success) {
      return response.data.data
    } else {
      throw new Error(response.data.message || '注册失败')
    }
  },

  // 用户登出
  async logout(): Promise<void> {
    try {
      await api.post<ApiResponse<string>>('/auth/logout')
    } finally {
      // 无论接口调用成功与否，都清除本地存储
      localStorage.removeItem('auth_token')
      localStorage.removeItem('user_info')
    }
  },

  // 验证邮箱
  async verifyEmail(token: string): Promise<void> {
    const response = await api.get<ApiResponse<string>>(`/auth/verify-email?token=${token}`)

    if (!response.data.success) {
      throw new Error(response.data.message || '邮箱验证失败')
    }
  },

  // 忘记密码
  async forgotPassword(email: string): Promise<void> {
    const response = await api.post<ApiResponse<string>>('/auth/forgot-password', null, {
      params: { email }
    })

    if (!response.data.success) {
      throw new Error(response.data.message || '发送重置邮件失败')
    }
  },

  // 重置密码
  async resetPassword(token: string, newPassword: string): Promise<void> {
    const response = await api.post<ApiResponse<string>>('/auth/reset-password', null, {
      params: { token, newPassword }
    })

    if (!response.data.success) {
      throw new Error(response.data.message || '密码重置失败')
    }
  },

  // 获取当前用户信息（从localStorage）
  getCurrentUser(): User | null {
    try {
      const userInfo = localStorage.getItem('user_info')
      return userInfo ? JSON.parse(userInfo) : null
    } catch {
      return null
    }
  },

  // 获取当前token
  getToken(): string | null {
    return localStorage.getItem('auth_token')
  },

  // 检查是否已登录
  isAuthenticated(): boolean {
    const token = this.getToken()
    const user = this.getCurrentUser()
    return !!(token && user)
  }
}

export default api