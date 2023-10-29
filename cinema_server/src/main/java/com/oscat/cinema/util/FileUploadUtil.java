package com.oscat.cinema.util;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;

import jakarta.annotation.Resource;

@Component
public class FileUploadUtil {
	
	@Resource
	Cloudinary cloudinary;
	
    public String uploadFile(MultipartFile multipartFile) throws IOException {
        return cloudinary.uploader()
                .upload(multipartFile.getBytes(),
                		// TODO 唯一ID
                        Map.of("public_id", UUID.randomUUID().toString()))
                // TODO 訪問這個文件的網路位址
                .get("url")
                .toString();
    }
}