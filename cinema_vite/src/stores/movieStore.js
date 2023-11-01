import { defineStore } from 'pinia';
import axios from 'axios';

const api = import.meta.env.VITE_OSCAT_API_ENDPOINT;

export const useMovieStore = defineStore({
  id: 'movie',
  state: () => ({
    ticketTypes: [],
    ticketCounts: {},
    error: null,
    cinemaName: '',
    movieName: '',
    screenRoomName: '',
    basicPrice: 0,
    showtimeId: '',
    filmType: '',
    showDateAndTime: '',
    extraFee: 0,
  }),
  actions: {
    increaseCount(typeId) {
      if (this.totalTicketCount < 10) {
        this.ticketCounts[typeId]++;
      }
    },
    decreaseCount(typeId) {
      if (this.ticketCounts[typeId] && this.ticketCounts[typeId] > 0) {
        this.ticketCounts[typeId]--;
      }
    },
    setBasicInfo(routeQuery) {
      this.cinemaName = routeQuery.cinemaName;
      this.movieName = routeQuery.movieName;
      this.screenRoomName = routeQuery.screenRoomName;
      this.basicPrice = parseInt(routeQuery.basicPrice || '0');
      this.showtimeId = routeQuery.showtimeId;
    },
    async fetchTicketTypes() {
      try {
        const response = await axios.get(`${api}/book/ticketTypes`);
        this.ticketTypes = response.data;

        this.ticketCounts = this.ticketTypes.reduce((acc, ticket) => {
          acc[ticket.typeId] = 0;
          return acc;
        }, {});
      } catch (error) {
        this.error = '無法獲取票種類型';
        console.error('Error fetching ticket types:', error);
      }
    },
    async fetchShowTimeDetails() {
      try {
        const response = await axios.get(
          `${api}/showtime/find/${this.showtimeId}`,
        );
        this.filmType = response.data.filmType;
        this.showDateAndTime = response.data.showDateAndTime;
        this.extraFee = Math.round(parseFloat(response.data.extraFee) * 100);
      } catch (error) {
        console.error('Error fetching showtime details:', error);
      }
    },
  },
  getters: {
    totalTicketCount() {
      return Object.values(this.ticketCounts).reduce(
        (acc, count) => acc + count,
        0,
      );
    },
    calculateTotalPrice() {
      return this.ticketTypes.reduce((acc, ticket) => {
        const priceForTicketType =
          this.basicPrice + this.extraFee + ticket.price;
        return (
          acc + priceForTicketType * (this.ticketCounts[ticket.typeId] || 0)
        );
      }, 0);
    },
  },
});
