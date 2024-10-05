package com.ecommerence_website.serviceDaoImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerence_website.entity.Banner;
import com.ecommerence_website.jpaRepository.BannerRepo;
import com.ecommerence_website.serviceDao.BannerService;

@Service
public class BannerDaoImpl implements BannerService {
	
	@Autowired
	private BannerRepo repo;

	@Override
	public List<Banner> getAllBanners() {
		
		return repo.findAll();
	}

	@Override
	public Banner create_banner(Banner banner) {
		
		return repo.save(banner);
	}

	@Override
	public void delete_banner(Integer banner_id) {
		Banner banner = repo.findById(banner_id).get();
		
		repo.delete(banner);
		
	}

}
