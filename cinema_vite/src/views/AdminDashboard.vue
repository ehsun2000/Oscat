<script setup>
import { RouterView } from 'vue-router';
import AdminSideBar from '@/components/AdminSideBar.vue';
import AdminHeader from '@/components/AdminHeader.vue';
</script>

<template>
  <!-- 
    頂部導覽 (Header)
    亮暗顏色控制
    索引到目標設定
  -->
  <!-- 
    側邊導覽 (SideBar)
    -- 首頁
    -- 電影管理
      -- 電影資訊 (default)
      -- 場次設定
    -- 會員管理
      -- 會員設定 (default)
      -- 分析報告 
    -- 影城管理
      -- 資訊設定 (default)
      -- 座位設定
      -- 販售設定
  -->
  <!-- MainLayout.vue -->
  <div class="container-fluid vh-100">
    <div class="row h-100">
      <div class="col-2 d-flex flex-column p-3">
        <AdminSideBar />
      </div>
      <div class="col-10">
        <AdminHeader />
        <router-view />
      </div>
    </div>
  </div>
</template>

<style scope></style>
