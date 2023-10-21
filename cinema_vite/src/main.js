import { createApp } from 'vue';
import { createPinia } from 'pinia';

import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle.min';
import 'bootstrap-icons/font/bootstrap-icons.css';

import '@sweetalert2/theme-dark/dark.css';

import App from './App.vue';
import router from './router';
import axios from 'axios';

axios.defaults.withCredentials = true;
document.body.setAttribute('data-bs-theme', 'dark');

const app = createApp(App);
app.use(router).use(createPinia()).mount('#app');
