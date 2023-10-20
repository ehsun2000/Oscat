package com.oscat.cinema.entity;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

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
@Table(name = "cinema")
public class Cinema {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cinema_id")
	private Integer cinemaId;

	@Column(name = "cinema_name", nullable = false, length = 255)
	private String cinemaName;

	@Column(name = "cinema_address", nullable = false, length = 255)
	private String cinemaAddress;

	@Column(name = "contact_phone", nullable = false, length = 20)
	private String contactPhone;

	@Column(name = "opening_hours", nullable = false, length = 100)
	private String openingHours;

	@Column(name = "facilities", nullable = false, columnDefinition = "VARCHAR(MAX)")
	private String facilities;

	@Column(name = "base_price", nullable = false, precision = 10, scale = 5)
	private BigDecimal basePrice;

	@Column(name = "cinema_img", nullable = false, length = 255)
	private String cinemaImg;

	@JsonManagedReference
	@OneToMany(mappedBy = "cinema", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ScreeningRoom> screeningRooms;

	@JsonManagedReference
	@OneToMany(mappedBy = "cinema", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CinemaTicketType> ticketTypes;
}
