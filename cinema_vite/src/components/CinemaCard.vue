<script setup>
import { useRouter } from 'vue-router';

// 定義和接收 props
const props = defineProps({
  cinemaName: {
    type: String,
    required: true,
  },
  imgUrl: {
    type: String,
    required: true,
  },
  id: {
    type: Number,
    required: true,
  },
});

// 使用解構賦值來取得單獨的 props
const { cinemaName, imgUrl, id } = props;

const router = useRouter();

// 處理點擊事件
const toDetail = () => {
  router.push({ path: `/cinema-detail/${id}` });
};
</script>

<template>
  <!-- 定義卡片組件 -->
  <div class="card" @click="toDetail">
    <!-- 使用 props 來設定圖片和文字 -->
    <img :src="imgUrl" class="card-img" alt="..." />
    <div
      class="card-img-overlay d-flex justify-content-evenly align-items-center text-center"
    >
      <h2 class="card-title">{{ cinemaName }}</h2>
    </div>
  </div>
</template>

<style scoped>
.card {
  cursor: pointer;
  width: 300px;
  height: 300px;
}
.card-img {
  transition: opacity 0.3s ease;
}

.card-img-overlay {
  background: rgba(0, 0, 0, 0);
  opacity: 0;
  transition:
    opacity 0.3s ease,
    background 0.3s ease;
}

.card:hover .card-img {
  opacity: 0.5;
}

.card:hover .card-img-overlay {
  background: rgba(0, 0, 0, 0.5);
  opacity: 1;
}
</style>
