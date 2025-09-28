// 用于测试认证功能的工具函数

import { authApi } from '@/api/auth'
import type { LoginRequest } from '@/types/auth'

export const testAuthConnection = async () => {
  console.log('🔍 开始测试前后端认证连接...')

  try {
    // 测试登录接口连通性
    const testLoginRequest: LoginRequest = {
      email: 'test@example.com',
      password: 'test123456'
    }

    console.log('📤 发送测试登录请求:', testLoginRequest)

    const response = await authApi.login(testLoginRequest)
    console.log('✅ 登录成功:', response)

    return {
      success: true,
      data: response
    }

  } catch (error: any) {
    console.log('❌ 登录测试失败:', error)

    // 分析错误类型
    if (error.code === 'NETWORK_ERROR' || error.message?.includes('Network Error')) {
      console.log('🔴 网络连接错误 - 请检查后端服务是否运行在 http://localhost:8080')
    } else if (error.response?.status === 401) {
      console.log('🟡 认证失败 - 用户名或密码错误（这是正常的，因为是测试数据）')
    } else if (error.response?.status === 404) {
      console.log('🟠 接口不存在 - 请检查后端路由配置')
    } else {
      console.log('🟢 接口连通但返回错误 - 这表明前后端连接正常')
    }

    return {
      success: false,
      error: error.message || '未知错误',
      status: error.response?.status,
      data: error.response?.data
    }
  }
}

// 在浏览器控制台中暴露测试函数
if (typeof window !== 'undefined') {
  (window as any).testAuthConnection = testAuthConnection
}