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
public interface TicketTypeMapper {
	
	// 將 TicketType.name 映射到 String
	default String toStrArr(TicketType ticketType) {
		return ticketType.getTicketTypeName();
	};

	List<String> toStrArr(List<TicketType> ticketTypes);
}
