import { createRouter, createWebHistory } from 'vue-router';
import Home from '../views/HomeVue.vue';
import Movie from '../views/AdminMovie.vue';
import Edit from '../views/AdminEditMovie.vue';
import Add from '../views/AdminAddMovie.vue';

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
        component: import('@/views/yuho/MemberAdmin.vue'),
      },
      {
        path: '/member/insert',
        name: 'Insert',
        component: import('@/views/yuho/MemberAdInsert.vue'),
      },
      {
        path: '/member/update/:memberId',
        name: 'Update',
        component: import('@/views/yuho/MemberAdUpdate.vue'),
      },
    ],
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
