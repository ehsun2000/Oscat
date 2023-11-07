<template>
  <div class="container">
    <div class="roll">
      <div class="row">
        <MovieDetail :movie="movie" />
        <CinemaSelection :movie="movie" />
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
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { useRoute } from 'vue-router';
import MovieDetail from './MovieDetail.vue';
import CinemaSelection from './CinemaSelection.vue';

const api = import.meta.env.VITE_OSCATOfficial_API_ENDPOINT;
const movie = ref({});

const route = useRoute();

onMounted(async () => {
  const movieId = route.params.movieId;

  try {
    const response = await axios.get(`${api}/${movieId}`);
    movie.value = response.data;
  } catch (error) {
    console.error('無法獲取電影資料：', error);
  }
});
</script>

<style scoped></style>
