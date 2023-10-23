package com.oscat.cinema.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oscat.cinema.dao.MovieRepository;
import com.oscat.cinema.dao.ScreeningRoomRepository;
import com.oscat.cinema.dao.ShowTimeRepository;
import com.oscat.cinema.dto.ShowTimeDTO;
import com.oscat.cinema.entity.Movie;
import com.oscat.cinema.entity.ScreeningRoom;
import com.oscat.cinema.entity.ShowTime;

@Service
public class ShowTimeManagerService {

	@Autowired
	private ShowTimeRepository showTimeRepository;
	@Autowired
	private MovieRepository movieRepository;
	@Autowired
	private ScreeningRoomRepository roomRepo;

	@Autowired
	public ShowTimeManagerService(ShowTimeRepository showTimeRepository, MovieRepository movieRepository) {
		this.showTimeRepository = showTimeRepository;
		this.movieRepository = movieRepository;
	}

	// 新增場次
	public ShowTime addShowTime(ShowTime showTime) {
		return showTimeRepository.save(showTime);
//		ShowTime showTime = new ShowTime();
//		showTime.setFilmType((String) stDTO.get);
//		showTime.setShowDateAndTime(LocalDateTime.parse((String) stDTO.get("showDateAndTime")));
//
//		String movieId = (String) stDTO.get("movieId");
//
//		Movie movie = movieRepository.findById(UUID.fromString(movieId)).orElse(null);
//		showTime.setMovie(movie);

	}

	public List<ShowTime> findAll() {
		// 調用相應的 ShowTimeRepository 方法來查找所有 show times
		return showTimeRepository.findAll();
	}

//  透過ID找場次
	public Optional<ShowTime> findShowTimeById(UUID id) {
		// 調用相應的 ShowTimeRepository 方法來查找特定 ID 的 show time
		return showTimeRepository.findById(id);
	}

	public Optional<ShowTime> convertToEntity(ShowTimeDTO showTimeDTO) {
		ShowTime showTime = new ShowTime();
		showTime.setFilmType(showTimeDTO.getFilmType());
		showTime.setExtraFee(showTimeDTO.getExtraFee());
		showTime.setShowDateAndTime(showTimeDTO.getShowDateAndTime());
		Optional<Movie> movie = movieRepository.findById(showTimeDTO.getMovieId());
		System.out.println(showTimeDTO.getMovieId());
		Optional<ScreeningRoom> room = roomRepo.findById(showTimeDTO.getRoomId());
		System.out.println(showTimeDTO.getRoomId());
		if (movie.isPresent() && room.isPresent()) {
			showTime.setMovie(movie.get());
			showTime.setScreeningRoom(room.get());
			showTime.setTransOrders(Collections.emptyList());
			return Optional.of(showTime);
		} else {
			return Optional.empty();
		}
	}
}
