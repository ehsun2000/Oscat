<template>
  <div class="container">
    <div class="cover-container d-flex w-100 h-100 p-3 mx-auto flex-column">
      <div class="mb-auto">
        <div>
          <router-link to="/"
            ><h3 class="float-md-start mb-0">OSCAT</h3></router-link
          >
          <nav class="nav nav-masthead justify-content-center float-md-end">
            <router-link
              to="/"
              class="nav-link fw-bold py-1 px-0 active"
              aria-current="page"
              >首頁</router-link
            >
            <router-link to="/signin" class="nav-link fw-bold py-1 px-0"
              >登入</router-link
            >
            <router-link to="/signup" class="nav-link fw-bold py-1 px-0"
              >註冊</router-link
            >
          </nav>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup></script>

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
</style>
