package com.oscat.cinema.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class CheckoutDataDTO {
    private String totalPrice;
    private UUID orderId;
}
