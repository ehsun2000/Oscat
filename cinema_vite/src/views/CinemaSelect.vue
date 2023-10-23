<script setup>
import { ref, onMounted } from 'vue'; // 從 vue 匯入 ref 和 onMounted
import CinemaCard from '@/components/CinemaCard.vue'; // 匯入 CinemaCard 組件

const cinemas = ref([]); // 宣告一個 ref 來儲存影城資料

// 當組件掛載後執行這個函數
onMounted(async () => {
  // 使用 fetch API 來獲取影城資料
  const response = await fetch(
    `${import.meta.env.VITE_OSCAT_API_ENDPOINT}/cinemas/?size=3`,
    {
      method: 'GET',
      credentials: 'include', // 帶有憑證的請求
    },
  );
  const data = await response.json();
  // 使用 Cinema 模型來轉換資料
  cinemas.value = data.content;
});
</script>

<template>
  <div class="content">
    <h1 class="text-center md-12 my-3">請選擇影城</h1>
    <div class="d-flex justify-content-evenly md-12">
      <!-- 使用 v-for 和 v-bind 來迭代影城資料 -->
      <CinemaCard
        v-for="cinema in cinemas"
        :key="cinema.id"
        :id="cinema.id"
        :cinemaName="cinema.name"
        :imgUrl="cinema.img"
      />
    </div>
  </div>
</template>

<style scoped></style>
