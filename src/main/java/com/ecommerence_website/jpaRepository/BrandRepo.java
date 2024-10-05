package com.ecommerence_website.jpaRepository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ecommerence_website.entity.BrandDetails;

public interface BrandRepo extends JpaRepository<BrandDetails, Integer> {
	
	public  List<BrandDetails> findByType(String type);
}
