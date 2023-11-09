<template>
  <div class="container">
    <h2>電影管理</h2>
    <RouterLink class="mb-3 btn btn-success" to="/movie/add"
      ><i class="bi bi-plus"></i> 新增</RouterLink
    >
    <div class="row mb-3">
      <div class="col-4">
        <SearchTextMovie
          v-model="datas.movieName"
          @searchInput="inputHandler"
        ></SearchTextMovie>
      </div>
    </div>
    <div>
      <div
        v-for="movie in formateMovie"
        :key="movie.movieId"
        class="card"
      >
        <a :href="movie.trailerLink">
          <img :src="movie.posterImage" class="card-img-top" alt="" />
        </a>
        <div class="card-body text-center">
          <h1 style="font-size: 0.8rem;">{{ movie.movieName }}</h1 >
          <button
            type="button"
            :class="[
              movie.movieStatus === '上映中'
                ? 'btn btn-primary'
                : 'btn btn-outline-primary',
            ]"
            :disabled="movie.movieStatus === '上映中'"
            @click="upMovie('上映中', movie.movieId)"
            :data-movie-id="movie.movieId"
          >
            <i class="bi bi-arrow-up-circle-fill"></i>
          </button>
          <button
            type="button"
            :class="[
              movie.movieStatus !== '上映中'
                ? 'btn btn-primary'
                : 'btn btn-outline-primary',
            ]"
            :disabled="movie.movieStatus !== '上映中'"
            @click="downMovie('下檔', movie.movieId)"
            :data-movie-id="movie.movieId"
          >
            <i class="bi bi-arrow-down-circle-fill"></i>
          </button>

          <RouterLink
            type="button"
            class="btn btn-outline-secondary"
            :to="'/movie/edit/' + movie.movieId"
          >
            <i class="bi bi-gear"></i>
          </RouterLink>

          <button type="button" class="btn btn-outline-danger">
            <i class="bi bi-trash-fill" @click="deleteMovie(movie.movieId)"></i>
          </button>
        </div>
      </div>
      <PageVue
        :totalPages="totalPages"
        :thePage="currentPage"
        @childClick="clickHandler"
      ></PageVue>
    </div>
  </div>
</template>

<script setup>
import SearchTextMovie from '@/components/SearchTextMovie.vue';
import { loadMovies, movies, totalPages } from '@/services/MovieService.js';
import { onMounted, computed, reactive, ref } from 'vue';
import axios from 'axios';
import PageVue from '@/components/PageVue.vue';

totalPages.value;
console.log(totalPages);
const currentPage = ref(1); // 當前頁碼

const clickHandler = (newPage) => {
  if (newPage >= 0 && newPage <= totalPages.value) {
    currentPage.value = newPage - 1;
    console.log(currentPage.value);
    loadMovies(currentPage.value); // 取得所有電影
  }
};

//載入所有電影，並取得 totalPage
onMounted(async () => {
  await loadMovies();
  totalPages.value;
});

//修改電影狀態
const upMovie = async (movieStatus, movieId) => {
  const URLAPI = `${import.meta.env.VITE_OSCAT_API_ENDPOINT}/movie/status`;
  const response = await axios.put(URLAPI, { movieStatus, movieId });
  console.log(response);
  loadMovies();
};

const downMovie = async (movieStatus, movieId) => {
  const URLAPI = `${import.meta.env.VITE_OSCAT_API_ENDPOINT}/movie/status`;
  const response = await axios.put(URLAPI, { movieStatus, movieId });
  console.log(response);
  loadMovies();
};

//搜尋
const datas = reactive({
  movieName: '',
});

const inputHandler = (value) => {
  datas.movieName = value;
  loadMovies();
};

const formateMovie = computed(() => {
  const filteredMovies = movies.value.filter((movie) => {
    return movie.movieName.includes(datas.movieName);
  });
  return filteredMovies;
});

//刪除
const deleteMovie = async (movieId) => {
  if (window.confirm('真的要刪除嗎?')) {
    const URLAPI = `${
      import.meta.env.VITE_OSCAT_API_ENDPOINT
    }/movie/delete/${movieId}`;
    const response = await axios.delete(URLAPI);
    if (response.data) {
      alert(response.data);
      loadMovies();
    }
  }
};
</script>

<style scoped>
img {
  width: 100%;
}

h1 {
  font-size: 18px;
}

.movie {
  border: 1px solid #ccc;
  border-radius: 10px;
  width: 200px;
  height: 300px;
  margin: 10px;
  padding: 10px;

  display: inline-block;
  position: relative;
  overflow: hidden;
}

.card {
  display: inline-block;
  /* 顯示card */
  width: 15rem;
  /* 設定card寬度 */
  margin-right: 10px;
  /* 設定card之間的右邊間距 */
  margin-bottom: 10px;
  /* 設定卡片之間的下間距 */
}
</style>
