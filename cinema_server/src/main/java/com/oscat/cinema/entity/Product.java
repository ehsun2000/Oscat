package com.oscat.cinema.entity;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "product")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(columnDefinition = "uniqueidentifier", name = "product_id")
	private UUID productId;

	@Column(name = "product_name", nullable = false, length = 255)
	private String productName;

	@Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
	private BigDecimal unitPrice;

	@Column(name = "product_type", nullable = false, length = 50)
	private String productType;

	@Column(name = "product_img", nullable = false, length = 255)
	private String productImg;

	@JsonIgnore
	@ManyToMany(mappedBy = "products")
	private List<Cinema> cinemaProducts;
	
}
