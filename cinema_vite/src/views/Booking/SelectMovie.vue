<template>
  <div class="maindiv">
    <h1>訂票系統</h1>
    <select v-model="selectedCinemaId">
      <option disabled value="">------請選擇戲院------</option>
      <option v-for="cinema in cinemas" :key="cinema.id" :value="cinema.id">
        {{ cinema.name }}
      </option>
    </select>
    <select v-model="selectedMovieId">
      <option disabled value="">------請選擇電影------</option>
      <option
        v-for="movie in movies"
        :key="movie.movieId"
        :value="movie.movieId"
      >
        {{ movie.movieName }}
      </option>
    </select>
    <br />
    <label for="datePicker">選擇日期：</label>
    <input
      type="date"
      id="datePicker"
      v-model="selectedDate"
      :min="minDate"
      :max="maxDate"
    />
    <div v-for="(times, date) in organizedShowTimes" :key="date">
      {{ date }}
      <br />
      <div v-for="time in times" :key="time">
        <button>
          {{ time }}
        </button>
      </div>
    </div>
    <div
      v-if="message"
      :class="[messageType === 'error' ? 'error-message' : 'success-message']"
    >
      {{ message }}
    </div>
  </div>
</template>

<script>
import { ref, onMounted, watch } from 'vue';

export default {
  setup() {
    const cinemas = ref([]);
    const selectedCinemaId = ref('');
    const movies = ref([]);
    const selectedMovieId = ref('');
    const message = ref(null);
    const messageType = ref('');
    const selectedDate = ref(null);
    const showTimes = ref([]);
    const organizedShowTimes = ref({});

    const api = import.meta.env.VITE_OSCAT_API_ENDPOINT;

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

    const fetchMovies = async () => {
      try {
        const response = await fetch(`${api}/book/showing`, {
          credentials: 'include',
        });
        if (response.ok) {
          movies.value = await response.json();
        } else {
          console.error('Failed to fetch movies:', response.statusText);
        }
      } catch (error) {
        console.error('Error fetching movies:', error);
      }
    };

    const today = new Date();
    const minDate = ref(
      `${today.getFullYear()}-${String(today.getMonth() + 1).padStart(
        2,
        '0',
      )}-${String(today.getDate()).padStart(2, '0')}`,
    );

    const sevenDaysFromNow = new Date(today);
    sevenDaysFromNow.setDate(sevenDaysFromNow.getDate() + 7);
    const maxDate = ref(
      `${sevenDaysFromNow.getFullYear()}-${String(
        sevenDaysFromNow.getMonth() + 1,
      ).padStart(2, '0')}-${String(sevenDaysFromNow.getDate()).padStart(
        2,
        '0',
      )}`,
    );

    const fetchShowTimes = async () => {
      if (
        !selectedCinemaId.value ||
        !selectedMovieId.value ||
        !selectedDate.value
      ) {
        return;
      }
      try {
        const response = await fetch(
          `${api}/book/${selectedMovieId.value}/${selectedCinemaId.value}/findtime?selectDate=${selectedDate.value}`,
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

    onMounted(() => {
      fetchCinemas();
      fetchMovies();
    });

    watch([selectedCinemaId, selectedMovieId, selectedDate], fetchShowTimes, {
      deep: true,
    });

    return {
      cinemas,
      selectedCinemaId,
      movies,
      selectedMovieId,
      message,
      messageType,
      selectedDate,
      minDate,
      maxDate,
      showTimes,
      organizedShowTimes,
    };
  },
};
</script>

<style scoped>
.maindiv {
  text-align: center;
}
.success-message {
  color: green;
}
.error-message {
  color: red;
}
</style>
