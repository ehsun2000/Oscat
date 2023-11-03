package com.oscat.cinema.dto;

import lombok.Data;

@Data
public class GenderProportionDTO {
	
	private long totalGender;
	private long maleCount;
	private long femaleCount;
	private long otherCount;
	Double maleProportion;
	Double femaleProportion;
	Double otherProportion;
	
}
