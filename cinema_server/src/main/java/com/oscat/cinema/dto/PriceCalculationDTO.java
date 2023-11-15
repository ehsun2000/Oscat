package com.oscat.cinema.dto;

import java.util.Map;
import java.util.UUID;

import lombok.Data;

@Data
public class PriceCalculationDTO {
    private String cinemaName;
    private UUID showtimeId;
    private Map<Integer, Integer> ticketTypeCounts; // Key: ticketTypeId, Value: count
}
