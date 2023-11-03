package com.oscat.cinema.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oscat.cinema.dao.FacilityRepository;
import com.oscat.cinema.entity.Facility;

@Service
public class FacilityService {

	@Autowired
	private FacilityRepository facilityRepo;

	// 新增
	public Facility addFacility(Facility newFacility) {
		return facilityRepo.save(newFacility);
	}

	// 更新
	public boolean updateFacility(Facility updatedFacility) {
		if (facilityRepo.existsById(updatedFacility.getFacilityId())) {
			facilityRepo.save(updatedFacility);
			return true;
		}
		return false;
	}

	// 刪除
	public boolean deleteFacilityById(Integer facilityId) {
		Optional<Facility> facility = facilityRepo.findById(facilityId);
		if (facility.isPresent()) {
			facilityRepo.delete(facility.get());
			return true;
		}
		return false;
	}

	// 找全部
	public List<Facility> getAllFacilities() {
		return facilityRepo.findAll();
	}

}
