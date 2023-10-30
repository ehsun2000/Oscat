<template>
  <div class="roll">
    <div class="box"></div>
    <h2>商品列表</h2>
    <div>
      <!-- 新增按鈕 會進入 Add.vue 這個 class 會有圓框藍底 -->
      <RouterLink class="btn btn-primary mb-3" to="/product/add">
        <!-- 這是一個 + -->
        <i class="bi bi-plus"></i> 新增
      </RouterLink>
      <div></div>
      <table class="table table-bordered">
        <thead>
          <tr>
            <th>編號</th>
            <th>名稱</th>
            <th>單價</th>
            <th>類型</th>
            <th>照片</th>
            <th>編輯</th>
          </tr>
        </thead>
        <tbody>
          <tr
            v-for="{
              productId,
              productName,
              unitPrice,
              productType,
              productImg,
            } in products"
            :key="productId"
          >
            <td>{{ productId }}</td>
            <td>{{ productName }}</td>
            <td>{{ unitPrice }}</td>
            <td>{{ productType }}</td>
            <td><img class="small-image" :src="productImg" /></td>
            <td>
              <RouterLink
                class="btn btn-secondary me-3"
                :to="'/product/update/' + productName"
                ><i class="bi bi-pencil-fill">修改</i>
              </RouterLink>
              <button
                class="btn btn-danger"
                @click="deleteHandler(productName)"
              >
                <i class="bi bi-trash-fill"></i>刪除
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
// 解構賦值
import { ref } from 'vue';
import axios from 'axios';
import Swal from 'sweetalert2';

// 使用 Vue 的 ref 初始化 products 變數，用於存儲商品數據
const products = ref([]);

// 從 .env 文件中讀取 API URL
const URL = import.meta.env.VITE_OSCAT_API_ENDPOINT;

const loadProducts = async () => {
  // 需要的 API
  const product_url = `${URL}/product/all`;
  try {
    const response = await axios.get(product_url);
    console.log(response.data);
    products.value = response.data.map((product, index) => ({
      ...product,
      productId: index + 1,
    }));
    console.log(products.value);
  } catch (error) {
    console.log('發生錯誤', error);
  }
};

//刪除
const deleteHandler = async (productName) => {
  //使用sweetalert2
  const { value } = await Swal.fire({
    title: '確認刪除',
    text: '真的要刪除嗎?',
    icon: 'warning',
    showCancelButton: true,
    confirmButtonColor: '#3085d6',
    cancelButtonColor: '#d33',
    confirmButtonText: '刪除',
    cancelButtonText: '取消',
  });

  if (value) {
    //帶入 Delete 的 API
    const delete_url = `${URL}/product/delete?productName=${productName}`;
    const response = await axios.delete(delete_url);
    if (response.data) {
      Swal.fire('刪除', '產品已刪除', 'success');
      //刪除完後重導頁面
      loadProducts();
    }
  }
};

// 初始的第一次 把資料帶進去
loadProducts();
</script>
<style>
.small-image {
  width: 100px;
  height: auto;
}
</style>
