package com.oscat.cinema.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CinemaTicketTypeId implements Serializable {
	private int cinemaId;
	private int ticketTypeId;
}