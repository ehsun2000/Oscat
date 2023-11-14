<template>
  <div class="container">
    <div v-if="movies.length === 0" class="no-results">
      沒有找到相關電影。
      <button
        type="button"
        class="btn btn-outline-light"
        @click="backHandler"
        style="width: 80px; height: 40px; margin-left: 5px"
      >
        回首頁
      </button>
    </div>
    <div v-else>
      <div class="roll">
        <div class="result">
          <h3>搜索結果</h3>
        </div>
        <div class="movies-list">
          <div class="d-flex">
            <div
              v-for="movie in movies"
              :key="movie.movieId"
              class="card"
              style="width: 15rem; height: 30rem"
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
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { useRouter } from 'vue-router';
import axios from 'axios';

const route = useRoute();
const movies = ref([]);
const router = useRouter();

onMounted(async () => {
  const keyword = route.params.keyword;
  try {
    const encodedKeyword = encodeURIComponent(keyword);
    const response = await axios.get(
      `${
        import.meta.env.VITE_OSCATOfficial_API_ENDPOINT
      }/nameLike?n=${encodedKeyword}`,
    );
    movies.value = response.data;
  } catch (error) {
    console.error('無法獲取電影數據:', error);
  }
});

const backHandler = () => {
  router.push('/');
};

const goToSelectMovie = (movie) => {
  router.push({
    name: 'SelectMovie',
    params: {
      movieId: movie.movieId,
    },
  });
};
</script>

<style scoped>
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

.card-text {
  font-size: 12px;
}
</style>
