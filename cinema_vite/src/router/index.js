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
        path: '/member-report',
        name: 'Analysis',
        component: import('@/views/MemberAnalysis.vue'),
      },
    ],
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
