<template>
  <div class="d-flex align-items-center">
    <img :src="props.movie.posterImage" class="card-img-top" alt="" />
    <div class="flex-grow-1 ms-3 details">
      <h3>{{ props.movie.movieName }}</h3>
      <p>{{ props.movie.plotSummary }}</p>
      <iframe
        width="560"
        height="315"
        :src="youtubeEmbedLinkComputed"
        frameborder="0"
        allowfullscreen
      ></iframe>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue';

const props = defineProps({
  movie: Object,
});

const youtubeEmbedLinkComputed = computed(() => {
  if (props.movie.trailerLink) {
    return props.movie.trailerLink.replace('watch?v=', 'embed/');
  }
  return '';
});
</script>

<style scoped>
img {
  width: 300px;
  margin: 30px 80px 50px 0px;
}
h3 {
  color: #e6be8a;
}
</style>
