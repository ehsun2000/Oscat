package com.oscat.cinema.dto;

import java.math.BigDecimal;
import java.util.List;

import com.oscat.cinema.entity.TicketType;

import lombok.Data;

@Data
public class CinemaDTO {
	private Integer id;
    private String name;
    private String address;
    private String phone;
    private String openingHours;
    private String facilities;
	private BigDecimal basePrice;
	private String img;
	private List<TicketTypeDTO> types;
}
