package com.oscat.cinema.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.oscat.cinema.dao.ScreeningRoomRepository;
import com.oscat.cinema.dto.ScreeningRoomDTO;
import com.oscat.cinema.dto.SeatDTO;
import com.oscat.cinema.entity.ScreeningRoom;
import com.oscat.cinema.service.SeatService;

@RestController
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

	@PutMapping("/updateSeatStatus")
	public ResponseEntity<String> updateSeatStatus(@RequestBody Map<String, Object> request) {
	    String seatName = (String) request.get("seatName");
	    Integer roomId = (Integer) request.get("roomId");
	    String status = (String) request.get("status");

	    boolean updated = seatService.updateSeatStatusBySeatNameAndRoomId(seatName, roomId, status);
	    if (updated) {
	        return ResponseEntity.ok("更新成功");
	    } else {
	        return ResponseEntity.notFound().build();
	    }
	}
	
	@PutMapping("/updateSeatStatusById")
	@CrossOrigin(origins = "http://localhost:8081")
	public ResponseEntity<String> updateSeatStatusById(@RequestParam UUID id,@RequestParam String status) {
		boolean result = seatService.updateSeatStatusBySeatId(status, id);
	    if (result) {
	        return ResponseEntity.ok("更新成功");
	    } else {
	        return ResponseEntity.notFound().build();
	    }
	}
	
	@DeleteMapping("/deleteSeat")
	public String deleteSeat(@RequestParam("id") UUID id) {
	    seatService.deleteSeatById(id);
	    return "刪除成功";
	}
	
	@GetMapping("/ScreeningRoom/findallScreeningRoom")
	@CrossOrigin(origins = "http://localhost:8081")
	public List<ScreeningRoomDTO> findAllScreeningRoom(){
	    List<ScreeningRoom> screeningRooms = srRepo.findAll();
	    List<ScreeningRoomDTO> dtos = new ArrayList<>();
	    for (ScreeningRoom screeningRoom : screeningRooms) {
	        dtos.add(seatService.convertToDTO(screeningRoom));
	    }
	    return dtos;
	}
	
    @GetMapping("/findAllSeatByRoomId")
    @CrossOrigin(origins = "http://localhost:8081")
    public List<SeatDTO> getSeatsByRoomId(@RequestParam Integer roomId) {
        return seatService.getAllSeatsByRoomIdSortedByName(roomId);
    }
	
}
