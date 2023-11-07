package com.oscat.cinema.dto;

import lombok.Data;

@Data
public class JoinDateProportionDTO {

	private long thisYearMonthCount;
	private long thisHalfYearCount;
	private long thisYearCount;
	private long lastYearMonthCount;
	private long lastHalfYearCount;
	private long lastYearCount;

}
