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
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { useRoute, useRouter } from 'vue-router';

export default {
  setup() {
    const route = useRoute();
    const ticketTypes = ref([]);
    const ticketCounts = ref({});
    const cinemaName = ref(route.query.cinemaName);
    const movieName = ref(route.query.movieName);
    const screenRoomName = ref(route.query.screenRoomName);
    const basicPrice = ref(parseInt(route.query.basicPrice || '0'));
    const showtimeId = ref(route.query.showtimeId);
    const filmType = ref('');
    const showDateAndTime = ref('');
    const extraFee = ref(0);
    const router = useRouter();

    const api = import.meta.env.VITE_OSCATOfficial_API_ENDPOINT;

    const fetchTicketTypes = async () => {
      try {
        const response = await axios.get(`${api}/ticketTypes`);
        ticketTypes.value = response.data;

        ticketTypes.value.forEach((ticket) => {
          ticketCounts.value[ticket.typeId] = 0;
        });
      } catch (error) {
        console.error('Error fetching ticket types:', error);
      }
    };

    const increaseCount = (typeId) => {
      if (totalTicketCount() < 10) {
        ticketCounts.value[typeId]++;
      }
    };

    const decreaseCount = (typeId) => {
      if (ticketCounts.value[typeId] && ticketCounts.value[typeId] > 0) {
        ticketCounts.value[typeId]--;
      }
    };

    const fetchShowTimeDetails = async () => {
      try {
        const response = await axios.get(
          `${api}/findshowtime/${showtimeId.value}`,
        );
        filmType.value = response.data.filmType;
        showDateAndTime.value = response.data.showDateAndTime;
        extraFee.value = Math.round(parseFloat(response.data.extraFee));
      } catch (error) {
        console.error('Error fetching showtime details:', error);
      }
    };

    const calculatePrice = (base, extra, price) => {
      return parseInt(base) + parseInt(extra) + price;
    };

    const calculateTotalPrice = () => {
      return ticketTypes.value.reduce((acc, ticket) => {
        const priceForTicketType = calculatePrice(
          basicPrice.value,
          extraFee.value,
          ticket.price,
        );
        return (
          acc + priceForTicketType * (ticketCounts.value[ticket.typeId] || 0)
        );
      }, 0);
    };

    const totalTicketCount = () => {
      return Object.values(ticketCounts.value).reduce(
        (acc, count) => acc + count,
        0,
      );
    };

    const formatDate = (dateStr) => {
      const date = new Date(dateStr);
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0'); // 加 1 因為月份是從 0 開始的
      const day = String(date.getDate()).padStart(2, '0');
      const hour = String(date.getHours()).padStart(2, '0');
      const minute = String(date.getMinutes()).padStart(2, '0');

      return `${year}/${month}/${day} ${hour}:${minute}`;
    };

    const navigateToSelectSeats = () => {
      const selectedTicketsQuery = Object.entries(ticketCounts.value).reduce(
        (acc, [typeId, count]) => {
          if (count > 0) {
            return { ...acc, [`ticket_${typeId}`]: count };
          }
          return acc;
        },
        {},
      );
      router.push({
        name: 'SelectSeats',
        query: {
          ...selectedTicketsQuery,
          movieName: movieName.value,
          cinemaName: cinemaName.value,
          screenRoomName: screenRoomName.value,
          filmType: filmType.value,
          showDateAndTime: formatDate(showDateAndTime.value),
          totalPrice: calculateTotalPrice(),
          totalTicketCount: totalTicketCount(),
          showtimeId: showtimeId.value,
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
</script>

<style scoped>
.maindiv {
  text-align: center;
}
.text {
  font-size: small;
}
</style>
