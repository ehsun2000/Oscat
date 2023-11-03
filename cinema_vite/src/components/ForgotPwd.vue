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

// 驗證email
const checkEmailBlur = () => {
  if (!email.value) {
    // emailError.value = true;
    emailErrMsg.value = '請輸入信箱';
  }
};

// 確認驗證碼
const checkOtp = async (otp) => {
  const url = `${import.meta.env.VITE_OSCAT_API_ENDPOINT}/member/checkOtp`;
  const response = await axios.post(url, { value: otp });
  // console.log(response);
  // console.log(`輸入的驗證碼是: ${otp}`);
  if (response.status === 200) {
    console.log('驗證成功重導向');
    router.push('/resetPwd');
  } else {
    Swal.fire({
      title: '驗證碼錯誤',
      text: '請重新操作',
      icon: 'error',
    });
    router.push('/forgotPwd');
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
    const url = `${import.meta.env.VITE_OSCAT_API_ENDPOINT}/member/sendOtp`;
    // console.log(url);
    const response = await axios.post(url, { email: email.value });
    // console.log(response);
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
  }
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
