<template>
  <div>
    <div v-for="ticket in ticketTypes" :key="ticket.typeId">
      {{ ticket.typeName }} - 數量: {{ count }}
      <button @click="increaseCount">票數</button>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import axios from 'axios';
// import { useRoute } from 'vue-router';

export default {
  setup() {
    const ticketTypes = ref([]);
    const count = ref(0);

    const api = import.meta.env.VITE_OSCAT_API_ENDPOINT;

    const fetchTicketTypes = () => {
      axios
        .get(`${api}/book/ticketTypes`)
        .then((response) => {
          ticketTypes.value = response.data;
        })
        .catch((error) => {
          console.error('Error fetching ticket types:', error);
        });
    };

    const increaseCount = () => {
      count.value += 1;
    };
    // const route = useRoute();
    // const movieId = route.params.movieId;
    // const cinemaId = route.params.cinemaId;
    // const selectedDate = route.params.date;
    // const selectedTime = route.params.time;

    onMounted(fetchTicketTypes);

    return {
      ticketTypes,
      count,
      increaseCount,
    };
  },
};
</script>

<style scoped></style>
