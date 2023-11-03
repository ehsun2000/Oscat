<template>
  <div class="checkout-container">
    <h1>結帳資訊</h1>
    <div>
      <h1>{{ movieName }}</h1>
      <h2>{{ cinemaName }}</h2>
      <h3>{{ screenRoomName }}</h3>
      <h4>{{ filmType }}</h4>
      <h3>{{ showDateAndTime }}</h3>
      <h3>總價：{{ totalPrice }}</h3>
    </div>
    <div>
      <h2>選擇的座位：{{ selectedSeatNames.join(', ') }}</h2>
      <h2>票種：{{ ticketDisplays.join(', ') }}</h2>
      <h2>場次ID：{{ showtimeId }}</h2>
    </div>
    <div class="buttons-container">
      <button @click="onSiteCheckout">現場結帳</button>
      <button @click="greenWorldCheckout">綠界結帳</button>
      <button @click="cancelOrder">取消訂單</button>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import axios from 'axios';

export default {
  setup() {
    const route = useRoute();
    const router = useRouter();

    const movieName = ref('');
    const cinemaName = ref('');
    const screenRoomName = ref('');
    const filmType = ref('');
    const showDateAndTime = ref('');
    const totalPrice = ref(0);
    const selectedSeatIds = ref([]);
    const selectedSeatNames = ref([]);
    const ticketTypeIds = ref([]);
    const showtimeId = ref('');
    const ticketDisplays = ref([]);

    const api = import.meta.env.VITE_OSCAT_API_ENDPOINT;

    const ticketTypesMapping = {
      ticket_1: '全票',
      ticket_2: '半票',
      ticket_3: '學生票',
      ticket_4: '軍警票',
    };

    let counts = [0, 0, 0, 0];

    onMounted(() => {
      movieName.value = route.query.movieName;
      cinemaName.value = route.query.cinemaName;
      screenRoomName.value = route.query.screenRoomName;
      filmType.value = route.query.filmType;
      showDateAndTime.value = route.query.showDateAndTime;
      totalPrice.value = route.query.totalPrice;
      selectedSeatIds.value = JSON.parse(route.query.selectedSeatIds || '[]');
      Object.keys(ticketTypesMapping).forEach((ticketKey, index) => {
        if (route.query[ticketKey]) {
          ticketDisplays.value.push(
            `${ticketTypesMapping[ticketKey]} * ${route.query[ticketKey]}`,
          );
          counts[index] = route.query[ticketKey];
        }
      });
      console.log(counts);
      showtimeId.value = route.query.showtimeId;
      selectedSeatNames.value = route.query.selectedSeatNames
        ? route.query.selectedSeatNames.split(',')
        : [];
    });

    console.log(route.query.ticketQueryParams);

    const onSiteCheckout = async () => {
      const postData = {
        showtimeId: showtimeId.value,
        memberId: '11800689-C733-8E66-5583-78A648C13257',
        paymentMethod: '現場付款',
        totalPrice: parseFloat(totalPrice.value),
        tickets: selectedSeatIds.value.map((seatId, index) => ({
          typeId: ticketTypeIds.value[index],
          seatId: seatId,
        })),
      };

      try {
        const response = await axios.post(`${api}/offical/booking`, postData);

        if (response.status === 200) {
          alert('訂票成功!');
          // 使用 Vue Router 導向首頁
          router.push('/');
        }
      } catch (error) {
        console.error('結帳失敗:', error);
      }
    };

    const greenWorldCheckout = () => {
      // 綠界結帳的邏輯
    };

    const cancelOrder = () => {
      // 取消訂單的邏輯
    };

    return {
      movieName,
      cinemaName,
      screenRoomName,
      filmType,
      showDateAndTime,
      totalPrice,
      selectedSeatIds,
      ticketTypeIds,
      showtimeId,
      onSiteCheckout,
      greenWorldCheckout,
      cancelOrder,
      ticketDisplays,
      selectedSeatNames,
    };
  },
};
</script>

<style scoped>
.checkout-container {
  max-width: 800px;
  margin: 0 auto;
  text-align: center;
  padding: 20px;
  box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
}

.buttons-container {
  margin-top: 20px;
}

button {
  margin: 5px;
  padding: 10px 20px;
  font-size: 16px;
  cursor: pointer;
  border: none;
  border-radius: 5px;
}

button:hover {
  opacity: 0.8;
}
</style>
