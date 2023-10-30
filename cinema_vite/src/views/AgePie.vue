<template>
  <div class="roll">
    <!-- 只有當 loaded 為 true 時才渲染 Pie 組件 -->
    <Pie
      v-if="loaded"
      class="my-chart-id"
      :options="chartOptions"
      :data="chartData"
    ></Pie>
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
            '	#804040',
          ],
          data: [],
        },
      ],
    });
    const chartOptions = ref({
      responsive: true,
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
/* 你的樣式 */
</style>
