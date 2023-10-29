<template>
  <input
    type="search"
    v-model="keyword"
    @input="inputHandler"
    placeholder="請輸入電影名稱"
    class="form-control mb-3"
  />
</template>

<script setup>
import { ref, watch } from 'vue';
const keyword = ref('');
const emit = defineEmits(['searchInput']);
const inputHandler = () => {
  console.log('觸發子組件');
  emit('searchInput', keyword.value);
};

watch(keyword, (newValue) => {
  console.log('keyword 變化為: ' + newValue);
});
</script>

<style scoped></style>
