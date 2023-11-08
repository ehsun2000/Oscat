package com.oscat.cinema.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.oscat.cinema.entity.Product;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ProductMapper {
	
	default String toDto(Product product) {
		return product.getProductName();
	}
	
	List<String> toDtos(List<Product> list);
}
