import { createRouter, createWebHistory } from 'vue-router';
import About from '../components/AboutVue.vue';
import Contact from '../components/ContactVue.vue';
import Home from '../components/HomeVue.vue';
import Movie from '../components/MovieVue.vue';

const routes = [
  { path: '/', component: Home },
  { path: '/about', component: About },
  { path: '/contact', component: Contact },
  { path: '/movie', component: Movie },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
