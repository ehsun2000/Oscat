package com.oscat.cinema.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

	/*
	 * /api/cinemas - 搜尋第一頁，使用預設的頁面大小和排序。 /api/cinemas?page=1 - 搜尋第二頁。
	 * /api/cinemas?size=5 - 指定大小為5。 /api/cinemas?sort=cinemaName,asc -
	 * 按cinemaName升序排列结果。
	 */
	// 找尋分頁影城資料
	@GetMapping("/")
	public ResponseEntity<Page<CinemaDTO>> getAllCinemas(Pageable pageable) {
		Page<CinemaDTO> cinemas = infoService.findAll(pageable);

		return ResponseEntity.ok(cinemas);
	}
	
	@GetMapping("/all")
	public ResponseEntity<List<CinemaDTO>> getCinemaIdAndName() {
	    List<CinemaDTO> cinemas = infoService.getAllCinemas();
	    return ResponseEntity.ok(cinemas);
	}

	// 修改影城資料
	@PutMapping("/")
	public ResponseEntity<String> updateCinema(@RequestBody CinemaDTO cinema) {

		if (infoService.update(cinema)) {
			return ResponseEntity.ok("Update Sucess!");
		}

		return ResponseEntity.notFound().build();
	}

}
