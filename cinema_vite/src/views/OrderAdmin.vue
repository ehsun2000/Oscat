<template>
  <div id="app">
    <div class="box"></div>
    <h2>訂單管理</h2>
    <div class="col-md-4" style="width: 200px">
      <input
        type="email"
        class="form-control"
        id="floatingEmail"
        v-model="selectedEmail"
        @input="emailSelected"
        placeholder="請輸入Email"
      />
    </div>
    <div style="padding: 20px">
      <table class="table table-hover table-striped">
        <thead class="thead-dark">
          <tr class="text-center">
            <th>訂單編號</th>
            <th>場次時間</th>
            <th>會員信箱</th>
            <th>付款方式</th>
            <th>下單日期</th>
            <th>總價格</th>
          </tr>
        </thead>
        <tbody>
          <tr
            v-for="transorder in filteredTransorders"
            :key="transorder.orderId"
          >
            <td>
              {{ transorder.orderId }}
            </td>
            <td>
              {{ formatDate(transorder.showtime) }}
            </td>
            <td>
              {{ transorder.email }}
            </td>
            <td>
              {{ transorder.paymentMethod }}
            </td>
            <td>
              {{ formatDate(transorder.bookingDateAndTime) }}
            </td>
            <td>
              {{ transorder.totalPrice }}
            </td>
            <td>
              <button @click="confirmOrder(transorder.orderId)">
                確認訂單
              </button>
            </td>
            <td>
              <button @click="cancelOrder(transorder.orderId)">取消訂單</button>
            </td>
          </tr>
        </tbody>
      </table>
      <Page
        :totalPages="totalPages"
        :thePage="currentPage"
        @childClick="clickHandler"
      ></Page>
    </div>
  </div>
</template>

<script setup>
import axios from 'axios';
import Page from '@/views/SearchPage.vue';
import { ref, onMounted, computed } from 'vue';

const transorders = ref([]);
const currentPage = ref(1);
const totalPages = ref(1);
const selectedEmail = ref(null);

// 分頁
const loadOrders = async (page = currentPage.value) => {
  const transorder_url = `${
    import.meta.env.VITE_OSCAT_API_ENDPOINT
  }/transorder/all?page=${page - 1}&size=7&sort=bookingDateAndTime,desc`;
  try {
    const response = await axios.get(transorder_url);
    transorders.value = response.data.content;
    totalPages.value = response.data.totalPages;
  } catch (error) {
    console.log('錯誤', error);
  }
};

// 不同頁數
const clickHandler = (newPage) => {
  if (newPage >= 1 && newPage <= totalPages.value) {
    currentPage.value = newPage;

    loadOrders(currentPage.value);
  }
};

const filteredTransorders = computed(() => {
  if (selectedEmail.value) {
    return transorders.value.filter((transorder) =>
      transorder.email.includes(selectedEmail.value),
    );
  } else {
    return transorders.value;
  }
});

// 處理用戶選擇信箱後的操作
const emailSelected = () => {
  loadOrders();
};

const formatDate = (dateString) => {
  const date = new Date(dateString);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  const hours = date.getHours();
  const minutes = date.getMinutes();
  const formattedTime = `${hours}:${minutes.toString().padStart(2, '0')}`;
  return `${year}-${month}-${day} ${formattedTime}`;
};

const confirmOrder = (orderId) => {
  const isConfirmed = window.confirm('確認訂單?');

  if (isConfirmed) {
    const requestData = {
      orderId: orderId,
    };

    const confirmOrderUrl = `${
      import.meta.env.VITE_OSCAT_API_ENDPOINT
    }/transorder/confirmOrder`;

    axios
      .post(confirmOrderUrl, requestData)
      .then(() => {
        const orderIndex = transorders.value.findIndex(
          (order) => order.orderId === orderId,
        );
        if (orderIndex !== -1) {
          transorders.value[orderIndex].paymentMethod = '已付款';
        }

        window.alert('訂單已成功確認');
      })
      .catch(() => {});
  }
};
const cancelOrder = (orderId) => {
  const isConfirmed = window.confirm('確定要取消訂單嗎？');

  if (isConfirmed) {
    const requestData = { orderId };

    const cancelOrderUrl = `${
      import.meta.env.VITE_OSCAT_API_ENDPOINT
    }/transorder/cancelOrder`;

    axios
      .post(cancelOrderUrl, null, {
        params: requestData,
      })

      .then(() => {
        const orderIndex = transorders.value.findIndex(
          (order) => order.orderId === orderId,
        );
        if (orderIndex !== -1) {
          transorders.value[orderIndex].paymentMethod = '已取消';
        }
        window.alert('取消成功');
      })
      .catch(() => {});
  }
};
onMounted(async () => {
  loadOrders();
});
</script>

<style>
td {
  text-align: center;
  vertical-align: middle;
}

.searchBox {
  width: 200px;
  margin: 20px auto;
  text-align: left;
}
</style>
