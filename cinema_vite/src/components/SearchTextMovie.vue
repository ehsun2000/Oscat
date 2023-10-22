<template>
  <input
    type="search"
    @input="inputHandler"
    v-model="keyword"
    placeholder="請輸入電影名稱"
    class="form-control mb-3"
  />
</template>

<script setup>
import { ref } from 'vue';
const keyword = ref('');
const emit = defineEmits(['searchInput']);
const inputHandler = () => {
  console.log('觸發子組件');
  emit('searchInput', keyword.value);
};
</script>

<style scoped></style>
