<template>
  <div class="card-body">
    <div class="roll">
      <!-- 只有當 loaded 為 true 時才渲染 Pie 組件 -->
      <div v-if="loaded" class="chart-container">
        <Pie
          class="my-chart-id"
          :options="chartOptions"
          :data="chartData"
        ></Pie>
      </div>
    </div>
    <!-- 顯示數據區域 -->
    <div class="data-display">
      <div v-for="(label, index) in chartData.labels" :key="index">
        {{ label }}: {{ chartData.datasets[0].data[index] }}位
      </div>
      <div>男性比例: {{ genderProportions.maleProportion }}</div>
      <div>女性比例: {{ genderProportions.femaleProportion }}</div>
      <div>其他比例: {{ genderProportions.otherProportion }}</div>
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
    const genderMember = ref({});
    const genderProportions = ref({});
    const chartData = ref({
      labels: ['男性', '女性', '其他'],
      datasets: [
        {
          backgroundColor: ['#36A2EB', '#FF6384', '#FFCE56'],
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
          `${import.meta.env.VITE_OSCAT_API_ENDPOINT}/member/genderProportion`,
        );
        genderMember.value = response.data;

        // 轉換型別並賦值
        chartData.value.datasets[0].data = [
          Number(genderMember.value.maleCount),
          Number(genderMember.value.femaleCount),
          Number(genderMember.value.otherCount),
        ];

        // 比例數據
        genderProportions.value = {
          maleProportion: (genderMember.value.maleProportion * 100).toFixed(2),
          femaleProportion: (genderMember.value.femaleProportion * 100).toFixed(
            2,
          ),
          otherProportion: (genderMember.value.otherProportion * 100).toFixed(
            2,
          ),
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
      genderProportions,
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
  flex: 1; /* 讓圖表容器可以伸縮填充空間 */
  padding-right: 20px; /* 可以添加一些間隔，如果需要的話 */
}

.data-display {
  padding-left: 20px; /* 添加一些間隔，如果需要的話 */
  color: black;
  line-height: 40px;
}
</style>
