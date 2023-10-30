package com.oscat.cinema.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oscat.cinema.dto.MovieDTO;
import com.oscat.cinema.dto.SearchCinemaForBook;
import com.oscat.cinema.dto.SearchShowDateForBook;
import com.oscat.cinema.dto.TicketTypeDTO;
import com.oscat.cinema.service.BookingService;
import com.oscat.cinema.service.MovieService;

@RestController
@RequestMapping(path = "/api/book")
public class BookingController {

	@Autowired
	private BookingService bookingService;
	@Autowired
	private MovieService movService;

	@GetMapping(path = "/{movieId}")
	public List<SearchCinemaForBook> findCinemas(@PathVariable(name = "movieId") UUID movieId) {
		return bookingService.findCinemas(movieId);
	}
	
	@GetMapping(path = "/showing")
	public List<MovieDTO> findShowingMovies() {
		return movService.getMovieShowing();
	}
	
	@GetMapping(path = "/{movieId}/{cinemaId}")
	public Set<SearchShowDateForBook> findShowDates(@PathVariable(name = "movieId") UUID movieId, 
						@PathVariable(name = "cinemaId")Integer cinemaId){
		System.out.println(cinemaId);
		return bookingService.findShowDate(movieId,cinemaId);
	}
	
	@GetMapping(path = "/{movieId}/{cinemaId}/findtime")
	public List<Map<String, Object>> findShowTimes(@PathVariable(name = "movieId") UUID movieId,
	        @PathVariable(name = "cinemaId") Integer cinemaId) {
	    return bookingService.findShowTime(movieId, cinemaId);
	}
	
	@GetMapping(path = "/ticketTypes")
	public List<TicketTypeDTO> gerTicketTypes(){
		return bookingService.getTicketTypesList();
	}
	
	
}
