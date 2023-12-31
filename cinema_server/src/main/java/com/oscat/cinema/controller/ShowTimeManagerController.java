package com.oscat.cinema.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oscat.cinema.dto.RequestShowTime;
import com.oscat.cinema.dto.ResponseCinemaForShowTime;
import com.oscat.cinema.dto.ShowTimeDTO;
import com.oscat.cinema.dto.ShowTimeForPut;
import com.oscat.cinema.entity.ShowTime;
import com.oscat.cinema.service.ShowTimeManagerService;

@RestController
@RequestMapping("/api/showtime")
public class ShowTimeManagerController {

	@Autowired
	private ShowTimeManagerService stmService;

	@PostMapping("/add")
	public ResponseEntity<?> addShowTime(@RequestBody ShowTimeDTO showTimeDTO) {
		if (stmService.isShowTimeValid(showTimeDTO)) {
			if (stmService.isShowTimeAvailable(showTimeDTO)) {
				Optional<ShowTime> optional = stmService.convertToEntity(showTimeDTO);

				if (optional.isPresent()) {
					ShowTime createdShowTime = stmService.addShowTime(optional.get());
					return new ResponseEntity<>(createdShowTime, HttpStatus.OK);
				}
				return new ResponseEntity<>("新增失敗", HttpStatus.BAD_REQUEST);

			} else {
				return new ResponseEntity<>("時間與其他場次衝突!!", HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>("沒有在營業時間內!", HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/addMore")
	public ResponseEntity<?> addMoreShowTime(@RequestBody List<ShowTimeDTO> showTimes) {
		List<ShowTime> createdShowTimes = new ArrayList<>();

		for (ShowTimeDTO showTimeDTO : showTimes) {
			Optional<ShowTime> optional = stmService.convertToEntity(showTimeDTO);
			if (optional.isPresent()) {
				ShowTime createdShowTime = stmService.addShowTime(optional.get());
				createdShowTimes.add(createdShowTime);
			}
		}

		if (!createdShowTimes.isEmpty()) {
			return new ResponseEntity<>(createdShowTimes, HttpStatus.OK);
		}
		return new ResponseEntity<>("新增失敗", HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/findAll")
	public Page<ShowTime> findAllShowTimes(Pageable pageable, Integer roomId) {
		return stmService.findAll(pageable, roomId);
	}

	@DeleteMapping("/delete/{showTimeId}")
	public ResponseEntity<String> deleteShowTime(@PathVariable UUID showTimeId) {
		boolean deleted = stmService.deleteShowTime(showTimeId);
		if (deleted) {
			return new ResponseEntity<>(showTimeId + " : 刪除成功 ", HttpStatus.OK);
		} else {
			return new ResponseEntity<>(" 無此ID ", HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/todayShowTimes")
	public ResponseEntity<List<Object[]>> getTodayShowTimes() throws ClassNotFoundException {
		List<Object[]> todayShowTimes = stmService.findTodayShowTimes();
		return new ResponseEntity<>(todayShowTimes, HttpStatus.OK);
	}

	@GetMapping("/AweekShowTimes")
	public ResponseEntity<List<Object[]>> getNextWeekShowTimes() throws ClassNotFoundException {
		List<Object[]> nextWeekShowTimes = stmService.findShowTimesForNextWeek();

		return new ResponseEntity<>(nextWeekShowTimes, HttpStatus.OK);
	}

	// 找出所有影城及其影廳
	@GetMapping("/findcinemas")
	public ResponseEntity<?> getCinemasAndRooms() {
		List<ResponseCinemaForShowTime> list = stmService.findAllCinemas();

		return ResponseEntity.ok(list);
	}

	// 找出所有電影
	@GetMapping("/movies")
	public ResponseEntity<?> getMovies() {
		List<Map<String, String>> movies = stmService.findMovies();

		return ResponseEntity.ok(movies);
	}

	// 根據影廳、日期區間、找出場次
	@PostMapping("/")
	public ResponseEntity<?> getShowTimes(@RequestBody RequestShowTime req) {
		List<Map<String, String>> showtimes = stmService.findShowTimes(req);
		return ResponseEntity.ok(showtimes);
	}

	// 單筆場次查詢
	@GetMapping("/{id}")
	public ResponseEntity<?> findShowTimeById(@PathVariable(name = "id") UUID showTimeId) {
		Map<String, String> showtime = stmService.findShowTimeById(showTimeId);
		if (showtime != null) {
			return ResponseEntity.ok(showtime);
		}

		return ResponseEntity.badRequest().body("找不到場次!");
	}

	// 場次資料更新
	@PutMapping("/")
	public ResponseEntity<?> updateShowTime(@RequestBody ShowTimeForPut dto) {
		String updatedShowTime = stmService.updateShowTime(dto);
		if ("success".equals(updatedShowTime)) {
			return ResponseEntity.ok("場次修改完成");
		}
		return ResponseEntity.badRequest().body(updatedShowTime);
	}
}
