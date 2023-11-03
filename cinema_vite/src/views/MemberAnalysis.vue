<template>
  <div class="container">
    <div class="text-center centered-card">
      <div class="card-header">
        <div class="btn-group" role="group" aria-label="比例">
          <button class="btn btn-primary" @click="showGenderPie">性別比</button>
          <button class="btn btn-primary" @click="showAgePie">年齡比</button>
        </div>
      </div>
      <component :is="currentComponent"></component>
    </div>
    <div class="text-center centered-card2">
      <div class="card-header">
        <div class="btn-group" role="group" aria-label="比例">
          <button class="btn btn-primary" @click="showJoinDateBar">
            加入時間比
          </button>
          <button class="btn btn-primary" @click="showBirthDateBar">
            生日月份比
          </button>
        </div>
      </div>
      <div class="card-body2">
        <component :is="barComponent"></component>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import GenderPie from '@/views/GenderPie.vue';
import AgePie from '@/views/AgePie.vue';
import JoinDateBar from '@/views/JoinDateBar.vue';
import birthDateBar from '@/views/BirthDateBar.vue';

const currentComponent = ref(GenderPie);
const barComponent = ref(JoinDateBar);

const showGenderPie = () => {
  currentComponent.value = GenderPie;
};

const showAgePie = () => {
  currentComponent.value = AgePie;
};

const showJoinDateBar = () => {
  barComponent.value = JoinDateBar;
};
const showBirthDateBar = () => {
  barComponent.value = birthDateBar;
};
</script>

<style scoped>
.btn {
  margin-right: 10px;
  margin-left: 10px;
}
.container {
  display: flex;
  justify-content: space-between;
  width: 100%;
  padding: 20px;
}

.centered-card {
  width: 100%;
  height: 100%;
  margin: auto 0; /*調整上邊距離*/
  padding: 20px;
  box-sizing: border-box;
  display: flex;
  flex-direction: column; /*垂直主軸*/
  align-items: center; /*水平*/
  justify-content: center; /*垂直置中*/
}

.centered-card2 {
  width: 70%;
  height: 50%;
  margin: 3vh auto 0;
  padding: 20px;
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.card-header {
  border: 1px solid #ccc;
  border-radius: 10px;
  padding: 10px;
}
.card-body2 {
  border: 1px solid #ccc;
  border-radius: 10px;
  padding: 10px;
  height: 550px;
  width: 450px;
  margin-top: 10px;
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
