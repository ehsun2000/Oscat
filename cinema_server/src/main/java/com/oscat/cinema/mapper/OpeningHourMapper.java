package com.oscat.cinema.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.oscat.cinema.dto.BusinessHourDto;
import com.oscat.cinema.entity.OpeningHour;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface OpeningHourMapper {
	@Mapping(target = "start", source = "startTime")
	@Mapping(target = "end", source = "endTime")
	BusinessHourDto toDto(OpeningHour openingHour);

	List<BusinessHourDto> toDtos(List<OpeningHour> openingHours);

}
