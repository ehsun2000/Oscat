package com.oscat.cinema.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;

@Data
public class ShowTimeDTO {
	private UUID movieId;
	private Integer roomId;
	private String filmType;
	private BigDecimal extraFee; 
	private LocalDateTime showDateAndTime;
}
 