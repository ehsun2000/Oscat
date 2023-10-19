package com.oscat.cinema.dao;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oscat.cinema.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
	
//	//模糊查詢
//	@Query(value = "FROM Product p WHERE ProductName LIKE %:n%")
//	List<Product> findProductByNameLike(@Param("n")String productName);
//	
//	//JPQL 查詢 查詢特定影城的所有產品
//	@Query("SELECT p FROM p" +
//			"JOIN p.cinemaProducts cp" +
//			"WHERE cp.cinema.cinemaId =: cinemaId")
//	List<Product> findProuductByCinemaId(@Param("cinemaId")Integer cinemaId);
	
	Optional<Product> findByProductName(String productName);


}
