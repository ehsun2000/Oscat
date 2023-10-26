package com.oscat.cinema.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.oscat.cinema.entity.Facility;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface FacilityMapper {
	default String toDto(Facility facility) {
		return facility.getFacilityName();
	};

	List<String> tDtos(List<Facility> facilities);
}
