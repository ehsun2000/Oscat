<template>
  <div class="container">
    <header class="header">
      <h2 class="title">Oscat會員資料分析</h2>
      <div class="extra-content">
        <h5 class="title2">最後更新時間:{{ latestJoinDate }}</h5>
      </div>
    </header>
    <div class="memberNum">
      <h5>會員總數:{{ memberNumber }}</h5>
    </div>

    <div class="charts-container">
      <div class="Pie">
        <div class="btn-group-container">
          <div class="btn-group" role="group" aria-label="比例">
            <button class="btn btn-light" @click="showGenderPie">性別比</button>
            <button class="btn btn-light" @click="showAgePie">年齡比</button>
            <button class="btn btn-light" @click="showJoinDateBar">
              加入時間比
            </button>
            <button class="btn btn-light" @click="showBirthDateBar">
              生日月份比
            </button>
          </div>
        </div>
        <component :is="currentComponent"></component>
      </div>
    </div>
  </div>
</template>

<script setup>
import axios from 'axios';
import { ref, computed } from 'vue';
import GenderPie from '@/components/GenderPie.vue';
import AgePie from '@/components/AgePie.vue';
import JoinDateBar from '@/components/JoinDateBar.vue';
import birthDateBar from '@/components/BirthDateBar.vue';

const currentComponent = ref(GenderPie);

const showGenderPie = () => {
  currentComponent.value = GenderPie;
};

const showAgePie = () => {
  currentComponent.value = AgePie;
};

const showJoinDateBar = () => {
  currentComponent.value = JoinDateBar;
};
const showBirthDateBar = () => {
  currentComponent.value = birthDateBar;
};

const members = ref([]);

const loadMembers = async () => {
  const member_url = `${import.meta.env.VITE_OSCAT_API_ENDPOINT}/member/all`;
  try {
    const response = await axios.get(member_url);
    members.value = response.data;
  } catch (error) {
    console.log('錯誤', error);
  }
};
const latestJoinDate = computed(() => {
  if (members.value.length > 0) {
    const newestMember = members.value[0];
    return new Date(newestMember.joinDate).toLocaleDateString('zh-TW', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit',
      second: '2-digit',
    });
  }
  return '錯誤';
});
const memberNumber = computed(() => {
  if (members.value.length > 0) {
    return members.value.length;
  }
  return '錯誤';
});
loadMembers();
</script>

<style scoped>
.btn {
  margin-right: 10px;
  margin-left: 10px;
}
.btn-group-container {
  align-self: flex-end; /* 對齊到自己的末端（右側） */
  padding: 10px 0; /* 為按鈕組添加一些垂直間距 */
}
.container {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  width: 100%;
}

.header {
  display: flex;
  align-items: center;
  width: 100%;
}

.charts-container {
  display: flex;
  justify-content: space-around;
  align-items: center;
  width: 100%;
}

.Pie,
.Bar {
  flex: 1;
  margin-right: 100px;
  display: flex;
  flex-direction: column; /* 排列方向設為垂直 */
  align-items: flex-end; /* 對齊到容器的末端（右側） */
}

.title {
  margin: 20px;
  padding: 20px;
  width: 500px;
  border: 1px solid #ccc;
  border-radius: 10px;
  text-align: center;
}
.title2 {
  margin: 40px 40px 0px 180px;
  padding: 10px;
  width: 450px;
  border: 1px solid #ccc;
  border-radius: 10px;
  text-align: center;
}
.memberNum {
  padding: 10px;
  width: 250px;
  border: 1px solid #ccc;
  border-radius: 10px;
  text-align: center;
  margin: 0px 0px 0px 20px;
}
</style>
