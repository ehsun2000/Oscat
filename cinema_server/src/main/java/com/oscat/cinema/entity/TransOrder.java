package com.oscat.cinema.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "trans_order")
public class TransOrder {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(columnDefinition = "uniqueidentifier", name = "order_id")
	private UUID orderId;

	@Column(name = "payment_method", nullable = false, length = 50)
	private String paymentMethod;

	@Column(name = "booking_date_and_time", nullable = false)
	private LocalDateTime bookingDateAndTime;

	@Column(name = "total_price", nullable = false, precision = 10, scale = 2)
	private BigDecimal totalPrice;

	@JsonBackReference(value = "showtime_order")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "showtime_id")
	private ShowTime showTime;

	@JsonBackReference(value = "member-order")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@JsonManagedReference(value = "order-type")
	@OneToMany(mappedBy = "transOrder", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Ticket> tickets;
}
