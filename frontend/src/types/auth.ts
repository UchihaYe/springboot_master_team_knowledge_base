// 认证相关的TypeScript类型定义

export interface LoginRequest {
  email: string
  password: string
}

export interface RegisterRequest {
  email: string
  password: string
  displayName: string
}

export interface User {
  id: number
  email: string
  displayName: string
  avatarUrl?: string
  role: UserRole
  emailVerified: boolean
  enabled: boolean
  lastLoginAt?: string
  createdAt: string
  updatedAt: string
  themePreference?: ThemePreference
}

export enum UserRole {
  ADMIN = 'ADMIN',
  EDITOR = 'EDITOR',
  VIEWER = 'VIEWER'
}

export enum ThemePreference {
  LIGHT = 'LIGHT',
  DARK = 'DARK',
  AUTO = 'AUTO'
}

export interface LoginResponse {
  token: string
  type: string // 'Bearer'
  user: User
}

export interface ApiResponse<T> {
  success: boolean
  message: string
  data: T
  timestamp: string
}

// 错误响应类型
export interface ErrorResponse {
  success: false
  message: string
  data: null
  timestamp: string
}