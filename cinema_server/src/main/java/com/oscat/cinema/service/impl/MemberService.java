package com.oscat.cinema.service.impl;

import java.util.Optional;
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

	// 新增會員並加密
	@Transactional
	public Member addMember(Member member) {
		String encodedPwd = pwdEncoder.encode(member.getPassword());
		member.setPassword(encodedPwd);
		return memberRepo.save(member);
	}
	
	// 加密
	@Transactional
	public String encodePassword(String password) {
		return pwdEncoder.encode(password);
	}

	// 確認email是否存在
	public boolean checkIfEmailExist(String email) {
		Optional<Member> dbMember = memberRepo.findByEmail(email);
		if (dbMember.isPresent()) {
			return true;
		} else {
			return false;
		}
	}

	// 確認會員登入
	public Member checkLogin(String email, String password) {
		Optional<Member> dbMember = memberRepo.findByEmail(email);
		if (dbMember.isPresent()) {
			if (pwdEncoder.matches(password, dbMember.get().getPassword())) {
				return dbMember.get();
			}
		}
		return null;
	}
	
	// 根據 email 查詢會員
	@Override
	public Member findByEmail(String email) {	
		Optional<Member> optional = memberRepo.findByEmail(email);
		if(optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	// 生驗證碼之後寄 mail
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