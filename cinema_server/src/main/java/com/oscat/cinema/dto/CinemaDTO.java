package com.oscat.cinema.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CinemaDTO {
	private Integer cinemaId;
	private String cinemaName;
	private BigDecimal basePrice;

}
