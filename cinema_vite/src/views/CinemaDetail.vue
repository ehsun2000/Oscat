<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';

const cinema = ref({});
const route = useRoute();

console.log('script setup 執行了'); // 確認整個 <script setup> 區塊是否執行

onMounted(async () => {
  console.log('onMounted 執行了'); // 確認 onMounted 是否執行

  try {
    const cinemaId = route.params.cinemaId;

    const response = await fetch(
      `http://localhost:8080/Oscat/api/cinemas/${cinemaId}`,
      {
        method: 'GET',
        credentials: 'include',
      },
    );

    if (!response.ok) {
      throw new Error('Network response was not ok');
    }

    const data = await response.json();
    cinema.value = data;
    console.log(cinema.value);
  } catch (error) {
    console.error('Fetch operation error:', error);
  }
});

// 修改影城資訊的函數
const modifyCinema = () => {};

// 返回上一頁的函數
const goBack = () => {};
</script>

<template>
  <div class="container mt-5">
    <div class="row">
      <!-- 左側：影城圖片 -->
      <div class="col-md-3">
        <img
          :src="cinema.img"
          alt="Cinema Image"
          class="img-fluid img-thumbnail rounded"
        />
      </div>
      <!-- 修改和返回按鈕 -->

      <!-- 右側：影城資訊 -->
      <div class="col-md-9">
        <!-- 影城名稱 -->
        <h2>{{ cinema.name }}</h2>

        <div class="mb-3">
          <button class="btn btn-primary me-2" @click="modifyCinema">
            <i class="bi bi-pencil-fill"></i> 修改
            <!-- Bootstrap Icons 修改圖標 -->
          </button>
          <button class="btn btn-secondary" @click="goBack">
            <i class="bi bi-arrow-left"></i> 返回
            <!-- Bootstrap Icons 返回圖標 -->
          </button>
        </div>
        <!-- 基本資訊 -->
        <div class="d-flex align-items-center mb-3">
          <h6 class="me-3">地址:</h6>
          <p class="mb-0">{{ cinema.address }}</p>
        </div>

        <div class="d-flex align-items-center mb-3">
          <h6 class="me-3">電話:</h6>
          <p class="mb-0">{{ cinema.phone }}</p>
        </div>

        <div class="d-flex align-items-center mb-3">
          <h6 class="me-3">營業時間:</h6>
          <p class="mb-0">{{ cinema.openingHours }}</p>
        </div>

        <!-- 票價類型 -->
        <div>
          <h6>票價類型:</h6>
          <ul>
            <li v-for="item in cinema.types" :key="item">
              {{ item }}
            </li>
          </ul>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
