import { createRouter, createWebHistory } from 'vue-router';

const routes = [
  {
    path: '/adminlogin',
    name: 'Login',
    component: () => import('@/views/AdminLogin.vue'),
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: () => import('@/views/AdminDashboard.vue'),
    meta: { requiresAuth: true },
    children: [
      {
        path: '/member',
        name: 'Member',
        component: import('@/views/MemberAdmin.vue'),
      },
      {
        path: '/member/insert',
        name: 'Insert',
        component: import('@/views/MemberAdInsert.vue'),
      },
      {
        path: '/member/update/:memberId',
        name: 'Update',
        component: import('@/views/MemberAdUpdate.vue'),
      },
      {
        path: '/cinema-select',
        name: 'CinemaSelect',
        component: import('@/views/CinemaSelect.vue'),
      },
      {
        path: '/cinema-detail/:cinemaId',
        name: 'CinemaDetail',
        component: import('@/views/CinemaDetail.vue'),
      },
    ],
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

router.beforeEach(async (to) => {
  const requiresAuth = to.meta.requiresAuth;

  if (requiresAuth) {
    try {
      // 發送 API 請求來檢查用戶身份
      const response = await fetch(
        `${import.meta.env.VITE_CHECK_API_ENDPOINT}`,
        {
          method: 'GET',
          credentials: 'include',
        },
      );

      if (response.status != 200) {
        return { name: 'Login' }; // 身份驗證失敗，導向登入頁
      }
    } catch (error) {
      return { name: 'Login' }; // API 請求失敗，導向登入頁
    }
  }
});

export default router;
