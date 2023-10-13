package com.oscat.cinema.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class SeatDTO {
	private UUID seatId;
	private String seatStatus;
	private String seatName;

}
