package com.oscat.cinema.controller;

import java.util.List;
import java.util.Optional;

import org.hibernate.resource.beans.internal.FallbackBeanInstanceProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.oscat.cinema.dto.CinemaDTO;
import com.oscat.cinema.service.impl.CinameInfoService;

import jakarta.servlet.http.HttpSession;

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

	// 修改影城資料
	@PutMapping("/")
	public ResponseEntity<String> updateCinema(@RequestPart("cinema") CinemaDTO cinema,
			@RequestPart(name = "file", required = false) MultipartFile file) {

		if (file != null) {
			String updateImg = infoService.updateImg(file, cinema.getId());
			
			if (!updateImg.equals("Success")) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(updateImg);
			}
		}

		boolean update = infoService.update(cinema);
		
		if (update) {
			return ResponseEntity.ok("Update Success!");
		}

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Update Faild!");
	}

	// 找尋全部種類資料
	@GetMapping("/types")
	public ResponseEntity<?> getAllTicketTypes() {
		List<String> types = infoService.findAllTicketTypes();

		return ResponseEntity.ok(types);
	}

	// 找尋全部設施資料
	@GetMapping("/facilities")
	public ResponseEntity<?> getAllFacilities() {
		List<String> facilities = infoService.findAllFacilities();

		return ResponseEntity.ok(facilities);
	}
	
	@GetMapping("/products")
	public ResponseEntity<?> getAllProducts(){
		List<String> products = infoService.findAllProducts();
		
		return ResponseEntity.ok(products);
	}
	
}
