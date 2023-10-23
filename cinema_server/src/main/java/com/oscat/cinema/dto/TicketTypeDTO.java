package com.oscat.cinema.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class TicketTypeDTO {
	private String type;
	private BigDecimal different;
}
