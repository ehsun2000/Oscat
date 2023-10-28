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
        <button class="btn btn-primary btn-sm" type="button" @click="signin">
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

const email = ref('');
const emailErrMsg = ref('');

// 驗證email
const checkEmailBlur = () => {
  if (!email.value) {
    // emailError.value = true;
    emailErrMsg.value = '請輸入信箱';
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
