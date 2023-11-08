package com.oscat.cinema.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oscat.cinema.entity.TransOrder;

@Repository
public interface TransOrderRepository extends JpaRepository<TransOrder, UUID> {

	@Query(value = "from TransOrder where bookingDateAndTime = :bookingDateAndTime")
	List<TransOrder> findBybookingDateAndTime(@Param("bookingDateAndTime") LocalDateTime bookingDateAndTime);

	Page<TransOrder> findAll(Pageable pageable);

	@Query("SELECT t FROM TransOrder t WHERE t.orderId = :orderId")
	TransOrder findTransOrderByOrderId(@Param("orderId") String orderId);

}
