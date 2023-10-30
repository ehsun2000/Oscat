package com.oscat.cinema.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oscat.cinema.entity.CinemaTicketType;
import com.oscat.cinema.entity.CinemaTicketTypeId;

public interface CinemaTicketTypeRepository extends JpaRepository<CinemaTicketType, CinemaTicketTypeId>{
}
