package com.oscat.cinema.entity;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "seat")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"..."})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "seatId")
public class Seat {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(columnDefinition = "uniqueidentifier", name = "seat_id")
	private UUID seatId;

	@Column(name = "seat_status", nullable = false, length = 50)
	private String seatStatus;

	@Column(name = "seat_name")
	private String seatName;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "room_id")
	private ScreeningRoom screeningRoom;

	@OneToMany(mappedBy = "seat", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Ticket> tickets;
}
