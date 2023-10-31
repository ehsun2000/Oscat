<template>
  <div>
    <h2>{{ cinemaName }}</h2>
    <h3>{{ screenRoomName }}</h3>
    <div v-for="ticket in ticketTypes" :key="ticket.typeId">
      {{ ticket.typeName }}<br />
      數量:
      <button @click="decreaseCount(ticket.typeId)">
        <i class="bi bi-chevron-left"></i>
      </button>
      {{ ticketCounts[ticket.typeId] || 0 }}
      <button @click="increaseCount(ticket.typeId)">
        <i class="bi bi-chevron-right"></i>
      </button>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { useRoute } from 'vue-router';

export default {
  setup() {
    const route = useRoute();
    const ticketTypes = ref([]);
    const ticketCounts = ref({});
    const cinemaName = ref(route.query.cinemaName);
    const movieName = ref(route.query.movieName);
    const screenRoomName = ref(route.query.screenRoomName);

    const api = import.meta.env.VITE_OSCAT_API_ENDPOINT;

    const fetchTicketTypes = async () => {
      try {
        const response = await axios.get(`${api}/book/ticketTypes`);
        ticketTypes.value = response.data;

        ticketTypes.value.forEach((ticket) => {
          ticketCounts.value[ticket.typeId] = 0;
        });
      } catch (error) {
        console.error('Error fetching ticket types:', error);
      }
    };

    const increaseCount = (typeId) => {
      ticketCounts.value[typeId]++;
    };

    const decreaseCount = (typeId) => {
      if (ticketCounts.value[typeId] && ticketCounts.value[typeId] > 0) {
        ticketCounts.value[typeId]--;
      }
    };

    onMounted(fetchTicketTypes);

    return {
      ticketTypes,
      ticketCounts,
      increaseCount,
      decreaseCount,
      cinemaName,
      movieName,
      screenRoomName,
    };
  },
};
</script>

<style scoped></style>
