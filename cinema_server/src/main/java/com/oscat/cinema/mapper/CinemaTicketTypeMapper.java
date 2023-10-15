package com.oscat.cinema.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

import com.oscat.cinema.dto.TicketTypeDTO;
import com.oscat.cinema.entity.Cinema;
import com.oscat.cinema.entity.CinemaTicketType;
import com.oscat.cinema.entity.TicketType;

//使用 mapstruct 轉換 dto
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface CinemaTicketTypeMapper {
	
	
	// 將 TicketType 映射到 TicketTypeDTO
	@Mapping(target = "type", source = "ticketType.ticketTypeName")
	@Mapping(target = "different", source = "ticketType.priceDifference")
	TicketTypeDTO toDto(CinemaTicketType ticketType);

	List<TicketTypeDTO> toDto(List<CinemaTicketType> ticketTypes);
}
