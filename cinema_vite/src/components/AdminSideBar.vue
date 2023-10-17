<script setup>
import CollapseMenu from './CollapseMenu.vue';

const movieItems = [
  { name: '場次設定', path: '/showtime' },
  { name: '電影設定', path: '/movie' },
];
const memberItems = [
  { name: '會員設定', path: '/member' },
  { name: '分析報告', path: '/member-report' },
];
const cinemaItems = [
  { name: '資訊設定', path: '/cinema-info' },
  { name: '座位設定', path: '/cinema-seat' },
  { name: '販售設定', path: '/cinema-product' },
];
</script>
<template>
  <div
    class="d-flex flex-column flex-shrink-0 p-3 text-white bg-dark fixed-top"
    style="height: 100dvh; width: 15%"
  >
    <router-link
      to="/"
      class="d-flex align-items-center mb-3 mb-md-0 me-md-auto text-white text-decoration-none"
    >
      <span class="fs-4">Oscat Admin</span>
    </router-link>
    <hr />
    <ul class="nav nav-pills flex-column mb-auto">
      <CollapseMenu
        :title="'電影管理'"
        :id="'movieManagement'"
        :items="movieItems"
      ></CollapseMenu>
      <CollapseMenu
        :title="'會員管理'"
        :id="'memberManagement'"
        :items="memberItems"
      ></CollapseMenu>
      <CollapseMenu
        :title="'影城管理'"
        :id="'cinemaManagement'"
        :items="cinemaItems"
      ></CollapseMenu>
    </ul>
    <hr />
    <div class="dropdown">
      <a
        href="#"
        class="d-flex align-items-center text-white text-decoration-none dropdown-toggle"
        id="dropdownUser1"
        data-bs-toggle="dropdown"
        aria-expanded="false"
      >
        <img
          src="https://github.com/mdo.png"
          alt=""
          width="32"
          height="32"
          class="rounded-circle me-2"
        />
        <strong>使用者名稱</strong>
      </a>
      <ul
        class="dropdown-menu dropdown-menu-dark text-small shadow"
        aria-labelledby="dropdownUser1"
      >
        <li>
          <router-link to="/new-project" class="dropdown-item"
            >新專案</router-link
          >
        </li>
        <li>
          <router-link to="/settings" class="dropdown-item">設定</router-link>
        </li>
        <li>
          <router-link to="/profile" class="dropdown-item"
            >個人資料</router-link
          >
        </li>
        <li><hr class="dropdown-divider" /></li>
        <li><a href="#" class="dropdown-item">登出</a></li>
      </ul>
    </div>
  </div>
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
