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
    <SearchBox class="searchBox" @searchInput="inputHandLer"></SearchBox>
    <table class="table table-hover">
      <thead>
        <tr>
          <th>編號</th>
          <th>信箱</th>
          <th>姓名</th>
          <th>手機</th>
          <th>性別</th>
          <th>生日</th>
          <th>加入時間</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(member, index) in formateMember" :key="member.memberId">
          <td>{{ (currentPage - 1) * 8 + index + 1 }}</td>
          <td>{{ member.email }}</td>
          <td>{{ member.memberName }}</td>
          <td>{{ member.phone }}</td>
          <td>{{ member.gender }}</td>
          <td>{{ member.birthDate }}</td>
          <td>{{ member.joinDate }}</td>
          <td>
            <RouterLink
              class="btn btn-secondary me-3"
              :to="'/member/update/' + member.memberId"
              ><i class="bi bi-pencil-fill"></i> 修改
            </RouterLink>
            <!-- <button
              class="btn btn-danger"
              @click="deleteHandler(member.memberId)"
            >
              <i class="bi bi-trash-fill"></i> 刪除
            </button> -->
          </td>
        </tr>
      </tbody>
    </table>
    <Page
      :totalPages="totalPages"
      :thePage="currentPage"
      @childClick="clickHandler"
    ></Page>
  </div>
</template>

<script setup>
import axios from 'axios';
import { ref, reactive, computed } from 'vue';
import SearchBox from '@/views/SearchBox.vue';
import Page from '@/views/SearchPage.vue';
// import Swal from 'sweetalert2';

const totalPages = ref(1);
totalPages.value;
const members = ref([]);
const currentPage = ref(1); //當前頁碼

// 分頁
const loadMembers = async (page = currentPage.value) => {
  const member_url = `${
    import.meta.env.VITE_OSCAT_API_ENDPOINT
  }/member/page?page=${page - 1}&size=8&sort=joinDate,desc`;
  try {
    const response = await axios.get(member_url);
    members.value = response.data.content;
    totalPages.value = response.data.totalPages;
  } catch (error) {
    console.log('錯誤', error);
  }
};

// 不同頁數
const clickHandler = (newPage) => {
  if (newPage >= 1 && newPage <= totalPages.value) {
    currentPage.value = newPage;

    loadMembers(currentPage.value);
  }
};

// 搜尋功能
const datas = reactive({
  email: '',
});

// 傳入參數重新載入
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

loadMembers(currentPage.value);

// 刪除
/*const deleteHandler = async (memberId) => {
  Swal.fire({
    title: '確定要刪除嗎?',
    icon: 'question',
    showCancelButton: true,
    confirmButtonColor: '#3085d6',
    cancelButtonColor: '#d33',
    confirmButtonText: '確定',
    cancelButtonText: '取消',
  }).then((result) => {
    if (result.isConfirmed) {
      const delete_url = `${
        import.meta.env.VITE_OSCAT_API_ENDPOINT
      }/member/delete/${memberId}`;
      axios.delete(delete_url).then((response) => {
        if (response.data) {
          Swal.fire('刪除成功', '', 'success').then(() => {
            loadMembers();
          });
        }
      });
    }
  });
};
loadMembers();*/
</script>

<style scoped>
.searchBox {
  width: 400px;
}
</style>
