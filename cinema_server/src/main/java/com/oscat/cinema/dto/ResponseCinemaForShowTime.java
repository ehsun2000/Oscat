package com.oscat.cinema.dto;

import java.util.List;

import lombok.Data;

@Data
public class ResponseCinemaForShowTime {
	private String cinema;
	private List<ScreeningRoomDTO> rooms;
}
