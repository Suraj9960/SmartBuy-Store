package com.ecommerence_website.serviceDao;

import java.util.List;
import com.ecommerence_website.entity.Product;

public interface ProductService {
	
	public List<Product> getAllProducts();
	
	public Product addProduct(Product product);
	
	public Product updateProduct(Integer product_id , Product product );
	
	public void deleteProduct(Integer product_id);
	
	public Product findById(Integer product_id);
	
	public List<Product> searchList(String query);
	
}
