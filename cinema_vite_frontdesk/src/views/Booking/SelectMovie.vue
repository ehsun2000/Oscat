<template>
  <div class="container">
    <div class="roll">
      <div class="row">
        <MovieDetail :movie="movie" />
        <CinemaSelection :movie="movie" />
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

<style scoped>
.mb-3-footer {
  width: 25%;
}
</style>
