<template>
  <div class="modal">
    <h2>修改座位狀態</h2>
    <form @submit.prevent="submitForm">
      <label for="status">座位狀態：</label>
      <input type="radio" v-model="selectedStatus" value="Normal" name="status" />
      正常
      <input type="radio" v-model="selectedStatus" value="Maintenance" name="status" />
      維修中
      <button type="submit">確認</button>
    </form>
  </div>
</template>

<script>
export default {
  data() {
    return {
      selectedStatus: '',
      seatStatusAPI: import.meta.env.VITE_API_UpdateSeatStatus,
    };
  },
  methods: {
    submitForm() {
      const selectedSeatId = this.someProperty; // 從父組件傳遞的座位 ID**這裡需要研究一下
      const status = this.selectedStatus;

      fetch(`${this.seatStatusAPI}?id=${selectedSeatId}&status=${status}`, {
        method: 'PUT',
      })
        .then((response) => {
          if (response.ok) {
            this.closeDialog();
          } else {
            console.error('更新失敗：' + response.statusText);
          }
        })
        .catch((error) => {
          console.error('請求失敗：' + error.message);
        });
    },

    closeDialog() {
      this.closeDialog();
    },
  },
};
</script>
