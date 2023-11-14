<script setup>
import { ref, onMounted, reactive } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import Swal from 'sweetalert2';

const cinema = ref({});
const route = useRoute();
const router = useRouter();
const isEditing = ref(false);
const allTicketTypes = ref([]);
const allFacilities = ref([]);
const allProducts = reactive([]);
const fileInput = ref(null);

onMounted(async () => {
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

const getWeekday = (number) => {
  const weekdays = ['周日', '周一', '周二', '周三', '周四', '周五', '周六'];
  return weekdays[number % 7];
};

// 修改影城資訊的函數
const modifyCinema = () => {
  isEditing.value = true;
};

const cancelEdit = () => {
  window.location.reload();
};

// 返回上一頁的函數
const goBack = () => {
  router.push({ path: `/cinema-select` });
};

const showTicketTypes = async () => {
  const response = await fetch(
    `${import.meta.env.VITE_OSCAT_API_ENDPOINT}/cinemas/types`,
    {
      method: 'GET',
      credentials: 'include',
    },
  );
  allTicketTypes.value = await response.json();

  // 生成 checkbox 的 HTML 字符串
  const checkboxesHtml = allTicketTypes.value
    .map((type) => {
      const checked = cinema.value.types.includes(type) ? 'checked' : '';
      return `
          <label>
            <input type="checkbox" name="ticket-types" value="${type}" ${checked} />
            <span class="text-light">${type}</span>
          </label>
        `;
    })
    .join('<br/>');

  const { value: selectedTypes } = await Swal.fire({
    title: '選擇票種',
    html: checkboxesHtml,
    focusConfirm: false,
    preConfirm: () => {
      return [
        ...document.querySelectorAll('input[name="ticket-types"]:checked'),
      ].map((input) => input.value);
    },
  });

  if (selectedTypes) {
    cinema.value.types = selectedTypes;
  }
};

const showFacilities = async () => {
  const response = await fetch(
    `${import.meta.env.VITE_OSCAT_API_ENDPOINT}/cinemas/facilities`,
    {
      method: 'GET',
      credentials: 'include',
    },
  );
  allFacilities.value = await response.json();

  const checkboxesHtml = allFacilities.value
    .map((facility) => {
      const checked = cinema.value.facilities.includes(facility)
        ? 'checked'
        : '';
      return `
          <label>
            <input type="checkbox" name="facilities" value="${facility}" ${checked} />
            <span class="text-light">${facility}</span>
          </label>
        `;
    })
    .join('<br/>');

  const { value: selectedFacilities } = await Swal.fire({
    title: '選擇設施',
    html: checkboxesHtml,
    focusConfirm: false,
    preConfirm: () => {
      return [
        ...document.querySelectorAll('input[name="facilities"]:checked'),
      ].map((input) => input.value);
    },
  });

  if (selectedFacilities) {
    cinema.value.facilities = selectedFacilities;
  }
};

const submitEdit = async () => {
  // 先彈出確認視窗
  const { isConfirmed } = await Swal.fire({
    title: '確定要送出嗎？',
    text: '您即將修改影城資訊。',
    icon: 'warning',
    showCancelButton: true,
    confirmButtonText: '是的，送出',
    cancelButtonText: '取消',
  });

  // 如果使用者確認，則繼續執行
  if (isConfirmed) {
    Swal.fire({
      title: '請稍等',
      text: '正在更新影城資訊...',
      allowOutsideClick: false, // 點擊外面不會關閉對話框
      didOpen: () => {
        Swal.showLoading(); // 顯示加載指示器
      },
    });
    try {
      const formData = new FormData();

      formData.append(
        'cinema',
        new Blob([JSON.stringify(cinema.value)], {
          type: 'application/json',
        }),
      );

      if (typeof fileInput.value.files[0] !== 'undefined') {
        formData.append('file', fileInput.value.files[0]);
      }

      // 發送跨域 PUT 請求
      const response = await fetch(
        `${import.meta.env.VITE_OSCAT_API_ENDPOINT}/cinemas/`,
        {
          method: 'PUT',
          credentials: 'include',
          body: formData,
        },
      );

      // 檢查是否成功
      if (response.status === 200 || response.status === 201) {
        window.location.reload();
      }
    } catch (error) {
      // 處理錯誤，例如顯示錯誤訊息
      console.error('提交出錯:', error);
    }
  }
};

// 觸發文件輸入元素的點擊事件
const triggerFileInput = () => {
  fileInput.value.click();
};

const handleFileChange = (event) => {
  const selectedFile = event.target.files[0];
  cinema.value.img = URL.createObjectURL(selectedFile);
  console.log(selectedFile);
};

const showProducts = async () => {
  const response = await fetch(
    `${import.meta.env.VITE_OSCAT_API_ENDPOINT}/cinemas/products`,
    {
      method: 'GET',
      credentials: 'include',
    },
  );

  allProducts.length = 0;
  const data = await response.json();
  data.forEach((item) => {
    allProducts.push(item);
  });

  const checkboxesHtml = allProducts
    .map((product) => {
      const checked = cinema.value.products.includes(product) ? 'checked' : '';
      return `
          <label>
            <input type="checkbox" name="products" value="${product}" ${checked} />
            <span class="text-light">${product}</span>
          </label>
        `;
    })
    .join('<br/>');

  const { value: selectedProducts } = await Swal.fire({
    title: '選擇商品',
    html: checkboxesHtml,
    focusConfirm: false,
    preConfirm: () => {
      return [
        ...document.querySelectorAll('input[name="products"]:checked'),
      ].map((input) => input.value);
    },
  });

  if (selectedProducts) {
    cinema.value.products = selectedProducts;
  }
};
</script>

<template>
  <div v-if="!isEditing">
    <!-- 讀取模式 -->
    <div class="container mt-5">
      <div class="row">
        <!-- 左側：影城圖片 -->
        <div class="col-md-4 text-center">
          <img
            :src="cinema.img"
            alt="Cinema Image"
            class="img-fluid img-thumbnail rounded"
          />
        </div>
        <!-- 修改和返回按鈕 -->

        <!-- 右側：影城資訊 -->
        <div class="col-md-8">
          <div class="row">
            <div class="col-md-12">
              <!-- 影城名稱 -->
              <h2>{{ cinema.name }}</h2>

              <div class="mb-3">
                <button class="btn btn-primary me-2" @click="modifyCinema">
                  <i class="bi bi-pencil"></i> 修改
                  <!-- Bootstrap Icons 修改圖標 -->
                </button>
                <button class="btn btn-secondary" @click="goBack">
                  <i class="bi bi-arrow-left"></i> 返回
                  <!-- Bootstrap Icons 返回圖標 -->
                </button>
              </div>
            </div>
            <div class="col-md-4">
              <!-- 基本資訊 -->
              <div class="mb-3">
                <h4 class="text-warning">
                  <span class="me-3">地址</span>
                  <i class="bi bi-signpost-2"></i>
                </h4>
                <p class="ms-3">{{ cinema.address }}</p>
              </div>

              <div class="mb-3">
                <h4 class="text-warning">
                  <span class="me-3">聯絡電話</span>
                  <i class="bi bi-telephone-fill"></i>
                </h4>
                <p class="ms-3">{{ cinema.phone }}</p>
              </div>

              <div class="mb-3">
                <h4 class="text-warning">
                  <span class="me-3">基本票價</span>
                  <i class="bi bi-tag"></i>
                </h4>
                <p class="ms-3">{{ cinema.basePrice }}</p>
              </div>

              <div>
                <h4 class="text-warning">
                  <span class="me-3">販售票種</span>
                  <i class="bi bi-ticket-perforated"></i>
                </h4>
                <ul class="ms-3">
                  <li v-for="item in cinema.types" :key="item">
                    {{ item }}
                  </li>
                </ul>
              </div>
            </div>

            <div class="col-md-4">
              <div class="mb-3">
                <h4 class="text-warning">
                  <span class="me-3">營業時間</span>
                  <i class="bi bi-clock-history"></i>
                </h4>
                <ul class="ms-3">
                  <li v-for="item in cinema.businessHours" :key="item.weekDay">
                    <strong>{{ getWeekday(item.weekDay) }}</strong>
                    {{ item.start.substring(0, 5) }} ~
                    {{ item.end.substring(0, 5) }}
                  </li>
                </ul>
              </div>

              <div>
                <h4 class="text-warning">
                  <span class="me-3">影城設施</span>
                  <i class="bi bi-wrench-adjustable"></i>
                </h4>
                <ul class="ms-3">
                  <li v-for="item in cinema.facilities" :key="item">
                    {{ item }}
                  </li>
                </ul>
              </div>
            </div>

            <div class="col-md-4">
              <div>
                <h4 class="text-warning">
                  <span class="me-3">影城商品</span>
                  <i class="bi bi-basket3-fill"></i>
                </h4>
                <ul class="ms-3">
                  <li v-for="item in cinema.products" :key="item">
                    {{ item }}
                  </li>
                </ul>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>

  <div v-else>
    <!-- 修改模式 -->
    <div class="container mt-5">
      <div class="row">
        <!-- 左側：影城圖片 -->
        <div class="col-md-4 text-center">
          <input
            type="file"
            accept="image/*"
            ref="fileInput"
            @change="handleFileChange"
            style="display: none"
          />
          <img
            :src="cinema.img"
            alt="Cinema Image"
            @click="triggerFileInput"
            class="img-fluid img-thumbnail rounded"
          />
        </div>
        <!-- 修改和返回按鈕 -->

        <!-- 右側：影城資訊 -->
        <div class="col-md-8">
          <div class="row">
            <div class="col-md-12">
              <!-- 影城名稱 -->
              <h2>{{ cinema.name }}</h2>

              <div class="mb-3">
                <button
                  class="btn btn-outline-success me-2"
                  @click="submitEdit"
                >
                  <i class="bi bi-check-lg"></i> 確認
                  <!-- Bootstrap Icons 確認圖標 -->
                </button>
                <button class="btn btn-outline-danger" @click="cancelEdit">
                  <i class="bi bi-x"></i> 取消
                  <!-- Bootstrap Icons 取消圖標 -->
                </button>
              </div>
            </div>
            <div class="col-md-4">
              <!-- 基本資訊 -->
              <div class="mb-3">
                <label class="form-label" for="cinemaAddress"
                  ><h4 class="text-warning">
                    <span class="me-3">地址</span>
                    <i class="bi bi-signpost-2"></i>
                  </h4>
                </label>
                <input
                  type="text"
                  id="cinemaAddress"
                  v-model="cinema.address"
                  class="form-control"
                />
              </div>

              <div class="mb-3">
                <label class="form-label" for="cinemaPhone"
                  ><h4 class="text-warning">
                    <span class="me-3">聯絡電話</span>
                    <i class="bi bi-telephone-fill"></i>
                  </h4>
                </label>
                <input
                  type="text"
                  id="cinemaPhone"
                  v-model="cinema.phone"
                  class="form-control"
                />
              </div>

              <div class="mb-3">
                <label class="form-label" for="cinemaBasePrice"
                  ><h4 class="text-warning">
                    <span class="me-3">基本票價</span>
                    <i class="bi bi-tag"></i>
                  </h4>
                </label>
                <input
                  type="number"
                  id="cinemaBasePrice"
                  v-model="cinema.basePrice"
                  class="form-control"
                />
              </div>

              <div>
                <h4 class="text-warning d-flex align-items-center">
                  <span class="me-3">販售票種</span
                  ><button
                    class="btn btn-outline-warning"
                    @click="showTicketTypes"
                  >
                    <i class="bi bi-ticket-perforated"></i>
                  </button>
                </h4>

                <div>
                  <ul>
                    <li v-for="item in cinema.types" :key="item">
                      {{ item }}
                    </li>
                  </ul>
                </div>
              </div>
            </div>

            <div class="col-md-8">
              <div class="row">
                <div class="col-md-7">
                  <h4 class="me-3 mb-3 text-warning">
                    <span class="me-3">營業時間</span>
                    <i class="bi bi-clock-history"></i>
                  </h4>
                  <div class="d-flex align-items-start flex-column mb-3">
                    <div
                      v-for="item in cinema.businessHours"
                      :key="item.weekDay"
                      class="mb-2"
                    >
                      <!-- 星期 -->
                      <label for="'weekDay-' + item.weekDay" class="me-2">
                        <strong>{{ getWeekday(item.weekDay) }}</strong></label
                      >
                      <!-- 開始時間 -->
                      <input
                        type="time"
                        :id="'start-' + item.weekDay"
                        v-model="item.start"
                        class="form-control me-2 d-inline-block"
                        style="width: auto"
                      />

                      <!-- 結束時間 -->
                      <input
                        type="time"
                        :id="'end-' + item.weekDay"
                        v-model="item.end"
                        class="form-control d-inline-block"
                        style="width: auto"
                      />
                    </div>
                  </div>
                </div>
                <div class="col-md-5">
                  <h4 class="text-warning d-flex align-items-center">
                    <span class="me-3">影城設施</span>
                    <button
                      class="btn btn-outline-warning"
                      @click="showFacilities"
                    >
                      <i class="bi bi-wrench-adjustable"></i>
                    </button>
                  </h4>
                  <div class="d-flex align-items-start mb-3">
                    <ul>
                      <li v-for="item in cinema.facilities" :key="item">
                        {{ item }}
                      </li>
                    </ul>
                  </div>
                  <h4 class="text-warning d-flex align-items-center mb-3">
                    <span class="me-3">影城商品</span>
                    <button
                      class="btn btn-outline-warning"
                      @click="showProducts"
                    >
                      <i class="bi bi-basket3-fill"></i>
                    </button>
                  </h4>
                  <div class="d-flex align-items-start mb-3">
                    <ul>
                      <li v-for="item in cinema.products" :key="item">
                        {{ item }}
                      </li>
                    </ul>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
img {
  width: 300px;
  height: 300px;
}
</style>
