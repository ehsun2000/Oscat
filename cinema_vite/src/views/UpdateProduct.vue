<template>
  <div class="row">
    <h2>修改產品</h2>
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
        <label for="productType" class="form-label">類型</label>
        <input
          type="text"
          class="form-control"
          id="productType"
          v-model="product.productType"
        />
      </div>
      <div class="mb-3">
        <label for="productImg" class="form-label">照片</label>
        <input
          type="text"
          class="form-control"
          id="productImg"
          v-model="product.productImg"
        />
      </div>

      <button class="btn btn-secondary" type="button" @click="editHandler">
        修改
      </button>
    </div>
    <div class="col-3"></div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import axios from 'axios';
import { useRouter, useRoute } from 'vue-router';
import Product from '@/models/Product.js';

const product = ref(Product);
const route = useRoute();
const router = useRouter();

const URL = import.meta.env.VITE_OSCAT_API_ENDPOINT;

//搜尋 這筆要更新資料
const editData = async () => {
  const productName = route.params.productName;
  const updateProduct_url = `${URL}/product/${productName}`;
  const { data } = await axios.get(updateProduct_url);
  product.value = data;
};

// 處發送出事件後 去後端更新
const editHandler = async () => {
  const update_url = `${URL}/product/update`;
  try {
    const response = await axios.put(update_url, product.value);
    console.log(product.value);
    if (response.status === 200) {
      console.log(response.status);
      alert('修改成功');
      router.push('/cinema-product');
    }
  } catch (error) {
    alert('修改失敗');
  }
};

editData();
</script>

<style scoped></style>
