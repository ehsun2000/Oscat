package com.oscat.cinema.handler;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException.BadRequest;
import org.springframework.web.client.HttpClientErrorException.NotFound;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(value=MaxUploadSizeExceededException.class)
	public String imageSizeHandler(Model model) {
		model.addAttribute("erroeMsg","圖片太大了");
		return "characters/characters_update";
	}
	
    @ExceptionHandler(value = NotFound.class)
    public String notFoundHandler(Model model) {
        model.addAttribute("errorMsg", "找不到頁面");
        return "error/404";
    }
    
    @ExceptionHandler(value = BadRequest.class)
    public String badRequestHandler(Model model) {
        model.addAttribute("errorMsg", "請求無效");
        return "error/400";
    }
    
    @ExceptionHandler(value = InternalServerError.class)
    public String internalServerErrorHandler(Model model) {
        model.addAttribute("errorMsg", "內部伺服器錯誤");
        return "error/500";
    }
	
}
