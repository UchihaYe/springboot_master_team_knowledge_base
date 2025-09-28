import { createRouter, createWebHistory } from 'vue-router'
import { ElMessage } from 'element-plus'
import MainLayout from '@/layouts/MainLayout.vue'
import LoginView from '@/views/LoginView.vue'
import RegisterView from '@/views/RegisterView.vue'
import DashboardView from '@/views/DashboardView.vue'
import EditorView from '@/views/EditorView.vue'
import { authApi } from '@/api/auth'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      name: 'login',
      component: LoginView
    },
    {
      path: '/register',
      name: 'register',
      component: RegisterView
    },
    {
      path: '/',
      component: MainLayout,
      children: [
        {
          path: '',
          redirect: '/dashboard'
        },
        {
          path: 'dashboard',
          name: 'dashboard',
          component: DashboardView
        },
        {
          path: 'editor/:id',
          name: 'editor',
          component: EditorView
        },
        {
          path: 'document/:id',
          name: 'document',
          component: () => import('@/views/DocumentView.vue')
        },
        {
          path: 'search',
          name: 'search',
          component: () => import('@/views/SearchView.vue')
        },
        {
          path: 'profile',
          name: 'profile',
          component: () => import('@/views/ProfileView.vue')
        },
        {
          path: 'spaces',
          name: 'spaces',
          component: () => import('@/views/SpacesView.vue')
        },
        {
          path: 'space/:id',
          name: 'space-detail',
          component: () => import('@/views/SpaceDetailView.vue')
        },
        {
          path: 'admin',
          name: 'admin',
          component: () => import('@/views/AdminView.vue')
        }
      ]
    }
  ],
})

// 全局路由守卫
router.beforeEach((to, from, next) => {
  const isAuthenticated = authApi.isAuthenticated()
  const publicPages = ['/login', '/register']
  const isPublicPage = publicPages.includes(to.path)

  // 如果用户已登录且访问登录/注册页面，重定向到首页
  if (isAuthenticated && isPublicPage) {
    next('/dashboard')
    return
  }

  // 如果用户未登录且访问受保护页面，重定向到登录页
  if (!isAuthenticated && !isPublicPage) {
    ElMessage.warning('请先登录')
    next('/login')
    return
  }

  // 正常跳转
  next()
})

export default router
