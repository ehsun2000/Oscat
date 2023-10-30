package com.oscat.cinema.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oscat.cinema.entity.ShowTime;
import com.oscat.cinema.service.ShowTimeManagerService;

@RestController
@RequestMapping("/api/showtime")
public class ShowTimeManagerController {

	@Autowired
	private ShowTimeManagerService stmService;

	@PostMapping("/add")
	public ResponseEntity<ShowTime> addShowtime(@RequestBody Map<String, Object> requestData) {
		ShowTime savedShowTime = stmService.addShowTime(requestData);
		return ResponseEntity.ok(savedShowTime);
	}

	@GetMapping("/findAll")
	public List<ShowTime> findAllShowTimes() {
		return stmService.findAll();
	}

	@GetMapping("/find/{id}")
	public Optional<ShowTime> findShowTimeById(@PathVariable UUID showTimeId) {
		return stmService.findShowTimeById(showTimeId);
	}
}