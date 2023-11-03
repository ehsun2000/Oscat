package com.oscat.cinema.service;

import com.oscat.cinema.entity.Member;

import jakarta.servlet.http.HttpSession;

public interface IMemberService {
	
	// 新增會員
	Member addMember(Member member);
	
	// 確認 email 是否存在
	boolean checkIfEmailExist(String email);
	
	// 確認是否登入
	Member checkLogin(String email,String password);
	
	// 根據 email查詢會員
	Member findByEmail(String email);
	
	// 寄送驗證碼
	void sendVerificationCode(String email,HttpSession sessiont);
}
