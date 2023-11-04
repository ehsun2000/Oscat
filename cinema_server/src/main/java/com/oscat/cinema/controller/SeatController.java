package com.oscat.cinema.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.oscat.cinema.dao.ScreeningRoomRepository;
import com.oscat.cinema.dto.SeatDTO;
import com.oscat.cinema.entity.ScreeningRoom;
import com.oscat.cinema.service.SeatService;

@RestController
@RequestMapping(path = "/api/seat")
public class SeatController {

	@Autowired
	private SeatService seatService;

	@Autowired
	private ScreeningRoomRepository srRepo;

	@PostMapping("/generateSmallRoom/{roomId}")
	public String generateSmallRoomSeats(@PathVariable Integer roomId) {
		Optional<ScreeningRoom> optional = srRepo.findById(roomId);

		if (optional.isPresent()) {
			seatService.generateSmallRoomAndInsertSeats(roomId);
			return "座位建立完成";
		}
		return "無此廳ID";
	}

	@PostMapping("/generateLargeRoom/{roomId}")
	public String generateLargeRoomSeats(@PathVariable Integer roomId) {
		Optional<ScreeningRoom> optional = srRepo.findById(roomId);

		if (optional.isPresent()) {
			seatService.generateLargeRoomAndInsertSeats(roomId);
			return "座位建立完成";
		}
		return "無此廳ID";
	}

	@GetMapping("/findSeatStatus")
	public ResponseEntity<String> findSeatStatus(@RequestParam String seatName, @RequestParam Integer roomId) {
		String seatStatus = seatService.findSeatStatusBySeatNameAndRoomId(seatName, roomId);
		if (seatStatus != null) {
			return ResponseEntity.ok(seatStatus);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PutMapping("/updateSeatStatusById")
	public ResponseEntity<String> updateSeatStatusById(@RequestParam UUID id, @RequestParam String status) {
		boolean result = seatService.updateSeatStatusBySeatId(status, id);
		if (result) {
			return ResponseEntity.ok("更新成功");
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/findAllSeatByRoomId")
	public List<SeatDTO> getSeatsByRoomId(@RequestParam Integer roomId) {
		return seatService.getAllSeatsByRoomIdSortedByName(roomId);
	}

}
