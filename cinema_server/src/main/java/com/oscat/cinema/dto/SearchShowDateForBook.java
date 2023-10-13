package com.oscat.cinema.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.Data;

@Data
public class SearchShowDateForBook {
	private String showDate;

	public SearchShowDateForBook(LocalDateTime showDate) {
		DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd EEEE");

		this.showDate = pattern.format(showDate);
	}
}
