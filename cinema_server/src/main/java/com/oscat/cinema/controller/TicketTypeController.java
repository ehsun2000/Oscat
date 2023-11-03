package com.oscat.cinema.controller;

import java.util.List;

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

import com.oscat.cinema.entity.TicketType;
import com.oscat.cinema.service.TicketTypeService;

@RestController
@RequestMapping(path = "api/tickettype")
public class TicketTypeController {

	@Autowired
	private TicketTypeService ticketTypeService;

	@PostMapping("/add")
	public ResponseEntity<TicketType> createTicketType(@RequestBody TicketType ticketType) {
		TicketType createdTicketType = ticketTypeService.createTicketType(ticketType);
		return new ResponseEntity<>(createdTicketType, HttpStatus.OK);
	}

	@PutMapping("/update")
	public ResponseEntity<String> updateTicketType(@RequestBody TicketType updatedTicketType) {
		boolean isUpdated = ticketTypeService.updateTicketType(updatedTicketType);
		if (isUpdated) {
			return ResponseEntity.ok("更新成功");
		} else {
			return ResponseEntity.badRequest().body("無法取得票種資訊");
		}
	}

	@DeleteMapping("/delete/{ticketTypeId}")
	public ResponseEntity<String> deleteTicketType(@PathVariable Integer ticketTypeId) {
		boolean isDeleted = ticketTypeService.deleteTicketType(ticketTypeId);
		if (isDeleted) {
			return ResponseEntity.ok("刪除成功");
		} else {
			return ResponseEntity.badRequest().body("無法刪除票種");
		}
	}

	@GetMapping("/all")
	public List<TicketType> getAllTicketTypes() {
		return ticketTypeService.getAllTicketTypes();
	}

}
