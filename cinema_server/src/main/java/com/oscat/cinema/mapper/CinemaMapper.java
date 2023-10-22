package com.oscat.cinema.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;
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
import com.oscat.cinema.entity.CinemaTicketType;
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

	@Mapping(target = "cinemaId", ignore = true)
	@Mapping(target = "screeningRooms", ignore = true)
	@Mapping(target = "ticketTypes", ignore = true)
	@Mapping(target = "products", ignore = true)
	@Mapping(target = "cinemaName", source = "dto.name")
	@Mapping(target = "cinemaImg", source = "dto.img")
	@Mapping(target = "cinemaAddress", source = "dto.address")
	@Mapping(target = "contactPhone", source = "dto.phone")
	void updateFromDto(CinemaDTO dto, @MappingTarget Cinema cinema, List<TicketType> ticketTypes);

	@Named("toEntityWithCinema")
	default CinemaTicketType toEntityWithCinema(String type, Cinema cinema, List<TicketType> ticketTypes) {
		CinemaTicketType entity = new CinemaTicketType();
		entity.setCinemaId(cinema.getCinemaId());
		entity.setCinema(cinema);
		// 建立 map 使用 name 查詢
		Map<String, TicketType> map = ticketTypes.stream()
				.collect(Collectors.toMap(TicketType::getTicketTypeName, Function.identity()));
		
		TicketType ticketType = map.get(type);
		// 如果有找到對應的 type 即加入
		if (ticketType != null) {
			entity.setTicketType(ticketType);
			entity.setTicketTypeId(ticketType.getTicketTypeId());
			return entity;
		}

		return null;
	}

	// 在 mapping 之後，映射 typeDto 欄位到 tickettype 中，並檢查是否更改、刪除
	@AfterMapping
	default void handleTicketTypes(@MappingTarget Cinema cinema, CinemaDTO cinemaDTO, List<TicketType> ticketTypes) {
		if (cinemaDTO.getTypes() != null) {
			// 從目標實體中取出既有的 CinemaTicketType
			List<CinemaTicketType> existingTicketTypes = cinema.getTicketTypes();

			// 創建一個 Map 以便快速查找
			Map<String, CinemaTicketType> existingTicketTypeMap = existingTicketTypes.stream().collect(Collectors.toMap(
					cinemaTicketType -> cinemaTicketType.getTicketType().getTicketTypeName(), Function.identity()));

			// 標記需要刪除的 CinemaTicketType
			List<CinemaTicketType> toRemove = new ArrayList<>();

			for (CinemaTicketType existingTicketType : existingTicketTypes) {
				if (!cinemaDTO.getTypes().stream()
						.anyMatch(type -> type.equals(existingTicketType.getTicketType().getTicketTypeName()))) {
					toRemove.add(existingTicketType);
				}
			}

			// 刪除標記為需要刪除的 CinemaTicketType
			existingTicketTypes.removeAll(toRemove);

			// 判斷是否包含 types 中的 type，沒有的話新增一筆加入 List
			for (String type : cinemaDTO.getTypes()) {
				CinemaTicketType existingTicketType = existingTicketTypeMap.get(type);

				if (existingTicketType == null) {
					// 如果不存在，創建新的 TicketType 並加入到列表中
					CinemaTicketType entityWithCinema = toEntityWithCinema(type, cinema, ticketTypes);
					if (entityWithCinema != null)
						existingTicketTypes.add(entityWithCinema);
				}
			}

			// 將 ticketTypes set 回 cinema（如果使用的是持久化對象，這步可能不是必需的，因為對象可能已經是持久化的）
//	        cinema.setTicketTypes(existingTicketTypes);
		}
	}
}
