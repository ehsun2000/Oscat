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
//	private CinemaRepository cinemaRepo;
	
	
	@Autowired
	public ProductService(ProductRepository productRepo) {
		this.productRepo=productRepo;
	}
	
//	@Autowired
//	public CinemaService(CinemaRepository cinemaRepo) {
//		this.cinemaRepo=cinemaRepo;
//	}
	
//	private ProductDTO convertToDTO(Product product) {
//		ProductDTO tempDTO = new ProductDTO();
//		tempDTO.setProductId(product.getProductId());
//		tempDTO.setProductName(product.getProductName());
//		tempDTO.setUnitPrice(product.getUnitPrice());
//		tempDTO.setProductType(product.getProductType());
//		tempDTO.setProductImg(product.getProductImg());
//		return tempDTO;		
//	}
	
//	//用名稱模糊查詢
//	public List<ProductDTO> findProductByNameLike(String productName){
//		//用Product這個容器去接 模糊查詢出來的結果
//		List<Product> products=productRepo.findProductByNameLike("%" +productName +"%");
//		//將查詢結果都轉成DTO
//		List<ProductDTO> productDTOs = products.stream()
//				.map(this::convertToDTO)
//				.collect(Collectors.toList());
//		return productDTOs;
//	}
	
//	//查詢特定影城的所有產品
//	public List<ProductDTO> findProductByCinemaId(Integer cinemaId){
//		List<Product> products= productRepo.findProuductByCinemaId(cinemaId);
//		return products.stream()
//				.map(this::convertToDTO)
//				.collect(Collectors.toList());
//	}
	
//	public List<ProductDTO> addProductForCinema(Integer cinemaId,ProductDTO productDTO){
//		Optional<Cinema> cinemaOptional  = cinemaRepo.findById(cinemaId);
//		if(cinemaOptional.isPresent()) {
//			Cinema cinema = cinemaOptional.get();
//			
//			ProductDTO newproductDTO = new ProductDTO();
//			newproductDTO.setProductName(productDTO.getProductName());
//			newproductDTO.setUnitPrice(productDTO.getUnitPrice());
//			newproductDTO.setProductType(productDTO.getProductType());
//			newproductDTO.setProductImg(productDTO.getProductImg());
//			
//			List<CinemaProduct> cinemaProducts = cinema.getCinemaProducts();
//			
//		}
//	}
	
	//新增產品
	public Product addProduct(Product product) {
		//先檢查是否有相同的名稱的產品
		Optional<Product> existingProductOptinal = productRepo.findByProductName(product.getProductName());
		if (existingProductOptinal.isPresent()) {
			//有相同名稱 則告知已經有相同的產品名稱
			throw new IllegalArgumentException("Already have the same product name");
		}else {
			//沒有則進行新增
			return productRepo.save(product);
		}
		
	}
	
	//查詢全部產品
	public List<Product> findAllProducts(){
		return productRepo.findAll();
	}
	
	//刪除產品
	public void deletePrroductById(UUID productId) {
		productRepo.deleteById(productId);
	}
	
	//更新產品資料
	public Optional<Product> updateProduct(Product updatedProduct){
		Optional<Product> existingProductOptinal = productRepo.findById(updatedProduct.getProductId());
		if(existingProductOptinal.isPresent()) {
			Product existingProduct = existingProductOptinal.get();
			existingProduct.setProductName(updatedProduct.getProductName());
			existingProduct.setUnitPrice(updatedProduct.getUnitPrice());
			existingProduct.setProductType(updatedProduct.getProductType());
			existingProduct.setProductImg(updatedProduct.getProductImg());
			
			return Optional.of(productRepo.save(existingProduct));
		}else {
			return Optional.empty();
		}
	}
}
