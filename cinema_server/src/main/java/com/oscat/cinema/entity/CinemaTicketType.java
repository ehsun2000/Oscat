package com.oscat.cinema.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "cinema_ticket_type")
@IdClass(CinemaTicketTypeId.class) // 使用一個IdClass來表示複合主鍵
public class CinemaTicketType {
	@Id
	@Column(name = "cinema_id")
	private int cinemaId;

	@Id
	@Column(name = "ticket_type_id")
	private int ticketTypeId;

	@ManyToOne
	@JsonBackReference
	@JoinColumn(name = "ticket_type_id", insertable = false, updatable = false)
	private TicketType ticketType;

	@ManyToOne
	@JsonBackReference
	@JoinColumn(name = "cinema_id", insertable = false, updatable = false)
	private Cinema cinema;

}
