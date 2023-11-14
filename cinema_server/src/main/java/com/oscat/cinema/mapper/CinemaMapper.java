package com.oscat.cinema.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import com.oscat.cinema.dto.BusinessHourDto;
import com.oscat.cinema.dto.CinemaDTO;
import com.oscat.cinema.entity.Cinema;
import com.oscat.cinema.entity.CinemaTicketType;
import com.oscat.cinema.entity.Facility;
import com.oscat.cinema.entity.Product;
import com.oscat.cinema.entity.TicketType;

// 使用 mapstruct 轉換 dto
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR
// 透過 TicketTypeMapper 將 List<TicketType> 轉換成 List<TicketTypeDTO>
		, uses = { CinemaTicketTypeMapper.class, FacilityMapper.class, 
				OpeningHourMapper.class, ProductMapper.class })
public interface CinemaMapper {
//	映射不同屬性名稱
	@Mapping(source = "cinemaId", target = "id")
	@Mapping(source = "cinemaName", target = "name")
	@Mapping(source = "cinemaAddress", target = "address")
	@Mapping(source = "cinemaImg", target = "img")
	@Mapping(source = "openingHours", target = "businessHours")
	@Mapping(source = "facilities", target = "facilities")
	@Mapping(source = "contactPhone", target = "phone")
	@Mapping(source = "ticketTypes", target = "types")
	CinemaDTO toDto(Cinema cinema);

	@Mapping(target = "cinemaId", ignore = true)
	@Mapping(target = "screeningRooms", ignore = true)
	@Mapping(target = "ticketTypes", ignore = true)
	@Mapping(target = "products", ignore = true)
	@Mapping(target = "openingHours", ignore = true)
	@Mapping(target = "facilities", ignore = true)
	@Mapping(target = "cinemaImg", ignore = true)
	@Mapping(target = "cinemaName", source = "dto.name")
	@Mapping(target = "cinemaAddress", source = "dto.address")
	@Mapping(target = "contactPhone", source = "dto.phone")
	void updateFromDto(CinemaDTO dto, @MappingTarget Cinema cinema, List<TicketType> ticketTypes,
			List<Facility> facilities, List<Product> products);

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

	@Named("toEntityWithFacility")
	default List<Facility> toFacilityEntity(List<String> inputFacs, List<Facility> facilities) {
		Map<String, Facility> facMap = facilities.stream()
				.collect(Collectors.toMap(Facility::getFacilityName, facility -> facility));

		List<Facility> finalFac = new ArrayList<>();

		for (String cinemaFac : inputFacs) {
			Optional<Facility> facOpt = Optional.ofNullable(facMap.get(cinemaFac));

			// 比對是否有在原本設施陣列中
			if (facOpt.isPresent()) {
				// 加入影城設施陣列中
				finalFac.add(facOpt.get());
			}
		}
		;

		return finalFac;
	};

	@Named("toEntityWithOpeningHour")
	default void toOpeningHourEntity(List<BusinessHourDto> inputHours, Cinema cinema) {
		Map<Integer, BusinessHourDto> inputOpenHourMap = inputHours.stream()
				.collect(Collectors.toMap(BusinessHourDto::getWeekDay, Function.identity()));

		cinema.getOpeningHours().forEach(openingHour -> {
			BusinessHourDto dto = inputOpenHourMap.get(openingHour.getWeekDay());
			// 檢查 dto、openingHour 中 start 、 end 有無變更 (LocalTime)
			// 若有，則重新 set dto 值進入 openinghour
			if (!openingHour.getStartTime().equals(dto.getStart())) {
				openingHour.setStartTime(dto.getStart());
			}
			if (!openingHour.getEndTime().equals(dto.getEnd())) {
				openingHour.setEndTime(dto.getEnd());
			}
		});
	};

	@Named("toEntityWithProducts")
	default List<Product> toProductEntity(List<String> inputProducts, List<Product> products) {
		Map<String, Product> productMap = products.stream()
				.collect(Collectors.toMap(Product::getProductName, product -> product));

		List<Product> finalProduct = new ArrayList<>();

		for (String cinemaProcuct : inputProducts) {
			Optional<Product> productOpt = Optional.ofNullable(productMap.get(cinemaProcuct));

			// 比對是否有在原本設施陣列中
			if (productOpt.isPresent()) {
				// 加入影城設施陣列中
				finalProduct.add(productOpt.get());
			}
		}
		;

		return finalProduct;
	};

	@AfterMapping
	default void handleCustomMapping(@MappingTarget Cinema cinema, CinemaDTO cinemaDTO, List<TicketType> ticketTypes,
			List<Facility> facilities, List<Product> products) {
		if (cinemaDTO.getTypes() != null) {
			List<CinemaTicketType> existingTicketTypes = cinema.getTicketTypes();

			Map<String, CinemaTicketType> existingTicketTypeMap = existingTicketTypes.stream().collect(Collectors.toMap(
					cinemaTicketType -> cinemaTicketType.getTicketType().getTicketTypeName(), Function.identity()));

			List<CinemaTicketType> toRemove = new ArrayList<>();

			for (CinemaTicketType existingTicketType : existingTicketTypes) {
				if (!cinemaDTO.getTypes().stream()
						.anyMatch(type -> type.equals(existingTicketType.getTicketType().getTicketTypeName()))) {
					toRemove.add(existingTicketType);
				}
			}

			existingTicketTypes.removeAll(toRemove);

			for (String type : cinemaDTO.getTypes()) {
				CinemaTicketType existingTicketType = existingTicketTypeMap.get(type);

				if (existingTicketType == null) {
					CinemaTicketType entityWithCinema = toEntityWithCinema(type, cinema, ticketTypes);
					if (entityWithCinema != null)
						existingTicketTypes.add(entityWithCinema);
				}
			}

			List<Facility> cinemaFacility = toFacilityEntity(cinemaDTO.getFacilities(), facilities);
			List<Product> CinemaProduct = toProductEntity(cinemaDTO.getProducts(), products);
			
			cinema.setFacilities(cinemaFacility);
			cinema.setProducts(CinemaProduct);

			toOpeningHourEntity(cinemaDTO.getBusinessHours(), cinema);
			// 將 ticketTypes set 回 cinema（如果使用的是持久化對象，這步可能不是必需的，因為對象可能已經是持久化的）
		}
	}
}
