package com.oscat.cinema.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oscat.cinema.dto.ShowTimeDTO;
import com.oscat.cinema.entity.ShowTime;
import com.oscat.cinema.service.ShowTimeManagerService;

@RestController
@RequestMapping("/api/showtime")
public class ShowTimeManagerController {

	@Autowired
	private ShowTimeManagerService stmService;

	@PostMapping("/add")
	public ResponseEntity<?> addShowTime(@RequestBody ShowTimeDTO showTimeDTO) {
		Optional<ShowTime> optional = stmService.convertToEntity(showTimeDTO);
		if(optional.isPresent()){
			ShowTime createdShowTime = stmService.addShowTime(optional.get());		
			return new ResponseEntity<>(createdShowTime, HttpStatus.OK);
		}
		return new ResponseEntity<>("新增失敗", HttpStatus.BAD_REQUEST); 
	}

	@GetMapping("/findAll")
	public List<ShowTime> findAllShowTimes() {
		return stmService.findAll();
	}

	@GetMapping("/find/{id}")
	public Optional<ShowTime> findShowTimeById(@PathVariable UUID showTimeId) {
		return stmService.findShowTimeById(showTimeId);
	}

//	@PutMapping("/update/{id}")
//	public ResponseEntity<ShowTime> updateShowTime(@PathVariable UUID id, @RequestBody ShowTime showTime) {
//		ShowTime updatedShowTime = stmService.updateShowTime(id, showTime);
//		if (updatedShowTime != null) {
//			return new ResponseEntity<>(updatedShowTime, HttpStatus.OK);
//		} else {
//			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//		}
//	}
//
//	@DeleteMapping("/delete/{id}")
//	public ResponseEntity<String> deleteShowTime(@PathVariable UUID id) {
//		boolean deleted = stmService.deleteShowTime(id);
//		if (deleted) {
//			return new ResponseEntity<>("ShowTime with ID " + id + " has been deleted.", HttpStatus.OK);
//		} else {
//			return new ResponseEntity<>("ShowTime with ID " + id + " not found.", HttpStatus.NOT_FOUND);
//		}
//	}
}
