import { defineStore } from 'pinia';

export const useBookingStore = defineStore('booking', {
  state: () => ({
    selectedMovie: null,
    selectedCinema: null,
    selectedShowTime: null,
    selectedSeats: [],
    selectedTicketTypes: [],
    totalPrice: 0,
    ticketCounts: {},
  }),
  actions: {
    setMovie(movie) {
      this.selectedMovie = movie;
    },
    setCinema(cinema) {
      this.selectedCinema = cinema;
    },
    setShowTime(showTime) {
      this.selectedShowTime = showTime;
    },
    setSeats(seats) {
      this.selectedSeats = seats;
    },
    setTicketTypes(ticketTypes) {
      this.selectedTicketTypes = ticketTypes;
    },
    setTicketCount(typeId, count) {
      this.ticketCounts[typeId] = count;
    },
    setTotalPrice(price) {
      this.totalPrice = price;
    },
    resetBooking() {
      this.selectedMovie = null;
      this.selectedCinema = null;
      this.selectedShowTime = null;
      this.selectedSeats = [];
      this.selectedTicketTypes = [];
      this.totalPrice = 0;
      this.ticketCounts = {};
    },
  },
});
