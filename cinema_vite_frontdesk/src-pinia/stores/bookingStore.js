import { defineStore } from 'pinia';

export const useBookingStore = defineStore('booking', {
  state: () => ({
    selectedMovie: null,
    selectedCinema: null,
    selectedSeats: [],
    selectedTicketTypes: [],
    totalPrice: 0,
  }),
  actions: {
    setMovie(movie) {
      this.selectedMovie = movie;
    },
    setCinema(cinema) {
      this.selectedCinema = cinema;
    },
    setSeats(seats) {
      this.selectedSeats = seats;
    },
    setTicketTypes(ticketTypes) {
      this.selectedTicketTypes = ticketTypes;
    },
    setTotalPrice(price) {
      this.totalPrice = price;
    },
  },
});
