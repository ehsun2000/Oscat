package com.oscat.cinema.entity;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "product")
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_id")
	private Integer productId;
	
	@Column(name = "product_name", nullable = false, length = 255)
	private String productName;
	
	@Column(name = "unit_price" , nullable = false, precision = 10, scale = 2)
	private BigDecimal unitPrice;
	
	@Column(name = "product_type", nullable = false, length = 50)
	private String productType;
	
	@Column(name = "cinema_img", nullable = false, length = 255)
    private String productImg;

	@OneToMany(mappedBy = "product",cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CinemaProduct>cinemaProducts;
	
}
