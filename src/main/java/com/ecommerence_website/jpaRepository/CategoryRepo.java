package com.ecommerence_website.jpaRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ecommerence_website.entity.Category;

public interface CategoryRepo extends JpaRepository<Category, Integer> {

	@Query("SELECT c FROM Category c WHERE c.category_name = :categoryName")
	Category findByCategoryName(@Param("categoryName") String categoryName);

}
