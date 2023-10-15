package com.oscat.cinema.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import com.oscat.cinema.dto.CinemaDTO;
import com.oscat.cinema.dto.TicketTypeDTO;
import com.oscat.cinema.entity.Cinema;
import com.oscat.cinema.entity.TicketType;

// 使用 mapstruct 轉換 dto
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR
// 透過 TicketTypeMapper 將 List<TicketType> 轉換成 List<TicketTypeDTO>
		, uses = { CinemaTicketTypeMapper.class })
public interface CinemaMapper {

//	映射不同屬性名稱
	@Mapping(source = "cinemaId", target = "id")
	@Mapping(source = "cinemaName", target = "name")
	@Mapping(source = "cinemaAddress", target = "address")
	@Mapping(source = "cinemaImg", target = "img")
	@Mapping(source = "contactPhone", target = "phone")
	@Mapping(source = "ticketTypes", target = "types")
	CinemaDTO toDto(Cinema cinema);

//	@Mapping(target = "cinemaId", ignore = true)
//	@Mapping(target = "screeningRooms", ignore = true)
//	@Mapping(target = "ticketTypes", ignore = true)
//	@Mapping(target = "cinemaName", source = "name")
//	@Mapping(target = "cinemaImg", source = "img")
//	@Mapping(target = "cinemaAddress", source = "address")
//	@Mapping(target = "contactPhone", source = "phone")
//	void updateFromDto(CinemaDTO dto, @MappingTarget Cinema cinema);
//
//	@Named("toEntityWithCinema")
//	default TicketType toEntityWithCinema(TicketTypeDTO dto, Cinema cinema) {
//		TicketType entity = new TicketType();
//		entity.setTicketTypeName(dto.getTicketTypeName());
//		entity.setPriceDifference(dto.getPriceDifference());
//		entity.setCinema(cinema);
//		return entity;
//	}
//	
//	// 在 mapping 之後，映射 typeDto 欄位到 tickettype 中，並檢查是否更改、刪除
//	@AfterMapping
//	default void handleTicketTypes(@MappingTarget Cinema cinema, CinemaDTO cinemaDTO) {
//	    if (cinemaDTO.getTypes() != null) {
//	        // 從目標實體中取出既有的 ticketTypes
//	        List<TicketType> existingTicketTypes = cinema.getTicketTypes();
//	        
//	        // 創建一個 Map 以便快速查找
//	        Map<String, TicketType> existingTicketTypeMap = existingTicketTypes.stream()
//	            .collect(Collectors.toMap(TicketType::getTicketTypeName, Function.identity()));
//
//	        // 標記需要刪除的 TicketType
//	        List<TicketType> toRemove = new ArrayList<>();
//	        
//	        for (TicketType existingTicketType : existingTicketTypes) {
//	            if (!cinemaDTO.getTypes().stream()
//	                .anyMatch(dto -> dto.getTicketTypeName().equals(existingTicketType.getTicketTypeName()))) {
//	                toRemove.add(existingTicketType);
//	            }
//	        }
//	        
//	        // 刪除標記為需要刪除的 TicketType
//	        existingTicketTypes.removeAll(toRemove);
//	        
//	        // 判斷是否包含 cinemaDTO.types 中的 typename，沒有的話新增一筆加入 List
//	        for (TicketTypeDTO dto : cinemaDTO.getTypes()) {
//	            TicketType existingTicketType = existingTicketTypeMap.get(dto.getTicketTypeName());
//
//	            if (existingTicketType != null) {
//	                // 若包含，檢查 discount 是否相同，有變動重新 set 值進去
//	                if (!existingTicketType.getPriceDifference().equals(dto.getPriceDifference())) {
//	                    existingTicketType.setPriceDifference(dto.getPriceDifference());
//	                }
//	            } else {
//	                // 如果不存在，創建新的 TicketType 並加入到列表中
//	                existingTicketTypes.add(toEntityWithCinema(dto, cinema));
//	            }
//	        }
//
//	        // 將 ticketTypes set 回 cinema（如果使用的是持久化對象，這步可能不是必需的，因為對象可能已經是持久化的）
////	        cinema.setTicketTypes(existingTicketTypes);
//	    }
//	}
}
