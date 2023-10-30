<template>
  <div class="container">
    <div class="roll">
      <div class="row">
        <div class="col-md-6">
          <img :src="movie.posterImage" class="card-img-top" alt="" />
          <h5>{{ movie.movieName }}</h5>
          <p>{{ movie.plotSummary }}</p>
          <iframe
            width="560"
            height="315"
            :src="youtubeEmbedLink"
            frameborder="0"
            allowfullscreen
          ></iframe>
        </div>

        <div class="col-md-6">
          <select v-model="selectedCinemaId">
            <option disabled value="">------請選擇戲院------</option>
            <option
              v-for="cinema in cinemas"
              :key="cinema.id"
              :value="cinema.id"
            >
              {{ cinema.name }}
            </option>
          </select>
          <div v-for="(times, date) in organizedShowTimes" :key="date">
            {{ date }}
            <br />
            <div class="button-container">
              <div v-for="time in times" :key="time">
                <button
                  :disabled="shouldDisableButton(date + ' ' + time)"
                  @click="goToTicketType(date, time)"
                >
                  {{ time }}
                </button>
              </div>
            </div>
          </div>
          <p>溫馨提醒：電影開始前30分鐘將無法線上訂票</p>
        </div>
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
import { ref, onMounted, watch } from 'vue';
import axios from 'axios';
import { useRoute, useRouter } from 'vue-router';

const route = useRoute();
const router = useRouter();
const movie = ref({}); // 初始化空的 movie 對象
const cinemas = ref([]);
const selectedCinemaId = ref('');
const organizedShowTimes = ref({});
const youtubeEmbedLink = ref('');

const api = import.meta.env.VITE_OSCAT_API_ENDPOINT;

onMounted(async () => {
  const movieId = route.params.movieId;

  try {
    const response = await axios.get(`${api}/movie/${movieId}`);

    movie.value = response.data;
  } catch (error) {
    console.error('無法獲取電影資料：', error);
  }
});

const fetchCinemas = async () => {
  try {
    const response = await fetch(`${api}/cinemas/all`, {
      credentials: 'include',
    });
    if (response.ok) {
      cinemas.value = await response.json();
    } else {
      console.error('Failed to fetch cinemas:', response.statusText);
    }
  } catch (error) {
    console.error('Error fetching cinemas:', error);
  }
};

const fetchShowTimes = async () => {
  if (!selectedCinemaId.value || !movie.value.movieId) {
    return;
  }
  try {
    const response = await fetch(
      `${api}/book/${movie.value.movieId}/${selectedCinemaId.value}/findtime`,
      {
        credentials: 'include',
      },
    );
    if (response.ok) {
      const data = await response.json();
      const tempTimes = {};

      data.forEach((item) => {
        const [date, hour] = item.showtime.split(' ');
        if (!tempTimes[date]) {
          tempTimes[date] = [];
        }
        tempTimes[date].push(hour);
      });

      organizedShowTimes.value = tempTimes;
    } else {
      console.error('Failed to fetch show times:', response.statusText);
    }
  } catch (error) {
    console.error('Error fetching show times:', error);
  }
};

const shouldDisableButton = (showtime) => {
  const now = new Date();
  const thresholdTime = new Date(now.getTime() + 30 * 60 * 1000); // 加30分鐘

  const [date, time] = showtime.split(' ');
  const [hours, minutes] = time.split(':');
  const showDateTime = new Date(date + 'T' + hours + ':' + minutes);

  return showDateTime <= thresholdTime;
};

const goToTicketType = (date, time) => {
  router.push({
    name: 'TicketType',
    params: {
      movieId: movie.value.movieId,
      cinemaId: selectedCinemaId.value,
      date: date,
      time: time,
    },
  });
};

onMounted(() => {
  fetchCinemas();
});

watch(selectedCinemaId, fetchShowTimes);

watch(movie, (newMovie) => {
  if (newMovie.trailerLink) {
    youtubeEmbedLink.value = newMovie.trailerLink.replace('watch?v=', 'embed/');
  }
});
</script>

<style scoped>
img {
  width: 300px;
}
.button-container {
  display: flex;
  gap: 10px; /* 這將添加每個按鈕之間的間距 */
}
</style>
