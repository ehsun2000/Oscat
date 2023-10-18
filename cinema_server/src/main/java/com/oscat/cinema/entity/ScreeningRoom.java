package com.oscat.cinema.entity;

import lombok.Data;
import jakarta.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "screening_room")
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

	public ScreeningRoom(int roomId) {
		this.roomId = roomId;
	}
}
