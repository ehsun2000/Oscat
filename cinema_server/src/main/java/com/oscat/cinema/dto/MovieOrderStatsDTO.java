package com.oscat.cinema.dto;

import com.oscat.cinema.entity.Movie;
import com.oscat.cinema.entity.ShowTime;

import lombok.Data;

@Data
public class MovieOrderStatsDTO {
	
	 private ShowTime showTime;
	 private int orderCount;
	 private Movie movie;

}
