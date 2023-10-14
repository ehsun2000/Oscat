package com.oscat.cinema.service;

import com.oscat.cinema.dto.CinemaDTO;

public interface ICinameInfoService {

	// 找尋一筆影城資料
	CinemaDTO findCinemaById(Integer id);
	
	
	// 找尋分頁影城資料
}
