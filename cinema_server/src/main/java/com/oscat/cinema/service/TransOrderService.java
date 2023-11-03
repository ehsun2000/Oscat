package com.oscat.cinema.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oscat.cinema.dao.TransOrderRepository;
import com.oscat.cinema.entity.TransOrder;

@Service
public class TransOrderService {

	@Autowired
	private TransOrderRepository orderRepository;

	// 新增單筆
	public TransOrder createOrder(TransOrder order) {
		return orderRepository.save(order);
	}

	// 找全部
	public List<TransOrder> getAllTransOrders() {
		return orderRepository.findAll();
	}

	// ID找訂單
	public TransOrder getTransOrderById(UUID orderId) {
		return orderRepository.findById(orderId).orElse(null);
	}

	// 刪除
	public void deleteTransOrder(UUID orderId) {
		orderRepository.deleteById(orderId);
	}

	// 修改
	public TransOrder updateTransOrder(UUID orderId, TransOrder updatedOrder) {
		TransOrder existingOrder = orderRepository.findById(orderId).orElse(null);
		if (existingOrder != null) {
			existingOrder.setPaymentMethod(updatedOrder.getPaymentMethod());
			existingOrder.setBookingDateAndTime(updatedOrder.getBookingDateAndTime());
			existingOrder.setTotalPrice(updatedOrder.getTotalPrice());
			existingOrder.setShowTime(updatedOrder.getShowTime());
			existingOrder.setMember(updatedOrder.getMember());

			// 保存更新後的訂單
			return orderRepository.save(existingOrder);
		} else {
			return null;
		}

	}

	public void createOrders(List<TransOrder> orders) {
		orderRepository.saveAll(orders);
	}
}