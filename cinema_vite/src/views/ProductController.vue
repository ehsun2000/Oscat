<template>
  <div class="roll">
    <div class="box"></div>
    <h2>商品列表</h2>
    <div>
      <!-- 新增按鈕 會進入 Add.vue 這個 class 會有圓框藍底 -->
      <RouterLink class="btn special-button" to="/product/add">
        <span>新增</span>
        <svg width="13px" height="10px" viewBox="0 0 13 10">
          <path d="M1,5 L11,5"></path>
          <polyline points="8 1 12 5 8 9"></polyline>
        </svg>
      </RouterLink>
      <div style="padding: 20px">
        <div class="search-container">
          <div class="search-box">
            <input
              class="search-input"
              v-model="productType"
              placeholder="請輸入類別"
            />
            <a herf="#" class="search-btn" @click="searchProducts">
              <i class="bi bi-search"></i>
            </a>
          </div>
        </div>
        <div style="padding: 20px">
          <p>{{ totalItems }}個商品</p>
          <table class="table table-bordered">
            <thead>
              <tr>
                <th class="text-center">編號</th>
                <th class="text-center">名稱</th>
                <th class="text-center">單價</th>
                <th class="text-center">類別</th>
                <th class="text-center">照片</th>
                <th class="text-center">編輯</th>
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
                  productNumber,
                } in products"
                :key="productId"
              >
                <td style="text-align: center; vertical-align: middle">
                  {{ productNumber }}
                </td>
                <td style="text-align: center; vertical-align: middle">
                  {{ productName }}
                </td>
                <td style="text-align: center; vertical-align: middle">
                  {{ unitPrice }}
                </td>
                <td style="text-align: center; vertical-align: middle">
                  {{ productType }}
                </td>
                <td style="text-align: center; vertical-align: middle">
                  <img class="small-image" :src="productImg" />
                </td>
                <td style="text-align: center; vertical-align: middle">
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
    </div>
    <Page
      :totalPages="totalPages"
      :thePage="currentPage"
      @childClick="clickHandler"
    >
    </Page>
    <a
      class="material-scrolltop"
      href="javascript: void(0);"
      @click="scrollToTop"
      >TOP</a
    >
  </div>
</template>

<script setup>
// 解構賦值
import { ref, watch, onMounted } from 'vue';
import axios from 'axios';
import Swal from 'sweetalert2';
import Page from '@/views/ProductPage.vue';
// import product from '@/models/Product';

// 使用 Vue 的 ref 初始化 products 變數，用於存儲商品數據
const products = ref([]);
const totalPages = ref(1);
const currentPage = ref(1);
const productType = ref('');
const productNumbers = ref([]);
const initialPage = ref(1); // 用於保存初始的頁碼
const page = ref(1);
const totalItems = ref(0);

// 從 .env 文件中讀取 API URL
const URL = import.meta.env.VITE_OSCAT_API_ENDPOINT;

const caculateProductNumbers = (page) => {
  const itemsPerPage = 6;
  const startingNumber = page * itemsPerPage + 1;
  const endingNumber = startingNumber + itemsPerPage - 1;
  const numbers = [];

  for (let i = startingNumber; 1 <= endingNumber; i++) {
    numbers.push(i);
  }
  console.log(productNumbers);
  productNumbers.value = numbers;
};

// 在載入商品列表或搜尋後更新總筆數
const updateTotalItems = (count) => {
  totalItems.value = count;
};

const loadProducts = async () => {
  const pageNumber = currentPage.value; // 使用當前頁碼
  const url = `${URL}/product/page/${pageNumber}`;
  console.log(url);
  try {
    const response = await axios.get(url);
    console.log(response.data);
    products.value = response.data.content.map((products, index) => ({
      ...products,
      productNumber: (pageNumber - 1) * 6 + index + 1, // 計算編號
    }));

    totalPages.value = response.data.totalPages;

    //更新總筆數
    updateTotalItems(response.data.totalElements);
  } catch (error) {
    console.error('獲取分頁商品數據失敗', error);
  }
};

// 不同頁數
const clickHandler = (newPage) => {
  if (newPage >= 1 && newPage <= totalPages.value) {
    currentPage.value = newPage; // 重置當前頁碼
    page.value = newPage;
    loadProducts();
  }
};

//查詢類別
const searchProducts = async () => {
  if (!productType.value) {
    Swal.fire({
      icon: 'warning',
      title: '警告',
      text: '請輸入商品類別',
    });
    return; // 結束函數，不執行後續操作
  }
  const search_url = `${URL}/product/byType?productType=${productType.value}`;
  console.log(search_url);
  try {
    const response = await axios.get(search_url);
    if (response.status === 200) {
      products.value = response.data.map((product, index) => ({
        ...product,
        productNumber: index + 1, // 自行生成編號
      }));

      // 重新計算總頁數，根據查詢結果的數量
      totalPages.value = Math.ceil(response.data.length / 6); // 假設每頁顯示 6 項商品

      // 重新計算總筆數，根據查詢結果的數量
      totalItems.value = response.data.length; // 更新總筆數

      currentPage.value = initialPage.value; // 重置為初始頁碼
    } else {
      Swal.fire({
        icon: 'error',
        title: '錯誤',
        text: '查詢商品類別失敗',
      });
    }
  } catch (error) {
    Swal.fire({
      icon: 'error',
      title: 'wrong',
      text: '查詢商品類別失敗: ' + error.message,
    });
  }
};

watch(page, (newPage) => {
  caculateProductNumbers(newPage);
});

watch(productType, (newProductType) => {
  if (newProductType === '') {
    // 如果 productType 被清空，則觸發查詢函數
    loadProducts();
  }
});

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
      Swal.fire('刪除', '商品已刪除', 'success');
      //刪除完後重導頁面
      loadProducts();
    }
  }
};

const scrollToTop = () => {
  window.scrollTo({ top: 0, behavior: 'smooth' });
};

// 這是一個偵聽滾動事件的函數，當用戶滾動頁面時，顯示或隱藏回到頂部的按鈕
const handleScroll = () => {
  const scrollButton = document.querySelector('.material-scrolltop');
  if (scrollButton) {
    if (window.scrollY > 100) {
      scrollButton.style.display = 'block';
    } else {
      scrollButton.style.display = 'none';
    }
  }
};

// 在組件掛載後註冊滾動事件的監聽
onMounted(() => {
  window.addEventListener('scroll', handleScroll);
});

// 初始的第一次 把資料帶進去
loadProducts();
</script>
<style scoped>
body {
  font-family: 'Arial', sans-serif; /* 替換 'YourFont' 為你喜歡的字體名稱 */
}
p,
.page-title {
  font-size: 24px;
  color: #d1d8e0;
}
/* 修改表格文字字體 */
.table {
  font-family: 'Arial', sans-serif; /* 替換 'YourTableFont' 為你喜歡的字體名稱 */
  font-size: 20px; /* 修改表格文字的字體大小 */
}

/* 修改表格文字顏色 */
.table td,
.table th {
  color: #ffffff; /* 修改表格中文字的顏色，這裡設定為深灰色 */
}

.search-container {
  display: flex;
  justify-content: flex-end; /* 將內容靠右對齊 */
  padding: 20px;
}
.small-image {
  width: 100px;
  height: 100px;
  object-fit: cover;
}

.search-box {
  height: 40px;
  background: #2c3e50;
  padding: 10px;
  border-radius: 40px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-box .search-input {
  background: none;
  border: none;
  outline: none;
  color: white;
  font-size: 20px;
  width: 0px;
  transition: 0.5s;
}

.search-box .search-btn {
  width: 40px;
  height: 40px;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 20px;
}

.search-box:hover .search-input {
  width: 250px;
}
.special-button {
  position: relative;
  color: #111111;
  font-size: 1rem;
  text-transform: uppercase;
  font-weight: bold;
  text-align: center;
  text-decoration: none;
  transition: all 0.2s ease;
  padding: 12px 20px;
  display: inline-flex;
  flex-direction: row;
  align-items: center;
  justify-content: center;
}
.special-button:before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  display: block;
  border-radius: 28px;
  background: rgba(255, 171, 157, 0.5);
  width: 56px;
  height: 56px;
  transition: all 0.3s ease;
}
.special-button span {
  position: relative;
  z-index: 1;
}
.special-button svg {
  position: relative;
  top: 0;
  margin-left: 10px;
  fill: none;
  stroke-linecap: round;
  stroke-linejoin: round;
  stroke: #111111;
  stroke-width: 2;
  transform: translateX(-5px);
  transition: all 0.3s ease;
}
.special-button:hover:before {
  width: 100%;
  background: #ffab9d;
}
.special-button:hover svg {
  transform: translateX(0);
}
.special-button:hover,
.special-button:focus {
  color: #111111;
}
.special-button:active {
  color: #111111;
  transform: scale(0.96);
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

tr:hover td {
  background-color: #2a2a2a;
  transition: all 0.3s ease-in-out;
}
</style>
