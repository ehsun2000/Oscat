package com.oscat.cinema.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class CinemaProductId implements Serializable{
	
	private Integer cinemaId;
	private Integer productId;
	
}
