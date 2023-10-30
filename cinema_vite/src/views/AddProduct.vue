<template>
  <div class="row">
    <h2>新增商品</h2>
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
          type="number"
          class="form-control"
          id="unitPrice"
          v-model="product.unitPrice"
          min="30"
          max="1000"
          @wheel="handleUnitPriceChange"
        />
      </div>
      <div class="mb-3">
        <label for="productType" class="form-label">類型</label>
        <input
          type="text"
          class="form-control"
          id="productType"
          v-model="product.productType"
        />
      </div>
      <div class="form-group mb-3">
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
      </div>
      <div class="button-container">
        <div class="row">
          <div class="col">
            <button
              class="btn btn-primary ml-2"
              type="button"
              @click="retrunHandler"
            >
              返回
            </button>
          </div>
          <div class="col">
            <button
              class="btn btn-primary ml-2"
              type="button"
              @click="addHandler"
            >
              新增
            </button>
          </div>
        </div>
      </div>
      <div class="col-3"></div>
    </div>
  </div>
</template>

<script setup>
import { ref, toRef } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';
import Product from '@/models/Product.js';
import Swal from 'sweetalert2';

const router = useRouter();
const product = ref(Product);
const unitPriceRef = toRef(product, 'unitPrice');

const URL = import.meta.env.VITE_OSCAT_API_ENDPOINT;

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

const addHandler = async () => {
  const addProduct_url = `${URL}/product/add`;
  console.log(product.value);
  try {
    const response = await axios.post(addProduct_url, product.value);
    if (response.status === 200) {
      console.log(response.status);
      Swal.fire({
        icon: 'success',
        title: '新增成功',
        showConfirmButton: false,
        timer: 1500, // 自動關閉訊息框
      });
      router.push('/cinema-product');

      product.value = ref(Product);
    }
  } catch (error) {
    Swal.fire({
      icon: 'error',
      title: '新增失敗',
      text: '已有相同名稱',
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
      }
    },
  });
};

const handleUnitPriceChange = (event) => {
  event.preventDefault();

  const step = event.deltaY > 0 ? 10 : -10; // 設定滾動一次的增量
  const newValue = parseInt(unitPriceRef.value) + step;

  if (newValue >= 30 && newValue <= 1000) {
    unitPriceRef.value = newValue;
  }
};

const loadAddProduct = () => {
  product.value = ref(Product);
};
loadAddProduct();
</script>

<style scoped>
.button-container {
  display: flex;
  justify-content: flex-end;
}
</style>
