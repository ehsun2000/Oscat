package com.oscat.cinema.dto;

import java.time.LocalTime;

import lombok.Data;

@Data
public class BusinessHourDto {
	private Integer weekDay;
    private LocalTime start;
    private LocalTime end;
}
