import { createRouter, createWebHistory } from 'vue-router'
import MainLayout from '@/layouts/MainLayout.vue'
import LoginView from '@/views/LoginView.vue'
import RegisterView from '@/views/RegisterView.vue'
import DashboardView from '@/views/DashboardView.vue'
import EditorView from '@/views/EditorView.vue'

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

export default router
