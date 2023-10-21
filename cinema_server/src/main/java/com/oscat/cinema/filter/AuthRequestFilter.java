package com.oscat.cinema.filter;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class AuthRequestFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		// 從請求的 session 找出 "userdetails"
		HttpSession session = request.getSession(false);

		// 初始化用戶詳情為 null
		UserDetails userDetails = null; // 請將 YourUserDetailsType 換成您實際用來存儲用戶詳情的類型

		// 檢查 session 和 "userdetails"
		if (session != null && session.getAttribute("userdetails") != null) {
			userDetails = (UserDetails) session.getAttribute("userdetails");
		}

		if (userDetails != null) {
			// 由於用戶已經登入，我們有了 userDetails 對象
			// 設置 Spring Security 的上下文
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
					userDetails, null, userDetails.getAuthorities());
			authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		}

		// 從請求的 cookie 中找出名為 "jwtToken" 的 cookie
//		Cookie[] cookies = request.getCookies();
//		String jwt = null;
//		if (cookies != null) {
//			for (Cookie cookie : cookies) {
//				if ("token".equals(cookie.getName())) {
//					jwt = cookie.getValue();
//					break;
//				}
//			}
//		}

		// 如果找到了 JWT，則進行驗證
//		if (jwt != null) {
//			String username = jwtUtil.extractUsername(jwt);
//			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//				UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
//				if (jwtUtil.validateToken(jwt, userDetails)) {
//					// 如果驗證成功，設置 Spring Security 上下文
//					UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
//							userDetails, null, userDetails.getAuthorities());
//					authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//					SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//				}
//			}
//		}

		chain.doFilter(request, response);
	}
}
