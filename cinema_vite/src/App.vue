<script setup>
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.js';
import 'bootstrap-icons/font/bootstrap-icons.css';
import NavbarVue from './components/NavbarVue.vue';
</script>

<template>
  <div class="container">
    <NavbarVue></NavbarVue>
    <hr />
    <Router-View></Router-View>
  </div>
</template>

<style scoped></style>
