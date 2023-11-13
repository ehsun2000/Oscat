<template>
  <div class="container w-50">
    <div class="card">
      <h5 class="card-header">新增電影</h5>

      <div class="card-body">
        <form class="needs-validation" @submit.prevent="submitForm">
          <div class="row g-3">
            <div class="col-sm-6">
              <label for="movieName" class="form-label">電影名稱</label>
              <input
                type="text"
                class="form-control"
                id="movieName"
                :class="{ 'is-invalid': nameError }"
                v-model="movie.movieName"
                required
                @blur="checkNameBlur"
              />
              <div id="nameError" class="error-message text-danger">
                {{ nameErrMsg }}
              </div>
            </div>

            <div class="col-sm-6">
              <label for="movieType" class="form-label">電影類型</label>
              <input
                type="text"
                class="form-control"
                id="movieType"
                :class="{ 'is-invalid': typeError }"
                v-model="movie.movieType"
                @blur="checkTypeBlur"
                required
              />
              <div id="typeError" class="error-message text-danger">
                {{ typeErrMsg }}
              </div>
            </div>

            <div class="col-sm-6">
              <label for="movieStatus" class="form-label">電影狀態</label>
              <select
                class="form-select"
                id="movieStatus"
                v-model="movie.movieStatus"
                @change="handleSelection"
              >
                <option :value="''">請選擇狀態</option>
                <option :value="'上映中'">上映中</option>
                <option :value="'下檔'">下檔</option>
              </select>
            </div>

            <div class="col-sm-6">
              <label for="director" class="form-label">導演</label>
              <input
                type="text"
                class="form-control"
                id="director"
                :class="{ 'is-invalid': directorError }"
                v-model="movie.director"
                @blur="checkDirectorBlur"
                required
              />
              <div id="directorError" class="error-message text-danger">
                {{ directorErrMsg }}
              </div>
            </div>

            <div class="col-sm-6">
              <label for="writerList" class="form-label">編劇</label>
              <input
                type="text"
                class="form-control"
                id="writerList"
                :class="{ 'is-invalid': writerError }"
                v-model="movie.writerList"
                @blur="checkWriterBlur"
                required
              />
              <div id="writerError" class="error-message text-danger">
                {{ writerErrMsg }}
              </div>
            </div>

            <div class="col-sm-6">
              <label for="actorList" class="form-label">演員</label>
              <div class="input-group has-validation">
                <input
                  type="text"
                  class="form-control"
                  id="actorList"
                  aria-describedby="inputGroupPrepend"
                  :class="{ 'is-invalid': actorError }"
                  v-model="movie.actorList"
                  @blur="checkActorBlur"
                  required
                />
                <div id="actorError" class="error-message text-danger">
                  {{ actorErrMsg }}
                </div>
              </div>
            </div>

            <div class="col-12">
              <label for="plotSummary" class="form-label">簡介</label>
              <div class="input-group has-validation">
                <textarea
                  class="form-control"
                  id="plotSummary"
                  aria-describedby="inputGroupPrepend"
                  :class="{ 'is-invalid': plotSummaryError }"
                  v-model="movie.plotSummary"
                  required
                  @blur="checkPlotSummaryBlur"
                ></textarea>
                <div id="plotSummaryError" class="error-message text-danger">
                  {{ plotSummaryErrMsg }}
                </div>
              </div>
            </div>

            <div class="row mt-3">
              <div class="col">
                <label for="releaseDate" class="form-label">上映日期</label>
                <input
                  type="date"
                  class="form-control"
                  id="releaseDate"
                  v-model="movie.releaseDate"
                  required
                />
              </div>

              <div class="col">
                <label for="duration" class="form-label">片長</label>
                <input
                  type="text"
                  class="form-control"
                  id="duration"
                  :class="{ 'is-invalid': durationError }"
                  v-model="movie.duration"
                  required
                  @blur="checkDurationBlur"
                />
                <div id="durationError" class="error-message text-danger">
                  {{ durationErrMsg }}
                </div>
              </div>

              <div class="col">
                <label for="classification" class="form-label">電影分級</label>
                <select
                  class="form-select"
                  id="classification"
                  v-model="movie.classification"
                  @change="handleSelection"
                >
                  <option value="普遍級">普遍級</option>
                  <option value="保護級">保護級</option>
                  <option value="輔導級">輔導級</option>
                  <option value="輔導十二級">輔導十二級</option>
                  <option value="輔導十五級">輔導十五級</option>
                  <option value="限制級">限制級</option>
                </select>
              </div>
            </div>

            <div class="col-12">
              <label for="trailerLink" class="form-label">預告片連結</label>
              <input
                type="text"
                class="form-control"
                id="trailerLink"
                :class="{ 'is-invalid': trailerLinkError }"
                v-model="movie.trailerLink"
                required
                @blur="checkTrailerLinkBlur"
              />
              <div id="trailerLinkError" class="error-message text-danger">
                {{ trailerLinkErrMsg }}
              </div>
            </div>

            <div class="col-12">
              <label for="posterImage" class="form-label">電影海報連結</label>
              <button
                type="button"
                class="ml-2 btn btn-outline-success"
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
                :class="{ 'is-invalid': posterImageError }"
                @blur="checkPosterImageBlur"
              />
              <img
                v-if="movie.posterImage"
                :src="movie.posterImage"
                alt="預覽"
                style="width: 300px"
              />
              <div id="posterImageError" class="mb-2 error-message text-danger">
                {{ posterImageErrMsg }}
              </div>
              <button
                type="button"
                class="btn btn-danger"
                @click="removePoster(index)"
              >
                删除
              </button>
            </div>

            <div class="col-12">
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
                  class="mb-2 form-control"
                  id="movieStills"
                  v-model="still.stillImageUrl"
                  :class="{ 'is-invalid': stillError }"
                  @blur="checkStillBlur(index)"
                />
                <img
                  v-if="still.stillImageUrl"
                  :src="still.stillImageUrl"
                  alt="預覽"
                  style="width: 300px"
                />
                <div id="stillError" class="mb-2 error-message text-danger">
                  {{ stillErrMsg }}
                </div>
                <button
                  type="button"
                  class="btn btn-danger mb-2"
                  @click="removeStills(index)"
                >
                  删除
                </button>
              </div>
            </div>
          </div>
          <hr class="my-4" />
          <button type="submit" class="btn btn-success">新增電影</button>
          <button
            type="button"
            class="btn btn-primary mx-2"
            @click="previousPage"
          >
            返回
          </button>
          <hr />
          <button
            type="button"
            class="btn btn-outline-secondary"
            @click="demo1"
          >
            電影資料1
          </button>
          <button
            type="button"
            class="btn btn-outline-secondary mx-1"
            @click="demo2"
          >
            電影資料2
          </button>
          <button
            type="button"
            class="btn btn-outline-secondary mx-1"
            @click="demo3"
          >
            電影資料3
          </button>
        </form>
      </div>
    </div>
  </div>
  <p></p>
</template>

<script setup>
import { ref } from 'vue';
import axios from 'axios';
import Movie from '@/models/Movie.js';
import Swal from 'sweetalert2';

const movie = ref(Movie);
// const movie.value.movieStatus = ref('');

// 驗證電影名稱
const nameError = ref(false);
const nameErrMsg = ref('');

const checkNameBlur = () => {
  if (!movie.value.movieName) {
    nameError.value = true;
    nameErrMsg.value = '不可空白，請輸入名稱';
  } else {
    nameError.value = false;
    nameErrMsg.value = '';
  }
};

// 驗證電影類型
const typeError = ref(false);
const typeErrMsg = ref('');

const checkTypeBlur = () => {
  if (!movie.value.movieType) {
    typeError.value = true;
    typeErrMsg.value = '不可空白';
  } else {
    typeError.value = false;
    typeErrMsg.value = '';
  }
};

const handleSelection = (event) => {
  if (event.target.value !== '') {
    console.log('選擇了：', event.target.value);
  } else {
    console.log('沒有選擇值');
    alert('請一定要選擇');
  }
};

// 驗證電影導演
const directorError = ref(false);
const directorErrMsg = ref('');

const checkDirectorBlur = () => {
  if (!movie.value.director) {
    directorError.value = true;
    directorErrMsg.value = '不可空白';
  } else {
    directorError.value = false;
    directorErrMsg.value = '';
  }
};

// 驗證電影作者
const writerError = ref(false);
const writerErrMsg = ref('');

const checkWriterBlur = () => {
  if (!movie.value.writerList) {
    writerError.value = true;
    writerErrMsg.value = '不可空白';
  } else {
    writerError.value = false;
    writerErrMsg.value = '';
  }
};

// 驗證電影演員
const actorError = ref(false);
const actorErrMsg = ref('');

const checkActorBlur = () => {
  if (!movie.value.actorList) {
    actorError.value = true;
    actorErrMsg.value = '不可空白';
  } else {
    actorError.value = false;
    actorErrMsg.value = '';
  }
};

// 驗證電影摘要
const plotSummaryError = ref(false);
const plotSummaryErrMsg = ref('');

const checkPlotSummaryBlur = () => {
  if (!movie.value.plotSummary) {
    plotSummaryError.value = true;
    plotSummaryErrMsg.value = '不可空白';
  } else {
    plotSummaryError.value = false;
    plotSummaryErrMsg.value = '';
  }
};

// 驗證電影片長
const durationError = ref(false);
const durationErrMsg = ref('');

const checkDurationBlur = () => {
  if (!movie.value.duration) {
    durationError.value = true;
    durationErrMsg.value = '不可空白';
  } else {
    durationError.value = false;
    durationErrMsg.value = '';
  }
};

// 驗證電影預告片連結
const trailerLinkError = ref(false);
const trailerLinkErrMsg = ref('');

const checkTrailerLinkBlur = () => {
  if (!movie.value.trailerLink) {
    trailerLinkError.value = true;
    trailerLinkErrMsg.value = '不可空白';
  } else {
    trailerLinkError.value = false;
    trailerLinkErrMsg.value = '';
  }
};

// 驗證電影海報連結
const posterImageError = ref(false);
const posterImageErrMsg = ref('');

const checkPosterImageBlur = () => {
  if (!movie.value.posterImage) {
    posterImageError.value = true;
    posterImageErrMsg.value = '請上傳檔案';
  } else {
    posterImageError.value = false;
    posterImageErrMsg.value = '';
  }
};

// 驗證電影宣傳照連結
const stillError = ref(false);
const stillErrMsg = ref('');

const checkStillBlur = (index) => {
  if (!movie.value.movieStills[index].stillImageUrl) {
    stillError.value = true;
    stillErrMsg.value = '請上傳檔案';
  } else {
    stillError.value = false;
    stillErrMsg.value = '';
  }
};

//返回
const previousPage = () => {
  history.back();
};

// 刪除圖片url
const removePoster = () => {
  movie.value.posterImage = '';
};

const removeStills = (index) => {
  movie.value.movieStills.splice(index, 1);
};

// 上傳圖片
// const previewImage = ref(null);

const addPosterLink = async () => {
  Swal.fire({
    title: '上傳電影海報',
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
        // console.log(imageUpload);
        const formData = new FormData();
        formData.append('imageUpload', imageUpload); // 將圖像文件附加到 FormData
        const response = await axios.post(URLAPI, formData); //上傳到cloudinary
        console.log(response);
        console.log(formData);
        const imageURL = response.data; //獲得圖片URL
        console.log(imageURL);
        movie.value.posterImage = imageURL; //更新海報圖片URL
        // previewImage.value = imageURL;
      }
    },
  });
};

const addStillsLink = async () => {
  Swal.fire({
    title: '上傳電影宣傳照',
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
        // movie.value.movieStills.splice(0, 0, newStillsObject);
      }
    },
  });
};

// 送出表單
const submitForm = async () => {
  try {
    const url = `${import.meta.env.VITE_OSCAT_API_ENDPOINT}/movie/add`;
    console.log(url);
    const response = await axios.post(url, movie.value);
    console.log(response);
    if (response.status === 200) {
      Swal.fire('新增成功', '成功新增電影', 'success');
      movie.value = ref(Movie);
    }
  } catch (error) {
    console.error(error.message);
    Swal.fire('新增失敗', '發生錯誤', 'error');
  }
};

const demo1 = () => {
  movie.value = {
    movieName: '影子背後',
    movieType: '驚悚/懸疑',
    movieStatus: '下檔',
    director: '李世偉 / 蔡坤庭',
    writerList: '李世偉',
    actorList: '王鈞浩 / 謝承均 / 吳東諺 / 白家綺 / 梁煒鈿 / 喬思語',
    plotSummary:
      '三個月前，因為車禍而失去記憶的男子阿草(王鈞浩飾)，某日醒來後發現，他車禍後這三個月以來，唯一認識且深愛的女友真真(喬思語飾)失蹤了… 當阿草踏上尋找女友的旅程，並與新認識的陌生朋友-記者尼克(吳東諺飾)，一起展開調查真真的身世之後，阿草發現，原來他先前所認知的一切，都不是他所想像的那樣，而這都與他之前出車禍的原因有關… 承辦檢察官（謝承均飾）對案件緝而不捨追查，協助進行嫌犯精神鑑定的琴醫師(白家綺飾)卻敵友難辨，還有牽動著阿草過去、現在和未來的律師馬天弓(梁煒鈿飾)，錯綜復雜的燒腦劇情，真相不到最後一刻無法參透！ 導演採用非線性敘事的手法，透過幾個不同的章節，帶著觀眾一步步拼湊出，藏在阿草影子背後的真相拼圖。',
    releaseDate: '2021-09-17',
    duration: '98',
    classification: '輔導十二級',
    trailerLink: 'https://www.youtube.com/watch?v=ivE9cKBHfdE',
    posterImage: '',
    movieStills: [
      {
        stillImageUrl: '',
      },
    ],
  };
};

const demo2 = () => {
  movie.value = {
    movieName: '當男人戀愛時',
    movieType: '喜劇/愛情/浪漫/家庭',
    movieStatus: '下檔',
    director: '殷振豪',
    writerList: '傅凱羚 / 簡持中',
    actorList: '邱澤 / 許瑋甯 / 蔡振南 / 鍾欣凌 / 藍葦華 / 陽靚 / 黃路梓茵',
    plotSummary:
      '阿成（邱澤 飾）是靠討債維生的流氓，總是嬉皮笑臉面對所有人的鄙視。從未體會過愛情滋味的阿成，某次討債時竟被欠債的浩婷（許瑋甯 飾） 深深吸引，阿成笨拙地畫了一份「塗鴉合約」，向浩婷提出用每一次的吃飯約會來抵銷她的債務，阿成一股腦的心意，漸漸卸下了浩婷的心防。當阿成認為自己總算要搞懂愛情了，一場突如其來的意外，卻無情打擊了兩人對未來的期待?想給浩婷一個依靠的阿成這時才發現，原來當男人戀愛時，是可以義無反顧放棄所有的。',
    releaseDate: '2021-04-01',
    duration: '105',
    classification: '輔導十二級',
    trailerLink: 'https://www.youtube.com/watch?v=gOjQxvRtMBI',
    posterImage:
      'https://taiwancinema.bamid.gov.tw/ImageData/60/2021/87754/t_87754.jpg?v=202310142232143119468',
    movieStills: [
      {
        stillImageUrl:
          'https://www.themoviedb.org/t/p/w533_and_h300_bestv2/nKNAWa7ATTHUP90lEMA00h7L26P.jpg',
      },
      {
        stillImageUrl:
          'https://www.themoviedb.org/t/p/w533_and_h300_bestv2/b7V5CpdvvoiqblcTjSXn8IwphC1.jpg',
      },
    ],
  };
};

const demo3 = () => {
  movie.value = {
    movieName: '我沒有談的那場戀愛',
    movieType: '愛情/浪漫',
    movieStatus: '下檔',
    director: '許智彥 / 徐譽庭',
    writerList: '徐譽庭',
    actorList: '吳慷仁 / 艾怡良 / 傅孟柏 / 9m88 / 林美秀',
    plotSummary:
      '郭勤勤是個令人頭疼的人物：不夠圓融、不夠親切，不怕得罪人，但偏偏，她認真負責、能力很強，所以公司上下眾多討厭她的人幫她取了一個外號「面癱姐」！ 33歲的她，單身，朋友不多、臉友不多，但封鎖名單就有兩個人，而且都是男性。 一個是拍完一部長片後幾乎賠掉自己人生的廣告導演「南之仰」，綽號「南人」。他在「談戀愛」方面的才華遠遠勝過他的電影夢，所以他被列入勤勤黑名單的原因，當然跟「很爛」有關。 另一個黑名單叫「趙書維」，他是個好人，但他好到讓人誤會、誤解、失望，就是個問題了——五年前，全世界都以為「他和郭勤勤在一起」，但他卻只當郭勤勤是最好的朋友！ 終於，郭勤勤決定不再封鎖自己、討厭自己，於是，她讓他們再次回到自己的人生。',
    releaseDate: '2021-02-10',
    duration: '99',
    classification: '普遍級',
    trailerLink: 'https://www.youtube.com/watch?v=NlbztVoW3qg',
    posterImage:
      'https://www.themoviedb.org/t/p/w600_and_h900_bestv2/81XRB9SKgbPyVFVnNRtx1f4uKlc.jpg',
    movieStills: [
      {
        stillImageUrl:
          'https://www.themoviedb.org/t/p/w533_and_h300_bestv2/ePfLXP6rMOKfe9TwOMWqnvHbyxe.jpg',
      },
      {
        stillImageUrl:
          'https://www.themoviedb.org/t/p/w533_and_h300_bestv2/4AbnMawQ83DKwYuX9W11waEqLsV.jpg',
      },
      {
        stillImageUrl:
          'https://www.themoviedb.org/t/p/w533_and_h300_bestv2/lnr4FsTaNU5n2UbzPlwJ6y8KFbI.jpg',
      },
    ],
  };
};
</script>
