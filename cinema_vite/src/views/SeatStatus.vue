<template>
  <div class="modal">
    <p>座位 ：{{ selectedSeatName }}</p>
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
import { ref } from 'vue';

export default {
  props: {
    selectedSeatId: String,
    selectedSeatName: String,
    selectedSeatStatus: String,
  },
  setup(props, context) {
    const selectedStatus = ref(props.selectedSeatStatus);
    const seatStatusAPI = import.meta.env.VITE_API_UpdateSeatStatus;

    const submitForm = () => {
      const selectedSeatId = props.selectedSeatId;
      const status = selectedStatus.value;

      if (selectedSeatId) {
        const url = `${seatStatusAPI}?id=${selectedSeatId}&status=${status}`;

        fetch(url, {
          method: 'PUT',
        })
          .then((response) => {
            if (response.ok) {
              context.emit('seatUpdated');
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
    };

    return {
      selectedStatus,
      submitForm,
    };
  },
};
</script>
