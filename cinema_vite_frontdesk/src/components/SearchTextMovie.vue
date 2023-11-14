<template>
  <input
    type="search"
    v-model="keyword"
    placeholder="請輸入電影名稱"
    class="form-control mb-3"
    style="height: 40px"
  />
  <button
    type="button"
    class="btn btn-outline-light"
    @click="inputHandler"
    style="width: 80px; height: 40px; margin-left: 5px"
  >
    查詢
  </button>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';

const keyword = ref('');
const router = useRouter();

const inputHandler = () => {
  if (keyword.value) {
    router.push({
      name: 'SearchMovie',
      params: { keyword: keyword.value },
    });
  }
};
</script>

<style scoped></style>
