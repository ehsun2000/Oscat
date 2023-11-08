<template>
  <div class="col-md-6">
    <select v-model="selectedCinemaId">
      <option disabled value="">------請選擇戲院------</option>
      <option v-for="cinema in cinemas" :key="cinema.id" :value="cinema.id">
        {{ cinema.name }}
      </option>
    </select>
    <div
      v-for="(timesByFilmType, date, index) in organizedShowTimes"
      :key="date"
    >
      <h5>{{ date }}</h5>
      <div v-for="(times, filmType) in timesByFilmType" :key="filmType">
        {{ filmType }}：
        <div class="button-container">
          <div v-for="slot in times" :key="slot.time">
            <button
              :disabled="shouldDisableButton(date + ' ' + slot.time)"
              @click="
                goToTicketType(date, slot.time, slot.roomName, slot.showtimeId)
              "
            >
              {{ slot.time }}
            </button>
          </div>
        </div>
      </div>
      <hr v-if="index < Object.keys(organizedShowTimes).length - 1" />
    </div>
    <p>溫馨提醒：電影開始前30分鐘將無法線上訂票</p>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();
const cinemas = ref([]);
const selectedCinemaId = ref('');
const organizedShowTimes = ref({});

const api = import.meta.env.VITE_OSCATOfficial_API_ENDPOINT;

const props = defineProps({
  movie: Object,
});

const fetchCinemas = async () => {
  try {
    const response = await fetch(`${api}/allCinema`, {
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
  if (!selectedCinemaId.value || !props.movie.movieId) {
    return;
  }
  try {
    const response = await fetch(
      `${api}/${props.movie.movieId}/${selectedCinemaId.value}/findtime`,
      {
        credentials: 'include',
        method: 'GET',
      },
    );

    if (response.ok) {
      const data = await response.json();
      const tempTimes = {};

      data.forEach((item) => {
        const date = item.showtime.split(' ')[0];
        const filmType = item.filmType;

        if (!tempTimes[date]) {
          tempTimes[date] = {};
        }
        if (!tempTimes[date][filmType]) {
          tempTimes[date][filmType] = [];
        }

        tempTimes[date][filmType].push({
          time: item.showtime.split(' ')[1],
          roomName: item.roomName,
          showtimeId: item.showtimeId,
        });
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

const goToTicketType = (date, time, roomName, showtimeId) => {
  const selectedCinema = cinemas.value.find(
    (cinema) => cinema.id === selectedCinemaId.value,
  );
  router.push({
    name: 'TicketType',
    query: {
      movieName: props.movie.movieName,
      cinemaName: selectedCinema.name,
      screenRoomName: roomName,
      basicPrice: selectedCinema.basePrice.toString(),
      showtimeId: showtimeId,
    },
  });
};

onMounted(() => {
  fetchCinemas();
});

watch(selectedCinemaId, fetchShowTimes);
</script>

<style scoped>
.button-container {
  display: flex;
  gap: 10px; /* 這將添加每個按鈕之間的間距 */
}
</style>
