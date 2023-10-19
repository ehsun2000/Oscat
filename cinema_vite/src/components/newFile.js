import axios from 'axios';

export default (await import('vue')).defineComponent({
  data() {
    return {
      newShowtime: {
        roomId: '',
        filmType: '',
        price: 0,
        extrafee: 0,
        showDateAndTime: '',
        movieId: null,
      },
      movies: [],
      showSuccessModal: false, // 控制模态框显示
    };
  },
  created() {
    // 获取电影列表
    axios
      .get(`${import.meta.env.VITE_API_OSCATURL}movies`)
      .then((response) => {
        this.movies = response.data;
      })
      .catch((error) => {
        console.error('Error fetching movies:', error);
      });
  },
  methods: {
    addShowtime() {
      // 在这里执行添加场次的逻辑，访问 newShowtime 变量来获取所有属性的值
      axios
        .post(
          `${import.meta.env.VITE_API_OSCATURL}showtime/manager/add`,
          this.newShowtime,
        )
        .then((response) => {
          console.log(response);
          // 添加成功后的处理
          this.showSuccessModal = true;
        })
        .catch((error) => {
          console.error('Error adding showtime:', error);
        });
    },
  },
});
