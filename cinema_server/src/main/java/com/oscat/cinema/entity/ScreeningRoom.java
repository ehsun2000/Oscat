package com.oscat.cinema.entity;

import java.util.List;

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
@Table(name = "screening_room")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"..."})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "roomId")
public class ScreeningRoom {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "room_id")
	private Integer roomId;

	@Column(name = "room_name", nullable = false, length = 255)
	private String roomName;

	@Column(name = "type", nullable = false, length = 50)
	private String type;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cinema_id")
	private Cinema cinema;

	@OneToMany(mappedBy = "screeningRoom", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Seat> seats;

	@OneToMany(mappedBy = "screeningRoom", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ShowTime> showTimes;
}
