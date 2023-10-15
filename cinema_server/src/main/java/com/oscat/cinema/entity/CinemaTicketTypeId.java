package com.oscat.cinema.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class CinemaTicketTypeId implements Serializable {
	private int cinemaId;
	private int ticketTypeId;
}