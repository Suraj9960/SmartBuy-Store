package com.ecommerence_website.serviceDaoImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerence_website.entity.BrandDetails;
import com.ecommerence_website.entity.Cart;
import com.ecommerence_website.entity.Category;
import com.ecommerence_website.entity.Product;
import com.ecommerence_website.jpaRepository.BrandRepo;
import com.ecommerence_website.jpaRepository.CartRepo;
import com.ecommerence_website.jpaRepository.ProductRepo;
import com.ecommerence_website.serviceDao.ProductService;

@Service
public class ProductDaoImpl implements ProductService {

	@Autowired
	private ProductRepo repo;

	@Autowired
	private CartRepo repo2;

	@Autowired
	private BrandRepo repo3;

	@Override
	public List<Product> getAllProducts() {

		return repo.findAll();
	}

	@Override
	public Product addProduct(Product product) {

		return repo.save(product);
	}

	@Override
	public Product updateProduct(Integer product_id, Product product) {
		Product product2 = repo.findById(product_id).get();

		product2.setProduct_name(product.getProduct_name());
		product2.setPrice(product.getPrice());

		return repo.save(product2);
	}

	@Override
	public void deleteProduct(Integer product_id) {
		Product product = repo.findById(product_id).get();

		List<Cart> cartsWithProduct = repo2.findAllByProductsContaining(product);

		for (Cart cart : cartsWithProduct) {
			cart.removeProduct(product);

			repo2.save(cart);
		}

		BrandDetails brandDetails = product.getBrandDetails();

		repo.delete(product);
		repo3.delete(brandDetails);

	}

	public List<Product> findByCategoryName(Category category) {
		return repo.findByCategories(category);
	}

	@Override
	public Product findById(Integer product_id) {

		return repo.findById(product_id).get();
	}

	public List<Product> convertToList(Product product) {

		List<Product> newProducts = new ArrayList<>();

		newProducts.add(product);

		return newProducts;
	}

	@Override
	public List<Product> searchList(String query) {

		List<Product> p = repo.searchProduct(query);

		if (p == null) {
			p = new ArrayList<>();
		}

		return p;
	}

	public List<Product> saveOrderedProduct(List<Product> products) {
		return repo.saveAll(products);
	}

	public List<Product> findByPrice(Integer min, Integer max) {
		return repo.findByPriceBetween(0, max);
	}

	public List<Product> findByBrand(String BramdName) {
		return repo.findByBrandName(BramdName);
	}

	public List<Product> findByType(String type) {

		List<Product> products = getAllProducts();

		List<Product> byType = new ArrayList<>();

		for (Product pro : products) {

			if (pro.getBrandDetails().getType().equals(type)) {
				byType.add(pro);
			}

		}

		return byType;

	}
}
