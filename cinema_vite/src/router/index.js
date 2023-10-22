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
        path: '/movie',
        name: 'Movie',
        component: import('@/views/AdminMovie.vue'),
      },
      {
        path: '/movie/add',
        name: 'Add',
        component: import('@/views/AdminAddMovie.vue'),
      },
      {
        path: '/movie/edit/:movieId',
        name: 'Edit',
        component: import('@/views/AdminEditMovie.vue'),
      },
    ],
  },
  // {

  //     path: '/movie',
  //     name: 'Movie',
  //     component: import('@/views/AdminMovie.vue'),
  //     children: [
  //       {
  //         path: 'add',
  //         name: 'Add'
  //         component: import('@/views/AdminAddMovie.vue'),
  //       },
  //       {
  //         path: 'edit/:movieId',
  //         name: 'Edit',
  //         component: import('@/views/AdminEditMovie.vue'),
  //       },
  //     ]

  // },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
