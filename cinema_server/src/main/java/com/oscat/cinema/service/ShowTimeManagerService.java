package com.oscat.cinema.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oscat.cinema.dao.MovieRepository;
import com.oscat.cinema.dao.ShowTimeRepository;
import com.oscat.cinema.dto.ShowCreationRequestDTO;
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

//	@Transactional
//	public ShowTime addShow(String movieId, ShowTime st) {
//		// 使用指定的 movieId 查找相應的 Movie 對象
//		Movie selectedMovie = movieRepository.findByMovieId(UUID.fromString(movieId));
//
//		if (selectedMovie != null) {
//			// 创建新的 ShowTime 对象并设置相应的属性
//			ShowTime newShowTime = new ShowTime();
//			newShowTime.setMovie(selectedMovie); // 設置 Movie 屬性
//			newShowTime.setFilmType(st.getFilmType());
//			newShowTime.setExtraFee(st.getExtraFee());
//			newShowTime.setShowDateAndTime(st.getShowDateAndTime());
//			newShowTime.setScreeningRoom(st.getScreeningRoom());
//			newShowTime.setTransOrders(st.getTransOrders());
//			newShowTime.setPrice(st.getPrice());
//
//			// 执行插入操作
//			showTimeRepository.save(newShowTime);
//
//			return newShowTime;
//		}
//		return null;
//	}
	public ShowTime addShowTime(Map<String, Object> requestData) {
		ShowTime showTime = new ShowTime();
		showTime.setFilmType((String) requestData.get("filmType"));
		showTime.setPrice((int) requestData.get("price"));
		showTime.setShowDateAndTime(LocalDateTime.parse((String) requestData.get("showDateAndTime")));

		// 获取电影对象并关联到场次
		String movieId = (String) requestData.get("movieId");
		System.out.println(movieId);
		Movie movie = movieRepository.findById(UUID.fromString(movieId)).orElse(null);
		showTime.setMovie(movie);

		// 保存ShowTime对象到数据库
		return showTimeRepository.save(showTime);
	}

	public List<ShowTime> findAll() {
		// 調用相應的 ShowTimeRepository 方法來查找所有 show times
		return showTimeRepository.findAll();
	}
//
//	public List<ShowCreationRequestDTO> findAllDto() {
//		return showTimeRepository.findAllDto();
//
//	}

	public Optional<ShowTime> findShowTimeById(UUID id) {
		// 調用相應的 ShowTimeRepository 方法來查找特定 ID 的 show time
		return showTimeRepository.findById(id);
	}
}
