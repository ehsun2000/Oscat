package com.oscat.cinema.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.oscat.cinema.entity.TicketType;

@Repository
public interface TicketTypeRepository extends JpaRepository<TicketType, Integer> {
	@Query("select ticketTypeId,ticketTypeName,priceDifference from TicketType")
	List<Object[]> findAllTypes();
}
