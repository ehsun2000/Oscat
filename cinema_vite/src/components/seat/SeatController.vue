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
      <div class="from" v-show="!isShow">
        <div v-if="isDialogVisible" id="dialog-container">
          <SeatStatusDialog
            :selectedSeatId="selectedSeat.seatId"
            :selectedSeatStatus="selectedSeat.seatStatus"
            @seatUpdated="closeSeatStatusDialog"
          />
          <button @click="close">關</button>
        </div>
      </div>
    </div>
    <div v-if="errorMessage" class="error-message">{{ errorMessage }}</div>
  </div>
</template>

<script>
import { ref } from 'vue';
import SeatStatusDialog from './SeatStatus.vue';

export default {
  data() {
    return {
      findSeatApi: import.meta.env.VITE_API_FindSeat,
      roomId: ref(''),
      seats: ref([]),
      errorMessage: '',
      isShow: true,
      isDialogVisible: false, // 控制彈出式視窗顯示
      selectedSeat: null, // 存儲所選座位資訊
    };
  },
  methods: {
    async getSeatStatus() {
      try {
        const response = await fetch(`${this.findSeatApi}` + `${this.roomId}`);
        if (response.ok) {
          const data = await response.json();
          this.seats = data;
          this.errorMessage = '';
        } else {
          this.errorMessage = '請求失敗：' + response.statusText;
        }
      } catch (error) {
        this.errorMessage = '請求失敗：' + error.message;
      }
    },
    openSeatStatusDialog(seat) {
      this.isShow = false;
      this.selectedSeat = seat;
      this.isDialogVisible = true;
    },
    async refreshSeatStatus(roomId) {
      try {
        const response = await fetch(`${this.findSeatApi}${roomId}`);
        if (response.ok) {
          const data = await response.json();
          this.seats = data;
          this.errorMessage = '';
        } else {
          this.errorMessage = '請求失敗：' + response.statusText;
        }
      } catch (error) {
        this.errorMessage = '請求失敗：' + error.message;
      }
    },
    close() {
      this.isShow = !this.isShow;
      this.refreshSeatStatus(this.roomId);
    },
    closeSeatStatusDialog() {
      this.isDialogVisible = false;
      this.selectedSeat = null;
      this.close(); // 這裡調用 close 方法
    },
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
.from {
  width: 400px;
  height: 400px;
  background: #efefef;
  display: flex;
  -webkit-align-items: center;
  align-items: center;
  -webkit-justify-content: center;
  justify-content: center;
}
</style>
