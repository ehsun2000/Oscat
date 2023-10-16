package com.oscat.cinema.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "cinema_product")
@IdClass(CinemaProductId.class) //使用 ID.Class來當複合主鍵
public class CinemaProduct {
	
	@Id
	@Column(name = "cinema_id")
	private Integer cinemaId;
	
	@Id
	@Column(name = "product_id")
	private Integer productId;
	
	@Column(name = "availability")
	private Integer availability;
	
	@ManyToOne
	@JoinColumn(name = "product_id",insertable = false, updatable = false)
	private Product product;
	
	
	@ManyToOne
	@JoinColumn(name = "cinema_id",insertable = false, updatable = false)
	private Cinema cinema;
	
	

}
