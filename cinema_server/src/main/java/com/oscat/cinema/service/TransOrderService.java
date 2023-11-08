package com.oscat.cinema.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.oscat.cinema.dao.MemberRepository;
import com.oscat.cinema.dao.ShowTimeRepository;
import com.oscat.cinema.dao.TransOrderRepository;
import com.oscat.cinema.dto.OrderDTO;
import com.oscat.cinema.entity.Member;
import com.oscat.cinema.entity.ShowTime;
import com.oscat.cinema.entity.TransOrder;

@Service
public class TransOrderService {

	@Autowired
	private TransOrderRepository orderRepository;
	@Autowired
	private ShowTimeRepository showTimeRepository;
	@Autowired
	private MemberRepository memberRepository;

	// 找全部
	public Page<Map<String, Object>> getAllTransOrders(Pageable pageable) {
		Page<TransOrder> orderPage = orderRepository.findAll(pageable);

		return orderPage.map(this::toOrderDto);
	}

	private Map<String, Object> toOrderDto(TransOrder transOrder) {
		Map<String, Object> dto = new HashMap<>();

		dto.put("orderId", transOrder.getOrderId());
		dto.put("showtime", transOrder.getShowTime().getShowDateAndTime());
		dto.put("email", transOrder.getMember().getEmail());
		dto.put("memberName", transOrder.getMember().getMemberName());
		dto.put("paymentMethod", transOrder.getPaymentMethod());
		dto.put("bookingDateAndTime", transOrder.getBookingDateAndTime());
		dto.put("totalPrice", transOrder.getTotalPrice());

		return dto;
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

			return orderRepository.save(existingOrder);
		} else {
			return null;
		}
	}

	// 更改付款方式
	public TransOrder updateOrder(TransOrder transOrder) {
		return orderRepository.save(transOrder);
	}

	// 根據下單日期查找訂單
	public List<TransOrder> findOrdersByTime(LocalDateTime bookingDateAndTime) {
		List<TransOrder> orders = orderRepository.findBybookingDateAndTime(bookingDateAndTime);
		return orders;
	}

	// 分頁
	public Page<TransOrder> findPage(Pageable pageable) {
		return orderRepository.findAll(pageable);
	}

	// 更新付款方式
	public TransOrder updateOrderPaymentStatus(UUID orderId, String paymentStatus) {
		TransOrder order = getTransOrderById(orderId);
		if (order != null) {
			order.setPaymentMethod(paymentStatus);
			orderRepository.save(order);
			return order;
		}
		return null;

	}

	public List<TransOrder> findOrdersByTimeAndEmail(LocalDateTime bookingDateAndTime, String email) {
		// TODO Auto-generated method stub
		return null;
	}

}
