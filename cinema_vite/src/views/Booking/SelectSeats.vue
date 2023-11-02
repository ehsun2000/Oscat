<template>
  <div class="maindiv">
    <h1>劃位</h1>
    <h1>{{ movieName }}</h1>
    <h2>{{ cinemaName }}</h2>
    <h3>{{ screenRoomName }}</h3>
    <p>{{ filmType }}</p>
    <p>{{ showDateAndTime }}</p>
    <form @submit.prevent="getSeatStatus">
      <i class="bi bi-box2"></i>可訂座位 <i class="bi bi-box2-fill"></i>無法預定
    </form>
    <div id="result" class="scrollable-container">
      <div class="seats-container" :style="gridStyle">
        <div v-for="seat in seats" :key="seat.seatId">
          <button
            @click="toggleSeatStatus(seat)"
            class="seat-button"
            :disabled="seat.tempStatus == 'Maintenance'"
          >
            <i
              v-if="seat.tempStatus === 'Maintenance'"
              class="bi bi-box2-fill"
            ></i>
            <i
              v-else-if="seat.tempStatus === 'Selected'"
              class="bi bi-box2-fill"
            ></i>
            <i v-else class="bi bi-box2"></i><br />
            {{ seat.seatName }}
          </button>
        </div>
      </div>
    </div>
    可選座位數：{{ remainingSeats }} 總價：{{ totalPrice }}
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue';
import { useRoute } from 'vue-router';

export default {
  setup() {
    const route = useRoute();

    const seats = ref([]);
    const movieName = ref(route.query.movieName);
    const cinemaName = ref(route.query.cinemaName);
    const screenRoomName = ref(route.query.screenRoomName);
    const filmType = ref(route.query.filmType);
    const showDateAndTime = ref(route.query.showDateAndTime);
    const totalPrice = ref(route.query.totalPrice);
    const totalTicketCount = ref(route.query.totalTicketCount);
    const showtimeId = ref(route.query.showtimeId);
    const roomId = ref(null);
    const showSeats = ref(false);
    const showButtons = ref(false);
    const message = ref('');
    const messageType = ref('');
    const selectedSeatsCount = ref(0);

    const api = import.meta.env.VITE_OSCAT_API_ENDPOINT;

    const getShowtimeDetails = async () => {
      try {
        const response = await fetch(
          `${api}/showtime/find/${showtimeId.value}`,
          {
            credentials: 'include',
          },
        );

        if (response.ok) {
          const data = await response.json();
          roomId.value = data.roomId;
        } else {
          console.error('請求 showtime 資料失敗:', response.statusText);
        }
      } catch (error) {
        console.error('請求 showtime 資料時出錯:', error.message);
      }
    };

    const getSeatStatus = async () => {
      if (!roomId.value) return;

      try {
        const response = await fetch(
          `${api}/seat/findAllSeatByRoomId?roomId=${roomId.value}`,
          {
            credentials: 'include',
          },
        );

        if (response.ok) {
          const data = await response.json();
          seats.value = data.map((seat) => ({
            ...seat,
            tempStatus: seat.seatStatus,
          }));
          if (seats.value.length > 0) {
            showSeats.value = true;
            showButtons.value = true;
          } else {
            showSeats.value = false;
            showButtons.value = false;
          }
        } else {
          message.value = '請求失敗：' + response.statusText;
          messageType.value = 'error';
        }
      } catch (error) {
        message.value = '請求失敗：' + error.message;
        messageType.value = 'error';
      }
    };

    const toggleSeatStatus = (seat) => {
      if (
        seat.tempStatus === 'Normal' &&
        selectedSeatsCount.value < totalTicketCount.value
      ) {
        seat.tempStatus = 'Selected';
        selectedSeatsCount.value++;
      } else if (seat.tempStatus === 'Selected') {
        seat.tempStatus = 'Normal';
        selectedSeatsCount.value--;
      }
    };

    const remainingSeats = computed(() => {
      return totalTicketCount.value - selectedSeatsCount.value;
    });

    const maxSeatsPerRow = computed(() => {
      let maxSeats = 0;
      let currentCount = 0;
      let currentRow =
        seats.value.length > 0 ? seats.value[0].seatName.charAt(0) : null;
      seats.value.forEach((seat) => {
        if (seat.seatName.charAt(0) === currentRow) {
          currentCount++;
        } else {
          if (currentCount > maxSeats) maxSeats = currentCount;
          currentRow = seat.seatName.charAt(0);
          currentCount = 1;
        }
      });
      if (currentCount > maxSeats) maxSeats = currentCount;
      return maxSeats;
    });

    const gridStyle = computed(() => {
      return {
        'grid-template-columns': `repeat(${maxSeatsPerRow.value}, 1fr)`,
      };
    });

    onMounted(async () => {
      await getShowtimeDetails();
      await getSeatStatus();
    });

    return {
      seats,
      getSeatStatus,
      toggleSeatStatus,
      gridStyle,
      movieName,
      cinemaName,
      screenRoomName,
      filmType,
      showDateAndTime,
      totalPrice,
      totalTicketCount,
      showtimeId,
      remainingSeats,
    };
  },
};
</script>

<style scoped>
.maindiv {
  text-align: center;
}
.seats-container {
  display: grid;
  gap: 1px;
  margin: 0;
  padding: 0;
}
.seat-button {
  background-color: #1a1a1a;
  border-color: #1a1a1a;
}
.scrollable-container {
  width: 800px;
  height: 400px;
  overflow-x: auto;
  overflow-y: auto;
  border: 1px solid #ccc;
  margin: auto;
  justify-content: center;
  align-items: center;
}
</style>
