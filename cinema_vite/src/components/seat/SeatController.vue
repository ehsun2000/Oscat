<template>
  <div>
    <h1>查詢所有座位</h1>
    <form @submit.prevent="getSeatStatus">
      <label for="roomId">放映廳ID：</label>
      <input v-model="roomId" type="text" id="roomId" name="roomId" />
      <button type="button" @click="getSeatStatus">查詢所有座位</button>
    </form>
    <div id="result">
      <p v-for="seat in seats" :key="seat.seatId" v-show="isShow">
        <button @click="openSeatStatusDialog(seat)">
          <span style="display: none">{{ seat.seatId }}</span>
          座位名稱: {{ seat.seatName }}, 狀態: {{ seat.seatStatus }}
        </button>
      </p>
      <div class="form" v-show="!isShow">
        <div v-if="isDialogVisible" id="dialog-container">
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
import { ref, onMounted } from 'vue';
import SeatStatusDialog from './SeatStatus.vue';

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
  width: 400px;
  height: 400px;
  background: #efefef;
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
