import { defineStore } from 'pinia';
import axios from 'axios';

export const useTicketStore = defineStore('ticketStore', {
  state: () => ({
    ticketTypes: [],
    ticketCounts: {},
    basicPrice: 0,
    showtimeId: null,
    filmType: '',
    showDateAndTime: '',
    extraFee: 0,
  }),
  actions: {
    async fetchTicketTypes() {
      const api = import.meta.env.VITE_OSCAT_API_ENDPOINT;
      try {
        const response = await axios.get(`${api}/book/ticketTypes`);
        console.log('Ticket Types Data:', response.data);
        this.ticketTypes = response.data;

        this.ticketTypes.forEach((ticket) => {
          this.ticketCounts[ticket.typeId] = 0;
        });
      } catch (error) {
        console.error('Error fetching ticket types:', error);
      }
    },

    async fetchShowTimeDetails() {
      const api = import.meta.env.VITE_OSCAT_API_ENDPOINT;
      try {
        const response = await axios.get(
          `${api}/showtime/find/${this.showtimeId}`,
        );
        console.log('Show Time Details Data:', response.data);
        this.filmType = response.data.filmType;
        this.showDateAndTime = response.data.showDateAndTime;
        this.extraFee = Math.round(parseFloat(response.data.extraFee) * 100);
      } catch (error) {
        console.error('Error fetching showtime details:', error);
      }
    },

    increaseCount(typeId) {
      if (this.totalTicketCount() < 10) {
        this.ticketCounts[typeId]++;
      }
    },

    decreaseCount(typeId) {
      if (this.ticketCounts[typeId] && this.ticketCounts[typeId] > 0) {
        this.ticketCounts[typeId]--;
      }
    },

    calculatePrice(base, extra, price) {
      return parseInt(base) + parseInt(extra) + price;
    },

    calculateTotalPrice() {
      return this.ticketTypes.reduce((acc, ticket) => {
        const priceForTicketType = this.calculatePrice(
          this.basicPrice,
          this.extraFee,
          ticket.price,
        );
        return (
          acc + priceForTicketType * (this.ticketCounts[ticket.typeId] || 0)
        );
      }, 0);
    },

    totalTicketCount() {
      return Object.values(this.ticketCounts).reduce(
        (acc, count) => acc + count,
        0,
      );
    },
  },
});
