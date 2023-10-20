package com.oscat.cinema.dao;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oscat.cinema.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
	
	Optional<Product> findByProductName(String productName);

}
