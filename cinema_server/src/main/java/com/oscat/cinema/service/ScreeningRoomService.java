package com.oscat.cinema.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oscat.cinema.dao.ScreeningRoomRepository;
import com.oscat.cinema.dto.ScreeningRoomDTO;
import com.oscat.cinema.entity.ScreeningRoom;

@Service
public class ScreeningRoomService {

	@Autowired
	private ScreeningRoomRepository screeningroomRepo;

	// C
	public void insert(ScreeningRoom screeningroom) {
		screeningroomRepo.save(screeningroom);
	}

	// R id
	public ScreeningRoom findById(Integer id) {
		Optional<ScreeningRoom> optionalSroom = screeningroomRepo.findById(id);

		if (optionalSroom.isPresent()) {
			ScreeningRoom screeningRoom = optionalSroom.get();
			return screeningRoom;
		}
		return null;
	}

	// R all
	public List<ScreeningRoom> findAll() {
		return screeningroomRepo.findAll();
	}

	// U
	@Transactional
	public ScreeningRoom updateScreeningRoomById(Integer id, ScreeningRoom newScreeningRoom) {
		Optional<ScreeningRoom> optionalScreeningRoom = screeningroomRepo.findById(id);

		if (optionalScreeningRoom.isPresent()) {
			ScreeningRoom screeningRoom = optionalScreeningRoom.get();
			screeningRoom.setRoomName(newScreeningRoom.getRoomName());
			return screeningroomRepo.save(screeningRoom);
		}
		return null;
	}

	// D
	public void deleteById(Integer id) {
		screeningroomRepo.deleteById(id);
	}

	public ScreeningRoomDTO convertToDTO(ScreeningRoom screeningRoom) {
		ScreeningRoomDTO dto = new ScreeningRoomDTO();
		dto.setRoomId(screeningRoom.getRoomId());
		dto.setRoomName(screeningRoom.getRoomName());
		dto.setType(screeningRoom.getType());
		return dto;
	}

	public List<ScreeningRoomDTO> getAllScreeningRoomById(Integer cinemaId) {
		List<ScreeningRoom> screeningRooms = screeningroomRepo.findScreeningRoomByCinemaIdOrderById(cinemaId);
		return screeningRooms.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

}
