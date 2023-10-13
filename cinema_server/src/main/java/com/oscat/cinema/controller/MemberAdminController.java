package com.oscat.cinema.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
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
import com.oscat.cinema.entity.Member;
import com.oscat.cinema.service.MemberAdminService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/admin")
public class MemberAdminController {


	@Autowired
	private MemberAdminService mService;

	// 找全部
	 @GetMapping("member/all")
	    public List<Member> getAllMembers() {
	        List<Member> members = mService.getAllMembers();
	        return members;
	    }
	
	// 新增單筆
	@Transactional
	@PostMapping("/member/add")
	@ResponseBody
	public ResponseEntity<ApiResponse<?>> postAddCustomer3(@Valid @RequestBody Member mem,
			BindingResult bindingResult) {

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

		Member savedMember = mService.addMember(mem);
		ApiResponse<Member> apiResponse = new ApiResponse<>(HttpStatus.OK.value(), "註冊成功", savedMember,
				LocalDateTime.now().toString());

		return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
	}

	// 透過email搜尋
	@Transactional
	@GetMapping("/member/byEmail/{email}")
	public ResponseEntity<?> findByEmail(@PathVariable String email) {
		Optional<Member> optional = mService.findMemberByEmail(email);

		if (optional.isPresent()) {
			Member result = optional.get();
			return new ResponseEntity<Member>(result, null, HttpStatus.OK);
		}

		return new ResponseEntity<String>("沒有該筆資料", null, HttpStatus.BAD_REQUEST);
	}


	// 刪除單筆
	@Transactional
    @DeleteMapping("/member/delete")
    public String deleteMemberByEmail(@RequestParam String email) {
        String result = mService.deleteMemberByEmail(email);
        return result;
    }
	

	@PutMapping("/member/update")
	@ResponseBody
	public ResponseEntity<ApiResponse<?>> updateMemberByEmail(@Valid @RequestBody Member newMember,
			BindingResult bindingResult) {

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

		Optional<Member> existingMember = mService.findMemberByEmail(newMember.getEmail());

		if (!existingMember.isPresent()) {
			ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "沒有該會員",
					"找不到該信箱: " + newMember.getEmail(), LocalDateTime.now().toString());

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
		}

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

}
