<template>
  <div>
    <h1>電影列表</h1>
    <table class="table">
      <thead>
        <tr>
          <th scope="col">電影名稱</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="movie in movies" :key="movie.movieId" :id="movie.movieId">
          <td>{{ movie.movieName }}</td>
        </tr>
      </tbody>
    </table>
    <div class="d-grid gap-2 d-md-flex justify-content-md-end">
      <router-link :to="'/showtime'">
        <button type="button" class="btn btn-outline-secondary">
          新增場次
        </button>
      </router-link>
    </div>
  </div>
</template>

<script setup>
// 引入 Axios
import axios from 'axios';
import { ref } from 'vue';

const movies = ref();
const loadMovies = async () => {
  const URLAPI = `${import.meta.env.VITE_OSCAT_API_ENDPOINT}/movie/`;
  const response = await axios.get(URLAPI, {
    withCredentials: true, // 将憑證模式設為 'include'
  });
  console.log(response);

  movies.value = response.data;
};

loadMovies();
</script>

<style>
/* 这里可以添加组件的样式 */
</style>
