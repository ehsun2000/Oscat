<template>
  <div class="card-body">
    <!-- 只有當 loaded 為 true 時才渲染 Line 組件 -->
    <div v-if="loaded" class="chart-container">
      <Bar
        v-if="loaded"
        class="my-chart-id"
        :options="chartOptions"
        :data="chartData"
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
  BarController,
  CategoryScale,
  LinearScale,
  Title,
  Tooltip,
} from 'chart.js';

export default {
  name: 'BarChart',
  components: { Bar },
  setup() {
    // 註冊必要的 Chart.js 組件
    Chart.register(BarController, CategoryScale, LinearScale, Title, Tooltip);

    // 定義參數
    const loaded = ref(false); // 控制 Bar 組件是否渲染
    const birthDateMember = ref({});
    const chartData = ref({
      labels: [
        '一月',
        '二月',
        '三月',
        '四月',
        '五月',
        '六月',
        '七月',
        '八月',
        '九月',
        '十月',
        '十一月',
        '十二月',
      ], // Y軸標籤
      datasets: [
        {
          label: '人數',
          backgroundColor: '#FFCE56',
          data: [],
        },
      ],
    });
    const chartOptions = ref({
      categoryPercentage: 0.7,
      barPercentage: 0.9,
      responsive: true,
    });

    // 數據加載
    onMounted(async () => {
      try {
        const response = await axios.get(
          `${import.meta.env.VITE_OSCAT_API_ENDPOINT}/member/birthProportion`,
        );
        birthDateMember.value = response.data;

        // 轉換型別並賦值
        chartData.value.datasets[0].data = chartData.value.labels.map(
          (monthLabel) => {
            let monthIndex = chartData.value.labels.indexOf(monthLabel) + 1; // +1 因為月份從1開始
            return birthDateMember.value[monthIndex] || 0; // 使用 || 0 確保未定義的月份返回 0
          },
        );

        // 資料加載完成，設置 loaded 為 true
        loaded.value = true;
      } catch (error) {
        console.error('Error fetching data:', error);
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
</style>
