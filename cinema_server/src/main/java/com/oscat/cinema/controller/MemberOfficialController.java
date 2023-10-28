package com.oscat.cinema.controller;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oscat.cinema.dto.ApiResponse;
import com.oscat.cinema.dto.LoginDto;

import com.oscat.cinema.entity.Member;
import com.oscat.cinema.service.impl.MemberService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/member")
public class MemberOfficialController {

	@Autowired
	private MemberService memberService;

//	@Autowired
//	private MemberValidator memberValidator;

//	@InitBinder
//	protected void initBinder(WebDataBinder binder) {
//		binder.setValidator(memberValidator);
//	}

	// 會員註冊
	@PostMapping("/register")
	public ResponseEntity<ApiResponse<?>> register(@Valid @RequestBody Member member, BindingResult result, HttpSession session) {

		if (result.hasErrors()) {
			List<ObjectError> errors = result.getAllErrors();
			StringBuilder sb = new StringBuilder();
			for (ObjectError er : errors) {
				sb.append(er.getDefaultMessage());
			}
			ApiResponse<String> errorResponse = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "註冊失敗，請檢查輸入格式",
					sb.toString(), LocalDateTime.now().toString());
			return ResponseEntity.badRequest().body(errorResponse);
		} else if (memberService.checkIfEmailExist(member.getEmail())) {
			ApiResponse<String> emailExist = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "email不能重複",
					member.getEmail(), new Date().toString());
			return ResponseEntity.badRequest().body(emailExist);
		} else {
			Date birthDate = member.getBirthDate();
			String birthdate = member.parseToString(birthDate);
			member.setBirthDateFromString(birthdate);
			Member registerMember = memberService.addMember(member);
			session.setAttribute("registerMember", registerMember);
			ApiResponse<Member> successResponse = new ApiResponse<>(HttpStatus.OK.value(), "註冊成功", registerMember,
					LocalDateTime.now().toString());
			return ResponseEntity.ok(successResponse);
		}
	}
	
//	public boolean checkEmailExists() {
//		
//	}

//	@PostMapping("/member/login")
//	public ResponseEntity<String> login(@RequestParam("email") String email, @RequestParam("password") String password, HttpSession session) {
//		Member loginMember = memberService.checkLogin(email, password);
//		if (loginMember != null) {
//			session.setAttribute("loginMember", loginMember);
//			List<String> list = new ArrayList<>();
//			list.add(session.getId());
//			list.add(loginMember.getMemberName());
//			ApiResponse<List<String>> successResponse = new ApiResponse<>("success", "登入成功", list);
//			return ResponseEntity.ok("登入成功");
//		} else {
//			ApiResponse<List<String>> errorResponse = new ApiResponse<>("error", "帳號密碼錯誤", null);
//			return ResponseEntity.badRequest().body("登入失敗");
//		}
//	}
	
	// 會員登入
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody LoginDto loginDto, HttpSession session) {	    
		String email = loginDto.getEmail();
	    String password = loginDto.getPassword();
	    
	    Member loggedInMember = memberService.checkLogin(email, password);
	    
	    if (loggedInMember != null) {
	        session.setAttribute("loggedInMember", loggedInMember);
	        return ResponseEntity.ok("登入成功");
	    }
	    return ResponseEntity.badRequest().body("登入失敗");
	}

	// 會員修改
	@PutMapping("/userUpdate")
	public ResponseEntity<ApiResponse<?>> updateUser(@Valid @RequestBody Member newMember, BindingResult result,
			HttpSession session) {

		if (result.hasErrors()) {
			List<ObjectError> errors = result.getAllErrors();
			StringBuilder sb = new StringBuilder();
			for (ObjectError er : errors) {
				sb.append(er.getDefaultMessage()).append(" ");
			}
			ApiResponse<String> errorResponse = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "更新失敗，請檢查輸入格式",
					sb.toString(), LocalDateTime.now().toString());
			return ResponseEntity.badRequest().body(errorResponse);
		}

		Member loginMember = (Member) session.getAttribute("loginMember");

		if (loginMember != null) {
			if (newMember.getMemberId() == loginMember.getMemberId()) {
				loginMember.setMemberName(newMember.getMemberName());
				loginMember.setEmail(newMember.getEmail());
				loginMember.setPassword(newMember.getPassword());
				loginMember.setPhone(newMember.getPhone());
				loginMember.setGender(newMember.getGender());
				loginMember.setBirthDate(newMember.getBirthDate());
				Member updateMember = memberService.addMember(loginMember);
				session.removeAttribute("loginMember");
				session.setAttribute("updateMember", updateMember);
				ApiResponse<Member> successResponse = new ApiResponse<>(HttpStatus.OK.value(), "修改成功", updateMember,
						LocalDateTime.now().toString());
				return ResponseEntity.ok(successResponse);
			}else {
	            ApiResponse<String> errorResponse = new ApiResponse<>(HttpStatus.FORBIDDEN.value(), "無權修改此帳號", null,
	                    LocalDateTime.now().toString());
	            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
			}
		} else {
			ApiResponse<String> errorResponse = new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "查無此帳號密碼", null,
					LocalDateTime.now().toString());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
	}

}
