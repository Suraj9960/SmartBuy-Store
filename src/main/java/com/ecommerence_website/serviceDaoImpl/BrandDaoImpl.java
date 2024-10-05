package com.ecommerence_website.serviceDaoImpl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerence_website.entity.BrandDetails;
import com.ecommerence_website.entity.Product;
import com.ecommerence_website.jpaRepository.BrandRepo;
import com.ecommerence_website.jpaRepository.ProductRepo;

@Service
public class BrandDaoImpl {

	@Autowired
	private BrandRepo repo;

	@Autowired
	private ProductRepo repo2;

	public List<BrandDetails> allDetails() {
		return repo.findAll();
	}

	public BrandDetails getDetail(Integer pid, BrandDetails brandDetails) {

		Product product = repo2.findById(pid).get();

		product.setBrandDetails(brandDetails);

		return repo.save(brandDetails);
	}

	public void deleteBrand(Integer id) {

		BrandDetails brandDetails = repo.findById(id).get();

		repo.delete(brandDetails);

	}

	public List<String> all_brand_name(List<BrandDetails> brandDetails) {

		List<String> brand_name = new ArrayList<>();

		for (BrandDetails brand : brandDetails) {

			String brandN = brand.getBrand_name();

			brand_name.add(brandN);
		}

		return brand_name;
	}

	public List<String> all_operating_system(List<BrandDetails> brandDetails) {

		List<String> brand_name = new ArrayList<>();

		for (BrandDetails brand : brandDetails) {

			String brandN = brand.getOperating_system();

			brand_name.add(brandN);
		}

		return brand_name;
	}
	public List<String> all_type(List<BrandDetails> brandDetails) {

		List<String> brand_name = new ArrayList<>();

		for (BrandDetails brand : brandDetails) {

			String brandN = brand.getType();

			brand_name.add(brandN);
		}

		return brand_name;
	}

}
