package com.ecommerence_website.serviceDao;

import java.util.List;

import com.ecommerence_website.entity.Category;

public interface CategoryService {
	
	public Category addCategory(Category category);
	
	public List<Category> getAllCategories();
	
	public Category updateCategory(Integer category_Id , Category category);
	
	public void deleteCategory(Integer category_Id  );

}
