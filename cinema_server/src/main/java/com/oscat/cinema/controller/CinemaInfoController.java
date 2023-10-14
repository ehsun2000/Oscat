package com.oscat.cinema.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oscat.cinema.dto.CinemaDTO;
import com.oscat.cinema.service.impl.CinameInfoService;

@RestController
@RequestMapping(path = "/api/cinemas")
public class CinemaInfoController {

	@Autowired
	private CinameInfoService infoService;

	// 找尋一筆影城資料
	@GetMapping(path = "/{cinemaId}")
	public ResponseEntity<CinemaDTO> getCinemaById(@PathVariable Integer cinemaId) {

		CinemaDTO cinema = infoService.findCinemaById(cinemaId);

		if (cinema == null) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(cinema);
		}

	}

	// 找尋分頁影城資料
	@GetMapping
	public ResponseEntity<Page<Object[]>> getAllCinemas(Pageable pageable) {
		return ResponseEntity.ok(null);
	}

}
