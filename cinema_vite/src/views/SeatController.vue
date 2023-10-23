<template>
  <div>
    <h1>查詢所有座位</h1>
    <form @submit.prevent="getSeatStatus">
      <label for="roomId">放映廳ID：</label>
      <input v-model="roomId" type="text" id="roomId" name="roomId" />
      <button type="button" @click="getSeatStatus">查詢所有座位</button>
      <br />
      <i class="bi bi-box2"></i>正常狀態 <i class="bi bi-box2-fill"></i>無法使用
    </form>
    <div id="result" class="scrollable-container">
      <div class="seats-container" :style="gridStyle">
        <div v-for="seat in seats" :key="seat.seatId">
          <button @click="toggleSeatStatus(seat)">
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
    <div v-if="errorMessage" class="error-message">{{ errorMessage }}</div>
  </div>
</template>

<script>
import { ref, computed } from 'vue';

export default {
  setup() {
    const roomId = ref('');
    const seats = ref([]);
    const errorMessage = ref(null);
    const showSeats = ref(false);
    const showButtons = ref(false);

    const api = import.meta.env.VITE_OSCAT_API_ENDPOINT;

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
          errorMessage.value = '請求失敗：' + response.statusText;
        }
      } catch (error) {
        errorMessage.value = '請求失敗：' + error.message;
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
      return {
        'grid-template-columns': `repeat(${maxSeatsPerRow.value}, 1fr)`,
      };
    });

    return {
      roomId,
      seats,
      errorMessage,
      getSeatStatus,
      toggleSeatStatus,
      updateAllSeats,
      gridStyle,
      showSeats,
      showButtons,
      resetTempStatus,
    };
  },
};
</script>

<style scoped>
.error-message {
  color: red;
}
.seats-container {
  display: grid;
  gap: 1px; /* 調整這個值來改變座位之間的間距 */
  margin: 0;
  padding: 0;
}
.showButtons {
  text-align: center;
  margin-top: 20px;
}
.scrollable-container {
  width: 800px; /* 這裡的寬度可以根據你的需求進行調整 */
  height: 400px; /* 這裡的高度可以根據你的需求進行調整 */
  overflow-x: auto; /* 橫向捲動 */
  overflow-y: auto; /* 縱向捲動 */
  border: 1px solid #ccc; /* 這是一個可選的邊框，僅為了視覺效果 */
}
</style>
