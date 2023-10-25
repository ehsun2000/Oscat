package com.oscat.cinema.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oscat.cinema.entity.ScreeningRoom;

@Repository
public interface ScreeningRoomRepository extends JpaRepository<ScreeningRoom, Integer> {
	
	@Query("SELECT s FROM ScreeningRoom s WHERE s.cinema.cinemaId = :cinemaId ORDER BY s.roomId")
	List<ScreeningRoom> findScreeningRoomByCinemaIdOrderById(@Param("cinemaId") Integer cinemaId);
}
