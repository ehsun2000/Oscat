package com.oscat.cinema.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.ldap.EmbeddedLdapServerContextSourceFactoryBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.oscat.cinema.filter.AuthRequestFilter; // 引入您自定義的 JWT 過濾器
import com.oscat.cinema.service.AdminUserDetailsService;

@Configuration // 標記為配置類
@EnableWebSecurity // 啟用Spring Security
public class SecurityConfig {

	@Autowired
	private AdminUserDetailsService detailsService; // 自定義的用戶詳細信息服務
	@Autowired
	private AuthRequestFilter authRequestFilter; // 自定義的 JWT 過濾器
	@Autowired
	private PasswordEncoder passwordEncoder;

	// 身份驗證提供者Bean
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(detailsService); // 設置用戶詳細信息服務
		provider.setPasswordEncoder(passwordEncoder); // 設置密碼加密器
		return provider;
	}

	// AuthenticationManager Bean
	@Bean
	public AuthenticationManager customAuthenticationManager() throws Exception {
		return new ProviderManager(Arrays.asList(authenticationProvider()));
	}

	// 安全過濾器鏈Bean
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//		http.cors() // 啟用 CORS 支持
//			.and()		
//			.csrf(); // 禁用 CSRF，如果您使用的是 RESTful API				
//		        		
//		http.sessionManagement()
//            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
		
//		http.authenticationProvider(authenticationProvider()) // 設置身份驗證提供者
//			.addFilterBefore(authRequestFilter, UsernamePasswordAuthenticationFilter.class) // 在用戶名密碼驗證之前加入 Auth 過濾器
//			.authorizeHttpRequests(auth -> 
//				auth.requestMatchers("/login","/generatekey").permitAll() // 允許所有人訪問登入接口 
//					.requestMatchers("/api/**").hasAnyRole("ADMIN") // 只有 ADMIN 才能使用 /api
//					.anyRequest().authenticated() // 其他請求需要身份驗證
//			);

		return http.build();
	}
}
