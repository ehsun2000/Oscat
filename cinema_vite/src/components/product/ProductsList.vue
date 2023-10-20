<template>
  <h2>產品列表</h2>

  <!-- 新增按鈕 會進入 Add.vue 這個 class 會有圓框藍底 -->
  <RouterLink class="btn btn-primary mb-3" to="/product/AddProduct.vue">
    <!-- 這是一個 + -->
    <i class="bi bi-plus"></i> 新增
  </RouterLink>
  <div></div>
  <table class="table table-bordered">
    <thead>
      <tr>
        <th>產品編號</th>
        <th>產品名稱</th>
        <th>單價</th>
        <th>商品類型</th>
        <th>照片</th>
        <th>編輯</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="{ id, name, price, type, image } in products" :key="id">
        <td>{{ id }}</td>
        <td>{{ name }}</td>
        <td>{{ price }}</td>
        <td>{{ type }}</td>
        <td>{{ image }}</td>
        <td>
          <RouterLink
            class="btn btn-secondary me-3"
            :to="'/products/edit/' + id"
          >
            <i class="bi bi-pencil-fill">修改</i>
          </RouterLink>
          <button class="btn btn-danger">
            <i class="bi bi-trash-fill"></i>刪除
          </button>
        </td>
      </tr>
    </tbody>
  </table>


</template>

<script setup>

// 解構賦值
import {ref,reactive } from 'vue';
import axios from 'axios';

// 使用 Vue 的 ref 初始化 products 變數，用於存儲產品數據
const products = ref([]);

// 使用 ref 初始化 totalPages 變數，用於存儲總頁數
// const totalPages = ref(0);

// 使用 reactive 初始化 datas 物件，用於存儲搜尋、分頁和排序的相關數據
const datas = reactive({
  start: 0, // 分頁起始位置
  rows: 10, // 每頁顯示紀錄數
  name: '', // 產品名稱搜尋關鍵字
});

// 從 .env 文件中讀取 API URL
const URL = import.meta.env.VITE_API_FINDALL;

const loadProducts = async () => {
  // 需要的 API
  const URLAPI = `{URL}products/all`;
  // 用 await 可以設計 get post
  const response = await axios.post(URLAPI, datas);
  console.log(response.data);
  //取得所有商品放進 products 變數
  products.value = response.data.list;
};

// 初始的第一次 把資料帶進去
loadProducts();
</script>

<style scoped></style>
