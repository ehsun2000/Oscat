<template>
  <div class="roll">
    <div class="box"></div>
    <h2>會員管理</h2>
    <RouterLink class="btn btn-outline-primary" to="/member/insert"
      ><i class="bi bi-plus"></i>新增</RouterLink
    >
    <div class="row mb-3">
      <div class="col-3"></div>
      <div class="col-6"></div>
      <div class="col-3"></div>
    </div>
    <SearchBox @searchInput="inputHandLer"></SearchBox>
    <table class="table table-hover">
      <thead>
        <tr>
          <th>編號</th>
          <th>姓名</th>
          <th>信箱</th>
          <th>密碼</th>
          <th>手機</th>
          <th>性別</th>
          <th>生日</th>
          <th>加入時間</th>
        </tr>
      </thead>
      <tbody>
        <tr
          v-for="{
            memberId,
            memberName,
            email,
            password,
            phone,
            gender,
            birthDate,
            joinDate,
          } in formateMember"
          :key="memberId"
        >
          <td>{{ memberId }}</td>
          <td>{{ memberName }}</td>
          <td>{{ email }}</td>
          <td>{{ password }}</td>
          <td>{{ phone }}</td>
          <td>{{ gender }}</td>
          <td>{{ birthDate }}</td>
          <td>{{ joinDate }}</td>
          <td>
            <RouterLink
              class="btn btn-secondary me-3"
              :to="'/member/update/' + memberId"
              ><i class="bi bi-pencil-fill"></i> 修改
            </RouterLink>
            <button class="btn btn-danger" @click="deleteHandler(memberId)">
              <i class="bi bi-trash-fill"></i> 刪除
            </button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
import axios from 'axios';
import { ref, reactive, computed } from 'vue';
import SearchBox from '../yuho/SearchBox.vue';

const members = ref([]);

// 載入所有會員
const loadMembers = async () => {
  const member_url = `${import.meta.env.VITE_API_JAVAURL}admin/member/all`;
  const response = await axios.get(member_url);
  members.value = response.data;
};

// 搜尋功能
const datas = reactive({
  email: '',
});

// 傳入參數重新仔入
const inputHandLer = (value) => {
  datas.email = value;
  loadMembers();
};

// 關鍵字(email)搜尋
const formateMember = computed(() => {
  const filteredMembers = members.value.filter((member) => {
    return member.email.includes(datas.email);
  });
  return filteredMembers.map((member) => {
    const date = new Date(member.joinDate); // 調整日期格式
    const formateDate = date.toLocaleDateString('zh-TW', {
      year: 'numeric', // 設置年份的格式為數字
      month: '2-digit', // 設置月份的格式為兩位數字
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit',
      second: '2-digit',
    });

    return {
      ...member, // 保留 member 的其他屬性
      joinDate: formateDate,
    };
  });
});

loadMembers();

// 刪除
const deleteHandler = async (memberId) => {
  if (window.confirm('確認刪除?')) {
    const delete_url = `${
      import.meta.env.VITE_API_JAVAURL
    }admin/member/delete/${memberId}`;
    const response = await axios.delete(delete_url);
    if (response.data) {
      alert('刪除成功');
      loadMembers();
    }
  }
};
loadMembers();
</script>

<style scoped></style>
