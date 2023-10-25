<template>
    <div class="maindiv">
      <h1>查詢所有座位</h1>
        <select v-model="selectedCinemaId">
          <option disabled value="">------請選擇戲院------</option>
          <option v-for="cinema in cinemas" :key="cinema.id" :value="cinema.id">
            {{ cinema.name }}
          </option>
        </select>
      <div
        v-if="message"
        :class="[messageType === 'error' ? 'error-message' : 'success-message']"
      >
        {{ message }}
      </div>
    </div>
  </template>
  
  <script>
  import { ref, computed, onMounted } from 'vue';
  
  export default {
    setup() {
      const cinemas = ref([]);
      const selectedCinemaId = ref('');
      const message = ref(null);
      const messageType = ref('');
  
      const api = import.meta.env.VITE_OSCAT_API_ENDPOINT;
  
      const fetchCinemas = async () => {
        try {
          const response = await fetch(`${api}/cinemas/all`, {
            credentials: 'include',
          });
          if (response.ok) {
            cinemas.value = await response.json();
          } else {
            console.error('Failed to fetch cinemas:', response.statusText);
          }
        } catch (error) {
          console.error('Error fetching cinemas:', error);
        }
      };

      onMounted(() => {
      fetchCinemas();
    });
  
      return {
        cinemas,
        selectedCinemaId,
        message,
        messageType,
      };
    },
  };
  </script>
  
  <style scoped>
  .maindiv {
    text-align: center;
  }
  .success-message {
    color: green;
  }
  .error-message {
    color: red;
  } 