<template>
  <div>
    <div v-for="ticket in ticketTypes" :key="ticket.typeId">
      {{ ticket.typeName }} - 數量: {{ ticketCounts[ticket.typeId] || 0 }}
      <button @click="increaseCount(ticket.typeId)">票數</button>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import axios from 'axios';

export default {
  setup() {
    const ticketTypes = ref([]);
    const ticketCounts = ref({}); // 使用對象來存儲每個票種的數量

    const api = import.meta.env.VITE_OSCAT_API_ENDPOINT;

    const fetchTicketTypes = async () => {
      try {
        const response = await axios.get(`${api}/book/ticketTypes`);
        ticketTypes.value = response.data;

        // 初始化每個票種的數量為 0
        ticketTypes.value.forEach(ticket => {
          ticketCounts.value[ticket.typeId] = 0;
        });
      } catch (error) {
        console.error('Error fetching ticket types:', error);
      }
    };

    const increaseCount = (typeId) => {
      ticketCounts.value[typeId]++;
    };

    onMounted(fetchTicketTypes);

    return {
      ticketTypes,
      ticketCounts, // 將 ticketCounts 返回到模板中
      increaseCount,
    };
  },
};
</script>

<style scoped></style>
