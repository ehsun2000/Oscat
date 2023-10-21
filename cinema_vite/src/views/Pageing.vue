<template>
  <nav aria-label="Page navigation example">
    <ul class="pagination">
      <li
        @click="handler(value)"
        class="page-item"
        v-for="(value, index) in totalPages"
        :key="index"
      >
        <a :class="{ 'page-link': true, currentPage: thePage === value }"
          >{{ value }}
        </a>
      </li>
    </ul>
  </nav>
</template>

<script setup></script>

<style scoped></style>
