package com.oscat.cinema.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;

@Data
public class ShowTimeForPut {
	private UUID showtimeId;
	private UUID movieId;
	private Integer roomId;
	private String filmType;
	private LocalDateTime showDateAndTime;
}
