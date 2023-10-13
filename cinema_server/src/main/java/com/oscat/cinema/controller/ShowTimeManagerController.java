package com.oscat.cinema.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oscat.cinema.dto.ShowCreationRequestDTO;
import com.oscat.cinema.service.ShowTimeManagerService;

@RestController
@RequestMapping("/showtime/manager")
public class ShowTimeManagerController {
	@Autowired
	private ShowTimeManagerService stmService;
	
	@PostMapping("/add")
	public List<LocalDateTime> createShowTime(@RequestBody ShowCreationRequestDTO showTimeDTO) {
		return stmService.addShow(showTimeDTO);
	}
}
