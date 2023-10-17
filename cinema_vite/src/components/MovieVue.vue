<template>
  <h1>Movie Page</h1>
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
        <i class="bi bi-trash-fill"></i>
      </button>
    </div>
  </div>
</template>

<script setup>
import { fetchMovies, movies } from '../config/function';
import { onMounted } from 'vue';

onMounted(async () => {
  await fetchMovies();
});
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
