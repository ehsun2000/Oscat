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

// 設置定時檢查
setInterval(async () => {
  // 獲取當前路由
  const currentRoute = router.currentRoute.value;

  // 如果用戶不在登入頁面
  if (currentRoute.name !== 'Login') {
    try {
      // 發送請求以檢查用戶是否已登入
      const response = await fetch(
        `${import.meta.env.VITE_CHECK_API_ENDPOINT}`,
        {
          method: 'GET',
          credentials: 'include',
        },
      );

      if (response.status != 200) {
        // 如果用戶未登入，則重定向到登入頁面
        router.push('/adminlogin');
      }
    } catch (error) {
      router.push('/adminlogin');
    }
  }
}, 120000);
