<template>
  <h2 class="title">Oscat訂單管理分析</h2>
  <div class="card-body">
    <!-- 只有當 loaded 為 true 時才渲染 Line 組件 -->
    <div v-if="loaded" class="chart-container">
      <Bar
        v-if="loaded"
        class="my-chart-id"
        :options="chartOptions"
        :data="chartData"
        type="bar"
      ></Bar>
    </div>
  </div>
</template>

<script>
import { Bar } from 'vue-chartjs';
import { ref, onMounted } from 'vue';
import axios from 'axios';
import {
  Chart,
  Title,
  Tooltip,
  Legend,
  BarElement,
  CategoryScale,
  LinearScale,
  ArcElement,
} from 'chart.js';

export default {
  name: 'PieChart',
  components: { Bar },
  setup() {
    // 註冊必要的 Chart.js 組件
    Chart.register(
      Title,
      Tooltip,
      Legend,
      BarElement,
      CategoryScale,
      LinearScale,
      ArcElement,
    );

    // 定義參數
    const loaded = ref(false); // 控制 Bar 組件是否渲染
    const movieName = ref({});
    const chartData = ref({
      labels: [],
      datasets: [
        {
          backgroundColor: ['#36A2EB', '#FF6384'],
          data: [],
        },
      ],
    });
    const chartOptions = ref({
      responsive: true,
      plugins: {
        legend: {
          display: true,
          // position: 'bottom', // 調整圖例位置
        },
      },
      cutout: '60%', // 調整中心孔大小
      borderWidth: 2, // 調整圓餅圖的邊框寬度
      borderColor: 'white', // 設置邊框顏色
    });

    // 數據加載
    onMounted(async () => {
      try {
        const response = await axios.get(
          `${
            import.meta.env.VITE_OSCAT_API_ENDPOINT
          }/transorder/movieOrderStats`,
        );
        const data = response.data;

        // 提取 movieName
        const movieNames = data.map((item) => item.movie.movieName);

        // 設置 chartData 的 labels
        chartData.value.labels = movieNames;

        // 設置 chartData 的 datasets 數據
        chartData.value.datasets[0].data = data.map((item) =>
          Number(item.orderCount),
        );

        chartOptions.value.plugins.title = {
          display: true,
          text: '訂單分析', // 你想要的標題文字
        };
        console.log(chartData.value.labels);
        console.log(chartData.value.datasets[0].data);

        // 數據加載完成，設置 loaded 為 true
        loaded.value = true;
      } catch (error) {
        console.error('Error fetching data:', error);
        // 如果發生錯誤，也將loaded 設置為true，以防組件一直處於未加載狀態
        loaded.value = true;
      }
    });

    return {
      chartData,
      chartOptions,
      loaded, // 返回 loaded 讓模板可以訪問它
    };
  },
};
</script>

<style scoped>
.card-body {
  display: flex;
  flex-direction: row; /* 新增這行來更改主軸方向 */
  align-items: center; /* 確保子元素在交叉軸上居中 */
  justify-content: center;
  width: 100%; /* 或者設置一個具體的寬度 */
  border: 1px solid #ccc;
  border-radius: 10px;
  padding: 10px;
  margin-top: 10px;
  background-color: #bebebe;
}
.chart-container {
  flex: 1;
  padding-right: 20px;
  height: 300px;
  display: flex; /* 使用 flexbox */
  justify-content: center; /* 水平居中 */
  align-items: center; /* 垂直居中 */
}

.my-chart-id {
  max-height: 300px;
}

.title {
  margin: 20px;
  padding: 20px;
  width: 500px;
  border: 1px solid #ccc;
  border-radius: 10px;
  text-align: center;
}
</style>
