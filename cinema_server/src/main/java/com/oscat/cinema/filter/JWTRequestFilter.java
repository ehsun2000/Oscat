package com.oscat.cinema.filter;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.oscat.cinema.util.JWTUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTRequestFilter extends OncePerRequestFilter {

	private final UserDetailsService userDetailsService;
	private final JWTUtil jwtUtil;

	public JWTRequestFilter(UserDetailsService userDetailsService, JWTUtil jwtUtil) {
		this.userDetailsService = userDetailsService;
		this.jwtUtil = jwtUtil;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		// 從請求的 cookie 中找出名為 "jwtToken" 的 cookie
		Cookie[] cookies = request.getCookies();
		String jwt = null;
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("token".equals(cookie.getName())) {
					jwt = cookie.getValue();
					break;
				}
			}
		}

		// 如果找到了 JWT，則進行驗證
		if (jwt != null) {
			String username = jwtUtil.extractUsername(jwt);
			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
				if (jwtUtil.validateToken(jwt, userDetails)) {
					// 如果驗證成功，設置 Spring Security 上下文
					UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authenticationToken);
				}
			}
		}
		// 執行後續的過濾器鏈
		chain.doFilter(request, response);
	}
}
