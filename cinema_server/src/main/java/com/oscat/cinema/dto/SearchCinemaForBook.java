package com.oscat.cinema.dto;

import lombok.Data;

@Data
public class SearchCinemaForBook {
	private Integer id;
	private String name;

	public SearchCinemaForBook(Integer cinemaId, String cinemaName) {
		this.id = cinemaId;
		this.name = cinemaName;
	}
}
