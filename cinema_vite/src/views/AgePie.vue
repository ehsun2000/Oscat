<template>
  <div
    class="card-body"
    style="
      display: flex;
      justify-content: center;
      align-items: center;
      height: 450px;
    "
  >
    <div class="roll" style="width: 100%; height: 100%">
      <!-- 只有當 loaded 為 true 時才渲染 Pie 組件 -->
      <div v-if="loaded" class="chart-container">
        <Pie
          v-if="loaded"
          class="my-chart-id"
          :options="chartOptions"
          :data="chartData"
          style="max-width: 100%; max-height: 100%; overflow: hidden"
        ></Pie>
        <div class="data-display">
          <div v-for="(label, index) in chartData.labels" :key="index">
            {{ label }}: {{ chartData.datasets[0].data[index] }}
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { Pie } from 'vue-chartjs';
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
  components: { Pie },
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
    const loaded = ref(false); // 控制 Pie 組件是否渲染
    const ageMember = ref({});
    const chartData = ref({
      labels: [
        '<18 歲',
        '18-25 歲',
        '26-35 歲',
        '36-50 歲',
        '51-65 歲',
        '>65 歲',
      ],
      datasets: [
        {
          backgroundColor: [
            '#36A2EB',
            '#FF6384',
            '#FFCE56',
            '#750075',
            '#00EC00',
            '#804040',
          ],
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
          `${import.meta.env.VITE_OSCAT_API_ENDPOINT}/member/ageProportion`,
        );
        ageMember.value = response.data;

        // 轉換型別並賦值
        chartData.value.datasets[0].data = [
          Number(ageMember.value.youngAge),
          Number(ageMember.value.teenagerAge),
          Number(ageMember.value.adultAge),
          Number(ageMember.value.middleageAge),
          Number(ageMember.value.elderlyAge),
          Number(ageMember.value.oldAge),
        ];

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
  overflow: auto;
  width: 350px;
  border: 1px solid #ccc;
  border-radius: 10px;
  padding: 10px;
  margin-top: 10px;
}
</style>
