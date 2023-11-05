<template>
  <div class="container">
    <h3>會 員 中 心</h3>
    <div class="py-5 text-center"></div>

    <div class="row g-5">
      <div class="col-md-5 col-lg-4 order-md-last">
        <h4 class="d-flex justify-content-between align-items-center mb-3">
          <span class="text-primary">訂單紀錄</span>
          <span class="badge bg-primary rounded-pill">3</span>
        </h4>
        <ul class="list-group mb-3">
          <li class="list-group-item d-flex justify-content-between lh-sm">
            <div>
              <h6 class="my-0">Product name</h6>
              <small class="text-body-secondary">Brief description</small>
            </div>
            <span class="text-body-secondary">$12</span>
          </li>
          <li class="list-group-item d-flex justify-content-between lh-sm">
            <div>
              <h6 class="my-0">Second product</h6>
              <small class="text-body-secondary">Brief description</small>
            </div>
            <span class="text-body-secondary">$8</span>
          </li>
          <li class="list-group-item d-flex justify-content-between lh-sm">
            <div>
              <h6 class="my-0">Third item</h6>
              <small class="text-body-secondary">Brief description</small>
            </div>
            <span class="text-body-secondary">$5</span>
          </li>
          <li
            class="list-group-item d-flex justify-content-between bg-body-tertiary"
          >
            <div class="text-success">
              <h6 class="my-0">Promo code</h6>
              <small>EXAMPLECODE</small>
            </div>
            <span class="text-success">−$5</span>
          </li>
          <li class="list-group-item d-flex justify-content-between">
            <span>Total (USD)</span>
            <strong>$20</strong>
          </li>
        </ul>

        <!-- <form class="card p-2">
          <div class="input-group">
            <input type="text" class="form-control" placeholder="Promo code" />
            <button type="submit" class="btn btn-secondary">Redeem</button>
          </div>
        </form> -->
      </div>
      <div class="col-md-7 col-lg-8">
        <h4 class="mb-3">個人資料</h4>
        <form class="needs-validation" @submit="modifyMember" novalidate>
          <div class="row g-3">
            <div class="col-sm-6">
              <label for="memberName" class="form-label">姓名</label>
              <input
                type="text"
                class="form-control"
                id="memberName"
                :class="{ 'is-invalid': nameError }"
                v-model="member.memberName"
                placeholder="請輸入中文或英文名"
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
                placeholder="請輸入手機號碼"
                v-model="member.phone"
                @blur="checkPhoneBlur"
                @input="checkPhone"
              />
              <div id="phoneError" class="error-message text-danger">
                {{ phoneErrMsg }}
              </div>
            </div>

            <div class="col-sm-6">
              <label for="birthDate" class="form-label">生日</label>
              <input
                type="text"
                class="form-control"
                id="birthDate"
                v-model="member.birthDate"
                readonly
              />
            </div>

            <div class="col-sm-6">
              <label class="form-label">性別</label>
              <div class="form-check">
                <input
                  type="radio"
                  id="man"
                  value="man"
                  v-model="member.gender"
                  class="form-check-input"
                />
                <label for="man" class="form-check-label">男</label>
              </div>
              <div class="form-check">
                <input
                  type="radio"
                  id="female"
                  value="female"
                  v-model="member.gender"
                  class="form-check-input"
                />
                <label for="female" class="form-check-label">女</label>
              </div>
            </div>

            <div class="col-12">
              <label for="email" class="form-label">信箱</label>
              <input
                type="email"
                class="form-control"
                id="email"
                :class="{ 'is-invalid': emailError }"
                placeholder="請輸入信箱"
                v-model="member.email"
                @blur="checkEmailBlur"
                @input="checkEmail"
              />
              <div id="emailError" class="error-message text-danger">
                {{ emailErrMsg }}
              </div>
            </div>

            <div class="col-12">
              <label for="password" class="form-label">密碼</label>
              <input
                type="password"
                class="form-control"
                id="password"
                v-model="member.password"
              />
            </div>

            <div class="col-12">
              <label for="joinDate" class="form-label">加入日期</label>
              <input
                type="email"
                class="form-control"
                id="joinDate"
                v-model="formatJoinDate"
                readonly
              />
            </div>
          </div>

          <hr class="my-4" />

          <button class="btn btn-primary btn-sm" type="submit">
            <i class="bi bi-pencil-fill"></i>
            修改個人資料
          </button>
          <button
            type="button"
            class="mx-2 btn btn-secondary btn-sm"
            @click="previousPage"
          >
            返回
          </button>
        </form>
      </div>
    </div>
    <footer class="my-5 pt-5 text-body-secondary text-center text-small">
      <p class="mb-1">&copy; 2023 Oscat</p>
      <ul class="list-inline">
        <li class="list-inline-item"><a href="#">Privacy</a></li>
        <li class="list-inline-item"><a href="#">Terms</a></li>
        <li class="list-inline-item"><a href="#">Support</a></li>
      </ul>
    </footer>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';
import Swal from 'sweetalert2';

const router = useRouter();
const member = ref([]);
const formatJoinDate = ref('');

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
    emailError.value = false;
    emailErrMsg.value = '';
    // await checkEmailRepeat(); // 驗證 email 是否已存在
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
// const emailExists = ref(false);

// const checkEmailRepeat = async () => {
//   const url = `${import.meta.env.VITE_OSCAT_API_ENDPOINT
//     }/official/member/check`;
//   try {
//     const response = await axios.post(url, { email: member.value.email });
//     if (response.status === 200) {
//       emailExists.value = false;
//       emailErrMsg.value = '';
//     }
//   } catch (error) {
//     emailExists.value = true;
//     emailErrMsg.value = '該信箱已被註冊';
//   }
// };

// 登出
const logout = async () => {
  sessionStorage.removeItem('isLogin');
  const url = `${
    import.meta.env.VITE_OSCAT_API_ENDPOINT
  }/official/member/logout`;
  const responce = await axios.post(url);
  // console.log(url);
  console.log(responce);
  // router.go(0);
  router.push('/');
};

// 載入會員資料
onMounted(async () => {
  try {
    const url = `${import.meta.env.VITE_OSCAT_API_ENDPOINT}/official/member/`;
    const response = await axios.post(url);
    member.value = response.data;
    // console.log(member.value);

    // 格式化日期
    if (member.value.joinDate) {
      const dateString = member.value.joinDate.split('T')[0];
      const timeString = member.value.joinDate.split('T')[1].split('.')[0];
      const formattedDate = `${dateString} ${timeString}`;

      // 顯示格式化日期
      formatJoinDate.value = formattedDate;
    }
  } catch (error) {
    const url = `${
      import.meta.env.VITE_OSCAT_API_ENDPOINT
    }/official/member/logout`;
    await axios.post(url);
    if (error.response && error.response.status === 404) {
      logout();
      // Swal.fire('錯誤', '請重新登入', 'error');
      await Swal.fire({
        title: '請重新登入',
        icon: 'error',
        timer: 1500, // 1.5秒後關閉畫面
        showConfirmButton: false,
      });
      router.push('/');
    } else {
      console.error(error);
      logout();
      // Swal.fire('錯誤', '發生了一些問題', 'error');
      await Swal.fire({
        title: '發生了一些問題',
        icon: 'error',
        timer: 1500, // 1.5秒後關閉畫面
        showConfirmButton: false,
      });
      router.push('/');
    }
  }
});

// 修改會員資料
const modifyMember = async (e) => {
  e.preventDefault(); // 防止表單預設行為

  const url = `${
    import.meta.env.VITE_OSCAT_API_ENDPOINT
  }/official/member/update`;
  console.log(url);
  console.log(member.value);
  const memberData = { ...member.value };
  console.log(memberData);
  try {
    const response = await axios.put(url, memberData);
    console.log(response);

    if (response.status === 200) {
      Swal.fire('修改成功', '會員修改成功', 'success');
    }
  } catch (error) {
    console.error(error);
    Swal.fire('修改失敗', '發生錯誤', 'error');
  }
};

// 返回前一頁
const previousPage = () => {
  history.back();
};
</script>

<style scoped></style>
