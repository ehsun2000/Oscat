package com.oscat.cinema.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.oscat.cinema.dao.CinemaRepository;
import com.oscat.cinema.dao.CinemaTicketTypeRepository;
import com.oscat.cinema.dao.FacilityRepository;
import com.oscat.cinema.dao.ProductRepository;
import com.oscat.cinema.dao.TicketTypeRepository;
import com.oscat.cinema.dto.CinemaDTO;
import com.oscat.cinema.entity.Cinema;
import com.oscat.cinema.entity.Facility;
import com.oscat.cinema.entity.Product;
import com.oscat.cinema.entity.TicketType;
import com.oscat.cinema.mapper.CinemaMapper;
import com.oscat.cinema.mapper.FacilityMapper;
import com.oscat.cinema.mapper.TicketTypeMapper;
import com.oscat.cinema.service.ICinameInfoService;
import com.oscat.cinema.util.CloudinaryUtil;

import jakarta.transaction.Transactional;

@Service
public class CinameInfoService implements ICinameInfoService {
	private final CinemaRepository cinemaRepo;
	private final CinemaMapper cinemaMapper;
	private final TicketTypeRepository typeRepository;
	private final TicketTypeMapper ticketTypeMapper;
	private final FacilityRepository facilityRepository;
	private final FacilityMapper facilityMapper;
	private final CloudinaryUtil cloudinaryUtil;
	// 建構子注入
	@Autowired
	public CinameInfoService(CinemaRepository cinemaRepo, CinemaMapper cinmeMapper,
			TicketTypeRepository typeRepository, FacilityRepository facilityRepository,
			TicketTypeMapper ticketTypeMapper,FacilityMapper facilityMapper,
			CloudinaryUtil cloudinaryUtil) {
		this.cinemaRepo = cinemaRepo;
		this.cinemaMapper = cinmeMapper;
		this.typeRepository = typeRepository;
		this.ticketTypeMapper = ticketTypeMapper;
		this.facilityRepository = facilityRepository;
		this.facilityMapper = facilityMapper;
		this.cloudinaryUtil = cloudinaryUtil;
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
		List<TicketType> types = typeRepository.findAll();
		List<Facility> facilities = facilityRepository.findAll();

//		判斷是否有找到值
		if (existingCinema.isPresent()) {

			Cinema cinema = existingCinema.get();
//			缺少 validator
			cinemaMapper.updateFromDto(dto, cinema, types, facilities);
			cinemaRepo.flush();

			cinemaRepo.save(cinema);

			return true;
		} else {
			return false;
		}
	}

	public List<String> findAllTicketTypes() {
		List<TicketType> types = typeRepository.findAll();
		return ticketTypeMapper.toStrArr(types);
	}

	public List<String> findAllFacilities() {
		List<Facility> facilities = facilityRepository.findAll();
		return facilityMapper.toDtos(facilities);
	}

	@Transactional
	public String updateImg(MultipartFile file, Integer id) {
		Optional<Cinema> cinemaOpt = cinemaRepo.findById(id);
		if(cinemaOpt.isPresent()) {
			Cinema cinema = cinemaOpt.get();
			// 上傳圖片
			String url = cloudinaryUtil.uploadImage(file);
			if(url == null) {
				return "Upload new picture faild!";
			}
			
			// 刪除舊圖片
			boolean deleteImage = cloudinaryUtil.deleteImage(cinema.getCinemaImg());
			if(deleteImage == false) {
				return "Delete old picture faild!";
			}
			cinema.setCinemaImg(url);
			cinemaRepo.flush();
			cinemaRepo.save(cinema);
		}
		return "Success";
	}

}
