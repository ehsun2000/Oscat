package com.oscat.cinema.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class RequestShowTime {
	private Integer roomId;
	private LocalDateTime start;
	private LocalDateTime end;
}
