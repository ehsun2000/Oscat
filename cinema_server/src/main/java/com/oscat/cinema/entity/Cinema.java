package com.oscat.cinema.entity;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

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
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"..."})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "cinemaId")
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

	@OneToMany(mappedBy = "cinema", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ScreeningRoom> screeningRooms;
	
	@OneToMany(mappedBy = "cinema", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CinemaTicketType> ticketTypes;
}
