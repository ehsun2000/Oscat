package com.oscat.cinema.dao;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oscat.cinema.dto.SearchShowDateForBook;
import com.oscat.cinema.dto.ShowCreationRequestDTO;
import com.oscat.cinema.entity.Movie;
import com.oscat.cinema.entity.ScreeningRoom;
import com.oscat.cinema.entity.ShowTime;
import com.oscat.cinema.entity.TransOrder;

@Repository
public interface ShowTimeRepository extends JpaRepository<ShowTime, UUID> {

	@Query("SELECT DISTINCT NEW com.oscat.cinema.dto.SearchShowDateForBook(st.showDateAndTime) " + "FROM ShowTime st "
			+ "JOIN st.screeningRoom sr " + "JOIN sr.cinema c "
			+ "WHERE st.movie.movieId = :movieId AND c.cinemaId = :cinemaId "
			+ "AND st.showDateAndTime >= :startDate AND st.showDateAndTime <= :endDate "
			+ "ORDER BY st.showDateAndTime ASC")

	Set<SearchShowDateForBook> findShowDateByMovieIdAndCinemaId(@Param("movieId") UUID movieId,
			@Param("cinemaId") Integer cinemaId, @Param("startDate") LocalDateTime startDate,
			@Param("endDate") LocalDateTime endDate);

	@Query("SELECT sr.roomName, st.showDateAndTime " + "FROM ShowTime st " + "JOIN st.screeningRoom sr "
			+ "JOIN sr.cinema c " + "WHERE st.movie.movieId = :movieId AND c.cinemaId = :cinemaId "
			+ "AND st.showDateAndTime >= :startDate AND st.showDateAndTime <= :endDate "
			+ "ORDER BY st.showDateAndTime, sr.roomName")
	List<Object[]> findShowTimeByMovieIdAndCinemaIdAndDate(@Param("movieId") UUID movieId,
			@Param("cinemaId") Integer cinemaId, @Param("startDate") LocalDateTime startDate,
			@Param("endDate") LocalDateTime endDate);

	@Query(value = "EXEC CreateShowTimes " + ":MovieId, :RoomId, :StartTime, "
			+ ":FilmType, :Price, :NumberOfShowsToAdd, " + ":Interval", nativeQuery = true)
	List<Timestamp> addShows(@Param("MovieId") UUID movieId, @Param("RoomId") Integer roomId,
			@Param("StartTime") LocalDateTime startTime, @Param("FilmType") String filmType,
			@Param("Price") Double price, @Param("NumberOfShowsToAdd") Integer numberOfShowsToAdd,
			@Param("Interval") Integer interval);

	@Procedure(name = "CreateShowTimes")
	List<Timestamp> addShows(Movie movie, UUID showTimeId, String filmType, BigDecimal extraFee,
			LocalDateTime showDateAndTime, ScreeningRoom screeningRoom, List<TransOrder> transOrders, int price,
			int duration);

//	@Query("form")
//	List<ShowCreationRequestDTO> findAllDto();

}
