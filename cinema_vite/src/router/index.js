import { createRouter, createWebHistory } from 'vue-router';
import Home from '../views/HomeVue.vue';
import Movie from '../views/AdminMovie.vue';
import Edit from '../views/AdminEditMovie.vue';
import Add from '../views/AdminAddMovie.vue';

const routes = [
  { path: '/', component: Home },
  { path: '/movie', component: Movie },
  { path: '/movie/edit/:movieId', component: Edit },
  { path: '/movie/add', component: Add },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
