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
  { path: '/', component: () => import('@/views/HomeVue.vue') },
  { path: '/signin', component: () => import('@/components/SignIn.vue') },
  { path: '/forgotPwd', component: () => import('@/components/ForgotPwd.vue') },
  { path: '/resetPwd', component: () => import('@/components/ResetPwd.vue') },
  { path: '/signup', component: () => import('@/components/SignUp.vue') },
  {
    path: '/agreement',
    component: () => import('@/components/PrivacyPolicy.vue'),
  },
  {
    path: '/member-center',
    name: 'MemberCenter',
    component: () => import('@/views/OfficialMember.vue'),
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

// router.beforeEach((to, from, next) => {
//   const isLogin = sessionStorage.getItem('isLogin') === 'true';

//   if (to.path === '/') {
//     if (!isLogin) {
//       // 如果未登入，可以訪問首頁
//       next();
//     } else {
//       // 如果已登入，仍將使用者導向至首頁
//       next('/');
//     }
//   } else {
//     // 其他路徑，正常導向
//     next();
//   }
// });

export default router;
