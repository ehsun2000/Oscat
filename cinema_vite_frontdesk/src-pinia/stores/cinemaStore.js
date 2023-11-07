import { defineStore } from 'pinia';

export const useCinemaStore = defineStore('cinema', {
  state: () => ({
    selectedShowtime: {},
  }),
  actions: {
    setSelectedShowtime(
      date,
      time,
      roomName,
      showtimeId,
      movieName,
      cinemaName,
      basePrice,
    ) {
      this.selectedShowtime = {
        date,
        time,
        roomName,
        showtimeId,
        movieName,
        cinemaName,
        basePrice,
      };
    },
    goToTicketType() {
      router.push({ name: 'TicketType' });
    },
  },
});
