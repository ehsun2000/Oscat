package com.oscat.cinema.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oscat.cinema.dto.CinemaDTO;

public interface ICinameInfoService {

	// 找尋一筆影城資料
	CinemaDTO findCinemaById(Integer id);

	// 找尋分頁影城資料
	Page<CinemaDTO> findAll(Pageable pageable);

	// 更新影城資料
	boolean update(CinemaDTO cinema);
	
}
