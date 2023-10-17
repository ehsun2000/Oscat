import { createRouter, createWebHistory } from 'vue-router';

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('../App.vue'),
  },
  {
    path: '/SeatController',
    component: () => import('../components/seat/SeatController.vue'),
  },
  {
    path: '/SeatStatus',
    component: () => import('../components/seat/SeatStatus.vue'),
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
