<template>
  <div class="row">
    <div class="mb-3">
      <h2>會員修改</h2>
      <div class="addFrame">
        <label for="exampleInputName1" class="form-label">姓名</label>
        <input
          type="text"
          class="form-control"
          id="memberName"
          aria-describedby="nameHelp"
          v-bind:class="{ 'is-invalid': nameError }"
          v-model="member.memberName"
          required
          @blur="checkNameBlur"
          @input="checkName"
        />
        <div id="nameError" class="error-message">{{ nameErrMsg }}</div>

        <div class="mb-3">
          <label for="exampleInputEmail1" class="form-label">信箱</label>
          <input
            type="email"
            class="form-control"
            id="email"
            aria-describedby="emailHelp"
            v-bind:class="{ 'is-invalid': emailError }"
            v-model="member.email"
            required
            @blur="checkEmailBlur"
            @input="checkEmail"
          />
          <div id="emailError" class="error-message">{{ emailErrMsg }}</div>
        </div>
        <div class="mb-3">
          <label for="exampleInputPassword1" class="form-label">密碼</label>
          <input
            type="password"
            class="form-control"
            id="password"
            v-bind:class="{ 'is-invalid': pwdError }"
            v-model="member.password"
            required
            @blur="checkPwdBlur"
            @input="checkPwd"
          />
          <div id="passwordHelp" class="form-text">
            請輸入包含英文跟數字，大於8位的密碼
          </div>
        </div>
        <div id="pwdError" class="error-message">{{ pwdErrMsg }}</div>
        <div class="mb-3">
          <label for="exampleInputPhone" class="form-label">手機</label>
          <input
            type="text"
            class="form-control"
            id="phone"
            aria-describedby="phoneHelp"
            v-bind:class="{ 'is-invalid': phoneError }"
            v-model="member.phone"
            @blur="checkPhoneBlur"
            @input="checkPhone"
          />
          <div id="phoneError" class="error-message">{{ phoneErrMsg }}</div>
        </div>
        <div class="mb-3">
          <label for="exampleInputPhone" class="form-label">性別</label>

          <div class="form-check form-check-inline">
            <input
              class="form-check-input"
              type="radio"
              name="inlineRadioOptions"
              id="inlineRadio1"
              value="man"
              v-model="member.gender"
            />
            <label class="form-check-label" for="inlineRadio1">男</label>
          </div>
          <div class="form-check form-check-inline">
            <input
              class="form-check-input"
              type="radio"
              name="inlineRadioOptions"
              id="inlineRadio2"
              value="female"
              v-model="member.gender"
            />
            <label class="form-check-label" for="inlineRadio2">女</label>
          </div>
          <div class="form-check form-check-inline">
            <input
              class="form-check-input"
              type="radio"
              name="inlineRadioOptions"
              id="inlineRadio3"
              value="other"
              v-model="member.gender"
            />
            <label class="form-check-label" for="inlineRadio3">其他</label>
          </div>
        </div>
        <div class="mb-3">
          <label for="exampleInputDate" class="form-label">生日</label>
          <input
            type="date"
            class="form-control"
            id="birthDate"
            aria-describedby="phoneHelp"
            v-model="member.birthDate"
          />
        </div>
        <button class="btn btn-primary" type="button" @click="editHandler">
          修改
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue';
import axios from 'axios';
import { useRoute, useRouter } from 'vue-router';
import Member from '@/models/Member.js';
import Swal from 'sweetalert2';

const route = useRoute();
const router = useRouter();
const member = ref(Member);

// 拿原本的資料
const existData = async () => {
  const memberId = route.params.memberId;
  const update_url = `${
    import.meta.env.VITE_OSCAT_API_ENDPOINT
  }/member/${memberId}`;
  const { data } = await axios.get(update_url);
  member.value = data;
};
existData();

// 宣告姓名、錯誤狀況和訊息
const memberName = computed({
  get: () => member.value.memberName,
  set: (value) => {
    member.value.memberName = value;
    checkName();
  },
});
const nameError = ref(false);
const nameErrMsg = ref('');

const email = computed({
  get: () => member.value.email,
  set: (value) => {
    member.value.email = value;
    checkEmail();
  },
});
const emailError = ref(false);
const emailErrMsg = ref('');

const password = computed({
  get: () => member.value.password,
  set: (value) => {
    member.value.password = value;
    checkPwd();
  },
});
const pwdError = ref(false);
const pwdErrMsg = ref('');

const phone = computed({
  get: () => member.value.phone,
  set: (value) => {
    member.value.phone = value;
    checkPhone();
  },
});
const phoneError = ref(false);
const phoneErrMsg = ref('');

// @blur
const checkNameBlur = () => {
  if (!memberName.value) {
    nameError.value = true;
    nameErrMsg.value = '不可空白，請輸入姓名';
  }
};

// @input
const checkName = () => {
  if (!memberName.value) {
    nameError.value = true;
    nameErrMsg.value = '不可空白，請輸入姓名';
  } else {
    nameError.value = false;
    nameErrMsg.value = '';
  }
};

const checkEmailBlur = () => {
  if (!email.value) {
    emailError.value = true;
    emailErrMsg.value = '不可空白，請輸入信箱';
  }
};

const checkEmail = () => {
  let emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
  if (!email.value) {
    emailError.value = true;
    emailErrMsg.value = '不可空白，請輸入信箱';
  } else if (!emailPattern.test(email.value)) {
    emailError.value = true;
    emailErrMsg.value = 'email格式錯誤';
  } else {
    emailError.value = false;
    emailErrMsg.value = '';
  }
};

// 信箱是否重複

const newEmail = ref('');
const oldEmail = ref('');

watch(
  () => member.value.email,
  (newValue, oldValue) => {
    oldEmail.value = oldValue;
    newEmail.value = newValue;
  },
);

const emailExists = ref(false);

const checkEmailRepeat = async () => {
  if (oldEmail.value && newEmail.value !== oldEmail.value) {
    const checkEmail_url = `${
      import.meta.env.VITE_OSCAT_API_ENDPOINT
    }/member/add/${member.value.email}`;
    try {
      const response = await axios.get(checkEmail_url);
      if (response.data == 'Y') {
        emailExists.value = true;
        emailErrMsg.value = '該信箱已被註冊';
      } else {
        emailExists.value = false;
        emailErrMsg.value = '';
      }
    } catch (error) {
      emailExists.value = true;
      emailErrMsg.value = '該信箱已被註冊';
      throw error;
    }
  }
};

const checkPwdBlur = () => {
  if (!password.value) {
    pwdError.value = true;
    pwdErrMsg.value = '不可空白，請輸入密碼';
  }
};

const checkPwd = () => {
  let include = /^(?=.*\d)(?=.*[a-zA-Z]).{8,}$/;
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

const checkPhoneBlur = () => {
  if (!phone.value) {
    phoneError.value = true;
    phoneErrMsg.value = '不可空白，請輸入號碼';
  }
};

const checkPhone = () => {
  let phonePattern =
    /^(\d{2,3}-?|\(\d{2,3}\))\d{3,4}-?\d{4}|09\d{2}(\d{6}|-\d{3}-\d{3})$/;
  if (!phone.value) {
    phoneError.value = true;
    phoneErrMsg.value = '不可空白，請輸入號碼';
  } else if (!phonePattern.test(phone.value)) {
    phoneError.value = true;
    phoneErrMsg.value = '手機格式錯誤';
  } else if (phone.value.replace(/[^0-9]/g, '').length > 10) {
    phoneError.value = true;
    phoneErrMsg.value = '號碼不可超過10位數字';
  } else {
    phoneError.value = false;
    phoneErrMsg.value = '';
  }
};

// 送出處理
const editHandler = async () => {
  await checkEmailRepeat();
  if (emailExists.value) {
    Swal.fire({
      title: '該信箱已被註冊',
      icon: 'error',
      confirmButtonColor: '#3085d6',
      confirmButtonText: '好的',
    });
    return; // 不提交表單
  }

  Swal.fire({
    title: '確定要修改嗎?',
    icon: 'question',
    showCancelButton: true,
    confirmButtonColor: '#3085d6',
    cancelButtonColor: '#d33',
    confirmButtonText: '確定',
    cancelButtonText: '取消',
  }).then((result) => {
    if (result.isConfirmed) {
      const update_url = `${
        import.meta.env.VITE_OSCAT_API_ENDPOINT
      }/member/update/${member.value.memberId}`;
      axios
        .put(update_url, member.value)
        .then((response) => {
          if (response.status === 200) {
            Swal.fire('修改成功', '', 'success').then(() => {
              router.push('/member');
            });
          } else {
            Swal.fire('修改失敗', '', 'error');
          }
        })
        .catch((error) => {
          Swal.fire('修改大失敗', error.message, 'error');
        });
    }
  });
};
</script>

<style scoped>
h2 {
  padding: 10px 0;
  text-align: center;
}

.addFrame {
  width: 80%;
  max-width: 400px;
  margin: 0 auto;
  padding: 20px;
  box-sizing: border-box;
}
.form-label {
  margin-right: 20px;
}
</style>
