package com.oscat.cinema.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oscat.cinema.dao.ProductRepository;
import com.oscat.cinema.entity.Product;

@Service
public class ProductService {

	private ProductRepository productRepo;

	@Autowired
	public ProductService(ProductRepository productRepo) {
		this.productRepo = productRepo;
	}

	// 新增產品
	public Product addProduct(Product product) {
		// 先檢查是否有相同的名稱的產品
		Optional<Product> existingProductOptinal = productRepo.findByProductName(product.getProductName());
		if (existingProductOptinal.isPresent()) {
			// 有相同名稱 則告知已經有相同的產品名稱
			throw new IllegalArgumentException("Already have the same product name");
		} else {
			// 沒有則進行新增
			return productRepo.save(product);
		}

	}

	// 查詢全部產品
	public List<Product> findAllProducts() {
		return productRepo.findAll();
	}

	// 刪除產品
	public void deletePrroductById(UUID productId) {
		productRepo.deleteById(productId);
	}

	// 更新產品資料
	public Optional<Product> updateProduct(Product updatedProduct) {
		Optional<Product> existingProductOptinal = productRepo.findById(updatedProduct.getProductId());
		if (existingProductOptinal.isPresent()) {
			Product existingProduct = existingProductOptinal.get();
			existingProduct.setProductName(updatedProduct.getProductName());
			existingProduct.setUnitPrice(updatedProduct.getUnitPrice());
			existingProduct.setProductType(updatedProduct.getProductType());
			existingProduct.setProductImg(updatedProduct.getProductImg());

			return Optional.of(productRepo.save(existingProduct));
		} else {
			return Optional.empty();
		}
	}
}
