package com.oscat.cinema.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oscat.cinema.entity.Facility;
import com.oscat.cinema.service.FacilityService;

@RestController
@RequestMapping(path = "/api/facility")
public class FacilityController {

	@Autowired
	private FacilityService facilityService;

	@PostMapping("/add")
	public ResponseEntity<String> addFacility(@RequestBody Facility newFacility) {
		Facility createdFacility = facilityService.addFacility(newFacility);
		if (createdFacility != null) {
			return ResponseEntity.ok("新增成功，設施ID：" + createdFacility.getFacilityId());
		} else {
			return ResponseEntity.badRequest().body("新增失敗");
		}
	}

	@PutMapping("/update")
	public ResponseEntity<String> updateFacility(@RequestBody Facility updatedFacility) {
		boolean isUpdated = facilityService.updateFacility(updatedFacility);
		if (isUpdated) {
			return ResponseEntity.ok("更新成功");
		} else {
			return ResponseEntity.badRequest().body("更新失敗");
		}
	}

	@DeleteMapping("/delete/{facilityId}")
	public ResponseEntity<String> deleteFacility(@PathVariable Integer facilityId) {
		boolean isDeleted = facilityService.deleteFacilityById(facilityId);
		if (isDeleted) {
			return ResponseEntity.ok("删除成功");
		} else {
			return ResponseEntity.badRequest().body("刪除失敗");
		}
	}

}
