package com.oscat.cinema.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.oscat.cinema.dto.CinemaDTO;
import com.oscat.cinema.entity.Cinema;

// 使用 mapstruct 轉換 dto
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR
// 透過 TicketTypeMapper 將 List<TicketType> 轉換成 List<TicketTypeDTO>
	, uses = { TicketTypeMapper.class }
	)

public interface CinemaMapper {

//	映射不同屬性名稱
	@Mapping(source = "cinemaId", target = "id")
	@Mapping(source = "cinemaName", target = "name")
	@Mapping(source = "cinemaAddress", target = "address")
	@Mapping(source = "cinemaImg", target = "img")
	@Mapping(source = "contactPhone", target = "phone")
	@Mapping(source = "ticketTypes", target = "types")
	CinemaDTO toDto(Cinema cinema);

	@Mapping(target = "cinemaId", ignore = true)
	@Mapping(target = "screeningRooms", ignore = true)
	@Mapping(target = "ticketTypes", ignore = true)
	@Mapping(target = "cinemaName", source = "name")
	@Mapping(target = "cinemaImg", source = "img")
	@Mapping(target = "cinemaAddress", source = "address")
	@Mapping(target = "contactPhone", source = "phone")
	void updateFromDto(CinemaDTO dto, @MappingTarget Cinema cinema);

}
