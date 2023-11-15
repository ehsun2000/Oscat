<template>
  <div>
    <AdCarousel></AdCarousel>
  </div>
  <div class="row mb-3">
    <div class="searchMovie d-flex justify-content">
      <SearchMovie></SearchMovie>
      <!-- <SearchMovie v-model="datas.movieName"> </SearchMovie> -->
    </div>
  </div>
  <div class="container">
    <div class="roll">
      <div class="onMovie">
        <h3>現正熱映</h3>
      </div>
      <div class="box"></div>
      <div id="carouselExample" class="carousel slide" data-bs-ride="carousel">
        <div class="carousel-inner">
          <!-- 使用 computed properties 分組每5個電影 -->
          <div
            class="carousel-item"
            style="margin: 0 110px"
            :class="{ active: index === 0 }"
            v-for="(group, index) in groupedMovies"
            :key="index"
          >
            <div class="d-flex">
              <div
                v-for="movie in group"
                :key="movie.movieId"
                class="card"
                style="width: 13rem; height: 25rem"
              >
                <a :href="movie.trailerLink">
                  <img
                    :src="movie.posterImage"
                    class="card-img-top"
                    alt="Movie Poster"
                  />
                </a>
                <div class="card-body">
                  <div class="card-title">
                    <h6>{{ movie.movieName }}</h6>
                    <p class="card-text">上映日 {{ movie.releaseDate }}</p>
                  </div>
                  <div class="modal-footer text-center">
                    <button
                      type="button"
                      class="btn btn-primary"
                      @click="goToSelectMovie(movie)"
                    >
                      前往訂票
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <button
            class="carousel-control-prev custom-carousel-prev"
            type="button"
            data-bs-target="#carouselExample"
            data-bs-slide="prev"
          >
            <span class="bi bi-chevron-compact-left"></span>
            <span class="visually-hidden">Previous</span>
          </button>
          <button
            class="carousel-control-next custom-carousel-next"
            type="button"
            data-bs-target="#carouselExample"
            data-bs-slide="next"
          >
            <span class="bi bi-chevron-compact-right" aria-hidden="true"></span>
            <span class="visually-hidden">Next</span>
          </button>
        </div>
      </div>
    </div>
    <footer
      class="row row-cols-1 row-cols-sm-2 row-cols-md-5 py-5 my-5 border-top"
    >
      <div class="col mb-3">
        <a
          href="/"
          class="d-flex align-items-center mb-3 link-body-emphasis text-decoration-none"
          style="margin: 0%"
        >
          <img
            rel="icon"
            src="/images/cat.png"
            style="height: 40px; width: 32px"
          />
        </a>
        <p class="text-body-secondary">&copy; OSCAT 2023</p>
      </div>

      <div class="col mb-3-footer">
        <h5>關於Oscat</h5>
        <ul class="nav flex-column">
          <li class="nav-item mb-2">
            <a href="#" class="nav-link p-0 text-body-secondary">最新消息</a>
          </li>
          <li class="nav-item mb-2">
            <a href="#" class="nav-link p-0 text-body-secondary">意見信箱</a>
          </li>
        </ul>
      </div>
      <div class="col mb-3-footer">
        <h5>其他資訊</h5>
        <ul class="nav flex-column">
          <li class="nav-item mb-2">
            <a href="#" class="nav-link p-0 text-body-secondary">常見問題</a>
          </li>
          <li class="nav-item mb-2">
            <a href="#" class="nav-link p-0 text-body-secondary">聯絡我們</a>
          </li>
        </ul>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import axios from 'axios';
import AdCarousel from '@/components/AdCarousel.vue';
import SearchMovie from '@/components/SearchTextMovie.vue';
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

// 上映的電影(一列5部)
const groupedMovies = computed(() => {
  const groups = [];
  for (let i = 0; i < movies.value.length; i += 5) {
    groups.push(movies.value.slice(i, i + 5));
  }
  return groups;
});

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
  height: 25rem;
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
  height: 15rem; /* 或你需要的高度 */
  object-fit: cover; /* 保持圖片比例 */
}
.mb-3 {
  width: 50%;
  margin: auto;
}
.carousel-item .d-flex {
  justify-content: start;
  flex-wrap: nowrap;
  overflow-x: auto;
}

.carousel-item .card {
  flex: 0 0 auto; /* Prevents the cards from shrinking */
  margin-right: 15px; /* Adds some space between the cards */
  width: 15rem;
}
.carousel-item {
  margin: 0 30px; /* 添加左右外邊距，調整適當的距離 */
}
.card-text {
  font-size: 12px;
}
.searchMovie {
  width: 100%;
}
.onMovie {
  margin-bottom: 25px;
}
.mb-3-footer {
  width: 25%;
}
.custom-carousel-prev,
.custom-carousel-next {
  width: 100px;
  height: 400px;
  font-size: 40px;
}
.carousel-inner {
  height: 400px;
}
</style>
