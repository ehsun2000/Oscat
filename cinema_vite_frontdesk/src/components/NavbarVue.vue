<template>
  <div class="container">
    <div class="cover-container d-flex w-100 h-100 p-3 mx-auto flex-column">
      <nav class="nav nav-masthead justify-content-between float-md-end">
        <router-link to="/" class="d-flex justify-content-start align-items-center text-decoration-none me-auto">
          <img
            rel="icon"
            src="/images/cat.png"
            style="height: 40px; width: 40px"
          />
          <h3>OSCAT</h3>
        </router-link>
          <router-link
            to="/"
            class="nav-link fw-bold py-1 px-0 active"
            aria-current="page"
            >首頁</router-link
          >
          <router-link
            v-if="isLogin"
            to="/member-center"
            class="nav-link fw-bold py-1 px-0"
            >會員中心</router-link
          >
          <router-link
            v-if="isLogin"
            to="/"
            class="nav-link fw-bold py-1 px-0"
            @click="logout"
            >登出</router-link
          >
          <!-- v-if="!isLogin" -->
          <router-link
            v-if="!isLogin"
            to="/sign-in"
            class="nav-link fw-bold py-1 px-0"
            >登入</router-link
          >
          <router-link
            v-if="!isLogin"
            to="/sign-up"
            class="nav-link fw-bold py-1 px-0"
            >註冊</router-link
          >
        </nav>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';

const router = useRouter();

// 監聽全局事件以更新 isLogin
const isLogin = ref(false);

onMounted(() => {
  if (sessionStorage.getItem('isLogin') === 'true') {
    isLogin.value = true;
  }
});

// 登出
const logout = async () => {
  sessionStorage.removeItem('isLogin');
  const url = `${
    import.meta.env.VITE_OSCATOfficial_API_ENDPOINT
  }/member/logout`;
  const responce = await axios.post(url);
  // console.log(url);
  console.log(responce);
  router.go(0);
  router.push('/');
};
</script>

<style scoped>
.nav-masthead .nav-link {
  color: rgba(255, 255, 255, 0.5);
  border-bottom: 0.25rem solid transparent;
}

.nav-masthead .nav-link:hover,
.nav-masthead .nav-link:focus {
  border-bottom-color: rgba(255, 255, 255, 0.25);
}

.nav-masthead .nav-link + .nav-link {
  margin-left: 1rem;
}

.nav-masthead .active {
  color: #fff;
  border-bottom-color: #fff;
}
.img {
  width: 100%;
  height: 25rem;
}
</style>
