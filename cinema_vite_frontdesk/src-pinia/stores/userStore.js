import { defineStore } from 'pinia';

export const useUserStore = defineStore('user', {
  state: () => ({
    isAuthenticated: false,
    redirectAfterLogin: null,
  }),
  actions: {
    setAuthenticated(status) {
      this.isAuthenticated = status;
    },
    setRedirectAfterLogin(path) {
      this.redirectAfterLogin = path;
    },
  },
});
