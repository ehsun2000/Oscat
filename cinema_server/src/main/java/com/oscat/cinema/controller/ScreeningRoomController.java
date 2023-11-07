package com.oscat.cinema.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.oscat.cinema.dto.ScreeningRoomDTO;
import com.oscat.cinema.service.ScreeningRoomService;

@RestController
@RequestMapping(path = "/api/screeningRoom")
public class ScreeningRoomController {
	
	@Autowired
	private ScreeningRoomService srService;
	
	@GetMapping("/all")
	public List<ScreeningRoomDTO> getSeatsByRoomId(@RequestParam Integer id) {
		return srService.getAllScreeningRoomById(id);
	}
	
}
