import { ref } from 'vue';
import axios from 'axios';

export const movies = ref([]); // export資料
export const totalPages = ref(0); // export總頁數

// export async function fetchMovies() {
//   const url = `${import.meta.env.VITE_OSCAT_API_ENDPOINT}/movie/`;
//   try {
//     const response = await axios.get(url);
//     movies.value = response.data;
//   } catch (error) {
//     console.error('無法獲取電影數據：', error);
//   }
// }

export async function loadMovies(page) {
  const url = `${
    import.meta.env.VITE_OSCAT_API_ENDPOINT
  }/movie/page?page=${page}&size=8&sort=releaseDate`;
  try {
    const response = await axios.get(url);
    movies.value = response.data.content;
    totalPages.value = response.data.totalPages; // 設定總頁數
  } catch (error) {
    console.error('無法獲取電影數據：', error);
  }
}
