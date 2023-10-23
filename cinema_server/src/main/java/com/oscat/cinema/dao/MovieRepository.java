package com.oscat.cinema.dao;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oscat.cinema.entity.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, UUID> {

	@Query(value = "select * from movie where name = :n", nativeQuery = true)
	List<Movie> findMovieByNameNativeQuery(@Param("n") String movieName);

	@Query(value = "from Movie where movieName like %:n%")
	List<Movie> findMovieByNameLike(@Param("n") String movieName);

	
	Optional<Movie> findBymovieName(@Param("n") String movieName);

}
