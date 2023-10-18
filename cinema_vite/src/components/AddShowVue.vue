<template>
     <div>
          <h1>新增場次</h1>
          <form @submit.prevent="addShowtime">
               <div class="mb-3">
                    <label for="filmType" class="form-label">電影類型</label>
                    <input type="text" class="form-control" id="filmType" v-model="newShowtime.filmType" required>
               </div>
               <div class="mb-3">
                    <label for="extraFee" class="form-label">額外費用</label>
                    <input type="number" class="form-control" id="extraFee" v-model="newShowtime.extraFee" required>
               </div>
               <div class="mb-3">
                    <label for="showDateAndTime" class="form-label">放映日期和時間</label>
                    <input type="datetime-local" class="form-control" id="showDateAndTime"
                         v-model="newShowtime.showDateAndTime" required>
               </div>
               <div class="mb-3">
                    <label for="movieId" class="form-label">選擇電影</label>
                    <select class="form-control" id="movieId" v-model="newShowtime.movieId" required>
                         <option value="" disabled selected>請選擇電影</option>
                         <option v-for="movie in movies" :key="movie.movieId" :value="movie.movieId">{{ movie.movieName }}
                         </option>
                    </select>
               </div>
               <!-- 其他字段和选择放映厅的方法 -->
               <button type="submit" class="btn btn-primary">新增場次</button>
          </form>
     </div>
</template>
   
<script>
import axios from 'axios';

export default {
     data() {
          return {
               newShowtime: {
                    filmType: '',
                    extraFee: 0,
                    showDateAndTime: '',
                    movieId: null,
                    // 其他字段
               },
               movies: [], // 电影列表，需要从后端获取
          };
     },
     created() {
          axios
               .get(`${import.meta.env.VITE_API_OSCATURL}showtime/manager/findAll`) // 获取场次列表
               .then((response) => {
                    // 这里将场次数据存储在newShowtime中，可以根据你的场次数据格式进行调整
                    // 假设场次数据是一个数组，你可以选择其中一个场次，或者自行处理
                    if (response.data.length > 0) {
                         this.newShowtime = response.data[0];
                    }
               })
               .catch((error) => {
                    console.error('Error fetching showtimes:', error);
               });
     }
}
</script>
   
<style>
/* 这里可以添加组件的样式 */
</style>
   