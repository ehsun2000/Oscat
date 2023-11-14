package com.oscat.cinema.dto;

import com.oscat.cinema.entity.Movie;
import com.oscat.cinema.entity.ShowTime;

import lombok.Data;

@Data
public class MovieOrderStatsDTO {
	
	 private int orderCount;
	 private Movie movie;

}
