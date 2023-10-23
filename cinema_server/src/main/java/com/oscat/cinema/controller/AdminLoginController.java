package com.oscat.cinema.controller;

import java.security.SecureRandom;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.oscat.cinema.dto.AuthenticationRequest;
import com.oscat.cinema.util.JWTUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RestController
public class AdminLoginController {

	private final AuthenticationManager authenticationManager; // 自動注入 AuthenticationManager

	@Autowired
	public AdminLoginController(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@GetMapping("/generatekey")
	public String generateKey() {
		// 創建 SecureRandom 實例
		SecureRandom secureRandom = new SecureRandom();

		// 創建 byte 陣列來儲存隨機數
		byte[] key = new byte[32]; // 256 bits

		// 生成隨機數
		secureRandom.nextBytes(key);

		// 將 byte 陣列編碼為 Base64 字串
		String encodedKey = Base64.getEncoder().encodeToString(key);

		// 打印生成的密鑰
		return encodedKey;
	}

	@PostMapping("/adminlogin")
	public ResponseEntity<?> login(@RequestBody AuthenticationRequest authenticationRequest,
			HttpServletRequest req,HttpSession session) {
		try {
			// 嘗試進行身份認證
			Authentication authentication = 
					authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					authenticationRequest.getAccount(), authenticationRequest.getPassword()));
			
			// 從 Authentication 對象中獲取 UserDetails
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			
			// 使用 Session 儲存驗證資訊
			session.setAttribute("userdetails", userDetails);
			session.setMaxInactiveInterval(60 * 60 * 10); // 10 小時
			
//			// 使用 JWT 存到 Cookie
//			// 生成 JWT 令牌
//			String jwt = jwtUtil.generateToken(userDetails);
//
//			// 將 JWT 令牌存儲到 Cookie 中
//			Cookie cookie = new Cookie("token", jwt);
//			cookie.setHttpOnly(true);
//			cookie.setMaxAge(60 * 60 * 10); // 10 小時
//			cookie.setPath("/");
//			response.addCookie(cookie);
			
			System.out.println(session.getId());
			System.out.println(userDetails.getUsername());
			System.out.println(userDetails.getPassword());
			return ResponseEntity.ok("Login Success");
		} catch (AuthenticationException e) {
			// 身份認證失敗，進行錯誤處理
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed: " + e.getMessage());
		}
	}
}
