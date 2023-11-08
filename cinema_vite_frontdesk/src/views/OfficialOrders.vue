<template>
  <div class="container w-75">
    <h3>訂 單 紀 錄</h3>
    <button
      type="button"
      class="mb-5 btn btn-secondary btn-sm"
      @click="previousPage"
    >
      返回
    </button>
    <div class="py-3 text-center"></div>
    <div class="row">
      <div v-if="transOrders && transOrders.length > 0" class="col">
        <div
          v-for="(order, index) in transOrders"
          :key="order.orderId"
          class="card mb-3 custom-card"
        >
          <div class="card-body">
            <h4 class="card-title">
              {{ order.movieName }}
              <span class="badge bg-primary rounded-pill">{{ index + 1 }}</span>
            </h4>
            <h6>地點:</h6>
            <p class="card-text">{{ order.cinemaName }} {{ order.roomName }}</p>
            <h6>時間:</h6>
            <p class="card-text">{{ formatDateTime(order.showDateAndTime) }}</p>
            <p></p>
            <h6>訂單編號:</h6>
            <p class="card-text">{{ order.orderId }}</p>
            <h6>付款方式:</h6>
            <p class="card-text">{{ order.paymentMethod }}</p>
            <h6>購票日期:</h6>
            <p class="card-text">
              {{ formatDateTime(order.bookingDateAndTime) }}
            </p>
            <div v-for="ticket in order.tickets" :key="ticketId">
              <h6>座位:</h6>
              <p class="card-text">
                {{ ticket.seat }} ({{ ticket.ticketTypeName }})
              </p>
            </div>
          </div>
        </div>
      </div>
      <div v-else>
        <h4>無訂單紀錄</h4>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRoute } from 'vue-router';
import axios from 'axios';

const route = useRoute();
const transOrders = ref([]);

// 返回前一頁
const previousPage = () => {
  history.back();
};

// 統一日期格式
function formatDateTime(dateTime) {
  if (!dateTime) return ''; // 未定義或空

  const date = dateTime.split('T')[0];
  const time = dateTime.split('T')[1].split('.')[0];
  return `${date} ${time}`;
}

// 所有訂單
const loadData = async () => {
  const id = route.params.memberId;
  const url = `${
    import.meta.env.VITE_OSCATOfficial_API_ENDPOINT
  }/member/order/${id}`;
  console.log(url);
  const data = await axios.get(url);
  transOrders.value = data.data.slice().reverse();
  console.log(transOrders.value);
};

loadData();
</script>

<style scoped>
/* card 樣式 浮動到右邊 */
.custom-card {
  float: left;
  margin-right: 10px;
}
</style>
