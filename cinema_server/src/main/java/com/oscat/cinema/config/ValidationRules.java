package com.oscat.cinema.config;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.oscat.cinema.entity.Member;

public class ValidationRules implements Validator{
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Member.class.equals(clazz);
	}

	@Override
	public void validate(Object obj, Errors errors) {
		Member mem=(Member) obj;
		
		ValidationUtils.rejectIfEmpty(errors, "email", "email.empty");
		
		if(mem.getMemberName()==null) {
			errors.rejectValue("memberName","請輸入姓名");
		}
	}

}
