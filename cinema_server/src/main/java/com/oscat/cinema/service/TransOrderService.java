package com.oscat.cinema.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.ArrayList;

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
import com.oscat.cinema.dto.MovieOrderStatsDTO;
import com.oscat.cinema.dto.OrderDTO;
import com.oscat.cinema.entity.Member;
import com.oscat.cinema.entity.Movie;
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
		return null;
	}
	
	
	//1109圖表
	public List<MovieOrderStatsDTO> getMovieOrderStats() {
	    List<TransOrder> allOrders = orderRepository.findAll();
	    Map<ShowTime, Integer> movieOrderCountMap = new HashMap<>();

	    for (TransOrder order : allOrders) {
	        ShowTime showTime = order.getShowTime();
	        if (showTime != null) {
	            // 檢查該電影是否已經在統計中
	            if (movieOrderCountMap.containsKey(showTime)) {
	                // 如果已經在統計中，則增加訂單數量
	                movieOrderCountMap.put(showTime, movieOrderCountMap.get(showTime) + 1);
	            } else {
	                // 如果還未在統計中，則添加新的電影並設置訂單數量為1
	                movieOrderCountMap.put(showTime, 1);
	            }
	        }
	    }

	    // 將統計結果轉換為DTO列表，包括Movie物件
	    List<MovieOrderStatsDTO> movieOrderStatsList = new ArrayList<>();
	    for (Map.Entry<ShowTime, Integer> entry : movieOrderCountMap.entrySet()) {
	        MovieOrderStatsDTO statsDTO = new MovieOrderStatsDTO();
	        statsDTO.setShowTime(entry.getKey());
	        statsDTO.setOrderCount(entry.getValue());

	        // 為MovieOrderStatsDTO設置Movie物件
	        Movie movie = entry.getKey().getMovie();
	        statsDTO.setMovie(movie);

	        movieOrderStatsList.add(statsDTO);
	    }

	    return movieOrderStatsList;
	}


}
