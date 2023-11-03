<template>
  <div class="container w-25">
    <div class="card">
      <h5 class="card-header">重設密碼</h5>
      <div class="card-body">
        <form class="needs-validation" @submit.prevent="onSubmit">
          <div class="row g-3">
            <div class="col-12">
              <label for="password" class="form-label">密碼</label>
              <div class="input-group has-validation">
                <input
                  type="password"
                  class="form-control"
                  id="password"
                  aria-describedby="inputGroupPrepend"
                  :class="{ 'is-invalid': pwdError }"
                  v-model="password"
                  required
                  @blur="checkPwdBlur"
                  @input="checkPwd"
                />
                <div id="pwdError" class="error-message text-danger">
                  {{ pwdErrMsg }}
                </div>
              </div>
            </div>
            <div class="col-12">
              <label for="checkPwd" class="form-label">確認密碼</label>
              <input
                type="password"
                class="form-control"
                id="checkPwd"
                aria-describedby="inputGroupPrepend"
                :class="{ 'is-invalid': checkPwdError }"
                v-model="checkPassword"
                required
                @blur="checkCheckPwdBlur"
                @input="checkCheckPwd"
              />
              <div id="checkPwdError" class="error-message text-danger">
                {{ checkPwdErrMsg }}
              </div>
            </div>
          </div>
          <hr class="my-4" />
          <button class="w-100 btn btn-primary btn-lg" type="submit">
            重設密碼
          </button>
        </form>
      </div>
    </div>
    <p class="mt-5 mb-3 text-body-secondary">&copy; OSCAT 2023</p>
  </div>
</template>

<script setup>
import axios from 'axios';
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import Swal from 'sweetalert2';

const router = useRouter();
const password = ref('');
const checkPassword = ref('');
const pwdError = ref(false);
const pwdErrMsg = ref('');
const checkPwdError = ref(false);
const checkPwdErrMsg = ref('');

// 驗證密碼
const checkPwdBlur = () => {
  if (!password.value) {
    pwdError.value = true;
    pwdErrMsg.value = '不可空白，請輸入密碼';
  }
};

const checkPwd = () => {
  let include = /^(?=.*[a-zA-Z])(?=.*\d).{8,}$/;
  if (!password.value) {
    pwdError.value = true;
    pwdErrMsg.value = '不可空白，請輸入密碼';
  } else if (password.value.length < 8) {
    pwdError.value = true;
    pwdErrMsg.value = '請勿少於8個字';
  } else if (!include.test(password.value)) {
    pwdError.value = true;
    pwdErrMsg.value = '至少包括一個英文字母或數字';
  } else {
    pwdError.value = false;
    pwdErrMsg.value = '';
  }
};

const checkCheckPwdBlur = () => {
  if (!checkPassword.value) {
    checkPwdError.value = true;
    checkPwdErrMsg.value = '不可空白，請再次輸入密碼';
  } else if (checkPassword.value != password.value) {
    checkPwdError.value = true;
    checkPwdErrMsg.value = '確認密碼與密碼不符';
  } else {
    checkPwdError.value = false;
    checkPwdErrMsg.value = '';
  }
};

const checkCheckPwd = () => {
  if (!checkPassword.value) {
    checkPwdError.value = true;
    checkPwdErrMsg.value = '不可空白，請再次輸入密碼';
  } else if (checkPassword.value !== password.value) {
    checkPwdError.value = true;
    checkPwdErrMsg.value = '確認密碼與密碼不符';
  } else {
    checkPwdError.value = false;
    checkPwdErrMsg.value = '';
  }
};

const onSubmit = async () => {
  const url = `${import.meta.env.VITE_OSCAT_API_ENDPOINT}/member/resetPwd`;
  console.log(url);
  const response = await axios.post(url, {
    password: password.value,
  });
  console.log(response);
  if (response.status === 200) {
    await Swal.fire({
      title: '修改成功',
      icon: 'success',
      timer: 1500, // 1.5秒後關閉畫面
      showConfirmButton: false,
    });
    router.push('/signin');
  } else {
    await Swal.fire({
      title: '修改失敗',
      text: '請檢查密碼',
      icon: 'error',
    });
  }
};
</script>

<style scoped></style>
