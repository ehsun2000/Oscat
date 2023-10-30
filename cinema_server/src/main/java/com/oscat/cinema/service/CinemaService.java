package com.oscat.cinema.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oscat.cinema.dao.CinemaRepository;
import com.oscat.cinema.dao.ProductRepository;
import com.oscat.cinema.entity.Cinema;
import com.oscat.cinema.entity.Product;

@Service
public class CinemaService {
	
	@Autowired
	private CinemaRepository cinemaRepo;
	
	//查詢產品列表
	public List<Product> getProductsByCinema(String cinemaName){
		//查詢指定名稱的影城
		Optional<Cinema> cinemaOptional = cinemaRepo.findByCinemaName(cinemaName);
		//如果影城存在的話
		if(cinemaOptional.isPresent()) {
			//就返回影城的產品列表
			return cinemaOptional.get().getProducts();
		}else {
			//如果沒有這個影城，就返回一個空的產品列表
			return new ArrayList<>();
		}
	}
	

}