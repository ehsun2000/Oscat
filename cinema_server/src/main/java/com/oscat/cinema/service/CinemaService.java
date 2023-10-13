package com.oscat.cinema.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oscat.cinema.dao.CinemaRepository;
import com.oscat.cinema.entity.Cinema;

@Service
public class CinemaService {
	
	
	private CinemaRepository cinemaRepo;
	
	@Autowired
	public CinemaService(CinemaRepository cinemaRepo) {
		this.cinemaRepo = cinemaRepo;
	}

	//新增影城 C
	public void insert(Cinema cin) {
		cinemaRepo.save(cin);
	}
	
	//ID查詢 R
	public Cinema findById(Integer id) {
		Optional<Cinema> optionalCinema = cinemaRepo.findById(id);
		
		if(optionalCinema.isPresent()) {
			return optionalCinema.get();
		}
		return null;
	}
	
	//查全部 R
	public List<Cinema> findAll(){
		return cinemaRepo.findAll();
	}
	
	//修改 U
	@Transactional
	public Cinema updateCinemaById(Integer id,Cinema newCinema){
		Optional<Cinema> optionalCinema = cinemaRepo.findById(id);
		
		if(optionalCinema.isPresent()) {
			Cinema cinema = optionalCinema.get();
			cinema.setCinemaName(newCinema.getCinemaName());
			return cinemaRepo.save(cinema);
		}
		return null;
	}
	//刪除 D
	public boolean deleteById(Integer id) {
		Optional<Cinema> optionalCinema = cinemaRepo.findById(id);
		
		if(optionalCinema.isPresent()) {
			cinemaRepo.deleteById(id);
			return true;
		}
		return false;
	}
}
