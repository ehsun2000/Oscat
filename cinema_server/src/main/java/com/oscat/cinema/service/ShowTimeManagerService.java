package com.oscat.cinema.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oscat.cinema.dao.MovieRepository;
import com.oscat.cinema.dao.ShowTimeRepository;
import com.oscat.cinema.dto.ShowCreationRequestDTO;
import com.oscat.cinema.entity.Movie;

@Service
public class ShowTimeManagerService {

	private ShowTimeRepository showTimeRepository;
	private MovieRepository movieRepository;

	@Autowired
	public ShowTimeManagerService(ShowTimeRepository showTimeRepository, MovieRepository movieRepository) {
		this.showTimeRepository = showTimeRepository;
		this.movieRepository = movieRepository;
	}

	public List<LocalDateTime> addShow(ShowCreationRequestDTO showTimeDTO) {
		
		Movie movie = movieRepository.findById(showTimeDTO.getMovieId()).get();
		
		List<Timestamp> showTimes = showTimeRepository.addShows(
				showTimeDTO.getMovieId(), 
				showTimeDTO.getRoomId(), 
				showTimeDTO.getStartTime(), 
				showTimeDTO.getFlimType(), 
				showTimeDTO.getPrice(),
				showTimeDTO.getNumberOfShowsToAdd(), 
				movie.getDuration());
		List<LocalDateTime> showDateTimes = new ArrayList<>();
		
		showTimes.forEach(showtime->showDateTimes.add(showtime.toLocalDateTime()));
		
		return showDateTimes;
	}

}
