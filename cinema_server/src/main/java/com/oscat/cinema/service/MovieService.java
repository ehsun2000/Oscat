package com.oscat.cinema.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oscat.cinema.dao.MovieRepository;
import com.oscat.cinema.entity.Movie;

@Service
public class MovieService {

	@Autowired
	private MovieRepository movRepo;

	public List<Movie> getAllMovies() {
		return movRepo.findAll();
	}

	public Movie saveMovie(Movie movie) {
		return movRepo.save(movie);
	}

	public Movie addMovie(Movie movie) {

		if (movie.getMovieName() == null || movie.getMovieName().isEmpty()) {
			throw new IllegalArgumentException("Movie name cannot be empty.");
		}

		return movRepo.save(movie);
	}

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

	public Movie getMovieByName(String movieName) {
		Optional<Movie> optional = movRepo.findBymovieName(movieName);

		Movie movie = null;
		if (optional.isPresent()) {
			movie = optional.get();
		} else {
			throw new RuntimeException("Movie not found for name ::" + movieName);
		}
		return movie;
	}

	public void deleteMovie(UUID movieId) {
		movRepo.deleteById(movieId);
	}

}
