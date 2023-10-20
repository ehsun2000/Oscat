package com.oscat.cinema.entity;

import lombok.Data;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Data
@Entity
@Table(name = "showtime")
public class ShowTime {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(columnDefinition = "uniqueidentifier", name = "showtime_id")
	private UUID showTimeId;

	@Column(name = "film_type", nullable = false, length = 255)
	private String filmType;

	@Column(name = "extra_fee", precision = 10, scale = 5, columnDefinition = "DECIMAL(10, 2) DEFAULT 0")
	private BigDecimal extraFee;

	@Column(name = "show_date_and_time", nullable = false)
	private LocalDateTime showDateAndTime;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "movie_id")
	private Movie movie;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "room_id")
	private ScreeningRoom screeningRoom;

	@OneToMany(mappedBy = "showTime", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<TransOrder> transOrders;

	@Column(name = "price")
	private int price;

	public void setMovie(Movie selectedMovie) {
		// TODO Auto-generated method stub

	}
}
