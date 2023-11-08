package com.oscat.cinema.controller;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oscat.cinema.dao.MemberRepository;
import com.oscat.cinema.dto.ApiResponse;
import com.oscat.cinema.dto.LoginDTO;
import com.oscat.cinema.dto.MemberDTO;
import com.oscat.cinema.entity.Member;
import com.oscat.cinema.service.impl.MemberService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/official/member")
public class MemberOfficialController {

	@Autowired
	private MemberService memberService;

	@Autowired
	private MemberRepository memberRepository;

	// 會員註冊
	@PostMapping("/register")
	public ResponseEntity<ApiResponse<?>> register(@Valid @RequestBody Member member, BindingResult result,
			HttpSession session) {

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

	// 會員登入
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody LoginDTO loginDto, HttpSession session) {
		String email = loginDto.getEmail();
		String password = loginDto.getPassword();

		Member loginMember = memberService.checkLogin(email, password);

		if (loginMember != null) {
			session.setAttribute("loginMember", loginMember.getMemberId());
			return ResponseEntity.ok("登入成功");
		}
		return ResponseEntity.badRequest().body("登入失敗");
	}

	// 確認 email 是否已經存在
	@PostMapping("/check")
	public ResponseEntity<String> checkEmailExists(@RequestBody String request) throws JSONException {
		JSONObject json = new JSONObject(request);
		String email = json.getString("email");
		Member member = memberService.findByEmail(email);
		if (member != null) {
			return ResponseEntity.badRequest().body("email已經被註冊");
		}
		return ResponseEntity.ok("email沒有註冊過");
	}

	// 根據 email 找會員並寄驗證碼
	@PostMapping("/sendOtp")
	public ResponseEntity<String> sendVerificationCode(@RequestBody String request, HttpSession session)
			throws JSONException {
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

	// 重設密碼
	@PostMapping("/resetPwd")
	public ResponseEntity<String> resetPwd(@RequestBody String request, HttpSession session) throws JSONException {

		JSONObject json = new JSONObject(request);
		String password = json.getString("password");
		Member member = (Member) session.getAttribute("member");
		if (member != null) {
			System.out.println(member.getPassword());
			String encryptPwd = memberService.encodePassword(password);
			member.setPassword(encryptPwd);
			memberRepository.saveAndFlush(member);
			return ResponseEntity.ok("密碼重設成功");
		}
		return ResponseEntity.badRequest().body("未找到會員");
	}

	// 查詢會員資料
	@PostMapping("/find")
	public ResponseEntity<?> findMember(HttpSession session) {
		UUID id = (UUID) session.getAttribute("loginMember");
		if (id != null) {
			Member member = memberService.findById(id);
			return ResponseEntity.ok(member);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("找不到會員");
	}

	// 會員修改
	@PutMapping("/update")
	public ResponseEntity<ApiResponse<?>> updateUser(@Valid @RequestBody MemberDTO newMember, BindingResult result,
			HttpSession session) {
		System.out.println(newMember.toString());
		UUID id = (UUID) session.getAttribute("loginMember");
		Member member = memberService.findById(id);
		if (member != null) {

			System.out.println(member.getMemberId() + " " + member.getMemberName());

			if (result.hasErrors()) {
				System.out.println("有錯誤");
				List<ObjectError> errors = result.getAllErrors();
				StringBuilder sb = new StringBuilder();
				for (ObjectError er : errors) {
					sb.append(er.getDefaultMessage()).append(" ");
				}
				ApiResponse<String> errorResponse = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "更新失敗，請檢查輸入格式",
						sb.toString(), LocalDateTime.now().toString());
				return ResponseEntity.badRequest().body(errorResponse);
			}
			if (newMember.getMemberId().equals(member.getMemberId())) {
				System.out.println("相同會員");
				memberService.update(newMember);
				ApiResponse<MemberDTO> successResponse = new ApiResponse<>(HttpStatus.OK.value(), "修改成功", newMember,
						LocalDateTime.now().toString());
				return ResponseEntity.ok(successResponse);
			} else {
				System.out.println("無權修改此帳號");
				ApiResponse<String> errorResponse = new ApiResponse<>(HttpStatus.FORBIDDEN.value(), "無權修改此帳號", null,
						LocalDateTime.now().toString());
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
			}
		} else {
			System.out.println("查無此帳號密碼");
			ApiResponse<String> errorResponse = new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "查無此帳號密碼", null,
					LocalDateTime.now().toString());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		}
	}


	// 會員登出
	@PostMapping("/logout")
	public ResponseEntity<String> logout(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.removeAttribute("loginMember");
//	    session.invalidate();
		return ResponseEntity.ok("登出");
	}

	// 取得會員訂單
	@GetMapping("/order")
	public ResponseEntity<?> getOrders(HttpSession session) {
		UUID mId = (UUID) session.getAttribute("loginMember");
		List<Map<String, Object>> memberOrders = memberService.getMemberOrders(mId);
		
		return ResponseEntity.ok(memberOrders);
	}
	// 取得會員訂單
	@GetMapping("/order/{memberId}")
	public ResponseEntity<?> getOrders(@PathVariable UUID memberId) {
		List<Map<String, Object>> memberOrders = memberService.getMemberOrders(memberId);
		
		return ResponseEntity.ok(memberOrders);
	}
}