package com.oscat.cinema.dao;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.oscat.cinema.entity.Seat;

@Repository
public interface SeatRepository extends JpaRepository<Seat, UUID> {

	@Query("SELECT s.seatStatus FROM Seat s WHERE s.seatName = :seatName AND s.screeningRoom.roomId = :roomId")
	String findSeatStatusBySeatNameAndRoomId(@Param("seatName") String seatName, @Param("roomId") Integer roomId);

	@Transactional
	@Modifying
	@Query("UPDATE Seat s SET s.seatStatus = :status WHERE s.seatName = :seatName AND s.screeningRoom.roomId = :roomId")
	Integer updateSeatStatusBySeatNameAndRoomId(@Param("status") String status, @Param("seatName") String seatName,
			@Param("roomId") Integer roomId);
	
    List<Seat> findByScreeningRoom_RoomIdOrderBySeatName(Integer roomId);
    
    @Query("SELECT s FROM Seat s WHERE s.screeningRoom.roomId = :roomId ORDER BY s.seatName")
    List<Seat> findSeatsByRoomIdOrderBySeatName(@Param("roomId") Integer roomId);

}
