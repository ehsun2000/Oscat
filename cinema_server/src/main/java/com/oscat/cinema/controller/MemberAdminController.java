package com.oscat.cinema.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.oscat.cinema.dto.ApiResponse;
import com.oscat.cinema.dto.MemberAdminDto;
import com.oscat.cinema.entity.Member;
import com.oscat.cinema.service.MemberAdminService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/member")
public class MemberAdminController {

	@Autowired
	private MemberAdminService mService;

	// 所有會員
	@GetMapping("/all")
	public ResponseEntity<List<MemberAdminDto>> getAllMembers() {

		List<Member> members = mService.getAllMembers();

		List<MemberAdminDto> memberDTOs = new ArrayList<>();
		for (Member member : members) {
			MemberAdminDto memberDTO = new MemberAdminDto();
			memberDTO.setMemberId(member.getMemberId());
			memberDTO.setMemberName(member.getMemberName());
			memberDTO.setEmail(member.getEmail());
			memberDTO.setPassword(member.getPassword());
			memberDTO.setPhone(member.getPhone());
			memberDTO.setGender(member.getGender());
			memberDTO.setBirthDate(member.getBirthDate());
			memberDTO.setJoinDate(member.getJoinDate());

			memberDTOs.add(memberDTO);
		}

		return ResponseEntity.ok(memberDTOs);
	}

	// 頁面(未完成)
	@GetMapping("/all/like")
	public Optional<Member> findMemberByNameLike(@RequestParam("n") String name) {
		return mService.findMemberByName(name);
	}

	// 檢查信箱是否被註冊過
	@GetMapping("/add/{email}")
	public ResponseEntity<?> checkEmailRegistered(@PathVariable String email) {
		System.out.println(email);
		Optional<Member> existingMember = mService.findMemberByEmail(email);
		if (existingMember.isPresent()) {
			return new ResponseEntity<String>("Y", null, HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("N", null, HttpStatus.OK);
		}
	}

	// 新增單筆 (測試沒過)
	@Transactional
	@PostMapping("/add")
	public ResponseEntity<?> postAddCustomer(@Valid @RequestBody Member mem, BindingResult bindingResult) {
		System.out.println(mem.getGender());
		if (bindingResult.hasErrors()) {
			List<ObjectError> errorList = bindingResult.getAllErrors();
			StringBuilder sb = new StringBuilder();
			for (ObjectError error : errorList) {
				sb.append(error.getDefaultMessage());
			}
			ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "註冊失敗", sb.toString(),
					LocalDateTime.now().toString());

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
		}

		Optional<Member> existingMember = mService.findMemberByEmail(mem.getEmail());
		if (existingMember.isPresent()) {
			ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "信箱已存在", "該信箱已被註冊",
					LocalDateTime.now().toString());

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
		}

		ResponseEntity<?> checkEmailResponse = checkEmailRegistered(mem.getEmail());

		if (checkEmailResponse.getStatusCode() == HttpStatus.OK) {

			Member savedMember = mService.addMember(mem);
			ApiResponse<Member> apiResponse = new ApiResponse<>(HttpStatus.OK.value(), "註冊成功", savedMember,
					LocalDateTime.now().toString());

			return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
		} else {
			return checkEmailResponse;
		}
	}

	// 透過email keyword搜尋
	@GetMapping("/keyEmail")
	public String find(@RequestParam String email) {
		List<Member> result = mService.findKeywordByEmail("%" + email + "%");
		return result.toString();
	}

	// 透過id搜尋
	@Transactional
	@GetMapping("/{memberId}")
	public ResponseEntity<?> findById(@PathVariable UUID memberId) {
		Optional<Member> optional = mService.findMemberById(memberId);

		if (optional.isPresent()) {
			Member result = optional.get();
			return new ResponseEntity<Member>(result, null, HttpStatus.OK);
		}

		return new ResponseEntity<String>("沒有該筆資料", null, HttpStatus.BAD_REQUEST);
	}

	// 更新 (未測試)
	@Transactional
	@PutMapping("/update/{memberId}")
	@ResponseBody
	public ResponseEntity<ApiResponse<?>> updateMemberById(@Valid @RequestBody Member newMember,
			@PathVariable UUID memberId, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			List<ObjectError> errorList = bindingResult.getAllErrors();
			StringBuilder sb = new StringBuilder();
			for (ObjectError error : errorList) {
				sb.append(error.getDefaultMessage()).append(", ");
			}

			ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "修改失敗", sb.toString(),
					LocalDateTime.now().toString());

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
		}

		Optional<Member> existingMember = mService.findMemberById(newMember.getMemberId());

		if (!existingMember.isPresent()) {
			ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "沒有該會員",
					"沒有該會員編號: " + newMember.getMemberId(), LocalDateTime.now().toString());

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
		}

		// 加入時間為最初新增的時間，經修改後不變
//		Member originalMember = existingMember.get();
//		newMember.setJoinDate(originalMember.getJoinDate());

		try {
			Optional<Member> updatedMember = mService.updateMember(newMember);
			if (updatedMember.isPresent()) {
				ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.OK.value(), "會員資料更新成功", updatedMember,
						LocalDateTime.now().toString());

				return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
			} else {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "會員資料更新失敗", "無法保存更新後的會員資料",
								LocalDateTime.now().toString()));
			}
		} catch (Exception e) {
			ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "會員資料更新失敗",
					e.getMessage(), LocalDateTime.now().toString());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
		}

	}

	// 刪除 (未測試)
	@Transactional
	@DeleteMapping("/delete/{memberId}")
	public String deleteMemberById(@PathVariable UUID memberId) {

		String result = mService.deleteMemberById(memberId);
		return result;
	}

	// 透過email搜尋
//	@Transactional
//	@GetMapping("/member/byEmail/{email}")
//	public ResponseEntity<?> findByEmail(@PathVariable String email) {
//		Optional<Member> optional = mService.findMemberByEmail(email);
//
//		if (optional.isPresent()) {
//			Member result = optional.get();
//			return new ResponseEntity<Member>(result, null, HttpStatus.OK);
//		}
//
//		return new ResponseEntity<String>("沒有該筆資料", null, HttpStatus.BAD_REQUEST);
//	}

}
