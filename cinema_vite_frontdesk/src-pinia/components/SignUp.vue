<template>
  <div class="container w-25">
    <div class="card">
      <h5 class="card-header">註冊會員</h5>
      <div class="card-body">
        <form class="needs-validation" @submit.prevent="onSubmit">
          <div class="row g-3">
            <div class="col-sm-6">
              <label for="memberName" class="form-label">姓名</label>
              <input
                type="text"
                class="form-control"
                id="memberName"
                :class="{ 'is-invalid': nameError }"
                v-model="member.memberName"
                required
                @blur="checkNameBlur"
                @input="checkName"
              />
              <div id="nameError" class="error-message text-danger">
                {{ nameErrMsg }}
              </div>
            </div>

            <div class="col-sm-6">
              <label for="phone" class="form-label">手機</label>
              <input
                type="text"
                class="form-control"
                id="phone"
                :class="{ 'is-invalid': phoneError }"
                v-model="member.phone"
                @blur="checkPhoneBlur"
                @input="checkPhone"
                required
              />
              <div id="phoneError" class="error-message text-danger">
                {{ phoneErrMsg }}
              </div>
            </div>

            <div class="col-12">
              <label for="email" class="form-label">信箱</label>
              <input
                type="text"
                class="form-control"
                id="email"
                :class="{ 'is-invalid': emailError }"
                v-model="member.email"
                placeholder="name@example.com"
                required
                @blur="checkEmailBlur"
                @input="checkEmail"
              />
              <div id="emailError" class="error-message text-danger">
                {{ emailErrMsg }}
              </div>
            </div>

            <div class="col-12">
              <label for="password" class="form-label">密碼</label>
              <div class="input-group has-validation">
                <input
                  type="password"
                  class="mb-2 form-control"
                  id="password"
                  aria-describedby="inputGroupPrepend"
                  :class="{ 'is-invalid': pwdError }"
                  v-model="member.password"
                  required
                  @blur="checkPwdBlur"
                  @input="checkPwd"
                />
                <div id="pwdError" class="error-message text-danger">
                  {{ pwdErrMsg }}
                </div>
              </div>

              <div class="col-md-6">
                <label for="birthDate" class="form-label">生日</label>
                <input
                  type="date"
                  class="mb-2 form-control"
                  id="birthDate"
                  v-model="member.birthDate"
                  required
                />
              </div>

              <div class="col-md-6">
                <label for="gender" class="form-label">性別</label>
                <select
                  class="form-select"
                  id="gender"
                  v-model="member.gender"
                  required
                >
                  <option selected disabled value="">...</option>
                  <option value="man">男</option>
                  <option value="female">女</option>
                  <option value="other">其它</option>
                </select>
              </div>
            </div>
          </div>

          <hr class="my-4" />

          <div class="form-check">
            <input
              class="form-check-input"
              type="checkbox"
              value=""
              id="invalidCheck"
              required
            />
            <label class="form-check-label" for="invalidCheck">
              我已閱讀並同意<RouterLink :to="'/agreement'"
                >使用條款/隱私政策</RouterLink
              >
            </label>
          </div>

          <hr class="my-4" />

          <button class="w-100 btn btn-primary btn-lg" type="submit">
            註冊
          </button>
        </form>
      </div>
    </div>
    <p class="mt-5 mb-3 text-body-secondary">&copy; OSCAT 2023</p>
  </div>
</template>

<script setup>
import Member from '@/models/member.js';
import axios from 'axios';
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import Swal from 'sweetalert2';

const member = ref(Member);
const router = useRouter();

const api = import.meta.env.VITE_OSCATOfficial_API_ENDPOINT;

// 防止表單預設提交
const onSubmit = (event) => {
  event.preventDefault(); // 防止表單預設提交行為
  register();
};

// 驗證姓名
const nameError = ref(false);
const nameErrMsg = ref('');

const checkNameBlur = () => {
  if (!member.value.memberName) {
    nameError.value = true;
    nameErrMsg.value = '不可空白，請輸入姓名';
  }
};

const checkName = () => {
  if (!member.value.memberName) {
    nameError.value = true;
    nameErrMsg.value = '不可空白，請輸入姓名';
  } else {
    nameError.value = false;
    nameErrMsg.value = '';
  }
};

// 驗證手機
const phoneError = ref(false);
const phoneErrMsg = ref('');

const checkPhoneBlur = () => {
  if (!member.value.phone) {
    phoneError.value = true;
    phoneErrMsg.value = '不可空白，請輸入號碼';
  }
};

const checkPhone = () => {
  let phonePattern =
    /^(\d{2,3}-?|\(\d{2,3}\))\d{3,4}-?\d{4}|09\d{2}(\d{6}|-\d{3}-\d{3})$/;
  if (!member.value.phone) {
    phoneError.value = true;
    phoneErrMsg.value = '不可空白，請輸入號碼';
  } else if (!phonePattern.test(member.value.phone)) {
    phoneError.value = true;
    phoneErrMsg.value = '手機格式錯誤';
  } else {
    phoneError.value = false;
    phoneErrMsg.value = '';
  }
};

// 驗證 email
const emailError = ref(false);
const emailErrMsg = ref('');

const checkEmailBlur = async () => {
  if (!member.value.email) {
    emailError.value = true;
    emailErrMsg.value = '不可空白，請輸入信箱';
  } else {
    await checkEmailRepeat(); // 驗證 email 是否已存在
  }
};

const checkEmail = () => {
  let emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
  if (!member.value.email) {
    emailError.value = true;
    emailErrMsg.value = '不可空白，請輸入信箱';
  } else if (!emailPattern.test(member.value.email)) {
    emailError.value = true;
    emailErrMsg.value = 'email格式錯誤';
  } else {
    emailError.value = false;
    emailErrMsg.value = '';
  }
};

// 驗證 email 是否重複
const emailExists = ref(false);

const checkEmailRepeat = async () => {
  const url = `${api}/member/check`;
  try {
    const response = await axios.post(url, { email: member.value.email });
    // console.log(member.value.email);
    // console.log(response);
    if (response.status === 200) {
      emailExists.value = false;
      emailErrMsg.value = '';
    }
  } catch (error) {
    emailExists.value = true;
    emailErrMsg.value = '該信箱已被註冊';
    // console.log(error);
  }
};

// 驗證密碼
const pwdError = ref(false);
const pwdErrMsg = ref('');

const checkPwdBlur = () => {
  if (!member.value.password) {
    pwdError.value = true;
    pwdErrMsg.value = '不可空白，請輸入密碼';
  }
};

const checkPwd = () => {
  let include = /^(?=.*[a-zA-Z])(?=.*\d).{8,}$/;
  if (!member.value.password) {
    pwdError.value = true;
    pwdErrMsg.value = '不可空白，請輸入密碼';
  } else if (member.value.password.length < 8) {
    pwdError.value = true;
    pwdErrMsg.value = '請勿少於8個字';
  } else if (!include.test(member.value.password)) {
    pwdError.value = true;
    pwdErrMsg.value = '至少包括一個英文字母或數字';
  } else {
    pwdError.value = false;
    pwdErrMsg.value = '';
  }
};

// 註冊會員
const register = async () => {
  await checkEmailRepeat();
  if (emailExists.value) {
    alert('新增失敗 - 該信箱已被註冊');
    return; // 不提交表單
  }
  try {
    const url = `${api}/member/register`;
    const response = await axios.post(url, member.value);
    if (response.status === 200) {
      await Swal.fire({
        title: '註冊成功',
        icon: 'success',
        timer: 1500, // 1.5秒後關閉畫面
        showConfirmButton: false,
      });
      // 導到首頁
      router.push('/');
    } else {
      await Swal.fire({
        title: '註冊失敗',
        text: '請檢查輸入格式',
        icon: 'error',
      });
    }
  } catch (error) {
    console.error('註冊出錯：', error);
  }
};

const restart = () => {
  member.value = ref(Member);
};
restart();
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
