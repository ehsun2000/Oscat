package com.oscat.cinema.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.oscat.cinema.config.MemberValidator;
import com.oscat.cinema.util.ApiResponse;
import com.oscat.cinema.entity.Member;
import com.oscat.cinema.service.impl.MemberService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/official")
public class MemberOfficialController {

	@Autowired
	private MemberService memberService;

	@Autowired
	private MemberValidator memberValidator;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.setValidator(memberValidator);
	}

	@PostMapping("/member/register")
	public ResponseEntity<ApiResponse<?>> register(@Valid @RequestBody Member member, BindingResult result) {

		if (result.hasErrors()) {
			List<ObjectError> errors = result.getAllErrors();
			ApiResponse<List<ObjectError>> errorResponse = new ApiResponse<>("error", "註冊失敗，請檢查輸入格式", errors);
			return ResponseEntity.badRequest().body(errorResponse);
		} else {
			Date birthDate = member.getBirthDate();
			String birthdate = member.parseToString(birthDate);
			member.setBirthDateFromString(birthdate);
			Member registerMember = memberService.addMember(member);
			ApiResponse<Member> successResponse = new ApiResponse<>("success", "註冊成功", registerMember);
			return ResponseEntity.ok(successResponse);
		}
	}

	@PostMapping("/member/login")
	public ResponseEntity<ApiResponse<List<String>>> login(@RequestParam("e") String email,
			@RequestParam("p") String password, HttpSession session) {
		Member loginMember = memberService.checkLogin(email, password);
		if (loginMember != null) {
			session.setAttribute("loginMember", loginMember);
			List<String> list = new ArrayList<>();
			list.add(session.getId());
			list.add(loginMember.getMemberName());
			ApiResponse<List<String>> successResponse = new ApiResponse<>("success", "登入成功", list);
			return ResponseEntity.status(HttpStatus.OK).body(successResponse);
		} else {
			ApiResponse<List<String>> errorResponse = new ApiResponse<>("error", "帳號密碼錯誤", null);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}
	}

	@PutMapping("/member/update")
	public ResponseEntity<ApiResponse<Member>> updateUser(@RequestParam("e") String email,
			@RequestParam("p") String password, @RequestBody Member newMember) {

		Member member = memberService.checkLogin(email, password);
		if (member != null) {
			member.setMemberName(newMember.getMemberName());
			member.setEmail(newMember.getEmail());
			member.setPassword(newMember.getPassword());
			member.setPhone(newMember.getPhone());
			member.setGender(newMember.getGender());
			member.setBirthDate(newMember.getBirthDate());
			Member updateMember = memberService.addMember(member);
			ApiResponse<Member> successResponse = new ApiResponse<>("success", "修改成功", updateMember);
			return ResponseEntity.ok(successResponse);
		} else {
			ApiResponse<Member> errorResponse = new ApiResponse<>("error", "查無此帳號密碼", null);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
	}
	


}
