package com.oscat.cinema.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.oscat.cinema.dao.MovieRepository;
import com.oscat.cinema.dto.CinemaDTO;
import com.oscat.cinema.dto.MovieDTO;
import com.oscat.cinema.dto.ScreeningRoomDTO;
import com.oscat.cinema.dto.SeatDTO;
import com.oscat.cinema.dto.TicketTypeDTO;
import com.oscat.cinema.entity.Movie;
import com.oscat.cinema.entity.ShowTime;
import com.oscat.cinema.service.BookingService;
import com.oscat.cinema.service.MovieService;
import com.oscat.cinema.service.ScreeningRoomService;
import com.oscat.cinema.service.SeatService;
import com.oscat.cinema.service.ShowTimeManagerService;
import com.oscat.cinema.service.impl.CinameInfoService;

@RestController
@RequestMapping(path = "/api/offical")
public class OfficalBooking {
	
	@Autowired
	private CinameInfoService infoService;
	@Autowired
	private MovieRepository movRepo;
	@Autowired
	private MovieService movieService;
	@Autowired
	private BookingService bookingService;
	@Autowired
	private ShowTimeManagerService stmService;
	@Autowired
	private SeatService seatService;
	@Autowired
	private ScreeningRoomService srService;
	
	@GetMapping("/allCinema")
	public ResponseEntity<List<CinemaDTO>> getCinemaIdAndName() {
	    List<CinemaDTO> cinemas = infoService.getAllCinemas();
	    return ResponseEntity.ok(cinemas);
	}
	
	@GetMapping("/allScreeningRoom")
	public List<ScreeningRoomDTO> getAllScreeningRoomRoomId(@RequestParam Integer id) {
		return srService.getAllScreeningRoomById(id);
	}
	
	@GetMapping("/showing")
	public List<MovieDTO> getMovieShowing(){
		return movieService.getMovieShowing();
	}
	
	@GetMapping("/{movieId}")
	public ResponseEntity<?> findById(@PathVariable UUID movieId) {
		Optional<Movie> optional = movRepo.findById(movieId);

		if (optional.isPresent()) {
			Movie movie = optional.get();
			return new ResponseEntity<Movie>(movie, null, HttpStatus.OK);
		}
		return new ResponseEntity<String>("沒有這筆資料", null, HttpStatus.BAD_REQUEST);
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
	
	@GetMapping("/find/{showTimeId}")
	public Optional<ShowTime> findShowTimeById(@PathVariable UUID showTimeId) {
		return stmService.findShowTimeById(showTimeId);
	}
	
	@GetMapping("/findAllSeatByRoomId")
	public List<SeatDTO> getSeatsByRoomId(@RequestParam Integer roomId) {
		return seatService.getAllSeatsByRoomIdSortedByName(roomId);
	}

}
