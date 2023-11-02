package com.oscat.cinema.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oscat.cinema.dao.TicketTypeRepository;
import com.oscat.cinema.entity.TicketType;

@Service
public class TicketTypeService {

	@Autowired
	private TicketTypeRepository tickettypeRepo;

	
	// 新增
	public TicketType createTicketType(TicketType ticketType) {
		return tickettypeRepo.save(ticketType);
	}
	
	
	// 更新
	public boolean updateTicketType(TicketType updatedTicketType) {
	    // 根據票種ID查找現有的票種資料
	    TicketType existingTicketType = tickettypeRepo.findById(updatedTicketType.getTicketTypeId()).orElse(null);

	    if (existingTicketType != null) {
	        // 更新票種資訊
	        existingTicketType.setTicketTypeName(updatedTicketType.getTicketTypeName());
	        existingTicketType.setPriceDifference(updatedTicketType.getPriceDifference());

	        // 保存更新後的票種訊息
	        tickettypeRepo.save(existingTicketType);

	        return true;
	    }

	    return false;
	}
	
	// 刪除
	public boolean deleteTicketType(Integer ticketTypeId) {
	    // 檢查票種是否存在
	    Optional<TicketType> optionalTicketType = tickettypeRepo.findById(ticketTypeId);

	    if (optionalTicketType.isPresent()) {
	        // 删除票種
	    	tickettypeRepo.deleteById(ticketTypeId);
	        return true;
	    }

	    return false;
	}



}
