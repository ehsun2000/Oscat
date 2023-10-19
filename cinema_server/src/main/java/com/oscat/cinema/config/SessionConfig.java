package com.oscat.cinema.config;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.MapSessionRepository;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

@Configuration // 標註為配置類
@EnableSpringHttpSession // 啟用 Spring Http Session
public class SessionConfig {

	// 設定請求時，回傳 sessionId 給 cookie 儲存
	@Bean
	public CookieSerializer cookieSerializer() {
		DefaultCookieSerializer serializer = new DefaultCookieSerializer();
		serializer.setCookieName("JSESSIONID"); // 設定 cookie 名稱
		serializer.setCookiePath("/"); // 設定 cookie 路徑
		serializer.setDomainNamePattern("^.+?\\.(\\w+\\.[a-z]+)$"); // 設定 cookie domain
		return serializer;
	}

	@Bean
	public MapSessionRepository sessionRepository() {
		return new MapSessionRepository(new ConcurrentHashMap<>());
	}
}
