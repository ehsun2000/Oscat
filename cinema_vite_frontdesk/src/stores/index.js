import { defineStore } from 'pinia';

export const useUserStore = defineStore('counter', {
  id: 'user',
  state: () => ({
    isLoggedIn: false,
  }),
  actions: {
    login() {
      // 更新狀態
      this.isLoggedIn = true;
    },
    logout() {
      // 更新狀態
      this.isLoggedIn = false;
    },
  },
});
