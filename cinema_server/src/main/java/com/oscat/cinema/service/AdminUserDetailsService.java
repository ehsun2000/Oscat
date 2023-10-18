package com.oscat.cinema.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminUserDetailsService implements UserDetailsService {
	private PasswordEncoder passwordEncoder;

	@Autowired  // 注入 PasswordEncoder
	public AdminUserDetailsService(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	// 僅用硬編碼的方式來進行用戶驗證。
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if ("admin".equals(username)) {
			return User.withUsername("admin")
                    .password(passwordEncoder.encode("password")) // {noop} 指的是使用明文進行比對，實際應用中，應使用加密方式儲存密碼
					.roles("ADMIN") // 指定角色為 ADMIN
					.build();
		} else if ("user".equals(username)) {
			return User.withUsername("user")
                    .password(passwordEncoder.encode("password"))
                    .roles("USER") // 指定角色為 USER
					.build();
		}
		throw new UsernameNotFoundException("User not found.");
	}

}
