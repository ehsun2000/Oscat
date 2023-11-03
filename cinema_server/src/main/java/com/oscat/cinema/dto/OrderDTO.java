package com.oscat.cinema.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class OrderDTO {
    private UUID showtimeId;
    private UUID memberId;
    private String paymentMethod;
    private BigDecimal totalPrice;
    private List<TicketRequest> tickets;

    @Data
    public static class TicketRequest {
        private int typeId;
        private UUID seatId;
    }

}
