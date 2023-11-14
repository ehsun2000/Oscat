package com.oscat.cinema.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oscat.cinema.dao.SeatRepository;
import com.oscat.cinema.dto.SeatDTO;
import com.oscat.cinema.entity.ScreeningRoom;
import com.oscat.cinema.entity.Seat;

@Service
public class SeatService {

	@Autowired
	private SeatRepository seatRepository;

	public void generateSmallRoomAndInsertSeats(Integer roomId) {
		// 假設有7排，每排11個座位
		int numRows = 7;
		int numSeatsPerRow = 11;

		// 座位名稱的字母
		String[] seatNames = { "A", "B", "C", "D", "E", "F", "G" };

		// 循環生成和插入座位
		for (int row = 1; row <= numRows; row++) {
			for (int seatNum = 1; seatNum <= numSeatsPerRow; seatNum++) {
				// 生成座位名稱，例如：A01, A02, B01, B02, ...
				String seatName = String.format("%s%02d", seatNames[row - 1], seatNum);

				// 創建 Seat 對象並設置屬性
				Seat seat = new Seat();
				seat.setSeatName(seatName);
				seat.setSeatStatus("Normal");

				// 設置 room_id
				ScreeningRoom screeningRoom = new ScreeningRoom();
				screeningRoom.setRoomId(roomId);
				seat.setScreeningRoom(screeningRoom);

				// 保存座位到數據庫
				seatRepository.save(seat);
			}
		}
	}

	public void generateLargeRoomAndInsertSeats(Integer roomId) {

		int numRows = 14;
		int numSeatsPerRow = 30;

		String[] seatNames = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N" };

		for (int row = 1; row <= numRows; row++) {
			for (int seatNum = 01; seatNum <= numSeatsPerRow; seatNum++) {
				String seatName = String.format("%s%02d", seatNames[row - 1], seatNum);

				Seat seat = new Seat();
				seat.setSeatName(seatName);
				seat.setSeatStatus("Normal");

				ScreeningRoom screeningRoom = new ScreeningRoom();
				screeningRoom.setRoomId(roomId);
				seat.setScreeningRoom(screeningRoom);

				seatRepository.save(seat);
			}
		}
	}

	public String findSeatStatusBySeatNameAndRoomId(String seatName, Integer roomId) {
		return seatRepository.findSeatStatusBySeatNameAndRoomId(seatName, roomId);
	}

	public boolean updateSeatStatusBySeatNameAndRoomId(String seatName, Integer roomId, String status) {
		int updatedRows = seatRepository.updateSeatStatusBySeatNameAndRoomId(status, seatName, roomId);
		return updatedRows > 0;
	}

	public boolean updateSeatStatusBySeatId(String status, UUID id) {
		int updatedRows = seatRepository.updateSeatStatusById(status, id);
		return updatedRows > 0;
	}

	public void deleteSeatById(UUID id) {
		seatRepository.deleteById(id);
	}

	private SeatDTO convertToDTO(Seat seat) {
		SeatDTO dto = new SeatDTO();
		dto.setSeatId(seat.getSeatId());
		dto.setSeatStatus(seat.getSeatStatus());
		dto.setSeatName(seat.getSeatName());
		return dto;
	}

	public List<SeatDTO> getAllSeatsByRoomIdSortedByName(Integer roomId) {
		List<Seat> seats = seatRepository.findByScreeningRoom_RoomIdOrderBySeatName(roomId);
		return seats.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

}
