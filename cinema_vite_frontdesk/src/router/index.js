import { createRouter, createWebHistory } from 'vue-router';

const routes = [
  {
    path: '/adminlogin',
    name: 'Login',
    component: () => import('@/views/AdminLogin.vue'),
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
