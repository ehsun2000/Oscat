package com.oscat.cinema.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;

@Configuration
public class CloudinaryConfig {
	private final String CLOUD_NAME = "dws3rw1cz";
    private final String API_KEY = "331171688447757";
    private final String API_SECRET = "YiJK9o4aALsMczboRkgVttofLL4";

    @Bean
    Cloudinary cloudinary(){
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name",CLOUD_NAME);
        config.put("api_key",API_KEY);
        config.put("api_secret",API_SECRET);
        return new Cloudinary(config);
    }
}