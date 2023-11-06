package com.oscat.cinema.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TicketTypeDTO {
	private Integer typeId;
	private String typeName;
	private BigDecimal price;
}
