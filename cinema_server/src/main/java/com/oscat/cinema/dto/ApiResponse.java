package com.oscat.cinema.dto;

import lombok.Data;

@Data
public class ApiResponse<T> {

	private int status;
	private String message;
	private T data;
	private String timestamp;
	
	public ApiResponse(int status, String message, T data, String timestamp) {
		this.status = status;
		this.message = message;
		this.data = data;
		this.timestamp = timestamp;
	}
	
	
}
