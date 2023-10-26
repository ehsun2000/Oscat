package com.oscat.cinema.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "facility")
public class Facility {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "facility_id")
	private Integer facilityId;

	@Column(name = "facility_name")
	private String facilityName;

	@Column(name = "facility_img")
	private String facilityImg;

	@Column(name = "description")
	private String description;

	@JsonIgnore
	@ManyToMany(mappedBy = "facilities")
	private List<Cinema> cinemas;

}
