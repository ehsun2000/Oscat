package com.oscat.cinema;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.session.SessionAutoConfiguration;

@SpringBootApplication
// 將默認的 sessionAutoConfig 取消
@EnableAutoConfiguration(exclude = {SessionAutoConfiguration.class})
public class CinemaApplication {

	public static void main(String[] args) {
		SpringApplication.run(CinemaApplication.class, args);
	}

}
