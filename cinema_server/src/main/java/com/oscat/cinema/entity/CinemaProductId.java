package com.oscat.cinema.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class CinemaProductId implements Serializable {
	
	//版本更改時，仍能夠正確進行序列化和反序列化
	private static final long serialVersionUID = 1L;
	
	private Integer cinemaId;
	private Integer productId;

}
