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
          type="text"
          class="form-control"
          id="unitPrice"
          v-model="product.unitPrice"
        />
      </div>
      <div class="mb-3">
        <label for="productType" class="form-label">類別</label>
        <select
          class="form-control"
          id="productType"
          v-model="product.productType"
        >
          <option v-for="type in productTypeOptions" :key="type" :value="type">
            {{ type }}
          </option>
        </select>
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
              class="btn btn-primary ml-2"
              type="button"
              @click="addHandler"
            >
              新增
            </button>
            <button class="btn btn-dark" type="button" @click="inputHandler">
              一鍵輸入
            </button>
          </div>
          <div class="col text-end">
            <button
              class="btn btn-primary ml-2"
              type="button"
              @click="retrunHandler"
            >
              取消
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
import { useRouter } from 'vue-router';
import Product from '@/models/Product.js';
import Swal from 'sweetalert2';

const router = useRouter();
const product = ref(Product);
const previewImage = ref(null);
const productTypeOptions = ref(['飲品', '炸物', '爆米花', '熟食']); // 商品類型選項
const products = ref([]); // 指定 products 變數的類型為 Product 數組

const URL = import.meta.env.VITE_OSCAT_API_ENDPOINT;

const retrunHandler = async () => {
  const confirmed = await Swal.fire({
    title: '確認取消',
    text: '在新增過程中選擇取消，尚未儲存的資料將會遺失',
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

      products.value.push(product.value);

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

//一鍵輸入
const inputHandler = () => {
  product.value = { productName: '咖啡', unitPrice: '70', productType: '飲品' };
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

const loadAddProduct = () => {
  product.value = ref(Product);
};

loadAddProduct();
</script>

<style scoped></style>
