package com.oscat.cinema.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oscat.cinema.dao.CinemaRepository;
import com.oscat.cinema.dao.ShowTimeRepository;
import com.oscat.cinema.dao.TicketRepository;
import com.oscat.cinema.dao.TicketTypeRepository;
import com.oscat.cinema.dto.SearchCinemaForBook;
import com.oscat.cinema.dto.SearchShowDateForBook;
import com.oscat.cinema.dto.TicketTypeDTO;
import com.oscat.cinema.entity.Cinema;
import com.oscat.cinema.entity.ShowTime;
import com.oscat.cinema.entity.Ticket;
import com.oscat.cinema.entity.TicketType;

import jakarta.persistence.EntityNotFoundException;

@Service
public class BookingService {

	private CinemaRepository cinemaRepository;
	private ShowTimeRepository showTimeRepository;
	private TicketTypeRepository ticketTypeRepository;
	private TicketRepository ticketRepository;

	@Autowired
	public BookingService(CinemaRepository cinemaRepository, ShowTimeRepository showTimeRepository,
			TicketTypeRepository ticketTypeRepository, TicketRepository ticketRepository) {
		this.cinemaRepository = cinemaRepository;
		this.showTimeRepository = showTimeRepository;
		this.ticketTypeRepository = ticketTypeRepository;
		this.ticketRepository = ticketRepository;
	}

	public List<SearchCinemaForBook> findCinemas(UUID movieId) {
		List<SearchCinemaForBook> cinemas = cinemaRepository.findCinemasByMovieId(movieId,
				LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0),
				LocalDateTime.now().plusDays(7).withHour(23).withMinute(59).withSecond(59).withNano(999999999));

		if (!cinemas.isEmpty()) {
			return cinemas;
		}
		return null;
	}

	public Set<SearchShowDateForBook> findShowDate(UUID movieId, Integer cinemaId) {
		Set<SearchShowDateForBook> showDates = showTimeRepository.findShowDateByMovieIdAndCinemaId(movieId, cinemaId,
				LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0),
				LocalDateTime.now().plusDays(7).withHour(23).withMinute(59).withSecond(59).withNano(999999999));
		return showDates;
	}

	public List<Map<String, Object>> findShowTime(UUID movieId, Integer cinemaId) {
		LocalDateTime startDate = LocalDate.now().atStartOfDay();
		LocalDateTime endDate = startDate.plusDays(7).withHour(23).withMinute(59).withSecond(59).withNano(999999999);

		List<Object[]> result = showTimeRepository.findShowTimeByMovieIdAndCinemaIdAndDate(movieId, cinemaId, startDate,
				endDate);

		List<Map<String, Object>> responseList = new ArrayList<>();
		for (Object[] obj : result) {
			Map<String, Object> entry = new HashMap<>();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			entry.put("showtimeId", (UUID) obj[0]);
			entry.put("screeningRoomId", (Integer) obj[1]);
			entry.put("roomName", (String) obj[2]);
			entry.put("showtime", formatter.format((LocalDateTime) obj[3]));
			entry.put("filmType", (String) obj[4]);
			responseList.add(entry);
		}

		return responseList;
	}

	public List<TicketTypeDTO> getTicketTypesList() {
		List<Object[]> findObj = ticketTypeRepository.findAllTypes();
		List<TicketTypeDTO> ticketTypeList = new ArrayList<>();

		for (Object[] obj : findObj) {
			Integer typeId = (Integer) obj[0];
			String typeName = (String) obj[1];
			BigDecimal price = (BigDecimal) obj[2];
			ticketTypeList.add(new TicketTypeDTO(typeId, typeName, price));
		}

		return ticketTypeList;
	}

	public List<Ticket> getTicketsByShowtimeId(UUID showTimeId) {
		return ticketRepository.findByTransOrder_ShowTime_ShowTimeId(showTimeId);
	}
	
    public BigDecimal calculateTotalPrice(String cinemaName, UUID showtimeId, Map<Integer, Integer> ticketTypeCounts) {
        // 首先找到對應的影城，以獲得基礎票價
        Cinema cinema = cinemaRepository.findCinemaByName(cinemaName);
        if (cinema == null) {
            throw new EntityNotFoundException("Cinema not found");
        }

        // 接著找到對應的場次，以獲得額外費用
        ShowTime showtime = showTimeRepository.findById(showtimeId)
                .orElseThrow(() -> new EntityNotFoundException("Showtime not found"));

        // 接下來，對每種票類型的數量進行計算
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (Map.Entry<Integer, Integer> entry : ticketTypeCounts.entrySet()) {
            // 找到票種類型，以獲得價格差異
            TicketType ticketType = ticketTypeRepository.findById(entry.getKey())
                    .orElseThrow(() -> new EntityNotFoundException("Ticket type not found"));

            BigDecimal priceForThisType = cinema.getBasePrice()
                    .add(ticketType.getPriceDifference())
                    .add(showtime.getExtraFee())
                    .multiply(new BigDecimal(entry.getValue()));
            totalPrice = totalPrice.add(priceForThisType);
        }

        return totalPrice;
    }

}
