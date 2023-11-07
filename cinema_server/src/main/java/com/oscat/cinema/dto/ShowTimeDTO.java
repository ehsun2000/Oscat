package com.oscat.cinema.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class ShowTimeDTO {
	private UUID movieId;
	private Integer roomId;
	private String filmType;
	private LocalDateTime showDateAndTime;
}