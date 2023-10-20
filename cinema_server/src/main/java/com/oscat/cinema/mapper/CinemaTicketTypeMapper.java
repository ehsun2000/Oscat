package com.oscat.cinema.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.oscat.cinema.entity.CinemaTicketType;

//使用 mapstruct 轉換 dto
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface CinemaTicketTypeMapper {

	// 將 TicketType 映射到 TicketTypeDTO
//	@Mapping(target = "type", source = "ticketType.ticketTypeName")
//	@Mapping(target = "different", source = "ticketType.priceDifference")
	default String toDto(CinemaTicketType ticketType) {
		return ticketType.getTicketType().getTicketTypeName();
	};

	List<String> toDto(List<CinemaTicketType> ticketTypes);
}
