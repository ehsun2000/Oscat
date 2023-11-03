package com.oscat.cinema.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oscat.cinema.entity.TransOrder;
import com.oscat.cinema.service.TransOrderService;

@RestController
@RequestMapping("api/transorder")
public class TransOrderController {

	@Autowired
	private TransOrderService orderService;

	@PostMapping("/add")
	public TransOrder createOrder(@RequestBody TransOrder order) {
		return orderService.createOrder(order);
	}

	@GetMapping("/all")
	public List<TransOrder> getAllTransOrders() {
		return orderService.getAllTransOrders();
	}

	@GetMapping("/all/{orderId}")
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

	@PostMapping("/addlist")
	public ResponseEntity<String> createOrders(@RequestBody List<TransOrder> orders) {
		orderService.createOrders(orders);
		return ResponseEntity.ok("成功新增訂單");
	}
}
