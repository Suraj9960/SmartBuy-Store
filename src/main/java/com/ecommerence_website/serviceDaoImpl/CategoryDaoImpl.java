package com.ecommerence_website.serviceDaoImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerence_website.entity.Category;
import com.ecommerence_website.jpaRepository.CategoryRepo;
import com.ecommerence_website.serviceDao.CategoryService;

@Service
public class CategoryDaoImpl implements CategoryService {
	
	@Autowired
	private CategoryRepo repo;
	
	@Override
	public Category addCategory(Category category) {
		
		return repo.save(category);
	}

	@Override
	public List<Category> getAllCategories() {
		
		return repo.findAll();
	}

	@Override
	public Category updateCategory(Integer category_Id, Category category) {
		
		Category category2 = repo.findById(category_Id).get();
		
		category2.setCategory_name(category.getCategory_name());
		category2.setDiscount(category.getDiscount());
		
		repo.save(category2);
		
		return category2;
	}

	@Override
	public void deleteCategory(Integer category_Id) {
		Category category = repo.findById(category_Id).get();
		
		repo.delete(category);
	}

	
	public Category findById(Integer category_Id ) {
		return repo.findById(category_Id).get();
	}
	
	
	public Category findByName(String category_name) {
		return repo.findByCategoryName(category_name);
	}
}
