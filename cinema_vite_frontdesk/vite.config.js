import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue()],
  // 讓 vite 專案知道 @/ == src/
  resolve: {
    alias: {
      '@': './src/',
    },
  },
  server: {
    port: 8082,
  },
});
