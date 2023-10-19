package com.oscat.cinema.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class CinemaDTO {
	
	 	private Integer cinemaId;
	    private String cinemaName;
	    private String cinemaAddress;
	    private String contactPhone;
	    private String openingHours;
	    private String facilities;
	    private BigDecimal basePrice;
	    private List<ProductDTO> cinemaProducts; // 代表與該影城相關的產品列表
}
