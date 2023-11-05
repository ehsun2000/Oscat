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
        path: '/cinema-seat',
        name: 'seat',
        component: import('@/views/SeatController.vue'),
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
  { path: '/', component: import('@/views/HomeVue.vue') },
  { path: '/signin', component: import('@/components/SignIn.vue') },
  { path: '/forgotPwd', component: import('@/components/ForgotPwd.vue') },
  { path: '/signup', component: import('@/components/SignUp.vue') },
  { path: '/agreement', component: import('@/components/PrivacyPolicy.vue') },
  {
    path: '/select-movie/:movieId',
    name: 'SelectMovie',
    component: import('@/views/Booking/SelectMovie.vue'),
  },
  {
    path: '/ticket-type',
    name: 'TicketType',
    component: import('@/views/Booking/TicketType.vue'),
  },
  {
    path: '/select-seats',
    name: 'SelectSeats',
    component: import('@/views/Booking/SelectSeats.vue'),
  },
  {
    path: '/book-checkout',
    name: 'BookCheckout',
    component: import('@/views/Booking/BookCheckout.vue'),
    props: true,
  },
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

export default router;
