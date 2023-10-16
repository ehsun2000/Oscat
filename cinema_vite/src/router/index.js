import { createRouter, createWebHistory } from 'vue-router';
import SeatController from '../components/seat/SeatController.vue';

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('../App.vue'),
  },
  {
    path: '/seatcontroller',
    component: SeatController,
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
