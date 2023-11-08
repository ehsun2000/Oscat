package com.oscat.cinema.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.oscat.cinema.dao.CinemaRepository;
import com.oscat.cinema.dao.MovieRepository;
import com.oscat.cinema.dao.ScreeningRoomRepository;
import com.oscat.cinema.dao.ShowTimeRepository;
import com.oscat.cinema.dto.RequestShowTime;
import com.oscat.cinema.dto.ResponseCinemaForShowTime;
import com.oscat.cinema.dto.ScreeningRoomDTO;
import com.oscat.cinema.dto.ShowTimeDTO;
import com.oscat.cinema.dto.ShowTimeForPut;
import com.oscat.cinema.entity.Cinema;
import com.oscat.cinema.entity.Movie;
import com.oscat.cinema.entity.OpeningHour;
import com.oscat.cinema.entity.ScreeningRoom;
import com.oscat.cinema.entity.ShowTime;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class ShowTimeManagerService {

	@Autowired
	private ShowTimeRepository showTimeRepository;
	@Autowired
	private MovieRepository movieRepository;
	@Autowired
	private ScreeningRoomRepository roomRepo;
	@Autowired
	private CinemaRepository cinemaRepository;
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	public ShowTimeManagerService(ShowTimeRepository showTimeRepository, MovieRepository movieRepository) {
		this.showTimeRepository = showTimeRepository;
		this.movieRepository = movieRepository;
	}

	// 新增場次
	public ShowTime addShowTime(ShowTime showTime) {
		showTimeRepository.flush();
		ShowTime save = showTimeRepository.saveAndFlush(showTime);
		return save;
	}

	// 新增場次，片長超過規定須格外加價
	public Optional<ShowTime> convertToEntity(ShowTimeDTO showTimeDTO) {
		ShowTime showTime = new ShowTime();
		showTime.setFilmType(showTimeDTO.getFilmType());
		showTime.setShowDateAndTime(showTimeDTO.getShowDateAndTime());
		Optional<Movie> movie = movieRepository.findById(showTimeDTO.getMovieId());
		Optional<ScreeningRoom> room = roomRepo.findById(showTimeDTO.getRoomId());
		if (movie.isPresent() && room.isPresent()) {
			if (movie.get().getDuration() > 110) { // 片長>110分
				showTime.setExtraFee(new BigDecimal(30)); // +30
			} else {
				showTime.setExtraFee(new BigDecimal(0)); // +0
			}

			showTime.setMovie(movie.get());
			showTime.setScreeningRoom(room.get());
			return Optional.of(showTime);
		} else {
			return Optional.empty();
		}

	}

	// 電影片長的30分鐘內，不得再新增場次
	public boolean isShowTimeAvailable(ShowTimeDTO showTimeDTO) {
		LocalDateTime showDateTime = showTimeDTO.getShowDateAndTime();

		// 獲取電影資訊
		Optional<Movie> movie = movieRepository.findById(showTimeDTO.getMovieId());

		if (movie.isPresent()) {
			// 從電影資訊中獲取電影時長
			int movieDuration = movie.get().getDuration();
			LocalDateTime showEndTime = showDateTime.plusMinutes(movieDuration + 30); // 計算場次結束時間

			// 獲取相同roomId的場次
			List<ShowTime> conflictingShowTimesAfter = showTimeRepository
					.findAfterConflictingShowTimesByRoomId(showDateTime, showEndTime, showTimeDTO.getRoomId());

			ShowTime findLatestShowTimeBefore = showTimeRepository.findLatestShowTimeBefore(showDateTime,
					showTimeDTO.getRoomId());

			if (findLatestShowTimeBefore == null) {
				if (conflictingShowTimesAfter.isEmpty()) {
					return true;
				}
			} else {
				LocalDateTime lastShowTime = findLatestShowTimeBefore.getShowDateAndTime();
				Integer duration = findLatestShowTimeBefore.getMovie().getDuration();

				LocalDateTime lastShowTimeEnd = lastShowTime.plusMinutes(duration + 30);

				if (!showDateTime.isBefore(lastShowTimeEnd) && conflictingShowTimesAfter.isEmpty()) {
					return true;
				}
			}

		}

		return false;
	}

	// 根據 ShowTimeDTO 和影城的營業時間判斷場次是否有效
	public boolean isShowTimeValid(ShowTimeDTO showTimeDTO) {
		LocalDateTime showDateTime = showTimeDTO.getShowDateAndTime();
		Optional<ScreeningRoom> roomOpt = roomRepo.findById(showTimeDTO.getRoomId());

		if (roomOpt.isPresent()) {
			Cinema cinema = roomOpt.get().getCinema();

			// 獲取影城的營業時間
			List<OpeningHour> OpeningHours = cinema.getOpeningHours();
			Map<Integer, OpeningHour> openingMap = OpeningHours.stream()
					.collect(Collectors.toMap(OpeningHour::getWeekDay, Function.identity()));

			// 找出當天營業、閉店時間
			OpeningHour openingHour = openingMap.get(showTimeDTO.getShowDateAndTime().getDayOfWeek().getValue());
			LocalTime openingTime = openingHour.getStartTime();
			LocalTime closingTime = openingHour.getEndTime();

			// 檢查場次時間是否在營業時間內
			if (showDateTime.toLocalTime().isBefore(openingTime) || showDateTime.toLocalTime().isAfter(closingTime)) {
				return false; // 場次時間不在營業時間內
			}
		}

		return true;
	}

	// 找全部
	public Page<ShowTime> findAll(Pageable pageable, Integer roomId) {
		// 調用相應的 ShowTimeRepository 方法來查找所有 show times
		return showTimeRepository.findAllByRoomId(pageable, roomId);
	}

	// 刪除單筆場次
	public boolean deleteShowTime(UUID showTimeId) {
		try {
			showTimeRepository.deleteById(showTimeId);
			return true; // 成功刪除
		} catch (Exception e) {
			return false; // 刪除失敗
		}
	}

	// 找當天場次
	public List<Object[]> findTodayShowTimes() throws ClassNotFoundException {
		// 獲取當前日期
		LocalDate today = LocalDate.now();
		// 設定開始時間為當天的凌晨
		LocalDateTime startOfDay = today.atStartOfDay();
		// 設定結束時間為當天的最後一刻
		LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

		// 定義 JPQL 查詢，用於查詢當天的場次
		String jpql = "SELECT st.movie.movieId, st.movie.movieName, st.showDateAndTime " + "FROM ShowTime st "
				+ "WHERE st.showDateAndTime >= :startOfDay " + "AND st.showDateAndTime <= :endOfDay";

		// 使用 JPQL 查詢來獲取當天的場次資訊
		@SuppressWarnings("unchecked")
		List<Object[]> todayShowTimes = entityManager.createQuery(jpql).setParameter("startOfDay", startOfDay)
				.setParameter("endOfDay", endOfDay).getResultList();

		if (todayShowTimes.isEmpty()) {
			throw new ClassNotFoundException("No showtimes found for today.");
		}

		return todayShowTimes;

	}

	// 找一週內場次
	public List<Object[]> findShowTimesForNextWeek() throws ClassNotFoundException {
		LocalDate today = LocalDate.now();
		LocalDate oneWeekLater = today.plusDays(7); // 从今天开始加七天
		LocalDateTime startOfToday = today.atStartOfDay();
		LocalDateTime endOfOneWeekLater = oneWeekLater.atTime(LocalTime.MAX);

		String jpql = "SELECT st.movie.movieId, st.movie.movieName, st.showDateAndTime " + "FROM ShowTime st "
				+ "WHERE st.showDateAndTime >= :startOfWeek " + "AND st.showDateAndTime <= :endOfWeek";

		@SuppressWarnings("unchecked")
		List<Object[]> showTimesForNextWeek = entityManager.createQuery(jpql).setParameter("startOfWeek", startOfToday)
				.setParameter("endOfWeek", endOfOneWeekLater).getResultList();

		if (showTimesForNextWeek.isEmpty()) {
			throw new ClassNotFoundException("無場次");
		}

		return showTimesForNextWeek;
	}

	public List<ResponseCinemaForShowTime> findAllCinemas() {
		List<Cinema> cinemas = cinemaRepository.findAll();
		List<ResponseCinemaForShowTime> cinemaList = cinemas.stream().map(cinema -> toCinemasDto(cinema)).toList();

		return cinemaList;
	}

	private ResponseCinemaForShowTime toCinemasDto(Cinema cinema) {
		ResponseCinemaForShowTime dto = new ResponseCinemaForShowTime();

		dto.setCinema(cinema.getCinemaName());
		dto.setRooms(cinema.getScreeningRooms().stream().map(room -> toScreeningRoomDTO(room)).toList());

		return dto;
	}

	private ScreeningRoomDTO toScreeningRoomDTO(ScreeningRoom room) {
		ScreeningRoomDTO dto = new ScreeningRoomDTO();

		dto.setRoomId(room.getRoomId());
		dto.setRoomName(room.getRoomName());
		dto.setType(room.getType());

		return dto;
	}

	public List<Map<String, String>> findMovies() {
		List<Movie> movies = movieRepository.findAll();
		List<Map<String, String>> dtoList = movies.stream().map(movie -> toMovieDto(movie)).toList();

		return dtoList;
	}

	private Map<String, String> toMovieDto(Movie movie) {
		Map<String, String> dto = new HashMap<>();

		dto.put("movie", movie.getMovieName());
		dto.put("id", movie.getMovieId().toString());

		return dto;
	}

	public List<Map<String, String>> findShowTimes(RequestShowTime req) {
		List<ShowTime> showtimes = showTimeRepository.findByStartAndEnd(req.getRoomId(), req.getStart(), req.getEnd());
		List<Map<String, String>> dtoList = showtimes.stream().map(showtime -> toShowTimeDto(showtime)).toList();

		return dtoList;
	}

	private Map<String, String> toShowTimeDto(ShowTime showTime) {
		Map<String, String> dto = new HashMap<>();

		dto.put("showTimeId", showTime.getShowTimeId().toString());
		dto.put("movieName", showTime.getMovie().getMovieName());
		dto.put("showtime", showTime.getShowDateAndTime().toString());
		dto.put("flimType", showTime.getFilmType());
		dto.put("duration", showTime.getMovie().getDuration().toString());

		return dto;
	}

	// 透過ID找場次
	public Map<String, String> findShowTimeById(UUID id) {
		Optional<ShowTime> showtimeOpt = showTimeRepository.findById(id);
		if (showtimeOpt.isPresent()) {
			return toShowTimeDto(showtimeOpt.get());
		}

		return null;
	}

	// 更新場次
	public String updateShowTime(ShowTimeForPut dto) {
		Optional<ShowTime> oldShowTimeOpt = showTimeRepository.findById(dto.getShowtimeId());
		Optional<Movie> movieOpt = movieRepository.findById(dto.getMovieId());

		if (oldShowTimeOpt.isPresent() && movieOpt.isPresent()) {
			ShowTime showTime = oldShowTimeOpt.get();
			Movie movie = movieOpt.get();

			List<OpeningHour> openingHours = showTime.getScreeningRoom().getCinema().getOpeningHours();
			Integer duration = movie.getDuration();

			boolean inOpening = inOpening(openingHours, dto.getShowDateAndTime(), duration);

			if (inOpening) {
				if (isAvailable(dto.getRoomId(), showTime.getShowTimeId(), dto.getShowDateAndTime(), duration)) {
					showTime.setFilmType(dto.getFilmType());
					showTime.setShowDateAndTime(dto.getShowDateAndTime());
					showTime.setMovie(movie);

					showTimeRepository.flush();
					showTimeRepository.saveAndFlush(showTime);
					return "success";
				} else {
					return "與其他場次發生衝突";
				}
			} else {
				return "沒有在營業時間內";
			}
		}

		return "查無電影 or 場次";
	}

	// 判斷是否衝突場次
	private boolean isAvailable(Integer roomId, UUID oldShowTimeId, LocalDateTime startTime, Integer duration) {
		// 所需屬性: roomId、oldShowTimeId、startTime、duration
		// 找出原本場次以外，播放時間之後的第一筆資料，若有，判斷結束時間是否在其開始之前
		ShowTime afterShow = showTimeRepository.findAfterShow(roomId, oldShowTimeId, startTime);
		if (afterShow != null) {
			System.out.println("afterShow:" + afterShow.getShowDateAndTime().toString());
			if (startTime.plusMinutes(duration + 30).isAfter(afterShow.getShowDateAndTime())) {
				return false;
			}
		}

		// 找出原本場次以外，之前的最後一筆資料，若有，判斷新時間是否在其結束之後
		ShowTime beforeShow = showTimeRepository.findBeforeShow(roomId, oldShowTimeId, startTime);
		System.out.println("beforeShow:" + beforeShow.getShowDateAndTime().toString());
		if (beforeShow != null) {
			Integer beforeDuration = beforeShow.getMovie().getDuration() + 30;
			System.out.println(
					"beforeShow:" + startTime.isBefore(beforeShow.getShowDateAndTime().plusMinutes(beforeDuration)));
			if (startTime.isBefore(beforeShow.getShowDateAndTime().plusMinutes(beforeDuration))) {
				return false;
			}
		}

		return true;
	}

	// 判斷是否在營業時間內
	private boolean inOpening(List<OpeningHour> openingHours, LocalDateTime showTime, Integer duration) {

		// 獲取影城的營業時間
		List<OpeningHour> OpeningHours = openingHours;
		Map<Integer, OpeningHour> openingMap = OpeningHours.stream()
				.collect(Collectors.toMap(OpeningHour::getWeekDay, Function.identity()));

		// 找出當天營業、閉店時間
		OpeningHour openingHour = openingMap.get(showTime.getDayOfWeek().getValue());
		LocalTime openingTime = openingHour.getStartTime();
		LocalTime closingTime = openingHour.getEndTime();

		// 檢查場次時間是否在營業時間內
		if (showTime.toLocalTime().isBefore(openingTime)
				|| showTime.toLocalTime().plusMinutes(duration).isAfter(closingTime)) {
			return false;
		}
		return true;
	}

	private void overtimeFee(Integer duration, ShowTime showTime) {
		if (duration > 110) {
			showTime.setExtraFee(new BigDecimal(30));
		}
	}

}
