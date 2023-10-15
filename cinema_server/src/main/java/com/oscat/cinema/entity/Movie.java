package com.oscat.cinema.entity;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Data
@Entity
@Table(name = "movie")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"..."})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "movieId")
@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
public class Movie {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(columnDefinition = "uniqueidentifier", name = "movie_id")
	private UUID movieId;

	@Column(name = "movie_name", nullable = false, length = 255)
	private String movieName;

	@Column(name = "movie_type", nullable = false, length = 255)
	private String movieType;

	@Column(name = "movie_status", nullable = false, length = 10)
	private String movieStatus;

	@Column(name = "director", nullable = false, length = 255)
	private String director;

	@Column(name = "writer_list", length = 255)
	private String writerList;

	@Column(name = "actor_list", length = 255)
	private String actorList;

	@Column(name = "plot_summary", nullable = false, columnDefinition = "VARCHAR(MAX)")
	private String plotSummary;

	@Column(name = "release_date", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date releaseDate;

	@Column(name = "duration", nullable = false)
	private Integer duration;

	@Column(name = "classification", nullable = false, length = 50)
	private String classification;

	@Column(name = "trailer_link", length = 255)
	private String trailerLink;

	@Column(name = "poster_image", length = 1000)
	private String posterImage;

	@OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<MovieStills> movieStills;
}
