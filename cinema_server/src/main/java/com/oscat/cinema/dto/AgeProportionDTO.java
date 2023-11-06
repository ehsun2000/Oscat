package com.oscat.cinema.dto;

import lombok.Data;

@Data
public class AgeProportionDTO {

	private long totalAge;
	private long youngAge;
	private long teenagerAge;
	private long adultAge;
	private long middleageAge;
	private long elderlyAge;
	private long oldAge;
	private Double youngAgeProportion;
	private Double teenagerAgeProportion;
	private Double adultAgeProportion;
	private Double middleageAgeProportion;
	private Double elderlyAgeProportion;
	private Double oldAgeProportion;
	
}
