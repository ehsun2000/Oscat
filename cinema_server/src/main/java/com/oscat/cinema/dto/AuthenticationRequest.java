package com.oscat.cinema.dto;

import lombok.Data;

@Data
public class AuthenticationRequest {
	private String account;
	private String password;
}
