<template>
  <nav aria-label="Page navigation example">
    <ul class="pagination">
      <li
        @click="handler(value)"
        class="page-item"
        v-for="(value, index) in totalPages"
        :key="index"
      >
        <a class="page-link" @click="clickHandler(index + 1)"
          >{{ index + 1 }}
        </a>
      </li>
    </ul>
  </nav>
</template>

<script setup>
const props = defineProps({
  totalPages: Number,
  thePage: Number,
});

console.log(props.totalPages);
console.log(props.thePage);

//定義父元件傳進來的事件名稱
const emit = defineEmits(['childClick']);

const clickHandler = (page) => {
  //引發父元件定義好的事件
  //並且把資料傳給父元件的事件綁定的函式
  //子傳父
  emit('childClick', page);
};
</script>

<style scoped>
.pagination li {
  cursor: pointer;
}
</style>
