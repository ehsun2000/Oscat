<template>
  <div class="maindiv">
    <h1>管理座位狀態</h1>
    <form @submit.prevent="getSeatStatus">
      <select v-model="selectedCinemaId" @change="updateScreeningRooms">
        <option disabled value="">------請選擇戲院------</option>
        <option
          v-for="cinema in cinemas"
          :key="cinema.cinemaId"
          :value="cinema.cinemaId"
        >
          {{ cinema.cinemaName }}
        </option>
      </select>
      <select v-model="selectedRoomId">
        <option disabled value="">------請選擇影廳------</option>
        <option
          v-for="room in screeningRooms"
          :key="room.roomId"
          :value="room.roomId"
        >
          {{ room.roomName }}
        </option>
      </select>
      <button type="submit">查詢所有座位</button>
      <br />
      <i class="bi bi-box2"></i>正常狀態 <i class="bi bi-box2-fill"></i>無法使用
    </form>
    <div id="result" class="scrollable-container">
      <div class="seats-container" :style="gridStyle">
        <div v-for="seat in seats" :key="seat.seatId">
          <button @click="toggleSeatStatus(seat)" class="seat-button">
            <i v-if="seat.tempStatus === 'Normal'" class="bi bi-box2"></i>
            <i v-else class="bi bi-box2-fill"></i><br />
            {{ seat.seatName }}
          </button>
        </div>
      </div>
      <div v-if="showButtons" class="showButtons">
        <button @click="updateAllSeats">確認更改</button>
        <button @click="resetTempStatus">取消更改</button>
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
import { ref, computed, onMounted } from 'vue';

export default {
  setup() {
    const cinemas = ref([]);
    const screeningRooms = ref([]);
    const selectedCinemaId = ref('');
    const selectedRoomId = ref('');
    const seats = ref([]);
    const message = ref(null);
    const messageType = ref('');
    const showSeats = ref(false);
    const showButtons = ref(false);

    const api = import.meta.env.VITE_OSCAT_API_ENDPOINT;

    const fetchCinemas = async () => {
      try {
        const response = await fetch(`${api}/cinema`, {
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

    const updateScreeningRooms = async () => {
      if (selectedCinemaId.value) {
        try {
          const response = await fetch(
            `${api}/screeningRoom/all?id=${selectedCinemaId.value}`,
            {
              credentials: 'include',
            },
          );
          if (response.ok) {
            screeningRooms.value = await response.json();
          } else {
            console.error(
              'Failed to fetch screening rooms:',
              response.statusText,
            );
          }
        } catch (error) {
          console.error('Error fetching screening rooms:', error);
        }
      } else {
        screeningRooms.value = [];
      }
    };

    onMounted(() => {
      fetchCinemas();
    });

    const getSeatStatus = async () => {
      if (!selectedRoomId.value) return;

      try {
        const response = await fetch(
          `${api}/seat/findAllSeatByRoomId?roomId=${selectedRoomId.value}`,
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
      seat.tempStatus = seat.tempStatus === 'Normal' ? 'Maintenance' : 'Normal';
    };

    const resetTempStatus = () => {
      seats.value.forEach((seat) => {
        seat.tempStatus = seat.seatStatus;
      });
    };

    const updateAllSeats = async () => {
      const seatsToUpdate = seats.value.filter(
        (seat) => seat.tempStatus !== seat.seatStatus,
      );

      for (const seat of seatsToUpdate) {
        const url = `${api}/seat/updateSeatStatusById?id=${seat.seatId}&status=${seat.tempStatus}`;
        await fetch(url, {
          method: 'PUT',
          credentials: 'include',
        });
      }
      await getSeatStatus();
      message.value = '更改成功';
      messageType.value = 'success';
    };

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
      const seatWidth = 50;
      const totalWidth = maxSeatsPerRow.value * seatWidth;
      return {
        'grid-template-columns': `repeat(auto-fill, minmax(${seatWidth}px, 1fr))`,
        width: `${totalWidth}px`,
      };
    });

    return {
      cinemas,
      selectedCinemaId,
      selectedRoomId,
      screeningRooms,
      seats,
      getSeatStatus,
      updateScreeningRooms,
      toggleSeatStatus,
      updateAllSeats,
      gridStyle,
      showSeats,
      showButtons,
      resetTempStatus,
      message,
      messageType,
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
.showButtons {
  text-align: center;
  margin-top: 20px;
}
.seats-container {
  display: grid;
  margin: 0;
  padding: 0;
  width: auto;
  margin: auto;
}
.seat-button {
  background-color: #1a1a1a;
  border-color: #1a1a1a;
  margin: 0; /* 減少邊距 */
  padding: 2px; /* 調整內距 */
  justify-content: center;
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
