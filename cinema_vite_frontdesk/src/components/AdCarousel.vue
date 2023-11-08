<template>
  <div
    id="carouselExampleCaptions"
    class="carousel slide"
    data-bs-ride="carousel"
  >
    <div class="carousel-inner">
      <div class="carousel-indicators">
        <button
          type="button"
          data-bs-target="#carouselExampleCaptions"
          data-bs-slide-to="0"
          class="active"
          aria-current="true"
          aria-label="Slide 1"
        ></button>
        <button
          type="button"
          data-bs-target="#carouselExampleCaptions"
          data-bs-slide-to="1"
          aria-label="Slide 2"
        ></button>
        <button
          type="button"
          data-bs-target="#carouselExampleCaptions"
          data-bs-slide-to="2"
          aria-label="Slide 3"
        ></button>
      </div>
      <div class="carousel-item active">
        <img src="/public/add01.jpg" class="d-block w-100" alt="..." />
      </div>
      <div class="carousel-item">
        <img src="/public/add02.jpg" class="d-block w-100" alt="..." />
      </div>
      <div class="carousel-item">
        <img src="/public/add03.jpg" class="d-block w-100" alt="..." />
      </div>
    </div>
    <button
      class="carousel-control-prev"
      type="button"
      data-bs-target="#carouselExampleCaptions"
      data-bs-slide="prev"
    >
      <span class="bi bi-chevron-compact-left"></span>
      <span class="visually-hidden">Previous</span>
    </button>
    <button
      class="carousel-control-next"
      type="button"
      data-bs-target="#carouselExampleCaptions"
      data-bs-slide="next"
    >
      <span class="bi bi-chevron-compact-right" aria-hidden="true"></span>
      <span class="visually-hidden">Next</span>
    </button>
  </div>
</template>

<script setup></script>

<style scoped>
.carousel-inner {
  width: auto;
  height: 500px;
  overflow: hidden;
  margin: 20px 0;
  position: relative;
}

.carousel-item {
  position: relative;
  width: 100%;
  height: 100%;
}

.carousel-item img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.carousel-control-prev,
.carousel-control-next {
  z-index: 2;
  width: 5%;
  height: 100%;
  font-size: 40px;
  position: absolute;
  top: 0;
}

.carousel-control-prev {
  left: 0;
}

.carousel-control-next {
  right: 0;
}

.carousel-inner {
  padding-left: 5%;
  padding-right: 5%;
}

/* 修复图片样式 */
.carousel-inner img {
  object-fit: contain; /* 保持图片的比例 */
  height: auto; /* 高度自动 */
  position: static; /* 移除绝对定位 */
}
</style>
