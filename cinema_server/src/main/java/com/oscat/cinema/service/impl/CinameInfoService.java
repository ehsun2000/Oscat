package com.oscat.cinema.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oscat.cinema.dao.CinemaRepository;
import com.oscat.cinema.dto.CinemaDTO;
import com.oscat.cinema.entity.Cinema;
import com.oscat.cinema.mapper.CinemaMapper;
import com.oscat.cinema.service.ICinameInfoService;

@Service
public class CinameInfoService implements ICinameInfoService{
	private final CinemaRepository cinemaRepo;
	private final CinemaMapper cinmeMapper;
	
	// 建構子注入
	@Autowired
	public CinameInfoService(CinemaRepository cinemaRepo, CinemaMapper cinmeMapper) {
		this.cinemaRepo = cinemaRepo;
		this.cinmeMapper = cinmeMapper;
	}
	
	// 單筆影城查詢方法
	@Override
	public CinemaDTO findCinemaById(Integer id) {
		Cinema cinema = cinemaRepo.findCinemaById(id);
		return cinmeMapper.toDto(cinema);
	}

}
