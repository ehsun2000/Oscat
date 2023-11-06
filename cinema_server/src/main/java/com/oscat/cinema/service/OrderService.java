package com.oscat.cinema.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oscat.cinema.dao.ShowTimeRepository;
import com.oscat.cinema.dao.TicketRepository;
import com.oscat.cinema.dao.TransOrderRepository;
import com.oscat.cinema.dao.MemberRepository;
import com.oscat.cinema.dao.TicketTypeRepository;
import com.oscat.cinema.dao.SeatRepository;
import com.oscat.cinema.dto.OrderDTO;
import com.oscat.cinema.dto.OrderDTO.TicketRequest;
import com.oscat.cinema.entity.Ticket;
import com.oscat.cinema.entity.TransOrder;

@Service
public class OrderService {
    @Autowired
    private TransOrderRepository transOrderRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private ShowTimeRepository showTimeRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private TicketTypeRepository ticketTypeRepository;
    @Autowired
    private SeatRepository seatRepository;

    @Transactional
    public void createOrder(OrderDTO request) {
    	
        TransOrder order = new TransOrder();
        order.setShowTime(showTimeRepository.findById(request.getShowtimeId()).orElse(null));
        order.setMember(memberRepository.findById(request.getMemberId()).orElse(null));
        order.setPaymentMethod(request.getPaymentMethod());
        order.setBookingDateAndTime(LocalDateTime.now());
        order.setTotalPrice(request.getTotalPrice());
        transOrderRepository.save(order);

        for (TicketRequest ticketRequest : request.getTickets()) {
            Ticket ticket = new Ticket();
            ticket.setTransOrder(transOrderRepository.findById(order.getOrderId()).orElse(null));
            ticket.setTicketType(ticketTypeRepository.findById(ticketRequest.getTypeId()).orElse(null));
            ticket.setSeat(seatRepository.findById(ticketRequest.getSeatId()).orElse(null));
            ticketRepository.save(ticket);
        }
    }
}
