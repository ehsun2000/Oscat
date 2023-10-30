<template>
  <div class="text-center centered-card">
    <div class="card-header">
      <div class="btn-group" role="group" aria-label="比例">
        <button class="btn btn-primary" @click="showGenderPie">性別比</button>
        <button class="btn btn-primary" @click="showAgePie">年齡比</button>
      </div>
    </div>
    <div class="card-body">
      <component :is="currentComponent"></component>
    </div>
  </div>
  <!-- <div class="card text-center">
    <div class="card-header">
      <div class="btn-group" role="group" aria-label="比例">
        <button class="btn btn-primary">加入時間比</button>
      </div>
    </div>
    <div class="card-body">
      <JoinDateLine></JoinDateLine> -->
  <!-- </div>
  </div> -->
</template>

<script setup>
import { ref } from 'vue';
import GenderPie from '@/views/GenderPie.vue';
import AgePie from '@/views/AgePie.vue';
// import JoinDateLine from '@/views/JoinDateLine.vue';

const currentComponent = ref(GenderPie);

const showGenderPie = () => {
  currentComponent.value = GenderPie;
};

const showAgePie = () => {
  currentComponent.value = AgePie;
};
</script>

<style scoped>
.btn {
  margin-right: 10px;
  margin-left: 10px;
}

.centered-card {
  width: 80%;
  height: 50%;
  margin: 0 auto;
  padding: 20px;
  box-sizing: border-box;
  display: flex;
  flex-direction: column; /*垂直主軸*/
  align-items: center; /*水平*/
  justify-content: center; /*垂直置中*/
}

.card-header {
  border: 1px solid #ccc;
  border-radius: 10px;
  padding: 10px;
}

.card-body {
  border: 1px solid #ccc;
  border-radius: 10px;
  padding: 10px;
  height: auto;
  position: relative;
}

.component {
  width: 100%;
  height: 100%;
  max-width: 100%;
  max-height: 100%;
  object-fit: contain;
  position: absolute;
  bottom: 10px;
}
</style>
