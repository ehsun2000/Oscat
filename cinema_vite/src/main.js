import { createApp } from 'vue';
import { createPinia } from 'pinia';
import 'bootstrap/dist/css/bootstrap.min.css';
import '@sweetalert2/theme-dark/dark.css';
import App from './App.vue';
import router from './router';

const app = createApp(App);

app.use(router).use(createPinia()).mount('#app');
