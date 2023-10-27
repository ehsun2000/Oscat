package com.oscat.cinema.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oscat.cinema.dao.MovieRepository;
import com.oscat.cinema.dao.ScreeningRoomRepository;
import com.oscat.cinema.dao.ShowTimeRepository;
import com.oscat.cinema.dto.ShowTimeDTO;
import com.oscat.cinema.entity.Movie;
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
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	public ShowTimeManagerService(ShowTimeRepository showTimeRepository, MovieRepository movieRepository) {
		this.showTimeRepository = showTimeRepository;
		this.movieRepository = movieRepository;
	}

	// 新增場次
	public ShowTime addShowTime(ShowTime showTime) {
		System.out.println(showTime);
		showTimeRepository.save(showTime);
		showTimeRepository.flush();
		return showTimeRepository.save(showTime);
	}

	// 新增場次
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

	// 新增多筆場次
	public List<ShowTime> addMoreShowTime(List<ShowTimeDTO> showTimeDTOs) {
		List<ShowTime> createdShowTimes = new ArrayList<>();

		for (ShowTimeDTO showTimeDTO : showTimeDTOs) {
			Optional<ShowTime> optionalShowTime = convertToEntity(showTimeDTO);

			if (optionalShowTime.isPresent()) {
				ShowTime createdShowTime = addShowTime(optionalShowTime.get());
				createdShowTimes.add(createdShowTime);
			}
		}

		return createdShowTimes;
	}

	// 找全部
	public List<ShowTime> findAll() {
		// 調用相應的 ShowTimeRepository 方法來查找所有 show times
		return showTimeRepository.findAll();
	}

	// 透過ID找場次
	public Optional<ShowTime> findShowTimeById(UUID id) {
		// 調用相應的 ShowTimeRepository 方法來查找特定 ID 的 show time
		return showTimeRepository.findById(id);
	}

	// 更新場次資料
	public ShowTime updateShowTime(UUID showTimeId, ShowTimeDTO showTimeDTO) {
		// 先檢查要更新的ShowTime是否存在
		Optional<ShowTime> existingShowTime = showTimeRepository.findById(showTimeId);
		if (existingShowTime.isPresent()) {
			ShowTime showTime = existingShowTime.get();

			// 創建或查找ScreeningRoom對象，然後設置到ShowTime中
			Integer roomId = showTimeDTO.getRoomId();
			ScreeningRoom screeningRoom = roomRepo.findById(roomId).orElse(null);
			showTime.setScreeningRoom(screeningRoom);

			// 更新ShowTime實體的其他屬性，使用DTO的值
			showTime.setShowDateAndTime(showTimeDTO.getShowDateAndTime());

			// 保存更新後的ShowTime實體
			ShowTime updated = showTimeRepository.save(showTime);

			return updated;
		} else {
			// 如果找不到要更新的ShowTime，返回null或拋出自定義異常
			return null;
		}
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

}
