package com.oscat.cinema.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;

@Data
public class ShowCreationRequestDTO {
	private UUID movieId;
	private Integer roomId;
	private LocalDateTime startTime;
	private String flimType;
	private Double price; 
	private int numberOfShowsToAdd;
}
