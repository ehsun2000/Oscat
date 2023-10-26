package com.oscat.cinema.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oscat.cinema.dto.MovieDTO;
import com.oscat.cinema.entity.Movie;

public interface IMovieService {
	
	//查詢所有電影
	List<Movie> getAllMovies();
	
	//新增單筆
	Movie saveMovie(Movie movie);
	
	//根據id查詢電影
	Movie getMovieById(UUID movieId);
	
	//根據id刪除電影
	String deleteMovie(UUID movieId);
	
	//更新電影
	Optional<Movie> updateMovie(UUID movieId, MovieDTO movieDTO);
	
	//修改電影狀態
	boolean updateStatusById(String movieStatus, UUID movieId);
}
