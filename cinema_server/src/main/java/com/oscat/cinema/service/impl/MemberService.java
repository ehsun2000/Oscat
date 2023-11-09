package com.oscat.cinema.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oscat.cinema.dao.MemberRepository;
import com.oscat.cinema.dto.MemberDTO;
import com.oscat.cinema.entity.Member;
import com.oscat.cinema.entity.Seat;
import com.oscat.cinema.entity.ShowTime;
import com.oscat.cinema.entity.Ticket;
import com.oscat.cinema.entity.TicketType;
import com.oscat.cinema.entity.TransOrder;
import com.oscat.cinema.service.IMemberService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;

@Service
public class MemberService implements IMemberService {

	@Autowired
	private PasswordEncoder pwdEncoder;

	@Autowired
	private MemberRepository memberRepo;

	@Autowired
	private JavaMailSender mailSender;

	@Transactional
	public Member addMember(Member member) {
		String encodedPwd = pwdEncoder.encode(member.getPassword());
		member.setPassword(encodedPwd);
		return memberRepo.save(member);
	}

	public boolean checkIfEmailExist(String email) {
		Member dbMember = memberRepo.findByEmail(email);
		if (dbMember != null) {
			return true;
		} else {
			return false;
		}
	}

	public Member checkLogin(String email, String password) {
		Member dbMember = memberRepo.findByEmail(email);
		if (dbMember != null) {
			if (pwdEncoder.matches(password, dbMember.getPassword())) {
				return dbMember;
			}
		}
		return null;
	}

	// 根據 id 查詢會員
	public Member findById(UUID memberId) {
		Optional<Member> optional = memberRepo.findById(memberId);
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	// 加密
	@Transactional
	public String encodePassword(String password) {
		return pwdEncoder.encode(password);
	}

	@Override
	public Member findByEmail(String email) {
		return memberRepo.findByEmail(email);
	}

	// 生驗證碼 寄mail
	@Override
	public void sendVerificationCode(String email, HttpSession session) {
		Integer otpvalue = 0;

		if (StringUtils.isNotEmpty(email)) {
			Random rd = new Random();
			otpvalue = rd.nextInt(2147483647);
		}
		String to = email;
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom("Oscat202309@gmail.com");
			helper.setTo(to);
			helper.setSubject("哈囉");
			helper.setText("你的驗證碼是: " + otpvalue, true);

			mailSender.send(message);
			session.setAttribute("otp", otpvalue);
			session.setAttribute("email", email);
			System.out.println("成功送出");
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	// 更新會員資料
	public Optional<Member> update(MemberDTO newMember) {
		Optional<Member> optional = memberRepo.findById(newMember.getMemberId());
		if (optional.isPresent()) {
			Member updateMember = optional.get();
			updateMember.setMemberId(newMember.getMemberId());
			updateMember.setMemberName(newMember.getMemberName());
			updateMember.setEmail(newMember.getEmail());
			updateMember.setPassword(newMember.getPassword());
			updateMember.setPhone(newMember.getPhone());
			updateMember.setGender(newMember.getGender());
			return Optional.of(memberRepo.save(updateMember));
		} else {
			return Optional.empty();
		}
	}

	// 查詢會員訂單
	public List<Map<String, Object>> getMemberOrders(UUID memberId) {
		List<TransOrder> orders = memberRepo.findOrdersByMid(memberId);
		List<Map<String, Object>> dtos = orders.stream().map(order -> toOrderDto(order)).toList();
		return dtos;
	}

	private Map<String, Object> toOrderDto(TransOrder transOrder) {
		Map<String, Object> dto = new HashMap<>();

		dto.put("orderId", transOrder.getOrderId());
		dto.put("paymentMethod", transOrder.getPaymentMethod());
		dto.put("bookingDateAndTime", transOrder.getBookingDateAndTime());
		dto.put("totalPrice", transOrder.getTotalPrice());
		List<Map<String, String>> tickets = transOrder.getTickets().stream().map(ticket -> toTicketDto(ticket))
				.toList();
		dto.put("tickets", tickets);

		ShowTime showTime = transOrder.getShowTime();
		if (showTime != null) {
			dto.put("movieName", showTime.getMovie().getMovieName());
			dto.put("roomName", showTime.getScreeningRoom().getRoomName());
			dto.put("cinemaName", showTime.getScreeningRoom().getCinema().getCinemaName());
			dto.put("showDateAndTime", showTime.getShowDateAndTime());
		}

		return dto;
	}

	private Map<String, String> toTicketDto(Ticket ticket) {
		Map<String, String> dto = new HashMap<>();
		dto.put("ticketId", ticket.getTicketId().toString());

		TicketType ticketType = ticket.getTicketType();
		if (ticketType != null) {
			dto.put("ticketTypeName", ticketType.getTicketTypeName());
		}

		Seat seat = ticket.getSeat();
		if (seat != null) {
			dto.put("seat", seat.getSeatName());
		}
		return dto;
	}
}