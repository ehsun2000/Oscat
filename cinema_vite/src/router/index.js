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
        path: '/cinema-info',
        name: 'CinemaInfo',
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

export default router;
