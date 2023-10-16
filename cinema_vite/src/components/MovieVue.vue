<template>
  <h1>Movie Page</h1>
  <div class="card" style="width: 16rem">
    <img
      src="https://www.themoviedb.org/t/p/w600_and_h900_bestv2/m4OKNz4f5p6yuH4eOyn2JPRYsqY.jpg"
      class="card-img-center"
      style="width: 100%"
      alt="迷失安狄"
      title="迷失安狄"
    />
    <div
      class="card-body"
      style="display: flex; justify-content: flex-end; align-items: flex-end"
    >
      <ol>
        <li>
          <a href="#"><i class="bi bi-arrow-up-circle-fill"></i></a>
        </li>
        <li>
          <a href="#"><i class="bi bi-arrow-down-circle-fill"></i></a>
        </li>
        <li>
          <a href="#"><i class="bi bi-gear"></i></a>
        </li>
        <li>
          <a href="#"><i class="bi bi-trash-fill"></i></a>
        </li>
      </ol>
    </div>
  </div>
  <div>
    <div v-for="movie in movies" :key="movie.movieId" class="movie">
      <a :href="movie.trailerLink">
        <img :src="movie.posterImage" alt="" />
      </a>
      <h1>{{ movie.movieName }}</h1>
      <!-- <p>{{ movie.movieType }}</p> -->
      <button><i class="bi bi-pencil-fill"></i></button>
      <button>
        <i class="bi bi-trash-fill"></i>
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import axios from 'axios';

const movies = ref([]);

const loadMovies = async () => {
  const url = `${import.meta.env.VITE_API_OSCATURL}movies`;
  const response = await axios.get(url);
  console.log(response);
  console.log(response.data);
  movies.value = response.data;
  console.log(movies.value);
};
loadMovies();
</script>

<style scoped>
img {
  width: 220px;
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
