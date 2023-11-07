package com.oscat.cinema.dao;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oscat.cinema.dto.RequestShowTime;
import com.oscat.cinema.dto.SearchShowDateForBook;
import com.oscat.cinema.entity.ShowTime;

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

	@Query("SELECT st.showTimeId, sr.id, sr.roomName, st.showDateAndTime  " + "FROM ShowTime st "
			+ "JOIN st.screeningRoom sr " + "JOIN sr.cinema c "
			+ "WHERE st.movie.movieId = :movieId AND c.cinemaId = :cinemaId "
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

	@Query("SELECT s FROM ShowTime s " + "WHERE s.showDateAndTime BETWEEN :startDateTime AND :endDateTime "
			+ "AND s.screeningRoom.roomId = :roomId")
	List<ShowTime> findAfterConflictingShowTimesByRoomId(@Param("startDateTime") LocalDateTime startDateTime,
			@Param("endDateTime") LocalDateTime endDateTime, @Param("roomId") Integer roomId);

	@Query("SELECT s FROM ShowTime s WHERE s.showDateAndTime < :targetDateTime "
			+ "AND s.screeningRoom.roomId = :roomId ORDER BY s.showDateAndTime DESC LIMIT 1")
	ShowTime findLatestShowTimeBefore(@Param("targetDateTime") LocalDateTime targetDateTime,
			@Param("roomId") Integer roomId);

	@Query("SELECT s FROM ShowTime s " + "WHERE s.screeningRoom.roomId = :roomId")
	Page<ShowTime> findAllByRoomId(Pageable pageable, @Param("roomId") Integer roomId);

	@Query("SELECT s FROM ShowTime s " + "WHERE s.screeningRoom.roomId = :roomId AND "
			+ "s.showDateAndTime BETWEEN :start AND :end " + "ORDER BY s.showDateAndTime DESC")
	List<ShowTime> findByStartAndEnd(@Param("roomId") Integer roomId, @Param("start") LocalDateTime start,
			@Param("end") LocalDateTime end);

	// 找出原本場次以外，播放時間之後的第一筆資料
	@Query("SELECT s FROM ShowTime s " + "WHERE s.screeningRoom.roomId = :roomId AND " + "s.showTimeId != :oldId AND "
			+ "s.showDateAndTime >= :start " + "ORDER BY s.showDateAndTime ASC " + "LIMIT 1")
	ShowTime findAfterShow(@Param("roomId") Integer roomId, @Param("oldId") UUID oldId,
			@Param("start") LocalDateTime localDateTime);

	// 找出原本場次以外，之前的最後一筆資料
	@Query("SELECT s FROM ShowTime s " + "WHERE s.screeningRoom.roomId = :roomId AND " + "s.showTimeId != :oldId AND "
			+ "s.showDateAndTime < :start " + "ORDER BY s.showDateAndTime DESC " + "LIMIT 1")
	ShowTime findBeforeShow(@Param("roomId") Integer roomId, @Param("oldId") UUID oldId,
			@Param("start") LocalDateTime localDateTime);
}
