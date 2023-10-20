package com.oscat.cinema.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@Table(name = "showtime")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "..." })
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "showTimeId")
@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
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
	@JsonBackReference
	private Movie movie;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "room_id")
	@JsonBackReference
	private ScreeningRoom screeningRoom;

	@OneToMany(mappedBy = "showTime", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	private List<TransOrder> transOrders;
}
