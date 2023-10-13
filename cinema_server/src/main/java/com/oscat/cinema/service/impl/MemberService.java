package com.oscat.cinema.service.impl;

import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.oscat.cinema.dao.MemberRepository;
import com.oscat.cinema.entity.Member;
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

	@Transactional
	public void encodePassword(Member member) {
		String encodedPwd = pwdEncoder.encode(member.getPassword());
		member.setPassword(encodedPwd);
	}
	
	@Override
	public Member findByEmail(String email) {
		return memberRepo.findByEmail(email);
	}

	//生驗證碼 寄mail
	@Override
	public void sendVerificationCode(String email,HttpSession session) {
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
		}catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}