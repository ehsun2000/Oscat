<template>
  <div>
    <h2>products Page</h2>

    <!-- 新增按鈕 會進入 Add.vue 這個 class 會有圓框藍底 -->
    <RouterLink class="btn btn-primary mb-3" to="/product/AddProduct.vue">
      <!-- 這是一個 + -->
      <i class="bi bi-plus"></i> 新增
    </RouterLink>

    <table class="table table-bordered">
      <thead>
        <th>產品編號</th>
        <th>產品名稱</th>
        <th>單價</th>
        <th>類別</th>
        <th>照片</th>
      </thead>
    </table>
    <tbody>
      <td>
        <button class="btn btn-secondary">修改</button>
        <button class="btn btn-danger">刪除</button>
      </td>
    </tbody>
  </div>
</template>

<script setup></script>

<style scoped></style>
