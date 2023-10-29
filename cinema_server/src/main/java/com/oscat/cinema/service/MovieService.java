package com.oscat.cinema.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oscat.cinema.dao.MovieRepository;
import com.oscat.cinema.dto.MovieDTO;
import com.oscat.cinema.dto.MovieStillsDTO;
import com.oscat.cinema.entity.Movie;
import com.oscat.cinema.entity.MovieStills;
import com.oscat.cinema.entity.ShowTime;

@Service
public class MovieService {

	@Autowired
	private MovieRepository movRepo;

	// 查詢所有電影
	public List<Movie> getAllMovies() {
		return movRepo.findAll();
	}

	// 新增電影
	public Movie saveMovie(Movie movie) {
		Movie addMovie = new Movie();
		addMovie.setMovieName(movie.getMovieName());
		addMovie.setMovieType(movie.getMovieType());
		addMovie.setMovieStatus(movie.getMovieStatus());
		addMovie.setDirector(movie.getDirector());
		addMovie.setWriterList(movie.getWriterList());
		addMovie.setActorList(movie.getActorList());
		addMovie.setPlotSummary(movie.getPlotSummary());
		addMovie.setReleaseDate(movie.getReleaseDate());
		addMovie.setDuration(movie.getDuration());
		addMovie.setClassification(movie.getClassification());
		addMovie.setTrailerLink(movie.getTrailerLink());
		addMovie.setPosterImage(movie.getPosterImage());

		List<MovieStills> movieStillsList = new ArrayList<>();
		for (MovieStills movieStill : movie.getMovieStills()) {
			MovieStills newMovieStill = new MovieStills();
			newMovieStill.setStillImageUrl(movieStill.getStillImageUrl());
			newMovieStill.setMovie(addMovie);
			movieStillsList.add(newMovieStill);
		}
		addMovie.setMovieStills(movieStillsList);
		return movRepo.save(addMovie);
	}

	// 根據 id 查詢所有電影
	public Movie getMovieById(UUID movieId) {
		Optional<Movie> optional = movRepo.findById(movieId);
		Movie movie = null;
		if (optional.isPresent()) {
			movie = optional.get();
		} else {
			throw new RuntimeException("Movie not found for id ::" + movieId);
		}
		return movie;
	}

	// 根據名字查詢電影
	public Movie getMovieByName(String movieName) {
		Optional<Movie> optional = movRepo.findBymovieName(movieName);

		Movie movie = null;
		if (optional.isPresent()) {
			movie = optional.get();
		} else {
			throw new RuntimeException("Movie not found for name :" + movieName);
		}
		return movie;
	}

	// 根據 id 刪除電影
	public String deleteMovie(UUID movieId) {
		Optional<Movie> optional = movRepo.findById(movieId);
		if(optional.isPresent()) {
			movRepo.deleteById(movieId);
			return "刪除成功";
		}	
		return "沒有這筆資料";
	}

	// 修改電影
	public Optional<Movie> updateMovie(UUID movieId, MovieDTO movieDTO) {
	    Optional<Movie> optional = movRepo.findById(movieId);
	    if (optional.isPresent()) {
	        Movie movieData = optional.get();
	        movieData.setMovieName(movieDTO.getMovieName());
	        movieData.setMovieType(movieDTO.getMovieType());
	        movieData.setMovieStatus(movieDTO.getMovieStatus());
	        movieData.setDirector(movieDTO.getDirector());
	        movieData.setWriterList(movieDTO.getWriterList());
	        movieData.setActorList(movieDTO.getActorList());
	        movieData.setPlotSummary(movieDTO.getPlotSummary());
	        movieData.setReleaseDate(movieDTO.getReleaseDate());
	        movieData.setDuration(movieDTO.getDuration());
	        movieData.setClassification(movieDTO.getClassification());
	        movieData.setTrailerLink(movieDTO.getTrailerLink());
	        movieData.setPosterImage(movieDTO.getPosterImage());

	        // 更新 MovieStills 列表
	        List<MovieStills> movieStills = movieData.getMovieStills();
	        movieStills.clear(); // 清除現有的 MovieStills
	        
	        for (MovieStillsDTO stillDTO : movieDTO.getMovieStills()) {
	            MovieStills still = new MovieStills();
	            still.setStillImageUrl(stillDTO.getStillImageUrl());
	            still.setMovie(movieData);
	            movieStills.add(still);
	        }

	        return Optional.of(movRepo.save(movieData));
	    }
	    return Optional.empty();
	}
	
	// 修改電影狀態
	public boolean updateStatusById(String movieStatus, UUID movieId) {
	 	Integer result = movRepo.updateStatuseById(movieStatus, movieId);
	 	return result > 0;
	}
	
	// 轉換moviedto by ann (未完成)
	public MovieDTO convertToDTO(Movie movie) {
		MovieDTO dto = new MovieDTO();
		dto.setMovieId(movie.getMovieId());
		dto.setMovieName(movie.getMovieName());
		dto.setMovieType(movie.getMovieType());
		dto.setMovieStatus(movie.getMovieStatus());
		dto.setDirector(movie.getDirector());
		dto.setWriterList(movie.getWriterList());
		dto.setActorList(movie.getActorList());
		dto.setPlotSummary(movie.getPlotSummary());
		dto.setReleaseDate(movie.getReleaseDate());
		dto.setDuration(movie.getDuration());
		dto.setClassification(movie.getClassification());
		dto.setTrailerLink(movie.getTrailerLink());
		dto.setPosterImage(movie.getPosterImage());
		return dto;
	}
	
	// 查詢所有上映中的movie by ann
	public List<MovieDTO> getMovieShowing(){
		List<Movie> movies=movRepo.findMovieShowing();
		return movies.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

}