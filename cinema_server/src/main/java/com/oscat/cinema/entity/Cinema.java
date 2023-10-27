package com.oscat.cinema.entity;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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

	@Column(name = "base_price", nullable = false, precision = 10, scale = 5)
	private BigDecimal basePrice;

	@Column(name = "cinema_img", nullable = false, length = 255)
	private String cinemaImg;

	@JsonManagedReference(value = "cinema-room")
	@OneToMany(mappedBy = "cinema", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ScreeningRoom> screeningRooms;

	@JsonManagedReference(value = "cinema-tickettype")
	@OneToMany(mappedBy = "cinema", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CinemaTicketType> ticketTypes;

	@JsonManagedReference(value = "cinema-open")
	@OneToMany(mappedBy = "cinema", cascade = CascadeType.ALL)
	private List<OpeningHour> openingHours;

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "cinema_product", joinColumns = @JoinColumn(name = "cinema_id"), inverseJoinColumns = @JoinColumn(name = "product_id"))
	private List<Product> products;

	@ManyToMany
	@JoinTable(name = "cinema_facility", joinColumns = @JoinColumn(name = "cinema_id"), inverseJoinColumns = @JoinColumn(name = "facility_id"))
	private List<Facility> facilities;
	
}