<script setup>
import { ref, onMounted, onBeforeMount, nextTick, reactive, watch } from 'vue';
import Swal from 'sweetalert2';

const movieColors = reactive({});
const timeLineRef = ref(null);
const timeLineWidth = ref(0);
const cinemaData = ref({});
const roomId = ref(null);
const roomName = ref(null);
const cinemaName = ref(null);
const movies = ref({});
const selectedColor = ref('orange');
const today = new Date();
today.setHours(0, 0, 0, 0);
const endDay = new Date(today);
endDay.setDate(today.getDate() + 6);
const range = ref({
  start: today,
  end: endDay,
});
const showtimesData = ref([]);
const selectShowTime = reactive({});
const dates = ref([]);
const types = reactive(['2D', '3D', 'IMax', 'Atmos']);

onBeforeMount(async () => {
  try {
    // 透過 fetch 請求取得影城和影廳的資料
    const response = await fetch(
      `${import.meta.env.VITE_OSCAT_API_ENDPOINT}/showtime/findcinemas`,
      {
        method: 'GET',
        credentials: 'include',
      },
    );
    cinemaData.value = await response.json();

    // 創建一個 SweetAlert 對話框，其中包含影城和影廳的選項
    const { value: formValues } = await Swal.fire({
      title: '選擇影城和影廳',
      html:
        '<select id="swal-cinema" class="swal2-input">' +
        cinemaData.value
          .map(
            (cinema) =>
              `<option value="${cinema.cinema}">${cinema.cinema}</option>`,
          )
          .join('') +
        '</select>' +
        '<select id="swal-room" class="swal2-input">' +
        '</select>',
      focusConfirm: false,
      preConfirm: () => {
        const roomSelect = document.getElementById('swal-room');
        return {
          cinema: document.getElementById('swal-cinema').value,
          roomId: roomSelect.value,
          roomName: roomSelect.options[roomSelect.selectedIndex].text,
        };
      },
      didOpen: () => {
        // 監聽影城選擇，動態更新影廳選項
        const cinemaSelect = document.getElementById('swal-cinema');
        const roomSelect = document.getElementById('swal-room');
        cinemaSelect.addEventListener('change', () => {
          const selectedCinema = cinemaData.value.find(
            (cinema) => cinema.cinema == cinemaSelect.value,
          );
          roomSelect.innerHTML = selectedCinema.rooms
            .map(
              (room) =>
                `<option value="${room.roomId}">${room.roomName}</option>`,
            )
            .join('');
        });
        // 初始設定第一個影城的影廳
        const initialCinema = cinemaData.value[0];
        roomSelect.innerHTML = initialCinema.rooms
          .map(
            (room) =>
              `<option value="${room.roomId}">${room.roomName}</option>`,
          )
          .join('');
      },
    });

    // 設置 roomId 和影城名稱
    if (formValues) {
      roomId.value = formValues.roomId;
      cinemaName.value = formValues.cinema;
      roomName.value = formValues.roomName;

      getFirstShowTime();
    }
  } catch (error) {
    console.error('An error occurred:', error);
  }
});

onMounted(async () => {
  dates.value = generateDateArray(
    new Date().toISOString(),
    new Date(new Date().setDate(new Date().getDate() + 6)).toISOString(),
  );

  await nextTick();
  if (timeLineRef.value) {
    timeLineWidth.value = timeLineRef.value[0].offsetWidth;
  }
});
const addShowTime = async () => {
  try {
    // 1. 跳出 SweetAlert 顯示加載中
    Swal.showLoading();

    // 2. 發送電影 API 請求找出所有電影名稱及 ID
    const response = await fetch(
      `${import.meta.env.VITE_OSCAT_API_ENDPOINT}/showtime/movies`,
      { credentials: 'include', method: 'GET' },
    );

    movies.value = await response.json();
    // 3. 等待非同步請求完成
    if (response.status == 200) {
      // 隱藏加載中的 SweetAlert
      Swal.close();

      // 生成選項
      const movieOptions = movies.value
        .map((movie) => `<option value="${movie.id}">${movie.movie}</option>`)
        .join('');

      const typeOptions = types
        .map((type) => `<option value="${type}">${type}</option>`)
        .join('');

      const { value: formValues } = await Swal.fire({
        title: '新增場次',
        html: `
          <label for="movieId">電影：</label>
          <select id="movieId" class="form-select mb-2">${movieOptions}</select>
          <div class="form-group mb-2">
            <label for="showTime">時間：</label>
            <input id="showTime" type="datetime-local" class="form-control">
          </div>
          <label for="typeId">放映種類：</label>
          <select id="typeId" class="form-select">${typeOptions}</select>
        `,
        focusConfirm: false,
        preConfirm: () => {
          return {
            movieId: document.getElementById('movieId').value,
            showTime: document.getElementById('showTime').value,
            typeId: document.getElementById('typeId').value,
          };
        },
      });

      // 4. 送出前再次顯示選擇的電影、時間
      if (formValues) {
        const { movieId, showTime, typeId } = formValues;

        // 找到選擇的電影名稱
        const selectedMovie = movies.value.find(
          (movie) => movie.id === movieId,
        );
        const confirmAction = await Swal.fire({
          title: '確認新增場次',
          icon: 'warning',
          html: `
            電影: <strong>${selectedMovie.movie}</strong><br />
            時間: <strong>${showTime}</strong><br />
            放映種類: <strong>${typeId}</strong>
          `,
          showCancelButton: true,
        });

        // 5. 確認後送出 新增場次請求
        if (confirmAction.isConfirmed) {
          const add = await fetch(
            `${import.meta.env.VITE_OSCAT_API_ENDPOINT}/showtime/add`,
            {
              credentials: 'include',
              method: 'POST',
              headers: {
                'content-type': 'application/json',
              },
              body: JSON.stringify({
                movieId: movieId,
                roomId: roomId.value,
                filmType: typeId,
                showDateAndTime: showTime,
              }),
            },
          );
          if (add.status == 200) {
            Swal.fire('成功', '場次已新增', 'success');
            getFirstShowTime();
          } else {
            const message = await add.text();
            Swal.fire(
              '失敗',
              `<span class="text-light">${message}</span>`,
              'error',
            );
          }
        }
      }
    }
  } catch (error) {
    console.log(error);
    Swal.fire('錯誤', '發生錯誤，請稍後再試', 'error');
  }
};

const filterMoviesByDate = (date) => {
  return showtimesData.value.filter((movie) => {
    return (
      new Date(movie.showtime).toDateString() === new Date(date).toDateString()
    );
  });
};

const calculateMovie = (movie) => {
  const firstShowtime = movie.showtime;
  const time = new Date(firstShowtime);
  const pixelPerMinute = timeLineWidth.value / ((24 - 8) * 60);

  const left =
    ((time.getHours() - 8) * 60 + time.getMinutes()) * pixelPerMinute;

  const movieWidth = movie.duration * pixelPerMinute;

  const backgroundColor = movieColors[movie.movieName];
  return {
    left: `${left}px`,
    backgroundColor: backgroundColor,
    width: `${movieWidth}px`,
  };
};
const generateRandomColor = () => {
  const maxBrightness = 0x80;
  let color = '#';
  for (let i = 0; i < 3; i++) {
    const component = Math.floor(Math.random() * maxBrightness);
    color += component.toString(16).padStart(2, '0');
  }
  return color;
};

watch(range, async (newRange) => {
  if (newRange.start && newRange.end) {
    dates.value = generateDateArray(newRange.start, newRange.end);

    getFirstShowTime();
  }
});

const generateDateArray = (start, end) => {
  const startDate = new Date(start);
  const endDate = new Date(end);
  const dateArray = [];

  let currentDate = startDate;
  while (currentDate <= endDate) {
    dateArray.push(toISOStringWithOffset(currentDate).split('T')[0]);
    currentDate.setDate(currentDate.getDate() + 1);
  }

  return dateArray;
};

const getFirstShowTime = async () => {
  const response = await fetch(
    `${import.meta.env.VITE_OSCAT_API_ENDPOINT}/showtime/`,
    {
      credentials: 'include',
      method: 'POST',
      headers: {
        'content-type': 'application/json',
      },
      body: JSON.stringify({
        roomId: roomId.value,
        start: toISOStringWithOffset(range.value.start),
        end: toISOStringWithOffset(
          new Date(
            new Date(range.value.end).setDate(
              new Date(range.value.end).getDate() + 1,
            ),
          ),
        ),
      }),
    },
  );

  showtimesData.value = await response.json();

  await showtimesData.value.forEach((showtime) => {
    if (!movieColors[showtime.movieName]) {
      movieColors[showtime.movieName] = generateRandomColor();
    }
  });
};

const toISOStringWithOffset = (date) => {
  var newDate = new Date(date.getTime());

  newDate.setHours(newDate.getHours() + 8);

  return newDate.toISOString().split('.')[0];
};

const showtimeInfo = async (e) => {
  try {
    Swal.showLoading();

    const getShowtime = await fetch(
      `${import.meta.env.VITE_OSCAT_API_ENDPOINT}/showtime/${e.target.id}`,
      { credentials: 'include', method: 'GET' },
    );

    const getMovies = await fetch(
      `${import.meta.env.VITE_OSCAT_API_ENDPOINT}/showtime/movies`,
      { credentials: 'include', method: 'GET' },
    );
    const typeOptions = types
      .map((type) => `<option value="${type}">${type}</option>`)
      .join('');
    Object.assign(selectShowTime, await getShowtime.json());
    movies.value = await getMovies.json();

    if (getMovies.status == 200) {
      Swal.close();
      const movieOptions = movies.value
        .map(
          (movie) => `<option value="${movie.movie}">${movie.movie}</option>`,
        )
        .join('');

      const { value: formValues, dismiss } = await Swal.fire({
        title: '更新 OR 刪除場次',
        html: `
          <label for="updateMovie">電影：</label>
          <select id="updateMovie" class="form-select mb-2">${movieOptions}</select>
          <div class="form-group mb-2">
            <label for="showTime">時間：</label>
            <input id="showTime" type="datetime-local" class="form-control">
          </div>
          <label for="typeId">放映種類：</label>
          <select id="typeId" class="form-select">${typeOptions}</select>
        `,
        focusConfirm: false,
        showCancelButton: true,
        confirmButtonText: '更改',
        cancelButtonText: '刪除',
        didOpen: () => {
          document.getElementById('updateMovie').value =
            selectShowTime.movieName;
          document.getElementById('showTime').value = toISOStringWithOffset(
            new Date(selectShowTime.showtime),
          );
          document.getElementById('typeId').value = selectShowTime.flimType;
        },
        preConfirm: () => {
          return {
            movieName: document.getElementById('updateMovie').value,
            showTime: document.getElementById('showTime').value,
            type: document.getElementById('typeId').value,
          };
        },
      });

      if (formValues) {
        const { movieName, showTime, type } = formValues;

        const selectedMovie = movies.value.find(
          (movie) => movie.movie === movieName,
        );

        const confirmAction = await Swal.fire({
          title: '確認修改場次',
          icon: 'warning',
          html: `
            電影: <strong>${selectedMovie.movie}</strong><br />
            時間: <strong>${showTime}</strong><br />
            放映種類: <strong>${type}</strong>
          `,
          showCancelButton: true,
        });

        if (confirmAction.isConfirmed) {
          const update = await fetch(
            `${import.meta.env.VITE_OSCAT_API_ENDPOINT}/showtime/`,
            {
              credentials: 'include',
              method: 'PUT',
              headers: {
                'content-type': 'application/json',
              },
              body: JSON.stringify({
                showtimeId: e.target.id,
                movieId: selectedMovie.id,
                roomId: roomId.value,
                filmType: type,
                showDateAndTime: showTime,
              }),
            },
          );
          if (update.status == 200) {
            Swal.fire('成功', `${await update.text()}`, 'success');
            getFirstShowTime();
          } else {
            const message = await update.text();
            Swal.fire(
              '失敗',
              `<span class="text-light">${message}</span>`,
              'error',
            );
          }
        }
      }
      if (dismiss) {
        const selectedTime = showtimesData.value.find(
          (movie) => movie.showTimeId === e.target.id,
        );

        const confirmAction = await Swal.fire({
          title: '確認刪除?',
          icon: 'warning',
          confirmButtonText: '確認刪除',
          cancelButtonText: '取消',
          html: `
            電影: <strong>${selectedTime.movieName}</strong><br />
            時間: <strong>${selectedTime.showtime}</strong><br />
            放映種類: <strong>${selectedTime.flimType}</strong>
          `,
          showCancelButton: true,
        });

        if (confirmAction.isConfirmed) {
          const deleteShow = await fetch(
            `${import.meta.env.VITE_OSCAT_API_ENDPOINT}/showtime/delete/${
              selectedTime.showTimeId
            }`,
            {
              credentials: 'include',
              method: 'DELETE',
            },
          );

          if (deleteShow.status == 200) {
            Swal.fire('刪除成功', '資料已刪除', 'success');
            getFirstShowTime();
          } else {
            Swal.fire('刪除失敗', `${await deleteShow.text()}`, 'success');
          }
        }
      }
    }
  } catch (error) {
    console.log(error);
    Swal.fire('錯誤', '發生錯誤，請稍後再試', 'error');
  }
};
</script>
<template>
  <div class="container">
    <div class="row text-center">
      <h1 class="mt-3">場次排程</h1>
      <div class="col-md-3 justify-content-start">
        <h2>{{ cinemaName }}</h2>
        <h3>{{ roomName }}</h3>
        <button
          class="btn btn-outline-info mb-3"
          type="button"
          @click="addShowTime"
        >
          新增場次
        </button>

        <VDatePicker
          v-model.range="range"
          mode="date"
          is-dark="system"
          :color="selectedColor"
        />
      </div>
      <div class="col-md-9">
        <div class="row">
          <!-- 時間標籤 -->
          <div class="col-2">Time</div>
          <div class="col-10">
            <div class="d-flex justify-content-between">
              <span>08</span>
              <span>09</span>
              <span>10</span>
              <span>11</span>
              <span>12</span>
              <span>13</span>
              <span>14</span>
              <span>15</span>
              <span>16</span>
              <span>17</span>
              <span>18</span>
              <span>19</span>
              <span>20</span>
              <span>21</span>
              <span>22</span>
              <span>23</span>
              <span>24</span>
              <!-- 其他時間 -->
            </div>
          </div>
        </div>

        <div class="row" v-for="date in dates" :key="date">
          <div class="col-2">
            {{ date }}
          </div>
          <div class="col-10">
            <div class="time-line position-relative" ref="timeLineRef">
              <div
                v-for="movie in filterMoviesByDate(date)"
                :key="movie.showTimeId"
                :style="calculateMovie(movie)"
                :id="movie.showTimeId"
                @click="showtimeInfo"
                class="movie-block position-absolute"
              >
                {{ movie.movieName }}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<style>
.time-slots {
  position: relative;
  height: 30px;
  border-top: 1px solid #ccc;
  border-bottom: 1px solid #ccc;
}

.showtime {
  position: absolute;
  top: 0;
  height: 30px;
  background-color: #007bff;
  color: white;
  text-align: center;
  line-height: 30px;
  border-radius: 5px;
}
.time-line {
  position: relative;
  height: 30px;
  border-top: 1px solid #ccc;
  border-bottom: 1px solid #ccc;
}

.movie-block {
  height: 30px;
  line-height: 30px;
  text-align: center;
  border-radius: 20px;
  color: white;
  overflow: hidden;
  user-select: none;
}
</style>
