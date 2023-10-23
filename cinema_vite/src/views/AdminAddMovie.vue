<template>
  <div class="row" style="padding-top: 100px">
    <div class="box"></div>
    <div class="col-3"></div>
    <div class="col-6">
      <h2>新增電影</h2>
      <form @submit.prevent="submitForm">
        <div class="form-group mb-3">
          <label for="movieName" class="form-label">電影名稱</label>
          <input
            type="text"
            class="form-control"
            id="movieName"
            v-model="movie.movieName"
          />
        </div>

        <div class="form-group mb-3">
          <label for="movieType" class="form-label">電影類型</label>
          <input
            type="text"
            class="form-control"
            id="movieType"
            v-model="movie.movieType"
          />
        </div>

        <div class="form-group mb-3">
          <label for="movieStatus" class="form-label">電影狀態</label>
          <select
            class="form-select"
            id="movieStatus"
            v-model="movie.movieStatus"
          >
            <option value="上映中">上映中</option>
            <option value="下檔">下檔</option>
          </select>
        </div>

        <div class="form-group mb-3">
          <label for="director" class="form-label">導演</label>
          <input
            type="text"
            class="form-control"
            id="director"
            v-model="movie.director"
          />
        </div>

        <div class="form-group mb-3">
          <label for="writerList" class="form-label">編劇</label>
          <input
            type="text"
            class="form-control"
            id="writerList"
            v-model="movie.writerList"
          />
        </div>

        <div class="form-group mb-3">
          <label for="actorList" class="form-label">演員</label>
          <input
            type="text"
            class="form-control"
            id="actorList"
            v-model="movie.actorList"
          />
        </div>

        <div class="form-group mb-3">
          <label for="plotSummary" class="form-label">簡介</label>
          <textarea
            class="form-control"
            id="plotSummary"
            v-model="movie.plotSummary"
            rows="4"
          ></textarea>
        </div>

        <div class="form-group mb-3">
          <label for="releaseDate" class="form-label">上映日期</label>
          <input
            type="date"
            class="form-control"
            id="releaseDate"
            v-model="movie.releaseDate"
          />
        </div>

        <div class="form-group mb-3">
          <label for="duration" class="form-label">片長</label>
          <input
            type="text"
            class="form-control"
            id="duration"
            v-model="movie.duration"
          />
        </div>

        <div class="form-group mb-3">
          <label for="classification" class="form-label">電影分級</label>
          <select
            class="form-select"
            id="movieStatus"
            v-model="movie.classification"
          >
            <option value="普遍級">普遍級</option>
            <option value="保護級">保護級</option>
            <option value="輔導級">輔導級</option>
            <option value="限制級">限制級</option>
          </select>
        </div>

        <div class="form-group mb-3">
          <label for="trailerLink" class="form-label">預告片連結</label>
          <input
            type="text"
            class="form-control"
            id="trailerLink"
            v-model="movie.trailerLink"
          />
        </div>

        <div class="form-group mb-3">
          <label for="posterImage" class="form-label">電影海報連結</label>
          <input
            type="text"
            class="form-control"
            id="posterImage"
            v-model="movie.posterImage"
          />
        </div>

        <div class="form-group mb-3">
          <label for="movieStills" class="form-label">宣傳照連結</label>
          <button
            type="button"
            class="btn btn-outline-success"
            style="
              --bs-btn-padding-y: 0.25rem;
              --bs-btn-padding-x: 0.5rem;
              --bs-btn-font-size: 0.75rem;
            "
            @click="addStillsLink"
          >
            <i class="bi bi-plus"></i>
          </button>
          <div v-for="(still, index) in movie.movieStills" :key="index">
            <input
              type="text"
              class="form-control"
              v-model="still.stillImageUrl"
            />
          </div>
        </div>

        <button type="submit" class="btn btn-success mx-2">新增電影</button
        ><button type="button" class="btn btn-primary" @click="previousPage">
          返回
        </button>
      </form>
    </div>
  </div>
  <p></p>
</template>

<script setup>
import { ref } from 'vue';
import axios from 'axios';
// import { useRouter } from 'vue-router';
import Movie from '@/models/Movie.js';
import Swal from 'sweetalert2';

// const router = useRouter();
const movie = ref(Movie);

const previousPage = () => {
  history.back();
};

const addStillsLink = () => {
  Swal.fire({
    title: '新增宣傳照連結',
    input: 'text',
    inputLabel: '請輸入連結',
    showCancelButton: true,
    confirmButtonText: '新增',
    cancelButtonText: '取消',
    preConfirm: (link) => {
      if (link) {
        const newStillsObject = { stillImageUrl: link };
        movie.value.movieStills.push(newStillsObject);
      }
    },
  });
};

const submitForm = async () => {
  try {
    const url = `${import.meta.env.VITE_OSCAT_API_ENDPOINT}/movie/add`;
    console.log(url);
    const response = await axios.post(url, movie.value);
    console.log(response);
    if (response.status === 200) {
      Swal.fire('新增成功', '成功新增電影', 'success');
      movie.value = ref(Movie);
      // setTimeout(() => {
      //   router.push('/movie');
      // }, 500);
    }
  } catch (error) {
    console.error(error);
    // 回傳錯誤訊息
    // 在這裡處理錯誤情況，例如顯示錯誤訊息給使用者
  }
};
// const restart = () => {
//   movie.value = ref(Movie);
// };
// restart();
</script>
