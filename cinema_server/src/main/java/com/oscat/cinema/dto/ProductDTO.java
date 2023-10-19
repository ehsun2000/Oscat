package com.oscat.cinema.dto;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Data;

@Data
public class ProductDTO {
	
	private UUID productId;
	private String productName;
	private BigDecimal unitPrice;
	private String productType;
	private String productImg;
}
 