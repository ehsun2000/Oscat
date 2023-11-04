package com.oscat.cinema.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.oscat.cinema.entity.Product;
import com.oscat.cinema.service.ProductService;
import com.oscat.cinema.util.aijie.FileUploadUtil;

import jakarta.annotation.Resource;

@RestController
@RequestMapping(path = "/api/product")
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	//新增產品
	@PostMapping("/add")
	public ResponseEntity<String> addProduct(@RequestBody Product product) {
		try {
			// 新增產品到資料庫
			productService.addProduct(product);
			return new ResponseEntity<>("商品新增成功",HttpStatus.OK);
		} catch (RuntimeException  e) {
			return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
		}
	}
	
	//查詢單筆
	@GetMapping("/{productName}")
	public ResponseEntity<?> findProductByProductName(@PathVariable String productName){
		 Optional<Product> optional = productService.findProductByProductName(productName);
		
		if(optional.isPresent()) {
			Product result = optional.get();
			return new ResponseEntity<Product>(result,null,HttpStatus.OK);
		}
		return new ResponseEntity<String>("沒有該產品",null,HttpStatus.BAD_REQUEST);
	}
	
	//查詢全部
	@GetMapping("/all")
	public List<Product> findAllProducts(){
		List<Product> products = productService.findAllProducts();
		return products;
	}
	
	//刪除產品
	@DeleteMapping("/delete")
	public String deleteProduct(@RequestParam("productName") String productName) { 
		productService.deleteProductByName(productName);
		return "刪除成功";
	}
	

	//更新產品
	@PutMapping("/update")
	public ResponseEntity<Product> updateProduct(@RequestBody Product updatedProduct) {
        Optional<Product> existingProductOptional = productService.updateProduct(updatedProduct);

        if (existingProductOptional.isPresent()) {
            // 更新成功
            return new ResponseEntity<>(existingProductOptional.get(), HttpStatus.OK);
        } else {
            // 更新失敗
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
	
	//分頁 
	@GetMapping("/page/{pageNumber}")
	public ResponseEntity<?> listProducts(@PathVariable Integer pageNumber) {
		Page<Product> productPage = productService.findByPage(pageNumber);
		return new ResponseEntity<Page<Product>>(productPage,HttpStatus.OK);
	}
	
	@Resource
	FileUploadUtil fileUploadUtil;
	
	//上傳圖片到雲端
	@PostMapping("upload")
	public ResponseEntity<String> upload(@RequestParam("imageUpload") MultipartFile imageFile){
		String imgurl = "";
		try {
			imgurl = fileUploadUtil.uploadFile(imageFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(imgurl);
		return ResponseEntity.ok(imgurl);
		
	}
	
}
