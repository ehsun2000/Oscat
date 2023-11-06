package com.oscat.cinema.dto;

import java.util.UUID;

import com.oscat.cinema.entity.Member;

import lombok.Data;

@Data
public class MemberDTO {
    
	private UUID memberId;
    private String memberName;
    private String email;
    private String password;
    private String phone;
    private String gender;

}





