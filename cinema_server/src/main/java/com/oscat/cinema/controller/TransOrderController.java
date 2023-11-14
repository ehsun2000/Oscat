package com.oscat.cinema.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.oscat.cinema.dto.MovieOrderStatsDTO;
import com.oscat.cinema.dto.OrderDTO;
import com.oscat.cinema.entity.TransOrder;
import com.oscat.cinema.service.TransOrderService;

@RestController
@RequestMapping(path = "api/transorder")
public class TransOrderController {

	@Autowired
	private TransOrderService orderService;

	@GetMapping("/all")
	public Page<Map<String, Object>> getAllTransOrders(Pageable pageable) {
		return orderService.getAllTransOrders(pageable);
	}

	@GetMapping("/{orderId}")
	public TransOrder getTransOrderById(@PathVariable UUID orderId) {
		return orderService.getTransOrderById(orderId);
	}

	@DeleteMapping("/delete/{orderId}")
	public ResponseEntity<String> deleteTransOrder(@PathVariable UUID orderId) {
		TransOrder existingOrder = orderService.getTransOrderById(orderId);
		if (existingOrder != null) {
			orderService.deleteTransOrder(orderId);
			return ResponseEntity.ok("訂單編號 " + orderId + " 已成功刪除。");
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PutMapping("/update/{orderId}")
	public ResponseEntity<TransOrder> updateTransOrder(@PathVariable UUID orderId,
			@RequestBody TransOrder updatedOrder) {
		TransOrder updatedTransOrder = orderService.updateTransOrder(orderId, updatedOrder);
		if (updatedTransOrder != null) {
			return ResponseEntity.ok(updatedTransOrder);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/keyOrder")
	public ResponseEntity<List<TransOrder>> find(
			@RequestParam("bookingDateAndTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime bookingDateAndTime,
			@RequestParam("email") String email) {
		List<TransOrder> result = orderService.findOrdersByTimeAndEmail(bookingDateAndTime, email);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/page")
	public ResponseEntity<Page<TransOrder>> getPage(Pageable pageable) {
		Page<TransOrder> page = orderService.findPage(pageable);
		return ResponseEntity.ok(page);
	}

	@PostMapping("/confirmOrder")
	public ResponseEntity<?> confirmOrder(@RequestBody Map<String, String> request) {
		String orderIdString = request.get("orderId");
		try {
			UUID orderId = UUID.fromString(orderIdString);
			return new ResponseEntity<>("訂單已確認", HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>("找不到此訂單", HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/cancelOrder")
	public ResponseEntity<?> cancelOrder(@RequestParam UUID orderId) {
		System.out.println(orderId.toString());
		TransOrder order = orderService.getTransOrderById(orderId);

		if (order == null) {
			return new ResponseEntity<>("訂單不存在", HttpStatus.NOT_FOUND);
		}

		// 檢查訂單狀態
		order.setPaymentMethod("已取消");
		orderService.updateOrder(order);

		return new ResponseEntity<>("訂單已取消", HttpStatus.OK);
	}
	
	//1109
	 @GetMapping("/movieOrderStats")
	    public ResponseEntity<List<MovieOrderStatsDTO>> getMovieOrderStats() {
	        List<MovieOrderStatsDTO> movieOrderStats = orderService.getMovieOrderStats();
	        return new ResponseEntity<>(movieOrderStats, HttpStatus.OK);
	    }

}
