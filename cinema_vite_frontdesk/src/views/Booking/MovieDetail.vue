<template>
  <div class="col-md-6">
    <img :src="props.movie.posterImage" class="card-img-top" alt="" />
    <h5>{{ props.movie.movieName }}</h5>
    <p>{{ props.movie.plotSummary }}</p>
    <iframe
      width="560"
      height="315"
      :src="youtubeEmbedLinkComputed"
      frameborder="0"
      allowfullscreen
    ></iframe>
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
}
</style>
