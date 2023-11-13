<script setup>
import CollapseMenu from './CollapseMenu.vue';
import { useRouter } from 'vue-router';

const router = useRouter();
// 加入圖標和選單資料
const menus = [
  {
    id: 'movieManagement',
    title: '電影管理',
    icon: 'bi-film', // Bootstrap icon
    items: [
      { name: '電影設定', path: '/movie' },
      { name: '場次設定', path: '/showtime' },
    ],
  },
  {
    id: 'memberManagement',
    title: '會員管理',
    icon: 'bi-person', // Bootstrap icon
    items: [
      { name: '會員一覽', path: '/member' },
      { name: '分析報告', path: '/member-report' },
      { name: '訂單管理', path: '/transorder' },
    ],
  },
  {
    id: 'cinemaManagement',
    title: '影城管理',
    icon: 'bi-building', // Bootstrap icon
    items: [
      { name: '資訊設定', path: '/cinema-select' },
      { name: '座位設定', path: '/cinema-seat' },
      { name: '販售設定', path: '/cinema-product' },
    ],
  },
];

const logout = async () => {
  try {
    const response = await fetch(`${import.meta.env.VITE_OUT_API_ENDPOINT}`, {
      credentials: 'include',
      method: 'GET',
    });

    if (response.ok) {
      await router.push('/adminlogin');
    } else {
      console.error('登出未成功!');
    }
  } catch (error) {
    console.error('登出過程中發生錯誤:', error);
  }
};
</script>

<template>
  <router-link
    to="/dashboard"
    class="d-flex justify-content-center align-items-center mb-3 mb-md-0 me-md-auto text-decoration-none text-light"
  >
    <span class="fs-4 text-center">Oscat Admin</span>
  </router-link>
  <hr />
  <ul class="nav nav-pills flex-column mb-auto">
    <CollapseMenu
      v-for="menu in menus"
      :key="menu.id"
      :title="menu.title"
      :icon="menu.icon"
      :items="menu.items"
    ></CollapseMenu>
  </ul>
  <hr />
  <span
    class="d-flex justify-content-center align-items-end mb-3 mb-md-0 me-md-auto text-decoration-none text-light"
    @click="logout"
    >登出</span
  >
</template>

<style scoped>
.collapse-custom {
  transition: height 0.3s ease-out;
  overflow: hidden;
}
.show {
  transition: height 0.4s ease-in;
}
</style>
