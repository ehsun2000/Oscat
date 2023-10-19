package com.oscat.cinema.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.oscat.cinema.entity.Product;
import com.oscat.cinema.service.ProductService;

@RestController
//@CrossOrigin(origins = "http://localhost:8081")
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	//新增產品
	@PostMapping("/product/add")
	public Product addProduct(@RequestBody Product product) {
		productService.addProduct(product);
		return product;
	}
	
	//查詢全部
	@GetMapping("/product/all")
	public List<Product> findAllProducts(){
		List<Product> products = productService.findAllProducts();
		return products;
	}
	
	//刪除產品
	@DeleteMapping("/product/delete")
	public String deleteProduct(@RequestParam("id")UUID id) {
		productService.deletePrroductById(id);
		return "刪除成功";
	}
	
	//更新產品
	@PutMapping("/product/update")
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
}
