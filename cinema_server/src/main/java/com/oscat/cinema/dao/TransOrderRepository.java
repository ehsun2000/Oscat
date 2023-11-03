package com.oscat.cinema.dao;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oscat.cinema.entity.TransOrder;

@Repository
public interface TransOrderRepository extends JpaRepository<TransOrder, UUID> {

//	TransOrder updateOrder(TransOrder updatedOrder);

}
