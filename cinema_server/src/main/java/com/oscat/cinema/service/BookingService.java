package com.oscat.cinema.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oscat.cinema.dao.CinemaRepository;
import com.oscat.cinema.dao.ShowTimeRepository;
import com.oscat.cinema.dao.TicketTypeRepository;
import com.oscat.cinema.dto.SearchCinemaForBook;
import com.oscat.cinema.dto.SearchShowDateForBook;

@Service
public class BookingService {

	private CinemaRepository cinemaRepository;
	private ShowTimeRepository showTimeRepository;
	private TicketTypeRepository ticketTypeRepository;

	@Autowired
	public BookingService(CinemaRepository cinemaRepository, 
						ShowTimeRepository showTimeRepository,
						TicketTypeRepository ticketTypeRepository) {
		this.cinemaRepository = cinemaRepository;
		this.showTimeRepository = showTimeRepository;
		this.ticketTypeRepository = ticketTypeRepository;
	}

	public List<SearchCinemaForBook> findCinemas(UUID movieId) {
		List<SearchCinemaForBook> cinemas = cinemaRepository.findCinemasByMovieId(movieId,
				LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0),
				LocalDateTime.now().plusDays(2).withHour(23).withMinute(59).withSecond(59).withNano(999999999));

		if (!cinemas.isEmpty()) {
			return cinemas;
		}
		return null;
	}

	public Set<SearchShowDateForBook> findShowDate(UUID movieId, Integer cinemaId) {
		Set<SearchShowDateForBook> showDates = showTimeRepository.findShowDateByMovieIdAndCinemaId(movieId, cinemaId,
				LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0),
				LocalDateTime.now().plusDays(2).withHour(23).withMinute(59).withSecond(59).withNano(999999999));
		return showDates;
	}

	public Map<String, List<String>> findShowTime(UUID movieId, Integer cinemaId, String showTime) {

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDateTime targetDate = LocalDate.parse(showTime, dtf).atStartOfDay();
		
		List<Object[]> result = showTimeRepository.findShowTimeByMovieIdAndCinemaIdAndDate(movieId, cinemaId,
				targetDate,	targetDate.withHour(23).withMinute(59).withSecond(59).withNano(999999999));
		
		Map<String, List<String>> roomNameAndShowtimes = new LinkedHashMap<>();
		
		for(Object[] obj : result) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			String roomName =  (String)obj[0];
			String showtime =  formatter.format((LocalDateTime)obj[1]);
			List<String> list = roomNameAndShowtimes.get(roomName);
			if(list == null) {
				roomNameAndShowtimes.put(roomName, new ArrayList<String>());
				list = roomNameAndShowtimes.get(roomName);
			}
			list.add(showtime);
		}
		
		return roomNameAndShowtimes;
	}

	public Map<Integer, String> gerTicketTypes() {
		Map<Integer, String> result = new LinkedHashMap<>();
		List<Object[]> findObj = ticketTypeRepository.findAllTypes();
		
		for(Object[] obj : findObj) {
			Integer typeId = (Integer)obj[0];
			String typeName = (String)obj[1];
			
			result.put(typeId, typeName);
		}

		return result;
	}

}
