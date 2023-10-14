package com.oscat.cinema.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.oscat.cinema.dto.CinemaDTO;
import com.oscat.cinema.entity.Cinema;

// 使用 mapstruct 轉換 dto
@Mapper(componentModel = "spring", 
		unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface CinemaMapper {

//	映射不同屬性名稱
	@Mapping(source = "cinemaId", target = "id")
	@Mapping(source = "cinemaName", target = "name")
	@Mapping(source = "cinemaAddress", target = "address")
	@Mapping(source = "contactPhone", target = "phone")
	CinemaDTO toDto(Cinema cinema);
}