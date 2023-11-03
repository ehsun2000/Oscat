package com.oscat.cinema.dto;

import java.util.Date;
import java.util.UUID;

import lombok.Data;

@Data
public class MemberAdminDTO {
	
	private UUID memberId;
	private String memberName;
	private String email;
	private String password;
	private String phone;
	private String gender;
	private Date birthDate;
	private Date joinDate;
}
