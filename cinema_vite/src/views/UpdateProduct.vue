<template>
  <div class="row">
    <h2>修改商品</h2>
    <div class="col-3"></div>
    <div class="col-6">
      <div class="mb-3">
        <label for="productName" class="form-label">名稱</label>
        <input
          type="text"
          class="form-control"
          id="productName"
          v-model="product.productName"
        />
      </div>
      <div class="mb-3">
        <label for="unitPrice" class="form-label">單價</label>
        <input
          type="text"
          class="form-control"
          id="unitPrice"
          v-model="product.unitPrice"
        />
      </div>
      <div class="mb-3">
        <label for="productType" class="form-label">類別</label>
        <select
          class="form-select"
          id="productType"
          v-model="product.productType"
        >
          <option v-for="type in productTypeOptions" :key="type" :value="type">
            {{ type }}
          </option>
        </select>
      </div>
      <div class="mb-3">
        <label for="productImg" class="form-label">照片</label>
        <button
          type="button"
          class="btn btn-primary"
          style="
            --bs-btn-padding-y: 0.25rem;
            --bs-btn-padding-x: 0.5rem;
            --bs-btn-font-size: 0.75rem;
          "
          @click="openUploadDialog"
        >
          <i class="bi bi-plus"></i>
        </button>
        <input
          type="text"
          class="form-control"
          id="productImg"
          v-model="product.productImg"
          readonly
        />
        <!-- 圖片預覽 -->
        <img
          v-if="previewImage"
          :src="previewImage"
          alt="預覽"
          style="max-width: 100px; max-height: 100px"
        />
      </div>
      <div class="button-container">
        <div class="row">
          <div class="col">
            <button
              class="btn btn-secondary"
              type="button"
              @click="editHandler"
            >
              修改
            </button>
          </div>
          <div class="col text-end">
            <button
              class="btn btn-primary ml-2"
              type="button"
              @click="retrunHandler"
            >
              返回
            </button>
          </div>
        </div>
      </div>
      <div class="col-3"></div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import axios from 'axios';
import { useRouter, useRoute } from 'vue-router';
import Product from '@/models/Product.js';
import Swal from 'sweetalert2';

const product = ref(Product);
const route = useRoute();
const router = useRouter();
const previewImage = ref(null);
const originImageURL = ref(null);
const productTypeOptions = ['飲品', '炸物', '爆米花', '熟食']; // 商品類型選項

const URL = import.meta.env.VITE_OSCAT_API_ENDPOINT;

//搜尋 這筆要更新資料
const editData = async () => {
  const productName = route.params.productName;
  const updateProduct_url = `${URL}/product/${productName}`;
  const { data } = await axios.get(updateProduct_url);
  product.value = data;
  product.value.productTypeOptions = data.productTypeOptions;
  originImageURL.value = data.productImg;
  previewImage.value = originImageURL.value;
};

// 處發送出事件後 去後端更新
const editHandler = async () => {
  const update_url = `${URL}/product/update`;
  try {
    const response = await axios.put(update_url, product.value);
    console.log(product.value);
    if (response.status === 200) {
      console.log(response.status);
      await Swal.fire({
        icon: 'success',
        title: '修改成功',
        showConfirmButton: false,
        timer: 1500, // 顯示成功訊息 1.5 秒後自動關閉
      });
      router.push('/cinema-product');
    }
  } catch (error) {
    await Swal.fire({
      icon: 'error',
      title: '修改失敗',
      text: '請稍後再試',
    });
  }
};

const openUploadDialog = async () => {
  Swal.fire({
    title: '上傳圖片',
    html: '<input type="file" id="imageUpload" accept="image/*">',
    showCancelButton: true,
    confirmButtonText: '確定',
    cancelButtonText: '取消',
    preConfirm: async () => {
      const imageInput = document.getElementById('imageUpload');
      if (imageInput) {
        const imageUpload = imageInput.files[0];
        if (imageUpload) {
          const upload_url = `${
            import.meta.env.VITE_OSCAT_API_ENDPOINT
          }/product/upload`;
          console.log(upload_url);
          const formData = new FormData();
          formData.append('imageUpload', imageUpload); // 將圖像文件附加到 FormData
          const response = await axios.post(upload_url, formData); //上傳到cloudinary
          console.log(response);
          console.log(formData);
          const imageURL = response.data; //獲得圖片URL
          console.log(imageURL);
          product.value.productImg = imageURL;

          previewImage.value = imageURL;
        }
      }
    },
  });
};

const retrunHandler = async () => {
  const confirmed = await Swal.fire({
    title: '確認返回',
    text: '確定要返回嗎？未儲存的更改將會遺失。',
    icon: 'warning',
    showCancelButton: true,
    confirmButtonText: '確定',
    cancelButtonText: '取消',
  });

  if (confirmed.isConfirmed) {
    router.push('/cinema-product');
  }
};

editData();
</script>

<style scoped></style>
