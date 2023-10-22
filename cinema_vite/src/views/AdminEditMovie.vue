<template>
  <div class="container">
    <div class="roll">
      <div class="box"></div>
      <form @submit="updateHandler">
        <div class="row">
          <div class="article">
            <div class="col-3">
              <img
                :src="movie.posterImage"
                :alt="movie.movieName"
                :title="movie.movieName"
                class="img"
              />
            </div>
            <div v-for="(movieStill, index) in movie.movieStills" :key="index">
              <img :src="movieStill.stillImageUrl" alt="" class="movieStill" />
            </div>
          </div>
          <div class="col-6">
            <div class="mb-3">
              <label for="movieName" class="form-label">電影名稱</label>
              <input
                type="text"
                class="form-control"
                id="movieName"
                v-model="movie.movieName"
              />

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
                    <th>
                      <button
                        type="button"
                        class="btn btn-outline-primary"
                        @click="addStillsLink"
                      >
                        <i class="bi bi-plus-square-dotted"></i>
                      </button>
                    </th>
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
                      <button
                        class="btn btn-danger"
                        @click="removeStillsLink(index)"
                      >
                        刪除
                      </button>
                    </td>
                  </tr>
                </tbody>
              </table>
              <button class="btn btn-secondary mx-2" type="submit">修改</button
              ><button
                type="button"
                class="btn btn-primary"
                @click="previousPage"
              >
                返回
              </button>
            </div>
          </div>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import axios from 'axios';
import { useRouter, useRoute } from 'vue-router';
import Movie from '@/models/Movie.js';
import Swal from 'sweetalert2';

const route = useRoute();
const router = useRouter();
const movie = ref(Movie);

const previousPage = () => {
  history.back();
};

const loadData = async () => {
  const id = route.params.movieId;
  const url = `${import.meta.env.VITE_OSCAT_API_ENDPOINT}/movie/${id}`;
  const { data } = await axios.get(url);
  console.log(data);
  movie.value = data;
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

const removeStillsLink = (index) => {
  if (confirm('真的要刪除嗎?')) {
    movie.value.movieStills.splice(index, 1);
  }
};

const updateHandler = async (e) => {
  const id = route.params.movieId;
  const url = `${import.meta.env.VITE_OSCAT_API_ENDPOINT}/movie/update/${id}`;
  console.log(movie.value);
  const movieData = { ...movie.value }; // 獲得實際的物件值
  // console.log(movieData);
  e.preventDefault();
  try {
    const response = await axios.put(url, movieData);
    if (response.status === 200) {
      Swal.fire('修改成功', '電影修改成功', 'success');
      router.push('/movie');
    } else {
      Swal.fire('修改失敗', '電影修改失敗', 'error');
    }
  } catch (error) {
    console.error(error);
    Swal.fire('修改失敗', '發生錯誤', 'error');
  }
};

loadData();
</script>
<style scoped>
.img {
  width: 500px;
  height: auto;
  margin-right: 20px;
  /* padding-bottom: 30%; */
}

.movieStill {
  width: 50%;
  /*height: auto;
  margin-right: 20px;*/
  padding-top: 20px;
}

.article {
  width: 50%;
  float: right;
}
</style>
