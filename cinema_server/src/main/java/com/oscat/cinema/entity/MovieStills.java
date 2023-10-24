package com.oscat.cinema.entity;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "movie_stills")
public class MovieStills {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(columnDefinition = "uniqueidentifier", name = "still_id")
	private UUID stillId;

	@Column(name = "still_image_url", nullable = false, length = 1000)
	private String stillImageUrl;

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference(value="still-ref")
	@JoinColumn(name = "movie_id", nullable = false)
	private Movie movie;
}
