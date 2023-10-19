import { createRouter, createWebHistory } from 'vue-router';

const routes = [
  {
    path: '/',
    component: () => import('../components/HomeVue.vue'),
  },
  {
    path: '/about',
    component: () => import('../components/AboutVue.vue'),
  },
  {
    path: '/contact',
    component: () => import('../components/ContactVue.vue'),
  },
  {
    path: '/products',
    component: () => import('../components/product/ProductsList.vue'),
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
