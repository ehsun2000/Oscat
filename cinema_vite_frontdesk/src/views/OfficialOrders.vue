<template>
  <div class="container w-75">
    <h3>訂 單 紀 錄</h3>
    <div class="py-5 text-center"></div>
    <div class="row">
      <div class="col-sm-6 mb-3 mb-sm-0">
        <div class="card">
          <div class="card-body">
            <h5 class="card-title">Special title treatment</h5>
            <p class="card-text">
              With supporting text below as a natural lead-in to additional
              content.
            </p>
            <a href="#" class="btn btn-primary">Go somewhere</a>
          </div>
        </div>
      </div>
      <!-- <div class="col-sm-6">
        <div class="card">
          <div class="card-body">
            <h5 class="card-title">Special title treatment</h5>
            <p class="card-text">
              With supporting text below as a natural lead-in to additional
              content.
            </p>
            <a href="#" class="btn btn-primary">Go somewhere</a>
          </div>
        </div>
      </div> -->
    </div>
  </div>
</template>

<script setup>
// import { ref } from 'vue';
import { useRoute } from 'vue-router';
import axios from 'axios';

const route = useRoute();

const loadData = async () => {
  const id = route.params.memberId;
  const url = `${
    import.meta.env.VITE_OSCATOfficial_API_ENDPOINT
  }/member/order/${id}`;
  console.log(url);
  const { data } = await axios.get(url);
  console.log(data);
  // movie.value = data;
};

loadData();
</script>

<style scoped></style>
