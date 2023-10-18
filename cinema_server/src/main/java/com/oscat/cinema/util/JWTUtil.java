package com.oscat.cinema.util;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JWTUtil {

	// 初始化 SECRET_KEY
	@Value("${jwt.secret-key}")
	private String SECRET_KEY;

	// 從 token 中提取用戶名
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	// 從 token 中提取過期時間
	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	// 泛型方法，用於從 token 中提取特定的 claim
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	// 從 token 中提取所有的 claims
	private Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
	}

	// 檢查 token 是否已過期
	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	// 生成 token
	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, userDetails.getUsername());
	}

	// 創建一個新的 token
	private String createToken(Map<String, Object> claims, String subject) {
		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
	}

	// 驗證 token 的有效性
	public boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
}
