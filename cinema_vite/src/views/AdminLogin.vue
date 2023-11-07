<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useUserStore } from '@/stores/index.js';
import Swal from 'sweetalert2/dist/sweetalert2.js';

const router = useRouter();
const account = ref('admin');
const password = ref('password');

// 創建一個非同步函數來處理登入邏輯
async function handleLogin() {
  // 使用 userStore
  const userStore = useUserStore();

  // 讀取用戶輸入的 account 和 password
  const accountValue = account.value;
  const passwordValue = password.value;

  // 從環境變量中讀取 API 端點
  const apiEndpoint = import.meta.env.VITE_LOGIN_API_ENDPOINT;
  const apiLink = apiEndpoint;

  // 設置請求選項
  const requestOptions = {
    method: 'POST',
    credentials: 'include', // 憑證模式設為 'include'
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      account: accountValue,
      password: passwordValue,
    }),
  };

  try {
    const response = await fetch(apiLink, requestOptions);
    if (response.ok) {
      // 使用 Pinia 的 userStore 來更新登入狀態
      userStore.login();

      // 跳轉到 dashboard 頁面
      router.push({ path: '/dashboard' });
    } else {
      Swal.fire({
        icon: 'error',
        title: '登入失敗',
        text: '用戶名或密碼不正確',
      });
    }
  } catch (error) {
    Swal.fire({
      icon: 'error',
      title: '出錯了',
      text: '無法完成登入',
    });
  }
}
</script>

<template>
  <div class="container py-5 h-100">
    <div class="row d-flex justify-content-center align-items-center h-100">
      <div class="col-12 col-md-8 col-lg-6 col-xl-5">
        <div class="card bg-dark text-white" style="border-radius: 1rem">
          <div class="card-body p-5 text-center">
            <div class="md-5 mt-md-4 pb-5">
              <h2 class="fw-bold mb-2 text-uppercase">Login</h2>

              <form @submit.prevent="handleLogin">
                <div class="form-outline form-white mb-4">
                  <input
                    type="text"
                    v-model="account"
                    id="account"
                    class="form-control form-control-lg text-light"
                    required
                  />
                  <label class="form-label" for="account">Account</label>
                </div>

                <div class="form-outline form-white mb-4">
                  <input
                    type="password"
                    v-model="password"
                    id="password"
                    class="form-control form-control-lg text-light"
                    required
                  />
                  <label class="form-label" for="password">Password</label>
                </div>

                <button class="btn btn-outline-light btn-lg px-5" type="submit">
                  Login
                </button>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
