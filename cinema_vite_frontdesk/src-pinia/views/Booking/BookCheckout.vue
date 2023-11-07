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
    </div>
    <div class="buttons-container">
      <button @click="onSiteCheckout">現場結帳</button>
      <button @click="ecpayCheckout">綠界結帳</button>
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

    const api = import.meta.env.VITE_OSCATOfficial_API_ENDPOINT;

    const ticketTypesMapping = {
      ticket_1: '全票',
      ticket_2: '半票',
      ticket_3: '學生票',
      ticket_4: '軍警票',
    };

    onMounted(() => {
      movieName.value = route.query.movieName;
      cinemaName.value = route.query.cinemaName;
      screenRoomName.value = route.query.screenRoomName;
      filmType.value = route.query.filmType;
      showDateAndTime.value = route.query.showDateAndTime;
      totalPrice.value = parseFloat(route.query.totalPrice);
      showtimeId.value = route.query.showtimeId;

      if (route.query.selectedSeatIds) {
        selectedSeatIds.value = JSON.parse(route.query.selectedSeatIds);
      }

      Object.keys(ticketTypesMapping).forEach((ticketKey) => {
        if (route.query[ticketKey]) {
          ticketDisplays.value.push(
            `${ticketTypesMapping[ticketKey]} x ${route.query[ticketKey]}`,
          );
          for (let i = 0; i < parseInt(route.query[ticketKey], 10); i++) {
            ticketTypeIds.value.push(ticketKey.replace('ticket_', ''));
          }
        }
      });

      showtimeId.value = route.query.showtimeId;
      selectedSeatNames.value = route.query.selectedSeatNames
        ? route.query.selectedSeatNames.split(',')
        : [];

      ticketDisplays.value.forEach((display) => {
        const matchedKey = Object.keys(ticketTypesMapping).find((key) =>
          display.includes(ticketTypesMapping[key]),
        );
        if (matchedKey) {
          const typeId = parseInt(matchedKey.split('_')[1]);
          const count = parseInt(display.split(' x ')[1]);
          for (let i = 0; i < count; i++) {
            ticketTypeIds.value.push(typeId);
          }
        }
      });
    });

    const onSiteCheckout = async () => {
      // 從API獲取已被預訂的座位ID
      let bookedSeatIds = [];
      try {
        const response = await axios.get(
          `${api}/showtime/${showtimeId.value}/seatIds`,
        );
        bookedSeatIds = response.data;
      } catch (error) {
        console.error('獲取已預訂座位ID失敗:', error);
        alert('無法獲取座位資訊，請稍後再試。');
        return;
      }

      // 檢查用戶選擇的座位是否已被預訂
      const isAnySeatBooked = selectedSeatIds.value.some((seatId) =>
        bookedSeatIds.includes(seatId),
      );

      if (isAnySeatBooked) {
        // 如果有座位已被預訂，通知用戶並返回選座位頁面
        alert('座位已被預訂，請重新預定');
        router.go(-1); // 將用戶踢回前一頁
        return;
      }

      // 如果座位檢查通過，則進行訂票操作
      const postData = {
        showtimeId: showtimeId.value,
        paymentMethod: '現場付款',
        totalPrice: parseFloat(totalPrice.value),
        tickets: selectedSeatIds.value.map((seatId, index) => ({
          typeId: ticketTypeIds.value[index],
          seatId: seatId,
        })),
      };
      try {
        const response = await axios.post(`${api}/booking`, postData);
        if (response.status === 201) {
          alert('訂票成功!');
          router.push('/');
        }
      } catch (error) {
        console.error('結帳失敗:', error);
      }
    };

    const ecpayCheckout = async () => {
      // 從API獲取已被預訂的座位ID
      let bookedSeatIds = [];
      try {
        const response = await axios.get(
          `${api}/showtime/${showtimeId.value}/seatIds`,
        );
        bookedSeatIds = response.data;
      } catch (error) {
        console.error('獲取已預訂座位ID失敗:', error);
        alert('無法獲取座位資訊，請稍後再試。');
        return;
      }

      // 檢查用戶選擇的座位是否已被預訂
      const isAnySeatBooked = selectedSeatIds.value.some((seatId) =>
        bookedSeatIds.includes(seatId),
      );

      if (isAnySeatBooked) {
        // 如果有座位已被預訂，通知用戶並返回選座位頁面
        alert('座位已被預訂，請重新預定');
        router.go(-1); // 將用戶踢回前一頁
        return;
      }

      // 如果座位檢查通過，則進行訂票操作
      const postData = {
        showtimeId: showtimeId.value,
        paymentMethod: '綠界付款',
        totalPrice: parseFloat(totalPrice.value),
        tickets: selectedSeatIds.value.map((seatId, index) => ({
          typeId: ticketTypeIds.value[index],
          seatId: seatId,
        })),
      };
      try {
        const response = await axios.post(`${api}/booking`, postData);
        if (response.status === 201) {
          const postData = {
            totalPrice: totalPrice.value.toString(),
            tradeDesc: ticketDisplays.value.join(', '),
          };

          try {
            const response = await axios.post(`${api}/goECPay`, postData);
            // 假設後端現在返回一個含有重導URL的JSON物件
            if (response.data && response.data.redirectUrl) {
              // 如果是一個URL，就可以直接重導
              window.location.href = response.data.redirectUrl;
            } else {
              // 如果後端返回的是HTML，則需要以其他方式處理
              // 例如，將HTML插入到頁面中，並自動提交表單
              document.body.innerHTML += response.data; // 假設response.data是HTML表單
              document.forms[0].submit(); // 自動提交第一個表單
            }
          } catch (error) {
            console.error('綠界結帳失敗:', error);
          }
        }
      } catch (error) {
        console.error('結帳失敗:', error);
      }
    };

    const cancelOrder = () => {
      if (confirm('真的要取消訂單嗎？')) {
        router.push('/');
      }
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
      ecpayCheckout,
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
