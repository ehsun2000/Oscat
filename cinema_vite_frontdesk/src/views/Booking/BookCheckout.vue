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
import { ref, onMounted, onUnmounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import axios from 'axios';
import { useUserStore } from '@/stores/userStore.js';

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
    const userStore = useUserStore();

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

      const savedTotalPrice = localStorage.getItem('totalPrice');
      if (savedTotalPrice) {
        totalPrice.value = parseFloat(savedTotalPrice);
      }
    });

    const calculateTotalPrice = async () => {
      const data = {
        cinemaName: cinemaName.value,
        showtimeId: showtimeId.value,
        ticketTypeCounts: {},
      };

      // 使用 Set 去除重複的票類型 ID
      const uniqueTicketTypeIds = [...new Set(ticketTypeIds.value)];

      // 計算每種票類型的張數
      uniqueTicketTypeIds.forEach((typeId) => {
        const count = ticketTypeIds.value.filter((id) => id === typeId).length;
        data.ticketTypeCounts[typeId] = count;
      });

      try {
        // 在計算新的總價格之前，先從LocalStorage中刪除舊的總價格
        localStorage.removeItem('totalPrice');

        const response = await axios.post(`${api}/calculate`, data);
        totalPrice.value = response.data.totalPrice;
        console.log(response.data.totalPrice);

        localStorage.setItem('totalPrice', totalPrice.value.toString());
      } catch (error) {
        console.error('計算總價格時發生錯誤:', error);
        alert('無法計算總價格，請稍後再試。');
      }
    };

    const onSiteCheckout = async () => {
      const originalTotalPrice = totalPrice.value; // 保存當前的總價格
      await calculateTotalPrice(); // 計算最新的總價格

      if (totalPrice.value !== originalTotalPrice) {
        alert('結帳金額有誤，將更新為正確的金額。');
        totalPrice.value = await calculateTotalPrice();
        location.reload();
        return;
      }
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
          localStorage.removeItem('totalPrice');
          router.push('/member-center');
        }
      } catch (error) {
        if (error.response && error.response.status === 401) {
          if (!userStore.isAuthenticated) {
            userStore.setRedirectAfterLogin(router.currentRoute.value.fullPath);
            alert('請先登入才能進行購票。');
            router.push('/sign-in');
          }
        } else {
          console.error('結帳失敗:', error);
        }
      }
    };

    const ecpayCheckout = async () => {
      const originalTotalPrice = totalPrice.value; // 保存當前的總價格
      await calculateTotalPrice(); // 計算最新的總價格

      if (totalPrice.value !== originalTotalPrice) {
        alert('結帳金額有誤，將更新為正確的金額。');
        totalPrice.value = await calculateTotalPrice();
        location.reload();
        return;
      }
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
          const orderId = response.data;
          console.log(response.data);
          const ecpayData = {
            totalPrice: totalPrice.value.toString(),
            orderId: orderId,
          };

          console.log(ecpayData);

          try {
            const response = await axios.post(`${api}/goECPay`, ecpayData);
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
        if (error.response && error.response.status === 401) {
          if (!userStore.isAuthenticated) {
            userStore.setRedirectAfterLogin(router.currentRoute.value.fullPath);
            alert('請先登入才能進行購票。');
            router.push('/sign-in');
          }
        } else {
          console.error('結帳失敗:', error);
        }
      }
    };

    const cancelOrder = () => {
      if (confirm('真的要取消訂單嗎？')) {
        localStorage.removeItem('totalPrice');
        router.push('/');
      }
    };

    onUnmounted(() => {
      localStorage.removeItem('totalPrice');
    });

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
