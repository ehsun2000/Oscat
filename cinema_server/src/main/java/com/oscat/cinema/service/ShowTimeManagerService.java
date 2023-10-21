package com.oscat.cinema.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oscat.cinema.dao.MovieRepository;
import com.oscat.cinema.dao.ShowTimeRepository;
import com.oscat.cinema.entity.Movie;
import com.oscat.cinema.entity.ShowTime;

@Service
public class ShowTimeManagerService {

	private ShowTimeRepository showTimeRepository;
	private MovieRepository movieRepository;

	@Autowired
	public ShowTimeManagerService(ShowTimeRepository showTimeRepository, MovieRepository movieRepository) {
		this.showTimeRepository = showTimeRepository;
		this.movieRepository = movieRepository;
	}

	// 新增場次
	public ShowTime addShowTime(Map<String, Object> requestData) {
		ShowTime showTime = new ShowTime();
		showTime.setFilmType((String) requestData.get("filmType"));
		showTime.setShowDateAndTime(LocalDateTime.parse((String) requestData.get("showDateAndTime")));

		String movieId = (String) requestData.get("movieId");

		Movie movie = movieRepository.findById(UUID.fromString(movieId)).orElse(null);
		showTime.setMovie(movie);
		
		return showTimeRepository.save(showTime);
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
}
