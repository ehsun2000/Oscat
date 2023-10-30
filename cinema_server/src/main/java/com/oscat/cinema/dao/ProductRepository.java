package com.oscat.cinema.dao;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.oscat.cinema.entity.Product;


public interface ProductRepository extends JpaRepository<Product, UUID> {
	
	//用產品名稱去查詢
	Optional<Product> findByProductName(String productName);
	
	//刪除指定名稱的產品
	public String deleteByProductName(String productName);
	
	@Query("from Product p order by p.productType asc")
	public List<Product>findAll();
	
	//查詢產品分頁
	Page<Product> findAll(Pageable pageable);
}
