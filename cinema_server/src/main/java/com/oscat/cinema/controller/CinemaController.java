package com.oscat.cinema.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.oscat.cinema.entity.Cinema;
import com.oscat.cinema.service.CinemaService;


@RestController
@RequestMapping(path = "/api")
public class CinemaController {
	
	@Autowired
	private CinemaService cinemaService;
	
	
	
//	//跳轉頁面
//	@GetMapping("/cinema/add")
//	public String uploadPage(){
//		return "cinema/cinema_uploadPage";
//	}
//	
//	//測試新增單筆
//	@PostMapping("/cinema/add1")
//	public Cinema postAddCinema() {
//		Cinema c1 = new Cinema();
//		c1.setCinemaName("台南影城");
//		c1.setCinameAddress("台南");
//		c1.setContactPhone("(02)2537-1889");
//		c1.setOpeningHours("15");
//		c1.setFacilities("IMAX");
//		
//		Cinema resultcinema = cinemaRepo.save(c1);
//		
//		return resultcinema;
//	}
	
	//新增  
	@PostMapping("/cinema/add")
	public Cinema addCinema(@RequestBody Cinema cinema) {
		cinemaService.insert(cinema);
		return cinema;
	}
	
	//查詢 
	@GetMapping("/cinema/{id}")
	public ResponseEntity<?> findById(@PathVariable Integer id){
		Cinema cinema = cinemaService.findById(id);
		
		if(cinema!=null) {
			return new ResponseEntity<>(cinema,HttpStatus.OK);
		}
		return new ResponseEntity<String>("沒有這筆資料",HttpStatus.NOT_FOUND);
	}
	 
	//查詢全部 
	@GetMapping("/cinemas")
	public List<Cinema> findAll(){
		return	cinemaService.findAll();
	}
	
	//修改
	@PutMapping("/cinema/update")
	public String updateCinemaById(@RequestParam("cId") Integer cinemaId,
			 					  @RequestParam("newName") String newName){
			Cinema cinema = cinemaService.findById(cinemaId);
		if(cinema!=null) {
			cinema.setCinemaName(newName);
			cinemaService.insert(cinema);
			return "修改OK";
		}
		
		return "沒有這筆資料";
	}
	
	//刪除 
	@DeleteMapping("/cinema/delete")
	public ResponseEntity<String> deleteById(@RequestParam Integer id) {
		 boolean deleteById = cinemaService.deleteById(id);
		 
		 if(deleteById) {
			 return new ResponseEntity<>("刪除成功", HttpStatus.OK);
		 }else {
			 return new ResponseEntity<>("沒有這家影城",HttpStatus.NOT_FOUND);
		 }
	}
}
