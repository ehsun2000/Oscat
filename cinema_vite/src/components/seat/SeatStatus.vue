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
      selectedStatus: '', // 初始值為空字符串
      seatStatusAPI: import.meta.env.VITE_API_UpdateSeatStatus,
    };
  },
  props: {
    selectedSeatId: String,
    selectedSeatStatus: String,
  },
  created() {
    this.selectedStatus = this.selectedSeatStatus;
  },
  methods: {
    submitForm() {
      const selectedSeatId = this.selectedSeatId;
      const status = this.selectedStatus;

      if (selectedSeatId) {
        const url = `${this.seatStatusAPI}?id=${selectedSeatId}&status=${status}`;

        fetch(url, {
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
      } else {
        console.error('selectedSeatId 為 undefined');
      }
    },
    closeDialog() {
      this.$emit('closeDialog'); // 傳遞關閉事件給父組件
    },
  },
};
</script>
