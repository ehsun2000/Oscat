package com.oscat.cinema.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TicketTypeDTO {
	private Integer typeId;
	private String typeName;
	private BigDecimal price;
}
