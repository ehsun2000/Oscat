package com.oscat.cinema.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.oscat.cinema.dto.MemberDto;
import com.oscat.cinema.entity.Member;
import com.oscat.cinema.service.impl.MemberService;

import jakarta.servlet.http.HttpSession;

@Controller
public class MemberController {

	@Autowired
	private MemberService memberService;
	
	@GetMapping("member/forgotPassword")
	public String forgotPassword() {
		return "member/forgotPassword";
	}

	// 要跳頁 session
	@PostMapping("/member/checkEmail")
	public ResponseEntity<String> sendVerificationCode(@RequestBody Map<String, String> requestBody, HttpSession session) {
	    String email = requestBody.get("email");
	    System.out.println("Email: " + email);

	    Member m = memberService.findByEmail(email);

	    if (m != null) {
	        session.setAttribute("member", m);
	        memberService.sendVerificationCode(email, session);

	        return new ResponseEntity<String>("Y", HttpStatus.OK);
	    } else {
	        return new ResponseEntity<String>("N", HttpStatus.OK);
	    }
	}
	
    @GetMapping("/member/validateOtpPage")
    public String validateOtpPage() {
        return "member/validateOtp"; 
    }
    
    @Transactional
    @PostMapping("/member/checkOtpPwd")
    public ResponseEntity<String> checkOtpPwd(@RequestBody MemberDto memberDto, HttpSession session) {
    	String otp = memberDto.getOtp();
    	String newPwd = memberDto.getNewPwd();

        Integer validateOtp = (Integer) session.getAttribute("otp");
        Member member = (Member) session.getAttribute("member");

        System.out.println(validateOtp);
        
        if (validateOtp != null && validateOtp.toString().equals(otp)) {
            member.setPassword(newPwd);
            memberService.addMember(member);
            return new ResponseEntity<>("Y", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("N", HttpStatus.OK);
        }
    }
	
    @GetMapping("/member/otpPwdReset")
    public String otpPwdPage() {
        return "member/otpPwdPage"; 
    }
    
    @PostMapping("/member/logout")
    public String logout(HttpSession session) {
    	session.invalidate();
    	return "index";
    }
}
