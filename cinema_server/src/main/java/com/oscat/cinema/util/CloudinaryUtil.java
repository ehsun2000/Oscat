package com.oscat.cinema.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Service
public class CloudinaryUtil {

	private final Cloudinary cloudinary;

	public CloudinaryUtil() {
		this.cloudinary = new Cloudinary(ObjectUtils.asMap("cloud_name", "dj6yyptpm", "api_key", "133548463866334",
				"api_secret", "FU9Oyxlz2hVOWcHIA0rFWbKAjPQ"));
	}

	@SuppressWarnings("unchecked")
	public String uploadImage(MultipartFile file) {
		try {
			Map<String, Object> options = new HashMap<>();
			options.put("folder", "cinema");
			Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), options);
			return uploadResult.get("url").toString();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean deleteImage(String urlString) {
		try {
			cloudinary.uploader().destroy(getPublicIdFromUrl(urlString), ObjectUtils.emptyMap());
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	private String getPublicIdFromUrl(String urlString) {
		try {
			URL url = new URL(urlString);
			String path = url.getPath();
			String[] segments = path.split("/");
			String cinemaPath = segments[segments.length - 2];
			String publicId = segments[segments.length - 1];
			if (publicId.contains(".")) {
				publicId = publicId.substring(0, publicId.lastIndexOf("."));
			}
			return cinemaPath + "/" + publicId;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
