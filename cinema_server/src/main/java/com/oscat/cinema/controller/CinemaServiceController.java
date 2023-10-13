package com.oscat.cinema.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.oscat.cinema.entity.ScreeningRoom;
import com.oscat.cinema.service.ScreeningRoomService;

@RestController
public class CinemaServiceController {
	
	@Autowired
	private ScreeningRoomService screeningRoomService;
	
	@PostMapping("/screeningRoom/add")
	public ScreeningRoom addScreeningRoom (@RequestBody ScreeningRoom screeningRoom) {
		screeningRoomService.insert(screeningRoom);
		return screeningRoom;
	}
	
	@GetMapping("/screeningRoom/{id}")
	public ResponseEntity<?> findById(@PathVariable Integer id){
		ScreeningRoom screeningRoom = screeningRoomService.findById(id);
		
		if(screeningRoom!=null) {
			return new ResponseEntity<>(screeningRoom,HttpStatus.OK);
		}
		return new ResponseEntity<String>("沒有這筆資料",HttpStatus.NOT_FOUND);
	}
	
	@GetMapping("/screeningRooms")
	public List<ScreeningRoom> findAll(){
		return screeningRoomService.findAll();
	}
	
	@DeleteMapping("/screeningRoom/delete")
	public void deleteById(@RequestParam Integer id) {
		screeningRoomService.deleteById(id);
	}
}
