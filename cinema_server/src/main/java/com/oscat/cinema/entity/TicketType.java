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
@Table(name = "ticket_type")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "..." })
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "ticketTypeId")
public class TicketType {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ticket_type_id")
	private Integer ticketTypeId;

	@Column(name = "ticket_type_name", nullable = false, length = 255)
	private String ticketTypeName;

	@Column(name = "price_difference", nullable = false, precision = 5, scale = 2)
	private BigDecimal priceDifference;

	@OneToMany(mappedBy = "ticketType", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Ticket> tickets;

	@OneToMany(mappedBy = "ticketType", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CinemaTicketType> cinemaTicketTypes;
}
