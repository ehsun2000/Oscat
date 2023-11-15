<template>
  <div class="container w-25">
    <h1 class="h3 mb-3 fw-normal">請輸入email</h1>
    <div class="form-floating">
      <input
        type="email"
        class="form-control"
        id="email"
        v-model="email"
        placeholder="name@example.com"
        required
        @blur="checkEmailBlur"
      />
      <div class="input-error">
        <span class="error-message text-danger">{{ emailErrMsg }}</span>
      </div>
    </div>
    <div class="text-center mt-3">
      <div class="d-grid gap-2 d-md-flex justify-content-md-end">
        <button class="btn btn-primary btn-sm" type="button" @click="getOtp">
          取得OTP
        </button>
        <button
          class="btn btn-secondary btn-sm"
          type="button"
          @click="backPage"
        >
          返回
        </button>
        <button
          type="button"
          class="btn btn-outline-secondary btn-sm"
          @click="demo"
        >
          demo
        </button>
      </div>
    </div>
    <p class="mt-5 mb-3 text-body-secondary">&copy; OSCAT 2023</p>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import Swal from 'sweetalert2';
import axios from 'axios';
import { useRouter } from 'vue-router';

const router = useRouter();
const email = ref('');
const emailErrMsg = ref('');
const api = import.meta.env.VITE_OSCATOfficial_API_ENDPOINT;

// 驗證email
const checkEmailBlur = () => {
  if (!email.value) {
    emailErrMsg.value = '請輸入信箱';
  } else {
    emailErrMsg.value = '';
  }
};

// 確認驗證碼
const checkOtp = async (otp) => {
  const url = `${api}/member/checkOtp`;
  const response = await axios.post(url, { value: otp });
  if (response.status === 200) {
    // console.log('驗證成功重導向');
    router.push('/reset-pwd');
  } else {
    Swal.fire({
      title: '驗證碼錯誤',
      text: '請重新操作',
      icon: 'error',
    });
    router.push('/forgot-pwd');
  }
};

const getOtp = async () => {
  if (email.value === '') {
    await Swal.fire({
      title: '請輸入email',
      text: '請輸入email',
      icon: 'error',
    });
  } else {
    try {
      const url = `${api}/member/sendOtp`;
      const response = await axios.post(url, { email: email.value });
      if (response.status === 200) {
        const { value: otp } = await Swal.fire({
          title: '請輸入驗證碼',
          input: 'text',
          inputAttributes: {
            autocapitalize: 'off',
          },
          showCancelButton: true,
          confirmButtonText: '確認',
          cancelButtonText: '取消',
          showLoaderOnConfirm: true,
          preConfirm: (input) => {
            if (!input) {
              Swal.showValidationMessage('請輸入驗證碼');
            }
          },
        });
        if (otp) {
          checkOtp(otp); // 在這裡調用驗證函式，傳入驗證碼
        }
      }
    } catch (error) {
      await Swal.fire({
        title: '查無信箱請註冊',
        icon: 'error',
        showConfirmButton: false,
      });
    }  
  }
};

// 一鍵輸入
const demo = () => {
  email.value = 'interpretationlove@gmail.com';
};

// 返回
const backPage = () => {
  history.back();
};
</script>

<style scoped>
/* 輸入框的placeholder */
::placeholder {
  color: #999 !important;
  font-size: 14px !important;
}

/* 錯誤訊息 */
.error-message {
  font-size: 12px;
}
</style>
