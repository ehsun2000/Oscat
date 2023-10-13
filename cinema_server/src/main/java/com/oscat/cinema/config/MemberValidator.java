package com.oscat.cinema.config;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.oscat.cinema.entity.Member;

@Component
public class MemberValidator implements Validator {
	
	private static final String NAME_PATTERN = "^[A-Za-z\\u4e00-\\u9fa5]+$";
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final String PASSWORD_PATTERN = "^(?=.*[a-zA-Z])(?=.*\\d).{8,}$";
    private static final String PHONE_PATTERN = "^(09)\\d{8}$";
    private static final String GENDER_PATTERN = "^(man|female|other)$";
    
    private static final Pattern namePattern = Pattern.compile(NAME_PATTERN);
    private static final Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);
    private static final Pattern passwordPattern = Pattern.compile(PASSWORD_PATTERN);
    private static final Pattern phonePattern = Pattern.compile(PHONE_PATTERN);
    private static final Pattern genderPattern = Pattern.compile(GENDER_PATTERN);
    
    public static boolean isValidName(String name) {
        Matcher matcher = namePattern.matcher(name);
        return matcher.matches();
    }

    public static boolean isValidEmail(String email) {
    	Matcher matcher = emailPattern.matcher(email);
    	return matcher.matches();
    }
    
    public static boolean isValidPassword(String password) {
    	Matcher matcher = passwordPattern.matcher(password);
    	return matcher.matches();	
    }
    
    public static boolean isValidPhone(String phone) {
    	Matcher matcher = phonePattern.matcher(phone);
    	return matcher.matches();
    }
    
    public static boolean isValidGender(String gender) {
    	Matcher matcher = genderPattern.matcher(gender);
    	return matcher.matches();
    }

    //驗證member
    @Override
    public boolean supports(Class<?> clazz) {
        return Member.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Member member = (Member) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "memberName", "姓名不得為空");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "email不得為空");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "密碼不得為空");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "phone", "手機號碼不得為空");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "gender", "性別不得為空");

        if (!isValidName(member.getMemberName())) {
            errors.rejectValue("memberName", "請輸入中文或英文");
        }

        if (!isValidEmail(member.getEmail())) {
            errors.rejectValue("email", "請輸入正確格式");
        }

        if (!isValidPassword(member.getPassword())) {
            errors.rejectValue("password", "請輸入英文數字至少8碼");
        }

        if (!isValidPhone(member.getPhone())) {
            errors.rejectValue("phone", "請輸入有效手機號碼");
        }

        if (!isValidGender(member.getGender())) {
            errors.rejectValue("gender", "請輸入man)、female或其他other");
        }

    }

}
