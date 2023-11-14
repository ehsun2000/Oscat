package com.oscat.cinema.dao;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oscat.cinema.entity.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID> {
	List<Ticket> findByTransOrder_ShowTime_ShowTimeId(UUID showTimeId);
}
