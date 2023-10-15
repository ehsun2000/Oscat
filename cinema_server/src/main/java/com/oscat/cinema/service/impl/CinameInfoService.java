package com.oscat.cinema.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.oscat.cinema.dao.CinemaRepository;
import com.oscat.cinema.dto.CinemaDTO;
import com.oscat.cinema.entity.Cinema;
import com.oscat.cinema.entity.TicketType;
import com.oscat.cinema.mapper.CinemaMapper;
import com.oscat.cinema.service.ICinameInfoService;

@Service
public class CinameInfoService implements ICinameInfoService {
	private final CinemaRepository cinemaRepo;
	private final CinemaMapper cinemaMapper;

	// 建構子注入
	@Autowired
	public CinameInfoService(CinemaRepository cinemaRepo,CinemaMapper cinmeMapper) {
		this.cinemaRepo = cinemaRepo;
		this.cinemaMapper = cinmeMapper;
	}

	// 單筆影城查詢方法
	@Override
	public CinemaDTO findCinemaById(Integer id) {
		Cinema cinema = cinemaRepo.findCinemaById(id);
		
		return cinemaMapper.toDto(cinema);
	}

	// 查詢分頁影城
	@Override
	public Page<CinemaDTO> findAll(Pageable pageable) {
		Page<Cinema> cinemas = cinemaRepo.findAll(pageable);
		// lambda 將 Page<Cinema> 中所有 Cinema 物件套用 cinemaMapper 中的 toDto 方法
		return cinemas.map(cinemaMapper::toDto);
	}

	// 更新影城資訊
	@Override
	public boolean update(CinemaDTO dto) {
		Optional<Cinema> existingCinema = cinemaRepo.findById(dto.getId());

//		判斷是否有找到值
		if (existingCinema.isPresent()) {

			Cinema cinema = existingCinema.get();

//			缺少 validator
//			cinemaMapper.updateFromDto(dto, cinema);

			cinemaRepo.save(cinema);

			return true;
		} else {
			return false;
		}
	}

}
