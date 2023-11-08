package com.oscat.cinema.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class CinemaDTO {
	private Integer id;
    private String name;
    private String address;
    private String phone;
	private BigDecimal basePrice;
	private String img;
	private List<BusinessHourDto> businessHours;
	private List<String> facilities;
	private List<String> types;
	private List<String> products;
}
