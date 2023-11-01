<template>
  <div class="maindiv">
    <h1>{{ movieName }}</h1>
    <h2>{{ cinemaName }}</h2>
    <h3>{{ screenRoomName }}</h3>
    <p>{{ filmType }}</p>
    <p>{{ formatDate(showDateAndTime) }}</p>
    <div v-for="ticket in ticketTypes" :key="ticket.typeId">
      {{ ticket.typeName }}
      數量:
      <button @click="decreaseCount(ticket.typeId)">
        <i class="bi bi-chevron-left"></i>
      </button>
      {{ ticketCounts[ticket.typeId] || 0 }}
      <button @click="increaseCount(ticket.typeId)">
        <i class="bi bi-chevron-right"></i>
      </button>
      價錢：{{ calculatePrice(basicPrice, extraFee, ticket.price) }}元
    </div>
    總價：{{ calculateTotalPrice() }}元<br />
    <button @click="navigateToSelectSeats">下一步-選擇座位</button>
    <br />
    <div class="text">
      *網路訂票為10張為上限 <br />
      全票：一般成人且無任何優惠條件者請選擇全票。<br />
      半票：凡滿65歲以上年長者、持身心障礙手冊身份者、6歲以下孩童憑身分證可購買。需於現場購票、網路取票時出示本人有效證件。無證件者須補費至全票金額。<br />
      學生票：指持有學生身份者即可享有優待票價。需於現場購票、網路取票時出示本人有效證件。無證件者須補費至全票金額。<br />
      軍警票：指持有軍警身份者即可享有優待票價。需於現場購票、網路取票時出示本人有效證件。無證件者須補費至全票金額。<br />
    </div>
  </div>
</template>

<script>
import { onMounted, reactive } from 'vue';
import { useTicketStore } from '@/stores/ticketStore.js';
import { useRoute, useRouter } from 'vue-router';

export default {
  setup() {
    const route = useRoute();
    const router = useRouter();
    const ticketStore = useTicketStore();

    // 初始化從路由中獲得的資料
    const initialData = reactive({
      cinemaName: route.query.cinemaName,
      movieName: route.query.movieName,
      screenRoomName: route.query.screenRoomName,
      basicPrice: parseInt(route.query.basicPrice || '0'),
      showtimeId: route.query.showtimeId,
    });

    Object.assign(ticketStore, initialData);

    // 由商店解構資料和方法
    const {
      ticketTypes,
      ticketCounts,
      increaseCount,
      decreaseCount,
      fetchTicketTypes,
      fetchShowTimeDetails,
      cinemaName,
      movieName,
      screenRoomName,
      basicPrice,
      showtimeId,
      filmType,
      showDateAndTime,
      calculatePrice,
      extraFee,
      calculateTotalPrice,
    } = ticketStore;

    const formatDate = (dateStr) => {
      const date = new Date(dateStr);
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      const hour = String(date.getHours()).padStart(2, '0');
      const minute = String(date.getMinutes()).padStart(2, '0');
      return `${year}/${month}/${day} ${hour}:${minute}`;
    };

    const navigateToSelectSeats = () => {
      router.push({
        name: 'SelectSeats',
        params: {
          movieName: ticketStore.movieName.value,
          cinemaName: ticketStore.cinemaName.value,
          screenRoomName: ticketStore.screenRoomName.value,
          filmType: ticketStore.filmType.value,
          showDateAndTime: formatDate(ticketStore.showDateAndTime.value),
          totalPrice: ticketStore.calculateTotalPrice(),
          totalTicketCount: ticketStore.totalTicketCount(),
          showtimeId: ticketStore.showtimeId.value,
        },
      });
    };

    onMounted(() => {
      fetchTicketTypes();
      fetchShowTimeDetails();
    });

    return {
      ticketTypes,
      ticketCounts,
      increaseCount,
      decreaseCount,
      cinemaName,
      movieName,
      screenRoomName,
      basicPrice,
      showtimeId,
      filmType,
      showDateAndTime,
      calculatePrice,
      extraFee,
      calculateTotalPrice,
      formatDate,
      navigateToSelectSeats,
    };
  },
};

console.log(ticketTypes.value, ticketCounts.value, cinemaName.value, movieName.value);
</script>

<style scoped>
.maindiv {
  text-align: center;
}
.text {
  font-size: small;
}
</style>
