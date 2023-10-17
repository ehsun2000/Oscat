<template>
  <form>
    <div class="row">
      <div class="col-3">
        <img :src="movie.posterImage" alt="" class="img" />
      </div>
      <div class="col-3"></div>
      <div class="col-6">
        <div class="mb-3">
          <label for="movieName" class="form-label">電影名稱</label>
          <input
            type="text"
            class="form-control"
            id="movieName"
            v-model="movie.movieName"
          />
        </div>
        <div class="mb-3">
          <label for="movieType" class="form-label">電影類型</label>
          <input
            type="text"
            class="form-control"
            id="movieType"
            v-model="movie.movieType"
          />
        </div>
        <div class="mb-3">
          <label for="movieStatus" class="form-label">電影狀態</label>
          <input
            type="text"
            class="form-control"
            id="movieStatus"
            v-model="movie.movieStatus"
          />
        </div>
        <div class="mb-3">
          <label for="director" class="form-label">導演</label>
          <input
            type="text"
            class="form-control"
            id="director"
            v-model="movie.director"
          />
        </div>
        <div class="mb-3">
          <label for="writerList" class="form-label">編劇</label>
          <input
            type="text"
            class="form-control"
            id="writerList"
            v-model="movie.writerList"
          />
        </div>
        <div class="mb-3">
          <label for="actorList" class="form-label">演員</label>
          <input
            type="text"
            class="form-control"
            id="actorList"
            v-model="movie.actorList"
          />
        </div>
        <div class="mb-3">
          <label for="plotSummary" class="form-label">簡介</label>
          <textarea
            class="form-control"
            id="plotSummary"
            v-model="movie.plotSummary"
            rows="4"
          ></textarea>
        </div>
        <div class="mb-3">
          <label for="releaseDate" class="form-label">上映日期</label>
          <input
            type="date"
            class="form-control"
            id="releaseDate"
            v-model="movie.releaseDate"
          />
        </div>
        <div class="mb-3">
          <label for="duration" class="form-label">片長</label>
          <input
            type="text"
            class="form-control"
            id="duration"
            v-model="movie.duration"
          />
        </div>
        <div class="mb-3">
          <label for="classification" class="form-label">電影分級</label>
          <input
            type="text"
            class="form-control"
            id="classification"
            v-model="movie.classification"
          />
        </div>
        <div class="mb-3">
          <label for="trailerLink" class="form-label">預告片連結</label>
          <input
            type="text"
            class="form-control"
            id="trailerLink"
            v-model="movie.trailerLink"
          />
        </div>
        <div class="mb-3">
          <label for="posterImage" class="form-label">電影海報連結</label>
          <input
            type="text"
            class="form-control"
            id="posterImage"
            v-model="movie.posterImage"
          />
        </div>

        <table class="table table-bordered">
          <thead>
            <tr>
              <th>宣傳照連結</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            <tr
              v-for="(still, index) in movie.movieStills"
              :key="still.stillId"
            >
              <td>
                <input
                  class="form-control"
                  v-model="movie.movieStills[index].stillImageUrl"
                  rows="4"
                />
              </td>
              <td>
                <button class="btn btn-danger" @click="removeStillsLink(index)">
                  刪除
                </button>
              </td>
            </tr>
          </tbody>
        </table>

        <button class="btn btn-secondary" type="button" @click="updateHandler">
          修改
        </button>
      </div>
      <div class="col-3"></div>
    </div>
  </form>
  <p></p>
</template>

<script setup>
import { ref } from 'vue';
import axios from 'axios';
import { useRouter, useRoute } from 'vue-router';
import Movie from '../models/Movie';
const route = useRoute();
const router = useRouter();
const movie = ref(Movie);

const loadData = async () => {
  const id = route.params.movieId;
  const url = `${import.meta.env.VITE_API_OSCATURL}movie/${id}`;
  const { data } = await axios.get(url);
  console.log(data);
  movie.value = data;
};

// const addStillsLink = () => {
//   movie.value.movieStills.push('');
// };

const removeStillsLink = (index) => {
  movie.value.movieStills.splice(index, 1);
};

const updateHandler = async () => {
  const url = `${import.meta.env.VITE_API_OSCATURL}/movie/update`;
  const response = await axios.put(url, movie.value);
  if (response.data.success) {
    alert(response.data.message);
    router.push('/movie');
  }
};

loadData();
</script>
.img { width: 100%; float: left; margin-right: 20px; padding: 50%; }
<style scoped></style>
