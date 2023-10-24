<template>
  <li class="nav-item pe-auto">
    <a
      class="nav-link d-flex justify-content-around align-items-center text-decoration-none text-light"
      @click="toggleOpen"
    >
      <!-- 加入 Bootstrap icon -->
      <i :class="['bi', icon, 'me-2']"></i>
      <span>{{ title }}</span>
      <i
        :class="[
          'bi',
          'bi-arrow-up-short',
          isOpen ? 'arrow-up' : 'arrow-down',
          'align-content-end',
        ]"
      ></i>
    </a>
    <div ref="collapseDiv" class="collapse-custom" :id="id">
      <ul class="nav flex-column ms-3">
        <li
          class="nav-item d-flex justify-content-around"
          v-for="item in items"
          :key="item.name"
        >
          <router-link
            :to="item.path"
            class="nav-link text-decoration-none text-light pe-auto"
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
  icon: {
    type: String,
    required: true,
  },
});

const isOpen = ref(false);
const collapseDiv = ref(null);

const toggleOpen = () => {
  isOpen.value = !isOpen.value;
};

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
