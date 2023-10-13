package com.oscat.cinema.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.oscat.cinema.dto.SearchCinemaForBook;
import com.oscat.cinema.dto.SearchShowDateForBook;
import com.oscat.cinema.service.BookingService;

@RestController
@RequestMapping(path = "/book")
public class BookingController {

	@Autowired
	private BookingService bookingService;

	@GetMapping(path = "/{movieId}")
	public List<SearchCinemaForBook> findCinemas(@PathVariable(name = "movieId") UUID movieId) {

		return bookingService.findCinemas(movieId);
	}
	
	@GetMapping(path = "/{movieId}/{cinemaId}")
	public Set<SearchShowDateForBook> findShowDates(@PathVariable(name = "movieId") UUID movieId, 
						@PathVariable(name = "cinemaId")Integer cinemaId){
		System.out.println(cinemaId);
		return bookingService.findShowDate(movieId,cinemaId);
	}
	
	@GetMapping(path = "/{movieId}/{cinemaId}/findtime")
	public Map<String, List<String>> findShowTimes(@PathVariable(name = "movieId") UUID movieId,
			@PathVariable(name = "cinemaId") Integer cinemaId,
			@RequestParam(name = "selectDate") String showTime){
		return bookingService.findShowTime(movieId,cinemaId,showTime);
	}
	
	@GetMapping(path = "/ticketTypes")
	public Map<Integer, String> gerTicketTypes(){
		return bookingService.gerTicketTypes();
	}
}
