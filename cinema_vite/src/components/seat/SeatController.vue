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
    <div id="result">
      <div class="seats-container" :style="gridStyle" v-show="isShow">
        <template v-for="seat in seats" :key="seat.seatId">
          <button @click="openSeatStatusDialog(seat)">
            <i v-if="seat.seatStatus === 'Normal'" class="bi bi-box2"></i>
            <i v-else class="bi bi-box2-fill"></i><br />
            {{ seat.seatName }}
          </button>
        </template>
      </div>
      <div class="form" v-show="!isShow">
        <div v-if="isDialogVisible">
          <SeatStatusDialog
            :selectedSeatId="selectedSeat.seatId"
            :selectedSeatName="selectedSeat.seatName"
            :selectedSeatStatus="selectedSeatStatus"
            @seatUpdated="closeSeatStatusDialog"
          />
          <button @click="closeSeatStatusDialog">關</button>
        </div>
      </div>
    </div>
    <div v-if="errorMessage" class="error-message">{{ errorMessage }}</div>
  </div>
</template>

<script>
import { ref, onMounted, computed } from 'vue';
import SeatStatusDialog from './SeatStatus.vue';
import 'bootstrap-icons/font/bootstrap-icons.css';

export default {
  setup() {
    const roomId = ref('');
    const seats = ref([]);
    const errorMessage = ref(null);
    const isShow = ref(true);
    const isDialogVisible = ref(false);
    const selectedSeat = ref(null);
    const selectedSeatStatus = ref('');
    const showError = ref(false);

    const findSeatApi = import.meta.env.VITE_API_FindSeat;

    const getSeatStatus = async () => {
      if (!roomId.value) {
        return;
      }
      try {
        const response = await fetch(`${findSeatApi}${roomId.value}`);
        if (response.ok) {
          const data = await response.json();
          seats.value = data;
          errorMessage.value = '';
          showError.value = false;
        } else {
          errorMessage.value = '請求失敗：' + response.statusText;
          showError.value = true;
        }
      } catch (error) {
        errorMessage.value = '請求失敗：' + error.message;
        showError.value = true;
      }
    };

    const openSeatStatusDialog = (seat) => {
      isShow.value = false;
      selectedSeat.value = seat;
      selectedSeatStatus.value = seat.seatStatus;
      isDialogVisible.value = true;
    };

    const closeSeatStatusDialog = () => {
      isDialogVisible.value = false;
      selectedSeat.value = null;
      selectedSeatStatus.value = '';
      isShow.value = true;
      getSeatStatus();
    };

    onMounted(() => {
      getSeatStatus();
    });

    // 在 setup() 中新增以下代碼
    const maxSeatsPerRow = computed(() => {
      // 找出最多座位的那一行
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
      if (currentCount > maxSeats) maxSeats = currentCount; // 確保最後一行的座位數量也被計算
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
      isShow,
      isDialogVisible,
      selectedSeat,
      selectedSeatStatus,
      getSeatStatus,
      openSeatStatusDialog,
      closeSeatStatusDialog,
      showError,
      maxSeatsPerRow,
      gridStyle,
    };
  },
  components: {
    SeatStatusDialog,
  },
};
</script>

<style scoped>
.error-message {
  color: red;
}
.form {
  width: 800px;
  background: #efefef;
  display: flex;
  align-items: center;
  justify-content: center;
}
.seats-container {
  display: grid;
  gap: 1px; /* 這個數值可以自行調整 */
  transform: scale(0.8);
  margin: 0;
  padding: 0;
}
</style>
