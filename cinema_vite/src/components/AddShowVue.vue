<template>
  <div>
    <h1>新增場次</h1>
    <form @submit.prevent="addShowtime">
      <div class="mb-3">
        <label for="movieId" class="form-label">選擇電影</label>
        <select
          class="form-control"
          id="movieId"
          @click="showid"
          v-model="newShowtime.movieId"
          required
        >
          <option value="" disabled selected>請選擇</option>
          <option
            v-for="movie in movies"
            :key="movie.movieId"
            :value="movie.movieId"
          >
            {{ movie.movieName }}
          </option>
        </select>
      </div>
      <div class="mb-3">
        <label for="screeningRoom" class="form-label">放映廳</label>
        <input
          type="text"
          class="form-control"
          id="screeningRoom"
          v-model="newShowtime.screeningRoom"
          required
        />
      </div>
      <div class="mb-3">
        <label for="filmType" class="form-label">電影類型</label>
        <input
          type="text"
          class="form-control"
          id="filmType"
          v-model="newShowtime.filmType"
          required
        />
      </div>
      <div class="mb-3">
        <label for="price" class="form-label">票價</label>
        <input
          type="number"
          class="form-control"
          id="price"
          v-model="newShowtime.price"
          required
        />
      </div>
      <div class="mb-3">
        <label for="showDateAndTime" class="form-label">放映日期和時間</label>
        <input
          type="datetime-local"
          class="form-control"
          id="showDateAndTime"
          v-model="newShowtime.showDateAndTime"
          required
        />
      </div>
      <button type="submit" class="btn btn-primary">新增場次</button>
    </form>
    <!-- 成功新增的視窗 -->
    <div v-if="showSuccessModal" class="success-modal">
      <div class="success-modal-content">
        <h3>新增成功</h3>
        <p>新增成功！</p>
        <button @click="closeSuccessModal" class="btn btn-primary">離開</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import axios from 'axios';
import { ref } from 'vue';

const newShowtime = {
  screeningRoom: '',
  filmType: '',
  price: 0,
  extraFee: 0,
  showDateAndTime: '',
  movieId: null,
};

const movies = ref([]);

// 控制模态框显示的变量
const showSuccessModal = ref(false);

// 获取电影列表
axios
  .get(`${import.meta.env.VITE_API_OSCATURL}movies`)
  .then((response) => {
    movies.value = response.data;
  })
  .catch((error) => {
    console.error('Error fetching movies:', error);
  });

// 添加场次的方法
const addShowtime = () => {
  axios
    .post(
      `${import.meta.env.VITE_API_OSCATURL}showtime/manager/add`,
      newShowtime,
    )
    .then((response) => {
      console.log(response);
      // 添加成功后的处理
      showSuccessModal.value = true;
      newShowtime.roomId = '';
      newShowtime.filmType = '';
      newShowtime.price = 0;
      newShowtime.extrafee = 0;
      newShowtime.showDateAndTime = '';
      newShowtime.movieId = null;
    })
    .catch((error) => {
      console.error('Error adding showtime:', error);
    });
};

// 关闭模态框的方法
const closeSuccessModal = () => {
  showSuccessModal.value = false;
};

const showid = () => {
  console.log(newShowtime.movieId);
};
</script>

<style>
/* 模态框样式 */
.success-modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.success-modal-content {
  background-color: white;
  padding: 20px;
  border-radius: 5px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
  text-align: center;
}
</style>
