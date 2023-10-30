<template>
  <li class="nav-item">
    <a
      class="nav-link d-flex justify-content-between align-items-center"
      @click="toggleOpen"
    >
      <span>{{ title }}</span>
      <i
        :class="['bi', 'bi-arrow-up-short', isOpen ? 'arrow-up' : 'arrow-down']"
      ></i>
    </a>
    <div ref="collapseDiv" class="collapse-custom" :id="id">
      <ul class="nav flex-column ms-3">
        <li class="nav-item" v-for="item in items" :key="item.name">
          <router-link
            :to="item.path"
            class="nav-link"
            :class="{ active: $route.path === item.path }"
          >
            {{ item.name }}
          </router-link>
        </li>
      </ul>
    </div>
  </li>
</template>

<script setup>
import { ref, watch, nextTick } from 'vue';

// eslint-disable-next-line no-unused-vars
const props = defineProps({
  title: {
    type: String,
    required: true,
  },
  id: {
    type: String,
    required: true,
  },
  items: {
    type: Array,
    required: true,
  },
});

const isOpen = ref(false);
const collapseDiv = ref(null);

const toggleOpen = () => {
  isOpen.value = !isOpen.value;
};

// 監聽 div 狀態，控制高度變化
watch(isOpen, async (newVal) => {
  await nextTick();
  if (newVal) {
    collapseDiv.value.style.height = `${collapseDiv.value.scrollHeight}px`;
  } else {
    collapseDiv.value.style.height = '0px';
  }
});
</script>

<style scoped>
.bi {
  font-size: 1.2em;
  transition: transform 0.3s ease;
}
.arrow-up {
  transform: rotate(0deg);
}
.arrow-down {
  transform: rotate(180deg);
}
.collapse-custom {
  overflow: hidden;
  transition: height 0.3s ease-in-out;
  height: 0;
}
</style>
