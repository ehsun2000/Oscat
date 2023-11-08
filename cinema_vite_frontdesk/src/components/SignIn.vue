<template>
  <div class="container w-25">
    <h1 class="h3 mb-3 fw-normal">會員登入</h1>
    <div class="form-floating">
      <input
        type="email"
        class="form-control"
        id="email"
        v-model="email"
        placeholder="請輸入email"
        required
        @blur="checkEmailBlur"
      />
      <div class="input-error">
        <span class="error-message text-danger">{{ emailErrMsg }}</span>
      </div>
    </div>
    <div class="form-floating">
      <input
        type="password"
        class="form-control"
        id="password"
        v-model="password"
        placeholder="請輸入密碼"
        required
        @blur="checkPwdBlur"
      />
      <div class="input-error">
        <span class="error-message text-danger">{{ pwdErrMsg }}</span>
      </div>
    </div>
    <div class="text-start my-3">
      <RouterLink :to="'/forgotPwd'">
        <button class="btn-text-only" role="button">忘記密碼</button>
      </RouterLink>
    </div>
    <button class="btn btn-primary w-100 py-2" type="submit" @click="signin">
      登入
    </button>
    <p class="mt-5 mb-3 text-body-secondary">&copy; OSCAT 2023</p>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import axios from 'axios';
import Swal from 'sweetalert2';
import { useRouter } from 'vue-router';
import { useUserStore } from '@/stores/userStore.js';

const email = ref('');
const password = ref('');
const router = useRouter();
const userStore = useUserStore();

// const pwdError = ref(false);
const pwdErrMsg = ref('');
const emailErrMsg = ref('');

// 驗證email
const checkEmailBlur = () => {
  if (!email.value) {
    // emailError.value = true;
    emailErrMsg.value = '不可空白，請輸入信箱';
  } else {
    emailErrMsg.value = '';
  }
};

// 驗證密碼
const checkPwdBlur = () => {
  if (!password.value) {
    // pwdError.value = true;
    pwdErrMsg.value = '不可空白，請輸入密碼';
  } else {
    pwdErrMsg.value = '';
  }
};

// 登入
const signin = async () => {
  try {
    const url = `${
      import.meta.env.VITE_OSCATOfficial_API_ENDPOINT
    }/member/login`;
    const response = await axios.post(url, {
      email: email.value,
      password: password.value,
    });
    console.log(response.data);
    if (response.status === 200) {
      sessionStorage.setItem('isLogin', 'true');
      userStore.setAuthenticated(true); // 設置身份驗證狀態

      await Swal.fire({
        title: '登入成功',
        icon: 'success',
        timer: 1500,
        showConfirmButton: false,
      });

      // 重定向到之前的頁面或預設頁面
      if (userStore.redirectAfterLogin) {
        router.push(userStore.redirectAfterLogin);
        userStore.setRedirectAfterLogin(null); // 清除重定向路徑
        setTimeout(() => {
          window.location.reload();
        }, 500);
      } else {
        router.push('/');
        setTimeout(() => {
          window.location.reload();
        }, 1000);
      }
    }
  } catch (error) {
    console.log(error.response);
    await Swal.fire({
      title: error.response.data.message,
      icon: 'error',
    });
  }
};
</script>

<style scoped>
/* 輸入框的placeholder */
::placeholder {
  color: #999 !important;
  font-size: 14px !important;
}

/* 忘記密碼 */
.btn-text-only {
  border: none;
  padding: 0;
  background-color: transparent;
  color: inherit;
  text-decoration: none;
}

.btn-text-only:focus,
.btn-text-only:hover {
  background-color: transparent;
  color: inherit;
  text-decoration: none;
}

/* 錯誤訊息 */
.error-message {
  font-size: 12px;
}
</style>
