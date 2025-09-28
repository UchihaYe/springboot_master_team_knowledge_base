// 认证状态管理

import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { authApi } from '@/api/auth'
import type { User, LoginRequest, RegisterRequest } from '@/types/auth'

export const useAuthStore = defineStore('auth', () => {
  // 状态
  const user = ref<User | null>(null)
  const token = ref<string | null>(null)
  const loading = ref(false)

  // 计算属性
  const isAuthenticated = computed(() => {
    return !!(token.value && user.value)
  })

  const isAdmin = computed(() => {
    return user.value?.role === 'ADMIN'
  })

  const isEditor = computed(() => {
    return user.value?.role === 'ADMIN' || user.value?.role === 'EDITOR'
  })

  // 动作
  const initAuth = () => {
    // 从localStorage初始化认证状态
    const savedToken = authApi.getToken()
    const savedUser = authApi.getCurrentUser()

    if (savedToken && savedUser) {
      token.value = savedToken
      user.value = savedUser
    }
  }

  const login = async (loginRequest: LoginRequest) => {
    loading.value = true
    try {
      const response = await authApi.login(loginRequest)

      // 更新状态
      token.value = response.token
      user.value = response.user

      return response
    } finally {
      loading.value = false
    }
  }

  const register = async (registerRequest: RegisterRequest) => {
    loading.value = true
    try {
      const newUser = await authApi.register(registerRequest)
      return newUser
    } finally {
      loading.value = false
    }
  }

  const logout = async () => {
    loading.value = true
    try {
      await authApi.logout()
    } finally {
      // 清除状态
      token.value = null
      user.value = null
      loading.value = false
    }
  }

  const updateUser = (updatedUser: Partial<User>) => {
    if (user.value) {
      user.value = { ...user.value, ...updatedUser }

      // 同步到localStorage
      localStorage.setItem('user_info', JSON.stringify(user.value))
    }
  }

  const verifyEmail = async (verificationToken: string) => {
    await authApi.verifyEmail(verificationToken)

    // 更新用户的邮箱验证状态
    if (user.value) {
      updateUser({ emailVerified: true })
    }
  }

  const forgotPassword = async (email: string) => {
    await authApi.forgotPassword(email)
  }

  const resetPassword = async (resetToken: string, newPassword: string) => {
    await authApi.resetPassword(resetToken, newPassword)
  }

  return {
    // 状态
    user,
    token,
    loading,

    // 计算属性
    isAuthenticated,
    isAdmin,
    isEditor,

    // 动作
    initAuth,
    login,
    register,
    logout,
    updateUser,
    verifyEmail,
    forgotPassword,
    resetPassword
  }
})