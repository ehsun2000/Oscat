package com.oscat.cinema.service;

import com.oscat.cinema.entity.Member;

import jakarta.servlet.http.HttpSession;

public interface IMemberService {
	
	Member addMember(Member member);
	boolean checkIfEmailExist(String email);
	Member checkLogin(String email,String password);
	
	Member findByEmail(String email);
	
	void sendVerificationCode(String email,HttpSession sessiont);
//	public boolean validateOtp(String email, int otp, HttpSession session);
}
