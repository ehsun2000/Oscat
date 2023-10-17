<template>
  <div class="modal">
    <h2>修改座位狀態</h2>
    <form @submit.prevent="submitForm">
      <label for="status">座位狀態：</label>
      <input
        type="radio"
        v-model="selectedStatus"
        value="Normal"
        name="status"
      />
      正常
      <input
        type="radio"
        v-model="selectedStatus"
        value="Maintenance"
        name="status"
      />
      維修中
      <button type="submit">確認</button>
    </form>
  </div>
</template>

<script>
export default {
  data() {
    return {
      selectedStatus: '', // 單選，只能存儲一個狀態
      seatStatusAPI: import.meta.env.VITE_API_UpdateSeatStatus,
    };
  },
  methods: {
    submitForm() {
      // 同之前的 submitForm 方法，將選擇的狀態值傳遞到後端 API 進行更新
      const selectedSeatId = this.someProperty; // 從父組件傳遞的座位 ID
      const status = this.selectedStatus;

      fetch(`${this.seatStatusAPI}?id=${selectedSeatId}&status=${status}`, {
        method: 'PUT',
      })
        .then((response) => {
          if (response.ok) {
            // 更新成功，處理成功後的邏輯
            this.closeDialog();
          } else {
            // 更新失敗，處理失敗後的邏輯
            console.error('更新失敗：' + response.statusText);
          }
        })
        .catch((error) => {
          // 處理請求失敗
          console.error('請求失敗：' + error.message);
        });
    },

    closeDialog() {
      this.closeDialog(); // 這是父組件中的關閉方法
    },
  },
};
</script>
