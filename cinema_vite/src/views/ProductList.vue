<template>
  <div>
    <div class="header-contentt">
      <div class="centered-content">
        <div class="category-buttons">
          <a
            href="javascript: void(0)"
            class="btn"
            @click="searchByCategory(productType)"
            v-for="productType in productTypes"
            :key="productType"
            :class="{ active: productType === activeProductType }"
            >{{ productType }}</a
          >
        </div>
      </div>

      <div class="card-container">
        <div class="card" v-for="product in products" :key="product.productId">
          <img
            class="product-img"
            :src="product.productImg"
            alt="Product Image"
          />
          <h3>{{ product.productName }}</h3>
          <p>售價: {{ product.unitPrice }}</p>
        </div>
      </div>
      <div>
        <a
          class="material-scrolltop"
          href="javascript: void(0);"
          @click="scrollToTop"
          style="display: none"
          >TOP</a
        >
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';

const productTypes = ['飲品', '炸物', '爆米花', '熟食'];
const activeProductType = ref('');
const products = ref([]);
const URL = import.meta.env.VITE_OSCAT_API_ENDPOINT;

const loadProductsList = async () => {
  const search_url = `${URL}/product/all`;
  try {
    const response = await axios.get(search_url);
    products.value = response.data;
  } catch (error) {
    console.error('查詢商品失敗', error);
  }
};

const searchByCategory = async (productType) => {
  activeProductType.value = productType;
  const productType_url = `${URL}/product/byType?productType=${productType}`;
  try {
    const response = await axios.get(productType_url);
    products.value = response.data;
  } catch (error) {
    console.log('API 請求失敗');
  }
};

const checkScrollTop = () => {
  // 檢查視窗的滾動位置，當超過一定高度時顯示 "TOP" 按鈕
  const scrollTopButton = document.querySelector('.material-scrolltop');
  if (scrollTopButton) {
    scrollTopButton.style.display = window.scrollY > 200 ? 'block' : 'none'; // 調整高度閾值
  }
};
const scrollToTop = () => {
  window.scrollTo({
    top: 0,
    behavior: 'smooth',
  });
};

window.addEventListener('scroll', checkScrollTop); // 監聽滾動事件，定期檢查

onMounted(() => {
  loadProductsList();
});
</script>

<style scoped>
.header-contentt {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.centered-content {
  margin-top: 20px; /* 调整按钮顶部间距 */
}

.category-buttons {
  display: flex;
  justify-content: center;
  margin-bottom: 20px; /* 调整按钮和卡片之间的间距 */
}
.head-content {
  display: flex;
  flex-direction: column; /* 將內容設置為垂直方向的 */
  align-items: center; /* 將內容在水平方向置中對齊 */
  justify-content: center; /* 將內容在垂直方向置中對齊 */
  height: 100vh; /* 使內容高度填滿整個視窗 */
}
.card-container {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-wrap: wrap;
}

.card {
  display: flex;
  flex-direction: column; /* 将内容设置为垂直方向排列 */
  align-items: center; /* 水平居中文本 */
  width: 18rem;
  margin-right: 20px;
  margin-bottom: 20px;
  padding: 1rem;
  border: 1px solid #ccc;
  border-radius: 5px;
  text-align: center; /* 文本水平居中 */
}

.product-img {
  width: 100%;
  height: 200px;
  object-fit: cover;
}

.btn,
.btn:focus {
  position: relative;
  z-index: 1;
  min-width: 200px;
  border: 2px solid #d24d57;
  border-radius: 0;
  color: #ffffff;
  font-size: 1rem;
  font-weight: bold;
  text-align: center;
  text-decoration: none;
  text-transform: uppercase;
  overflow: hidden;
  text-shadow:
    0 0 1px rgba(0, 0, 0, 0.2),
    0 1px 0 rgba(0, 0, 0, 0.2);
  -webkit-transition: all 1s ease;
  -moz-transition: all 1s ease;
  -o-transition: all 1s ease;
  transition: all 1s ease;
  padding: 10px 20px;
}
.btn:after {
  content: '';
  position: absolute;
  height: 0%;
  left: 50%;
  top: 50%;
  width: 150%;
  z-index: -1;
  background: #d24d57;
  -moz-transform: translateX(-50%) translateY(-50%) rotate(-25deg);
  -ms-transform: translateX(-50%) translateY(-50%) rotate(-25deg);
  -webkit-transform: translateX(-50%) translateY(-50%) rotate(-25deg);
  transform: translateX(-50%) translateY(-50%) rotate(-25deg);
  -webkit-transition: all 0.75s ease 0s;
  -moz-transition: all 0.75s ease 0s;
  -o-transition: all 0.75s ease 0s;
  transition: all 0.75s ease 0s;
}
.btn:hover {
  color: #ffffff;
  text-shadow: none;
}
.btn:hover:after {
  height: 450%;
}
.card {
  display: inline-block;
  /* 顯示card */
  width: 18rem;
  /* 設定card寬度 */
  margin-right: 20px;
  /* 設定card之間的右邊間距 */
  margin-bottom: 20px;
  /* 設定卡片之間的下間距 */
}
img {
  width: 100%;
}
.card .product-img {
  width: 100%;
  height: 200px; /* 設定統一的高度，可以根據你的需求調整 */
  object-fit: cover; /* 保持圖片比例並填滿容器 */
}
.material-scrolltop {
  position: fixed;
  bottom: 20px;
  right: 20px;
  display: none;
  background: #007bff;
  color: #fff;
  padding: 10px 20px;
  border-radius: 5px;
  text-decoration: none;
  cursor: pointer;
  z-index: 999;
}

.material-scrolltop:hover {
  background: #0056b3;
}
</style>
