<template>
  <h3>MovieList</h3>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';

const movies = ref([]);

const fetchMovies = async () => {
  const url = `${import.meta.env.VITE_API_OSCATURL}movies`;
  try {
    const response = await axios.get(url);
    movies.value = response.data;
  } catch (error) {
    console.error('無法獲取電影數據：', error);
  }
};

onMounted(() => {
  fetchMovies();
});
</script>

<style scoped></style>
