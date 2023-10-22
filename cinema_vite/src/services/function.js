import { ref } from 'vue';
import axios from 'axios';

export const movies = ref([]); //export資料

export async function fetchMovies() {
  const url = `${import.meta.env.VITE_OSCAT_API_ENDPOINT}/movie/`;
  try {
    const response = await axios.get(url);
    movies.value = response.data;
  } catch (error) {
    console.error('無法獲取電影數據：', error);
  }
}
