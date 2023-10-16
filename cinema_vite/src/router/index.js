import { createRouter, createWebHistory } from 'vue-router';

const routes = [
  {
    path: '/adminlogin',
    name: 'Login',
    component: () => import('@/views/LoginVue.vue'),
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: () => import('@/views/DashboardVue.vue'),
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
