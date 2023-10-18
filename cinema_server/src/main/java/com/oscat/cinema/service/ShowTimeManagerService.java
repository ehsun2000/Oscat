package com.oscat.cinema.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

	 @Transactional
	    public ShowTime addShow(ShowTime st) {
	        List<Movie> movies = movieRepository.findAll();

	        if (!movies.isEmpty()) {
	            Movie selectedMovie = movies.get(2); // 假設選擇第一個電影

	            // 创建新的 ShowTime 对象并设置相应的属性
	            ShowTime newShowTime = new ShowTime();
	            newShowTime.setMovie(selectedMovie);
	            newShowTime.setFilmType(st.getFilmType());
	            newShowTime.setExtraFee(st.getExtraFee());
	            newShowTime.setShowDateAndTime(st.getShowDateAndTime());
	            newShowTime.setScreeningRoom(st.getScreeningRoom());
	            newShowTime.setTransOrders(st.getTransOrders());
	            newShowTime.setPrice(st.getPrice());

	            // 执行插入操作
	            showTimeRepository.save(newShowTime);

	            return newShowTime;
	        }
	        return null;
	    }

	public List<ShowTime> findAll() {
		// 調用相應的 ShowTimeRepository 方法來查找所有 show times
		return showTimeRepository.findAll();
	}

	public Optional<ShowTime> findShowTimeById(UUID id) {
		// 調用相應的 ShowTimeRepository 方法來查找特定 ID 的 show time
		return showTimeRepository.findById(id);
	}
}
