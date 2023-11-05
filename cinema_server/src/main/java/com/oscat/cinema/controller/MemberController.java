package com.oscat.cinema.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oscat.cinema.dao.MemberRepository;
import com.oscat.cinema.dto.LoginDTO;
import com.oscat.cinema.dto.MemberDTO;
import com.oscat.cinema.entity.Member;
import com.oscat.cinema.service.impl.MemberService;

import jakarta.servlet.http.HttpSession;

// 移到 MemberOfficialController
@RestController
@RequestMapping("/api/member")
public class MemberController {

	@Autowired
	private MemberService memberService;
	
	@Autowired
	private MemberRepository memberRepository;

//	@Autowired
//	private JWTUtil jwtUtil;

//	@Autowired
//	private MemberValidator memberValidator;

//	@InitBinder
//	protected void initBinder(WebDataBinder binder) {
//		binder.setValidator(memberValidator);
//	}
	
	@GetMapping("member/forgotPassword")
	public String forgotPassword() {
		return "member/forgotPassword";
	}

	// 根據 email 找會員並寄驗證碼
	@PostMapping("/sendOtp")
	public ResponseEntity<String> sendVerificationCode(@RequestBody String request, HttpSession session) throws JSONException {
		JSONObject json = new JSONObject(request);
		String email = json.getString("email");
		Member member = memberService.findByEmail(email);
	    if (member != null) {
	        session.setAttribute("member", member);
	        memberService.sendVerificationCode(email, session);
	        return new ResponseEntity<String>("寄送成功", HttpStatus.OK);
	    } else {
	        return new ResponseEntity<String>("查無會員", HttpStatus.BAD_REQUEST);
	    }
	}
	
//    @GetMapping("/member/validateOtpPage")
//    public String validateOtpPage() {
//        return "member/validateOtp"; 
//    }
    
	// 重設密碼
//	@Transactional
//	@PostMapping("/checkOtpPwd")
//	public ResponseEntity<String> checkOtpPwd(@RequestBody MemberDto memberDto, HttpSession session) {
//		String otp = memberDto.getOtp();
//		String newPwd = memberDto.getNewPwd();
//		
//		Integer validateOtp = (Integer) session.getAttribute("otp");
//		Member member = (Member) session.getAttribute("member");
//		
//		System.out.println(validateOtp);
//		
//		if (validateOtp != null && validateOtp.toString().equals(otp)) {
//			member.setPassword(newPwd);
//			memberService.addMember(member);
//			return new ResponseEntity<>("Y", HttpStatus.OK);
//		} else {
//			return new ResponseEntity<>("N", HttpStatus.OK);
//		}
//	}
	
	// @Transactional
	@PostMapping("/resetPwd")
	public ResponseEntity<String> resetPwd(@RequestBody String request, HttpSession session) throws JSONException {
		
	        JSONObject json = new JSONObject(request);
	        String password = json.getString("password");        
	        System.out.println(password);
	        Member member = (Member) session.getAttribute("member");
	        if (member != null) {
	        	System.out.println(member.getPassword());
            	String encryptPwd = memberService.encodePassword(password);
                member.setPassword(encryptPwd);
                System.out.println(encryptPwd);
                memberRepository.saveAndFlush(member);
                return ResponseEntity.ok("密碼重設成功");	            
	        } 
	        return ResponseEntity.badRequest().body("未找到會員");	        
	}

   
    // 確認驗證碼
    @PostMapping("/checkOtp")
    public ResponseEntity<String> checkOtp(@RequestBody String request, HttpSession session) throws JSONException {
        JSONObject json = new JSONObject(request);
        String otp = json.getString("value");       
        Integer validateOtp = (Integer) session.getAttribute("otp");       
        if (validateOtp != null && validateOtp.toString().equals(otp)) {
        	session.removeAttribute("otp");
        	return ResponseEntity.ok("驗證碼正確");
        } else {
            return ResponseEntity.badRequest().body("驗證碼錯誤");
        }
    }
	
}
