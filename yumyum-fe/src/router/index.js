import { createRouter, createWebHistory } from 'vue-router'
import DefaultLayout from '@/layouts/DefaultLayout.vue'
import AuthLayout from '@/layouts/AuthLayout.vue'
import { useAuthStore } from '@/stores/auth'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      component: DefaultLayout,
      children: [
        {
          path: '', // 메인 페이지
          name: 'Main',
          component: () => import('@/views/main/MainView.vue')
        },
        {
          path: 'dashboard', // 대시보드 페이지
          name: 'Dashboard',
          component: () => import('@/views/dashboard/DashboardView.vue')
        },
        {
          path: 'group', // 그룹 페이지
          name: 'Group',
          component: () => import('@/views/group/GroupMainView.vue')
        },
        {
          path: 'meals', // /meals
          name: 'Meal',
          component: () => import('@/views/meal/MealView.vue')
        },
        {
          path: 'exercise', // /exercise
          name: 'Exercise',
          component: () => import('@/views/exercise/ExerciseView.vue')
        },
        {
          path: 'mypage', // /mypage
          name: 'MyPage',
          component: () => import('@/views/user/MyPageView.vue')
        },
        // {
        //   path: '/auth',
        //   component: () => import('@/layouts/AuthLayout.vue'),
        //   children: [
        //     { path: 'login', component: () => import('@/views/auth/LoginView.vue') },
        //     { path: 'signup', component: () => import('@/views/auth/SignupView.vue') },
        //     { path: 'onboarding', component: () => import('@/views/auth/OnboardingView.vue') }
        //   ]
        // },
        {
          path: 'badges', // /badges
          name: 'Badges',
          component: () => import('@/views/badges/BadgesView.vue')
        }          
      ]
    },
    {
      path: '/admin',
      component: () => import('@/layouts/AdminLayout.vue'),
      // 어드민 권한 가드: role이 ADMIN이 아니면 홈으로 리다이렉트
      beforeEnter: (to, from, next) => {
        const authStore = useAuthStore();
        if (authStore.user?.role !== 'ADMIN') {
          alert('관리자 권한이 필요합니다.');
          return next('/');
        }
        return next();
      },
      children: [
        {
          path: 'food',
          name: 'AdminFood',
          component: () => import('@/views/admin/AdminFoodView.vue')
        },
        {
          path: 'diet',
          name: 'AdminDiet',
          component: () => import('@/views/admin/AdminDietView.vue')
        }
      ]
    },
    {
      path: '/auth',
      component: AuthLayout,
      children: [
        {
          path: 'login',
          name: 'Login',
          component: () => import('@/views/auth/LoginView.vue')
        },
        {
          path: 'signup',
          name: 'Signup',
          component: () => import('@/views/auth/SignupView.vue')
        },
        {
          path: 'onboarding',
          name: 'Onboarding',
          component: () => import('@/views/auth/OnboardingView.vue')
        }
      ]
    }
  ]
})

router.beforeEach((to, from, next) => {
  const authStore = useAuthStore()
  const isAuthRoute = to.path.startsWith('/auth')

  if (!authStore.isAuthenticated && !isAuthRoute) {
    return next('/auth/login')
  }

  if (authStore.isAuthenticated && isAuthRoute && to.path !== '/auth/onboarding') {
    return next('/')
  }

  return next()
})

export default router
