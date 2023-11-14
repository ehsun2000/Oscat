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
              <button
                @click="confirmOrder(transorder.orderId)"
                :disabled="
                  transorder.paymentMethod === '線上付款' ||
                  transorder.paymentMethod === '已付款' ||
                  transorder.paymentMethod === '已取消' ||
                  transorder.paymentMethod === '綠界付款'
                "
              >
                確認訂單
              </button>
            </td>
            <td>
              <button
                @click="cancelOrder(transorder.orderId)"
                :disabled="
                  transorder.paymentMethod === '已取消' ||
                  transorder.paymentMethod === '已付款'
                "
              >
                取消訂單
              </button>
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
    <!-- <component :is="currentComponent"></component> -->
  </div>
</template>

<script setup>
import Swal from 'sweetalert2';
import axios from 'axios';
import Page from '@/views/SearchPage.vue';
import { ref, onMounted, computed } from 'vue';
// import MovieAnalysis from '@/components/OrderBar.vue';

const transorders = ref([]);
const currentPage = ref(1);
const totalPages = ref(1);
const selectedEmail = ref(null);

// const currentComponent = ref(MovieAnalysis);

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
// 確認訂單
const confirmOrder = (orderId) => {
  Swal.fire({
    title: '確認訂單?',
    text: '您確定要確認此訂單嗎？',
    icon: 'warning',
    showCancelButton: true,
    confirmButtonText: '確定',
    cancelButtonText: '取消',
  }).then((result) => {
    if (result.isConfirmed) {
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

          Swal.fire('訂單已成功確認', '', 'success');
        })
        .catch(() => {
          Swal.fire('訂單確認失敗', '', 'error');
        });
    }
  });
};
// 取消訂單，並有退款成功之後的畫面
const cancelOrder = (orderId) => {
  Swal.fire({
    title: '確定取消訂單?',
    text: '您確定要取消此訂單嗎？',
    icon: 'warning',
    showCancelButton: true,
    confirmButtonColor: '#3085d6',
    cancelButtonColor: '#d33',
    confirmButtonText: '確定',
    cancelButtonText: '取消',
  }).then((result) => {
    if (result.isConfirmed) {
      showRefundInProgressAlert();
      setTimeout(() => {
        const cancelOrderUrl = `${
          import.meta.env.VITE_OSCAT_API_ENDPOINT
        }/transorder/cancelOrder?orderId=${orderId}`;

        axios
          .post(cancelOrderUrl)
          .then(() => {
            const orderIndex = transorders.value.findIndex(
              (order) => order.orderId === orderId,
            );
            if (orderIndex !== -1) {
              transorders.value[orderIndex].paymentMethod = '已取消';
            }

            showCancelOrderSuccessAlert();
          })
          .catch((error) => {
            console.error('Error canceling order:', error);
            showCancelOrderFailureAlert();
          });
      }, 5000);
    }
  });
};

const showRefundInProgressAlert = () => {
  let timerInterval;
  Swal.fire({
    title: '退款中',
    html: '請稍候，退款中 <b></b> 秒',
    timer: 5000,
    timerProgressBar: true,
    didOpen: () => {
      Swal.showLoading();
      const timer = Swal.getPopup().querySelector('b');
      timerInterval = setInterval(() => {
        timer.textContent = `${Math.ceil(Swal.getTimerLeft() / 1000)}`;
      }, 1000);
    },
    willClose: () => {
      clearInterval(timerInterval);
    },
  });
};

const showCancelOrderSuccessAlert = () => {
  Swal.fire({
    title: '訂單已取消',
    text: '訂單已成功取消',
    icon: 'success',
  });
};

const showCancelOrderFailureAlert = () => {
  Swal.fire({
    title: '取消失敗',
    text: '取消訂單時出現錯誤',
    icon: 'error',
  });
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
