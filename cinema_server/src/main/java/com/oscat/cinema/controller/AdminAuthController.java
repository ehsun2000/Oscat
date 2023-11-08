package com.oscat.cinema.controller;

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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
public class AdminAuthController {

	private final AuthenticationManager authenticationManager; // 自動注入 AuthenticationManager

	@Autowired
	public AdminAuthController(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
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
			
			return ResponseEntity.ok("Login Success");
		} catch (AuthenticationException e) {
			// 身份認證失敗，進行錯誤處理
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed: " + e.getMessage());
		}
	}
	
	// 確認用戶是否登入
	@GetMapping("/checkLogin")
	public ResponseEntity<?> checkLogin(Authentication authentication) {
	    // 如果Authentication對象不為null，代表用戶已登入
	    if (authentication != null && authentication.isAuthenticated()) {
	        return new ResponseEntity<>("已登入", HttpStatus.OK);
	    }
	    return new ResponseEntity<>("未登入", HttpStatus.UNAUTHORIZED);
	}
}
