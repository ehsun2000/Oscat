<template>
  <div class="row">
    <div class="col-3"></div>
    <div class="col-6">
      <div class="mb-3">
        <label for="id" class="form-label">產品編號</label>
        <input
          type="text"
          class="form-control"
          id="id"
          v-model="product.product_id"
        />
      </div>
      <div class="mb-3">
        <label for="name" class="form-label">商品名稱</label>
        <input
          type="text"
          class="form-control"
          id="name"
          v-model="product.product_name"
        />
      </div>
      <div class="mb-3">
        <label for="price" class="form-label">單價</label>
        <input
          type="text"
          class="form-control"
          id="price"
          v-model="product.unit_price"
        />
      </div>
      <div class="mb-3">
        <label for="type" class="form-label">商品類型</label>
        <input
          type="text"
          class="form-control"
          id="type"
          v-model="product.product_type"
        />
      </div>
      <div class="mb-3">
        <label for="img" class="form-label">商品圖片</label>
        <input
          type="text"
          class="form-control"
          id="img"
          v-model="product.product_img"
        />
      </div>

      <button class="btn btn-primary" type="button" @click="addHandler">
        新增
      </button>
    </div>
    <div class="col-3"></div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';
// 引入 Product 的類別，這個類別應該要在 models/Product.js 裡
import Product from '../models/Product';
const router = useRouter();
// 定義 product 變數，它是一個 ref 並且包含 Product 類別的實例
const product = ref(new Product());
const addHandler = async () => {
  const API_URL = `${import.meta.env.VITE_API_JAVAURL}products/`;
  console.log(product.value);

  // 使用 axios.post 來發送 POST 請求，將 product.value 自動序列化成 JSON
  const response = await axios.post(API_URL, product.value);
  console.log(response);

  if (response.data.success) {
    alert(response.data.message);
    // 導航到 /products 頁面
    router.push('/products');
  }
  // 如果新增失敗，顯示錯誤訊息
  if (response.data.success === false) {
    alert(response.data.message);
  }
};
</script>

<style></style>
