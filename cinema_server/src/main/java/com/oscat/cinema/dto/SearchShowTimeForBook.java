package com.oscat.cinema.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class SearchShowTimeForBook {
	private List<LocalDateTime> showTime;
	private String roomName;
}
