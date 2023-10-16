<template>
  <div>
    <h1>查詢所有座位</h1>
    <form @submit.prevent="getSeatStatus">
      <label for="roomId">放映廳ID：</label>
      <input v-model="roomId" type="text" id="roomId" name="roomId" />
      <button type="button" @click="getSeatStatus">查詢所有座位</button>
    </form>
    <div id="result">
      <p v-for="seat in seats" :key="seat.seatId">
        座位名稱: {{ seat.seatName }}, 狀態: {{ seat.seatStatus }}
      </p>
    </div>
    <div v-if="errorMessage" class="error-message">{{ errorMessage }}</div>
  </div>
</template>

<script>
import { ref } from 'vue';

export default {
  data() {
    return {
      findSeatApi: import.meta.env.VITE_API_FindSeat,
      seatIds: [],
      roomId: ref(''),
      seats: ref([]),
      errorMessage: '',
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
  },
};
</script>

<style scoped>
.error-message {
  color: red;
}
</style>
