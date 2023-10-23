<template>
  <RouterLink class="btn btn-outline-success" to="/movie/add"
    ><i class="bi bi-plus"></i> 新增</RouterLink
  >
  <div class="row mb-3">
    <div class="col-6"></div>
    <div class="col-5">
      <SearchTextMovie></SearchTextMovie>
    </div>
  </div>
  <div>
    <div v-for="movie in movies" :key="movie.movieId" class="movie">
      <a :href="movie.trailerLink">
        <img :src="movie.posterImage" alt="" />
      </a>
      <h1>{{ movie.movieName }}</h1>
      <button type="button" class="btn btn-outline-primary">
        <i class="bi bi-arrow-up-circle-fill"></i>
      </button>

      <button type="button" class="btn btn-outline-primary">
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
</template>

<script setup>
import SearchTextMovie from '../components/SearchTextMovie.vue';
import { fetchMovies, movies } from '../config/function';
import { onMounted } from 'vue';
import axios from 'axios';
// import Swal from 'sweetalert2';
onMounted(async () => {
  await fetchMovies();
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
