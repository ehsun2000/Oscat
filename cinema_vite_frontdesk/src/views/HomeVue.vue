<template>
  <div class="container">
    <div class="roll">
      <div class="box"></div>
      <div>
        <div
          v-for="movie in movies"
          :key="movie.movieId"
          class="card"
          style="width: 18rem"
        >
          <a :href="movie.trailerLink">
            <img :src="movie.posterImage" class="card-img-top" alt="" />
          </a>
          <div class="card-body">
            <div class="card-body">
              <h6>{{ movie.movieName }}</h6>
              <h6>上映日 {{ movie.releaseDate }}</h6>
            </div>
            <div class="modal-footer text-center">
              <button
                type="button"
                class="btn btn-primary w-100 py-2"
                @click="goToSelectMovie(movie)"
              >
                前往訂票
              </button>
            </div>
          </div>
        </div>
      </div>

      <footer class="mt-auto text-white-50">
        <p>
          Cover 模板供
          <a href="https://getbootstrap.com/" class="text-white">Bootstrap</a>
          使用，由
          <a href="https://twitter.com/mdo" class="text-white">@mdo</a> 開發。
        </p>
      </footer>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';

const movies = ref([]);
const router = useRouter();
const api = import.meta.env.VITE_OSCATOfficial_API_ENDPOINT;

const loadMovies = async () => {
  const url = `${api}/showing`;
  try {
    const response = await axios.get(url);
    movies.value = response.data;
  } catch (error) {
    console.error('無法獲取電影數據：', error);
  }
};

const goToSelectMovie = (movie) => {
  router.push({
    name: 'SelectMovie',
    params: {
      movieId: movie.movieId,
    },
  });
};

loadMovies();
</script>

<style scoped>
.bd-placeholder-img {
  font-size: 1.125rem;
  text-anchor: middle;
  -webkit-user-select: none;
  -moz-user-select: none;
  user-select: none;
}

@media (min-width: 768px) {
  .bd-placeholder-img-lg {
    font-size: 3.5rem;
  }
}

.b-example-divider {
  width: 100%;
  height: 3rem;
  background-color: rgba(0, 0, 0, 0.1);
  border: solid rgba(0, 0, 0, 0.15);
  border-width: 1px 0;
  box-shadow:
    inset 0 0.5em 1.5em rgba(0, 0, 0, 0.1),
    inset 0 0.125em 0.5em rgba(0, 0, 0, 0.15);
}

.b-example-vr {
  flex-shrink: 0;
  width: 1.5rem;
  height: 100vh;
}

.bi {
  vertical-align: -0.125em;
  fill: currentColor;
}

.nav-scroller {
  position: relative;
  z-index: 2;
  height: 2.75rem;
  overflow-y: hidden;
}

.nav-scroller .nav {
  display: flex;
  flex-wrap: nowrap;
  padding-bottom: 1rem;
  margin-top: -1px;
  overflow-x: auto;
  text-align: center;
  white-space: nowrap;
  -webkit-overflow-scrolling: touch;
}

.btn-bd-primary {
  --bd-violet-bg: #712cf9;
  --bd-violet-rgb: 112.520718, 44.062154, 249.437846;

  --bs-btn-font-weight: 600;
  --bs-btn-color: var(--bs-white);
  --bs-btn-bg: var(--bd-violet-bg);
  --bs-btn-border-color: var(--bd-violet-bg);
  --bs-btn-hover-color: var(--bs-white);
  --bs-btn-hover-bg: #6528e0;
  --bs-btn-hover-border-color: #6528e0;
  --bs-btn-focus-shadow-rgb: var(--bd-violet-rgb);
  --bs-btn-active-color: var(--bs-btn-hover-color);
  --bs-btn-active-bg: #5a23c8;
  --bs-btn-active-border-color: #5a23c8;
}

.bd-mode-toggle {
  z-index: 1500;
}

.bd-mode-toggle .dropdown-menu .active .bi {
  display: block !important;
}

img {
  width: 100%;
}

h1 {
  font-size: 18px;
}

.movie {
  border: 1px solid #ccc;
  border-radius: 10px;
  width: 220px;
  height: 400px;
  margin: 10px;
  padding: 10px;

  display: inline-block;
  position: relative;
  overflow: hidden;
}

.card {
  display: inline-block;
  /* 顯示card */
  width: 18rem;
  /* 設定card寬度 */
  margin-right: 20px;
  /* 設定card之間的右邊間距 */
  margin-bottom: 20px;
  /* 設定卡片之間的下間距 */
}
.card-img-top {
  height: 450px; /* 或你需要的高度 */
  object-fit: cover; /* 保持圖片比例 */
}
</style>
