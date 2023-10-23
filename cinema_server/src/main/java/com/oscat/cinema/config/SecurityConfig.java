package com.oscat.cinema.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.oscat.cinema.filter.AuthRequestFilter; // 引入您自定義的 JWT 過濾器
import com.oscat.cinema.service.AdminUserDetailsService;

@Configuration // 配置類
@EnableWebSecurity // 啟用 Spring Security
public class SecurityConfig {

	@Autowired
	private AdminUserDetailsService detailsService; // 自定義的用戶詳細信息服務

	@Autowired
	private AuthRequestFilter authRequestFilter; // 自定義的 JWT 過濾器

	@Autowired
	private PasswordEncoder passwordEncoder; // 密碼加密器

	// 身份驗證提供者，設定 UserdetailsService 和 PasswordEncoder
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(detailsService);
		provider.setPasswordEncoder(passwordEncoder);
		return provider;
	}

	// AuthenticationManager 實例
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return new ProviderManager(authenticationProvider());
	}

	// 跨域請求設定
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(List.of("http://localhost:8081"));
		config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
		config.setAllowCredentials(true);
		config.setAllowedHeaders(List.of("*"));

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}

	// 安全過濾鏈
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.cors() // 啟用 CORS
				.and().csrf().disable(); // 禁用 CSRF

		// 設定驗證的提供者
		http.authenticationProvider(authenticationProvider()).addFilterBefore(authRequestFilter,
				UsernamePasswordAuthenticationFilter.class);

		// 控制 api 使用權限
		http.authorizeHttpRequests(authorize -> {
			authorize.requestMatchers("/adminlogin", "/generatekey").permitAll();
			authorize.requestMatchers("/api/**").hasAnyRole("ADMIN");
			authorize.anyRequest().authenticated();
		});

		return http.build();
	}

}
