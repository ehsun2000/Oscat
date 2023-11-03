package com.oscat.cinema.controller;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oscat.cinema.dto.ApiResponse;
import com.oscat.cinema.dto.LoginDto;

import com.oscat.cinema.entity.Member;
import com.oscat.cinema.service.impl.MemberService;
import com.oscat.cinema.util.JWTUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/official/member")
public class MemberOfficialController {

	@Autowired
	private MemberService memberService;
	
	@Autowired
	private JWTUtil jwtUtil;

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
	
	// 確認 email 是否已經存在
	@PostMapping("/check")
	public ResponseEntity<String> checkEmailExists(@RequestBody String request) throws JSONException {
		JSONObject json = new JSONObject(request);
		String email = json.getString("email");
		Member member = memberService.findByEmail(email);
		if(member != null) {
			return ResponseEntity.badRequest().body("email已經被註冊");
		}
		return ResponseEntity.ok("email沒有註冊過");
	}
	
	// 會員登入
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody LoginDto loginDto, HttpSession session) {	    
		String email = loginDto.getEmail();
	    String password = loginDto.getPassword();
	    
	    Member loggedInMember = memberService.checkLogin(email, password);
	    
	    if (loggedInMember != null) {
	        session.setAttribute("loggedInMember", loggedInMember);
	        return ResponseEntity.ok("登入成功"); //登入成功 再多一個api getsessionS
	    }
	    return ResponseEntity.badRequest().body("登入失敗");
	}

	// 查詢會員資料
	@PostMapping("/find")
	public ResponseEntity<?> findMember(HttpSession session){
		Member member = (Member) session.getAttribute("loggedInMember");
		if(member != null) {
			return ResponseEntity.ok(member);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("找不到會員");
	}
	
	// 會員修改
	@PutMapping("/update")
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
	
	// 會員登出
	@PostMapping("/logout")
	public ResponseEntity<String>  logout(HttpServletRequest request) {
	    HttpSession session = request.getSession();
	    session.removeAttribute("loginMember");
//	    session.invalidate();
	    return ResponseEntity.ok("登出");
	}    
}
