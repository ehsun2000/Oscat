<template>
  <div class="container">
    <div class="roll">
      <div class="box"></div>
      <form @submit.prevent="updateHandler">
        <div class="row">
          <div class="article">
            <div class="col-3">
              <div class="card" style="width: 18rem">
                <img
                  :src="movie.posterImage"
                  class="card-img-top"
                  :alt="movie.movieName"
                  :title="movie.movieName"
                />
                <div class="card-body">
                  <h5 class="card-title">電影海報</h5>
                  <button
                    type="button"
                    class="btn btn-primary btn-sm"
                    @click="addPosterLink"
                  >
                    修改
                  </button>
                  <button
                    type="button"
                    class="btn btn-primary btn-sm mx-2"
                    @click="viewPoster"
                  >
                    檢視
                  </button>
                </div>
              </div>
            </div>

            <div
              v-for="(movieStill, index) in movie.movieStills"
              :key="index"
              class="card"
              style="width: 18rem"
            >
              <img
                :src="movieStill.stillImageUrl"
                class="card-img-top"
                :alt="movie.movieName"
              />
              <div class="card-body">
                <h5 class="card-title">({{ index + 1 }}) 宣傳照</h5>
                <button
                  type="button"
                  class="btn btn-primary btn-sm"
                  @click="viewStills(index)"
                >
                  檢視
                </button>
              </div>
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
                <button
                  type="button"
                  class="btn btn-outline-success"
                  style="
                    --bs-btn-padding-y: 0.25rem;
                    --bs-btn-padding-x: 0.5rem;
                    --bs-btn-font-size: 0.75rem;
                  "
                  @click="addPosterLink"
                >
                  <i class="bi bi-plus"></i>
                </button>
                <input
                  type="text"
                  class="mb-2 form-control"
                  id="posterImage"
                  v-model="movie.posterImage"
                />
                <button
                  type="button"
                  class="btn btn-danger"
                  @click="removePoster(index)"
                >
                  删除
                </button>
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
              <button class="btn btn-secondary" type="submit">修改</button
              ><button
                type="button"
                class="btn btn-primary mx-2"
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

// 返回
const previousPage = () => {
  history.back();
};

// 檢視圖片
const viewPoster = () => {
  Swal.fire({
    imageUrl: movie.value.posterImage,
    imageWidth: 400,
    imageHeight: 600,
  });
};

const viewStills = (index) => {
  Swal.fire({
    imageUrl: movie.value.movieStills[index].stillImageUrl,
    imageWidth: 450,
    imageHeight: 250,
  });
};

// 刪除圖片url
const removePoster = () => {
  if (confirm('真的要刪除嗎?')) {
    movie.value.posterImage = '';
  } else {
    return false;
  }
};

const removeStillsLink = (index) => {
  if (confirm('真的要刪除嗎?')) {
    movie.value.movieStills.splice(index, 1);
  } else {
    index.preventDefault();
    return false;
  }
};

const loadData = async () => {
  const id = route.params.movieId;
  const url = `${import.meta.env.VITE_OSCAT_API_ENDPOINT}/movie/${id}`;
  const { data } = await axios.get(url);
  console.log(data);
  movie.value = data;
};

// 上傳圖片
const addPosterLink = async () => {
  Swal.fire({
    title: '上傳海報',
    html: '<input type="file" id="imageUpload" accept="image/*">',
    showCancelButton: true,
    confirmButtonText: '新增',
    cancelButtonText: '取消',
    preConfirm: async () => {
      const imageInput = document.getElementById('imageUpload');
      const imageUpload = imageInput.files[0];
      if (imageUpload) {
        const URLAPI = `${
          import.meta.env.VITE_OSCAT_API_ENDPOINT
        }/movie/upload`;
        console.log(URLAPI);
        const formData = new FormData();
        formData.append('imageUpload', imageUpload); // 將圖像文件附加到 FormData
        const response = await axios.post(URLAPI, formData); //上傳到cloudinary
        const imageURL = response.data; //獲得圖片URL
        movie.value.posterImage = imageURL; //更新海報圖片URL
      }
    },
  });
};

const addStillsLink = async () => {
  Swal.fire({
    title: '上傳宣傳照',
    html: '<input type="file" id="imageUpload" accept="image/*">',
    showCancelButton: true,
    confirmButtonText: '新增',
    cancelButtonText: '取消',
    preConfirm: async () => {
      const imageInput = document.getElementById('imageUpload');
      const imageUpload = imageInput.files[0];
      if (imageUpload) {
        const URLAPI = `${
          import.meta.env.VITE_OSCAT_API_ENDPOINT
        }/movie/upload`;
        const formData = new FormData();
        formData.append('imageUpload', imageUpload); // 將圖像文件附加到 FormData
        const response = await axios.post(URLAPI, formData); //上傳到cloudinary
        const imageURL = response.data;
        const newStillsObject = { stillImageUrl: imageURL }; //把連結加到movie.movieStills
        movie.value.movieStills.push(newStillsObject);
      }
    },
  });
};

// 修改
const updateHandler = async (e) => {
  const id = route.params.movieId;
  const url = `${import.meta.env.VITE_OSCAT_API_ENDPOINT}/movie/update/${id}`;
  console.log(movie.value);
  const movieData = { ...movie.value }; // 獲得實際的物件值
  e.preventDefault();
  try {
    const response = await axios.put(url, movieData);
    if (response.status === 200) {
      Swal.fire('修改成功', '電影修改成功', 'success');
      router.push('/movie');
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
}

.movieStill {
  width: 50%;
  padding-top: 20px;
}

.article {
  width: 30%;
  float: right;
}
</style>
