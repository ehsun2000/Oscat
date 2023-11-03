import { createApp } from 'vue';
import { createPinia } from 'pinia';

import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle.min';
import 'bootstrap-icons/font/bootstrap-icons.css';

import 'v-calendar/style.css';
import '@sweetalert2/theme-dark/dark.css';

import VCalendar from 'v-calendar';
import App from './App.vue';
import router from './router';
import axios from 'axios';
import Swal from 'sweetalert2';

axios.defaults.withCredentials = true;
document.body.setAttribute('data-bs-theme', 'dark');

const app = createApp(App);
app.use(router).use(createPinia()).use(VCalendar, {}).mount('#app');

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
        const { isConfirmed } = await Swal.fire({
          title: '驗證失效',
          text: '您的驗證已失效，請重新登入',
          icon: 'info',
          confirmButtonText: '回到登入頁面',
        });

        if (isConfirmed) {
          router.push('/adminlogin');
        }
      }
    } catch (error) {
      const { isConfirmed } = await Swal.fire({
        title: '驗證失效',
        text: '您的驗證已失效，請重新登入',
        icon: 'info',
        confirmButtonText: '回到登入頁面',
      });

      if (isConfirmed) {
        router.push('/adminlogin');
      }
    }
  }
}, 10000);
