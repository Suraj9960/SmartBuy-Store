package com.ecommerence_website.jpaRepository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ecommerence_website.entity.Category;
import com.ecommerence_website.entity.Product;

public interface ProductRepo extends JpaRepository<Product, Integer> {
	
	public List<Product> findByCategories(Category categories);
	
	@Query("select p from Product p where LOWER(p.product_name) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(p.brandName) LIKE LOWER(CONCAT('%', :query, '%'))")
	public List<Product> searchProduct(@Param("query") String query);
	
	public  List<Product> findByPriceBetween(Integer minPrice, Integer maxPrice);
	
	public  List<Product> findByBrandName(String brandName);
	
}
