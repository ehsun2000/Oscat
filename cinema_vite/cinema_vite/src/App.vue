<script setup>
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.js';
import Navbar from './components/NavbarMem.vue';
</script>

<template>
  <div class="container">
    <Navbar></Navbar>
    <router-view></router-view>
  </div>
</template>

<style scoped></style>
