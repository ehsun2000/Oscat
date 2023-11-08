package com.oscat.cinema.util.aijie;

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

	/**
	 * 這個方法用來上傳文件到 Cloudinary 服務。
	 *
	 * @param multipartFile 要上傳的文件
	 * @return 上傳後的文件網址
	 * @throws IOException 如果上傳過程中發生IO錯誤
	 */
	public String uploadFile(MultipartFile multipartFile) throws IOException {
		// 使用 Cloudinary 的 uploader 方法上傳文件，並指定 public_id 作為唯一ID
		return cloudinary.uploader().upload(multipartFile.getBytes(),
				// 用於設定上傳的文件的唯一ID。
				Map.of("public_id", UUID.randomUUID().toString()))
				// 從上傳結果中取得文件的網路位址，並轉換為字串，然後返回。
				.get("url").toString();
	}
}
