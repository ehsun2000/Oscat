<template>
  <div class="card-body">
    <div class="roll">
      <!-- 只有當 loaded 為 true 時才渲染 Pie 組件 -->
      <div v-if="loaded" class="chart-container">
        <Pie
          v-if="loaded"
          class="my-chart-id"
          :options="chartOptions"
          :data="chartData"
        ></Pie>
      </div>
    </div>
    <div class="data-display">
      <div class="left-group">
        <div v-for="(label, index) in chartData.labels" :key="index">
          {{ label }}: {{ chartData.datasets[0].data[index] }}位
        </div>
      </div>
      <div class="right-group">
        <div>小於18歲比例: {{ ageProportions.youngAgeProportion }}</div>
        <div>18-25歲比例: {{ ageProportions.teenagerAgeProportion }}</div>
        <div>26-35歲比例: {{ ageProportions.adultAgeProportion }}</div>
        <div>36-50歲比例: {{ ageProportions.middleageAgeProportion }}</div>
        <div>51-65歲比例: {{ ageProportions.elderlyAgeProportion }}</div>
        <div>大於65歲比例: {{ ageProportions.oldAgeProportion }}</div>
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
    const ageProportions = ref({});
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
        // 比例數據
        ageProportions.value = {
          youngAgeProportion: (
            ageMember.value.youngAgeProportion * 100
          ).toFixed(2),
          teenagerAgeProportion: (
            ageMember.value.teenagerAgeProportion * 100
          ).toFixed(2),
          adultAgeProportion: (
            ageMember.value.adultAgeProportion * 100
          ).toFixed(2),
          middleageAgeProportion: (
            ageMember.value.middleageAgeProportion * 100
          ).toFixed(2),
          elderlyAgeProportion: (
            ageMember.value.elderlyAgeProportion * 100
          ).toFixed(2),
          oldAgeProportion: (ageMember.value.oldAgeProportion * 100).toFixed(2),
        };

        // 資料加載完成，設置 loaded 為 true
        loaded.value = true;
      } catch (error) {
        console.error('Error fetching data:', error);
      }
    });

    return {
      chartData,
      chartOptions,
      ageProportions,
      loaded, // 返回 loaded 讓模板可以訪問它
    };
  },
};
</script>

<style scoped>
.card-body {
  display: flex;
  flex-direction: row; /* 新增這行來更改主軸方向 */
  justify-content: center; /* 新增這行來在元素之間添加空間 */
  align-items: center; /* 確保子元素在交叉軸上居中 */
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
}

.data-display {
  display: flex;
  justify-content: space-between;
  color: black;
}
.left-group,
.right-group {
  width: 200px;
  line-height: 40px;
}
</style>
