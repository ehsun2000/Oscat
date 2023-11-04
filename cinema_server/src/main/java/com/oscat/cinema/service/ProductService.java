package com.oscat.cinema.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oscat.cinema.dao.ProductRepository;
import com.oscat.cinema.entity.Product;



@Service
public class ProductService {
	
	@Autowired
	private ProductRepository productRepo;
	
	// 新增商品
	  public Product addProduct(Product product) {
	      String productName = product.getProductName();
	      
	      //檢查是否有相同名稱的商品
	      Optional<Product> existingProduct = productRepo.findByProductName(productName);
	      if(existingProduct.isPresent()) {
	    	throw new RuntimeException("已有相同名稱的商品");
	      }
	      
	      return productRepo.save(product);
	  }
	  
	  
	// 查詢全部商品
	public List<Product> findAllProducts() {
		return productRepo.findAll();
	}
	
	// 用名稱查詢單筆商品
	public Optional<Product> findProductByProductName(String productName ) {
		return productRepo.findByProductName(productName);
	}
	

	// 刪除商品
	@Transactional
	public void deleteProductByName(String productName) {
		productRepo.deleteByProductName(productName);;
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
	
	//分頁
	public Page<Product> findByPage(Integer pageNumber){
		Pageable pgb = PageRequest.of(pageNumber-1,6,Sort.Direction.ASC,"productId");
		Page<Product> page = productRepo.findAll(pgb);		    
		return page;
	}
	
	// 用產品類別模糊查詢
	public List<Product> findProductByType(String ProductType){
		return productRepo.findProductByProductTypeLike(ProductType);
	}
}
