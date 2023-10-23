<template>
  <div class="roll">
    <div class="box"></div>
    <h2>電影管理</h2>
    <RouterLink class="btn btn-outline-success" to="/movie/add"
      ><i class="bi bi-plus"></i> 新增</RouterLink
    >
    <div class="row mb-3">
      <div class="col-6"></div>
      <div class="col-5">
        <SearchTextMovie @searchInput="inputHandler"></SearchTextMovie>
      </div>
    </div>
    <div>
      <div v-for="movie in formateMovie" :key="movie.movieId" class="movie">
        <a :href="movie.trailerLink">
          <img :src="movie.posterImage" alt="" />
        </a>
        <h1>{{ movie.movieName }}</h1>
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
  </div>
</template>

<script setup>
import SearchTextMovie from '@/components/SearchTextMovie.vue';
import { fetchMovies, movies } from '@/services/function.js';
import { onMounted, computed, reactive } from 'vue';
import axios from 'axios';

//載入所有電影
onMounted(async () => {
  await fetchMovies();
});

//修改電影狀態
const upMovie = async (movieStatus, movieId) => {
  const URLAPI = `${import.meta.env.VITE_OSCAT_API_ENDPOINT}/movie/status`;
  console.log(URLAPI);
  const response = await axios.put(URLAPI, { movieStatus, movieId });
  console.log(response);
  fetchMovies();
};

const downMovie = async (movieStatus, movieId) => {
  const URLAPI = `${import.meta.env.VITE_OSCAT_API_ENDPOINT}/movie/status`;
  console.log(URLAPI);
  console.log(movieStatus);
  console.log(movieId);
  const response = await axios.put(URLAPI, { movieStatus, movieId });
  console.log(response);
  fetchMovies();
};

//搜尋
const datas = reactive({
  movieName: '',
});

const inputHandler = (value) => {
  datas.movieName = value;
  // console.log(value);
  fetchMovies();
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
      import.meta.env.VITE_API_OSCATURL
    }movie/delete/${movieId}`;
    const response = await axios.delete(URLAPI);
    if (response.data) {
      alert(response.data);
      fetchMovies();
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
  width: 220px;
  height: 400px;
  margin: 10px;
  padding: 10px;

  display: inline-block;
  position: relative;
  overflow: hidden;
}
</style>
